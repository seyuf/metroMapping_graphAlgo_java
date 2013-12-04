package algo.graph;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import serialize.GraphSerialized;
import algo.graph.Node;
import algo.graph.interfaces.IGraph;
import algo.graph.parsing.Parse;
import algo.graph.parsing.Stops;

public class Graph implements IGraph, Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6538625992628686871L;
	public Map<String, Node> node;
	private ArrayList<Compare> listRelation;
	public ArrayList<Stops> stops;
	public Graph()
	{
		node = new HashMap<String, Node>();
		listRelation = new ArrayList<Compare>();
		stops = new ArrayList<Stops>();
	}
	
	public void addRoute(Stops start, Stops target, int weight,String line, String modeTransport) 
	{
		Compare comp = new Compare(start.stop_name,target.stop_name);
		
		int isDouble = 0;
		
		//System.out.println("Ligne : " + mode);
		for(int i = 0 ; i < listRelation.size() ; i++)
		{
			if(listRelation.get(i).depart.equals(start) && listRelation.get(i).arrive.equals(target))
				isDouble = 1;
		}
		
		if(isDouble == 0)
		{
			Node n1 = (Node) node.get(start.stop_name);
			Node n2 = (Node) node.get(target.stop_name);
			
			if(n1 == null)
			{
				n1 = new Node(start.stop_name,start.stop_lat,start.stop_long);
				n1.weight = 0;
				node.put(start.stop_name,n1);
			}
			
			if(n2 == null)
			{
				n2 = new Node(target.stop_name,target.stop_lat,target.stop_long);

				int poidsn1 = 0;
				if(n1.weight != null)
					poidsn1 = n1.weight;
				
				n2.weight = poidsn1 + weight;
				node.put(target.stop_name,n2);
			}
			
			Relation r = new Relation(n1,n2,weight,line,modeTransport);
			
			listRelation.add(comp);
			n1.addRelation(r);
			n2.addRelation(r);
		}
	}
	
	public void Init(Map<String, Node> node,Node start)
	{
		for(String ville : node.keySet())
		{
			if(!ville.equals(start.town))
			{
				node.get(ville).weight = 9999999;
				node.get(ville).previousNode = null;
			}
		}
		
		node.get(start.town).weight = 0;
	}
	
	public Integer Weight(Node start,Node end)
	{
		for(int i = 0 ; i < node.get(start.town).getRelations().size() ; i++)
		{
			if(node.get(start.town).getRelations().get(i).getEndNode().toString().equals(end.town))
				return node.get(start.town).getRelations().get(i).getWeight();			
		}
		
		System.out.println("OK");
		
		return 0;
	}

	// This method allow create an list with the station of the graph
	public void createListGraph(Map<String, Node> graphTotal,List<Node> listNode,String modeTransport)
	{
		if(modeTransport == "TOUS") // If the mode of transport is "Tous" then add all of the graph
		{
			for(String ville : graphTotal.keySet())
				listNode.add(graphTotal.get(ville));
		}
		
		else
		{	
			for(String station : graphTotal.keySet())
			{
				for(int i = 0 ; i < graphTotal.get(station).getRelations().size() ; i++)
				{
					if(graphTotal.get(station).getRelations().get(i).getStartNode().toString().equals(station))
					{
						if(graphTotal.get(station).getRelations().get(i).getLigneTransport().equals(modeTransport))
							listNode.add((Node) graphTotal.get(station).getRelations().get(i).getEndNode());
					}
				}
			}
		}
	}
	
	public Node heuristic_cost_minimum(List<Node> listGraph)
	{
		Node min = new Node();
		min = listGraph.get(0);
		
		for(int i = 1 ; i < listGraph.size() ; i++)
		{
			if(min.weight > listGraph.get(i).heuristic)
				min = listGraph.get(i);
		}
		
		return min;
	}
	
	public Integer heuristic_cost_estimate(Node start,Node goal)
	{
		try
		{
			double lat1  = Double.parseDouble(start.latitude);
			double lon1 = Double.parseDouble(start.longitude);
			
			double lat2 = Double.parseDouble(goal.latitude);
			double lon2 = Double.parseDouble(goal.longitude);
		
			lat1 = Math.toRadians(lat1);
	        lon1 = Math.toRadians(lon1);
	        lat2 = Math.toRadians(lat2);
	        lon2 = Math.toRadians(lon2);
	 
	        double distance = distanceVolOiseauEntre2Points(lat1, lon1, lat2, lon2);
			
	        return (int)(distance*6366);
		}
		catch(Exception E)
		{
			System.out.println("Exception ");
			return 99999;
		}
	}
	
	public List<Node> reconstruct_path(Node start,Node end)
	{		
		List<Node> chemin = new ArrayList<Node>();
		Node n = end.came_from;
		
		while(n != null)
		{
			chemin.add(n);
			n = n.came_from;
		}
		chemin.add(start);
		
		return chemin;
	}
		
	public boolean inClosedset(List<Node> closed_set,Node neighbor)
	{
		for(int i = 0 ; i < closed_set.size();i++)
		{
			if(closed_set.get(i).town.equals(neighbor.town))
				return true;
		}
		
		return false;
	}
	
	public boolean inOpenset(List<Node>  open_set,Node neighbor)
	{
		for(int i = 0 ; i < open_set.size();i++)
		{
			if(open_set.get(i).town.equals(neighbor.town))
				return true;
		}
		
		return false;
	}
	
	public static double distanceVolOiseauEntre2Points(double lat1, double lon1, double lat2, double lon2) 
	{
        return
        Math.acos(
            Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)
        );
    }
	
	public List<Node> AStar(Map<String, Node> graph,String start,String end,String modeTransport,String critere)
	{
		// Permet de préremplir les weight ( pout les heuristique )
		//Dijkstra(graph,start,end,modeTransport,critere);

		List<Node> closedset = new ArrayList<Node>();
		List<Node> openset = new ArrayList<Node>();
		
		Map<String, Node> came_from = new HashMap<String,Node>();
		
		openset.add(graph.get(start));
		
		Node current = new Node();
		
		// G Score
		graph.get(start).weight = 0;
		
		// F Score
		graph.get(start).heuristic = graph.get(start).weight + heuristic_cost_estimate(graph.get(start),graph.get(end));
		
		while(openset.size() > 0)
		{
			current = heuristic_cost_minimum(openset);
			
			if(current.equals(graph.get(end)))
			{
				System.out.println("FINISHED");
				List<Node> listNode = reconstruct_path(graph.get(start),graph.get(end));
				listNode.add(graph.get(end));
				
				for(int i = listNode.size() - 1 ; i > 0 ; i--)
				{
					for(int h = 0 ; h < node.get(listNode.get(i).town).getRelations().size() ; h++)
					{
						if(node.get(listNode.get(i).town).getRelations().get(h).getEndNode().town.equals(listNode.get(i-1).town))
						{
							System.out.println(listNode.get(i).town + " => " + listNode.get(i-1).town + " ligne : " + node.get(listNode.get(i).town).getRelations().get(h).getmodeTransport());
							break;
						}
					}
				}
				return listNode;
			}
			
			openset.remove(current);
			closedset.add(current);
			
			for(int i = 0 ; i < graph.get(current.town).getRelations().size() ; i++)
			{
				if(graph.get(current.town).getRelations().get(i).getStartNode().town.equals(current.town))
				{
					int tentative_g_score = graph.get(current.town).weight + Weight(graph.get(current.town),graph.get(current.town).getRelations().get(i).getEndNode());
					int tentative_f_score = tentative_g_score + heuristic_cost_estimate(graph.get(current.town).getRelations().get(i).getEndNode(),graph.get(end));
					
					if(inClosedset(closedset,graph.get(current.town).getRelations().get(i).getEndNode()) && tentative_f_score >= graph.get(current.town).getRelations().get(i).getEndNode().heuristic)
							continue;
					
					if(!inClosedset(closedset,graph.get(current.town).getRelations().get(i).getEndNode()) || tentative_f_score < graph.get(current.town).getRelations().get(i).getEndNode().heuristic)
					{	
						graph.get(current.town).getRelations().get(i).getEndNode().came_from = current;
						graph.get(current.town).getRelations().get(i).getEndNode().came_from.line = current.line;
						
						graph.get(current.town).getRelations().get(i).getEndNode().weight = tentative_g_score;
						graph.get(current.town).getRelations().get(i).getEndNode().heuristic = tentative_f_score;
						
						//System.out.println("End node : " + graph.get(current.town).getRelations().get(i).getEndNode().town);
						if(!inOpenset(openset,graph.get(current.town).getRelations().get(i).getEndNode()))
						{
							openset.add(graph.get(current.town).getRelations().get(i).getEndNode());
						}
					}
				}
			}			
		}
		
		// System.out.println("FINISH");
		
		return null;
	}
	
	// Method Dijkstra : Algorithm to search shortest route between 2 stations
	public List<Node> Dijkstra(Map<String, Node> graph,String start,String end,String modeTransport,String critere)
	{
		try
		{
			// Initialization of the summits of graph
			Init(graph,graph.get(start));
			
			// Create list with all stations
			List<Node> listNode = new ArrayList<Node>();
			
			createListGraph(graph,listNode,modeTransport);
			
			Node n1 = new Node();
			
			// Traverses the list until it is empty
			while(listNode.size() != 0)
			{
				n1 = Trouve_min(listNode);
				listNode.remove(n1);
				
				// Traverses the relations of the current station
				for(int i = 0 ; i < graph.get(n1.town).getRelations().size() ; i++)
				{		
					if(graph.get(n1.town).getRelations().get(i).getStartNode().town.equals(n1.town))
					{						
						if(graph.get(n1.town).getRelations().get(i).getEndNode().weight > graph.get(n1.town).getRelations().get(i).getStartNode().weight + Weight(graph.get(n1.town).getRelations().get(i).getStartNode(),graph.get(n1.town).getRelations().get(i).getEndNode()))
						{
							graph.get(n1.town).getRelations().get(i).getEndNode().weight = graph.get(n1.town).getRelations().get(i).getStartNode().weight + Weight(graph.get(n1.town).getRelations().get(i).getStartNode(),graph.get(n1.town).getRelations().get(i).getEndNode());
							graph.get(n1.town).getRelations().get(i).getEndNode().previousNode = graph.get(n1.town).getRelations().get(i).getStartNode();
													
							try
							{
								graph.get(n1.town).getRelations().get(i).getEndNode().line = graph.get(n1.town).getRelations().get(i).getmodeTransport();
								
								if(graph.get(n1.town).getRelations().get(i).getStartNode().line != null)
								{
									if(!graph.get(n1.town).getRelations().get(i).getStartNode().line.equalsIgnoreCase(graph.get(n1.town).getRelations().get(i).getEndNode().line))
									{
										if(critere.equals("CORRESPONDANCE"))
										{			
											if(!graph.get(n1.town).getRelations().get(i).getEndNode().line.equals(graph.get(end).line))
												graph.get(n1.town).getRelations().get(i).getEndNode().weight += 100;
										}

										else
											graph.get(n1.town).getRelations().get(i).getEndNode().weight += 5;
									}
								}
							}
							catch(Exception E)
							{
								
							}
						}
					}
				}
			}
			
			List<Node> chemin = new ArrayList<Node>();
			Node n = graph.get(end);
			
			while(n != graph.get(start))
			{
				chemin.add(n);
				n = n.previousNode;
			}
			chemin.add(graph.get(start));
			
			System.out.println("AFFICHAGE : ");
			String tempLigne = chemin.get(0).line;
			
			chemin.get(chemin.size()-1).line = chemin.get(chemin.size()-2).line;
			for(int i = 0 ; i < chemin.size() ; i++)
			{
				System.out.println(chemin.get(i).town + " Ligne : " + chemin.get(i).line);	
			}
			
			
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("FIN");
			
												
			return chemin;
		}
		catch(Exception E)
		{
			System.out.println("PAS DE SOLUTIONS");
			return null;
		}
	}
	
	public Node Trouve_min(List<Node> listGraph)
	{
		Node min = new Node();
		min = listGraph.get(0);
		
		for(int i = 1 ; i < listGraph.size() ; i++)
		{
			if(min.weight > listGraph.get(i).weight)
				min = listGraph.get(i);
		}
		
		return min;
	}

	public void findRoute(String start, String target) 
	{
		// Algorithme de Dijkstra
		 for(String ville : node.keySet())
		 {
			System.out.println("Poids de " + ville + " : " + node.get(ville).weight);
			
			System.out.println("");
		 }		
	}
	
	public static void main(String[] args)
	{
		/*Graph g = new Graph();
		
		g.listRelation.clear();
		g.addRoute("A","B",85,"l");
		g.addRoute("B","F",80,"l");
		g.addRoute("F","I",250,"l");
		g.addRoute("I","J",84,"l");
		
		g.addRoute("A","C",217,"l");
		g.addRoute("C","G",186,"l");
		
		g.addRoute("C","H",103,"l");
		g.addRoute("H","D",183,"l");
		g.addRoute("H","J",167,"l");
		
		g.addRoute("A","E",173,"l");
		g.addRoute("E","J",502,"l");*/
		
		Parse parse = new Parse();
		Graph g = parse.getGraph();
		
		 /*for(String ville : g.node.keySet())
		 {
			 if(g.getRelation(ville) > 1)
			 {
				 for(int i = 0 ; i < g.node.get(ville).getRelations().size() ; i++)
				 {
					 System.out.println(g.node.get(ville).getRelations().get(i).getStartNode().town + " => " + g.node.get(ville).getRelations().get(i).getEndNode().town);
				 }
			 }
		 }*/
		
		//List<Node> chemin = g.Dijkstra(g.node,"La Courneuve-8-Mai-1945","Balard","TOUS");
		g.AStar(g.node,"La Courneuve-8-Mai-1945","Montparnasse-Bienvenue","TOUS","OK");
		// List<Node> chemin = g.Dijkstra(g.node,"Balard","Pointe du Lac","TOUS","OK");
		
		//List<Node> chemin = g.Test(g.node,"La Courneuve-8-Mai-1945","Buttes-Chaumont");
		/*for(int i = chemin.size()-1 ; i >= 0  ; i--)
		{
			if(i != 0)
			{
				for(int j = 0 ; j < g.node.get(chemin.get(i).town).getRelations().size() ; j++)
				{
					if(g.node.get(chemin.get(i).town).getRelations().get(j).getEndNode().town.equals(chemin.get(i-1).town))
					{
						chemin.get(i).ligne = g.node.get(chemin.get(i).town).getRelations().get(j).getmodeTransport();
						ligne = chemin.get(i).ligne;
					}
				}
			}
			
			else
			{
				chemin.get(i).ligne = ligne;
			}
		}*/
		
		/*String messageDisplay = "";
		String ligneTemp = "";
		ligneTemp = chemin.get(chemin.size()-1).line;

		System.out.println("De " + chemin.get(chemin.size()-1).town + " : " + chemin.get(chemin.size()-1).line);
		for(int i = chemin.size()-1 ; i >= 0  ; i--)
		{
			if(!ligneTemp.equals(chemin.get(i).line))
			{
				System.out.println(" jusqua : " + chemin.get(i+1).town);

				System.out.println("De " + chemin.get(i+1).town + " : " + chemin.get(i).line);
			}
			ligneTemp = chemin.get(i).line;
		}
		
		System.out.println(" jusqua : " + chemin.get(0).town);*/
	}


}