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

/**
 * this class represent the graph of the intercity transport network of Paris
 * @author  ESGI Students COULIBALY // DA-COSTA // BEKAERT 
 * @version 1.0
 */
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
	

	/**
	 * this will generate relations beetween two 
	 * stops
	 * 
	 * @param start  the first stop in the relation
	 * @param target this is the second stop in the relation 
	 * @param weight this define the weight of the relation
	 * @param line   this the subway line to consider
	 * @param modeTransport this is the transportation mode 
	 * 
	 */
	public void addRoute(Stops start, Stops target, int weight,String line, String modeTransport) 
	{
		Compare comp = new Compare(start.stop_name,target.stop_name);
		
		int isDouble = 0;
		
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
	
	/**
	 * initialize the stacks of node of the graph 
	 * 
	 * @param node   the nodes map of the graph
	 * @param start  the first stop in the relation
	 *  
	 * 
	 */
	public void Init(Map<String, Node> node,Node start)
	{
		for(String ville : node.keySet())
		{
			if(!ville.equals(start.town))
			{
				node.get(ville).weight = 9999999;
				node.get(ville).previousNode = null;
				node.get(ville).line = null;
			}
		}
		
		node.get(start.town).weight = 0;
	}
	
	/**
	 * this return the weight of the relation between two nodes
	 * 
	 * @param node   the nodes map of the graph
	 * @param start  the first stop in the relation
	 * @return       the weight of the relation
	 * 
	 */
	public Integer Weight(Node start,Node end)
	{
		for(int i = 0 ; i < node.get(start.town).getRelations().size() ; i++)
		{
			if(node.get(start.town).getRelations().get(i).getEndNode().toString().equals(end.town))
				return node.get(start.town).getRelations().get(i).getWeight();			
		}
				
		return 0;
	}

	
	/**
	 * create a station list which will represent the graph 
	 * 
	 * @param graphTotal represent the full graph
	 * @param listNode   represent some list of nodes
	 * @param modeTransport the transportation mode 
	 * 
	 */
	public void createListGraph(Map<String, Node> graphTotal,List<Node> listNode,String modeTransport)
	{
		if(modeTransport == "TOUS") // If the mode of transport is "Tous" then add all of the graph
		{
			for(String ville : graphTotal.keySet())
			{
				listNode.add(graphTotal.get(ville));
			}
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
	
	
	/**
	 * this return the minimum cost for the Astar method 
	 * 
	 * @param listGraph   represent some list of nodes
	 * @return return the node with the minimum heuristic
	 */
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
	
	/**
	 * calculate an estimation between two geo points 
	 * 
	 * @param start the departure
	 * @param goal the arrival	
	 * @return the estimation
	 */
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
	
	/**
	 * calculate the pathway from A point to B point
	 * 
	 * @param start the departure
	 * @param end   the arrival 	
	 * @return a list of node representing the pathway
	 */
	public List<Node> reconstruct_path(Node start,Node end)
	{		
		List<Node> chemin = new ArrayList<Node>();
		Node n = end.came_from;
		
		chemin.add(end);
		
		while(n != null)
		{
			chemin.add(n);
			n = n.came_from;
		}
		//chemin.add(start);
		
		List<Node> cheminReverse = new ArrayList<Node>();
		
		for(int i = 0 ; i < chemin.size() ; i++)
		{
			cheminReverse.add(chemin.get(i));
		}
		
		for(int i = 0 ; i < cheminReverse.size() ; i++)
		{
			for(int h = 0 ; h < node.get(cheminReverse.get(i).town).getRelations().size() ; h++)
			{
				if(i != cheminReverse.size() - 1)
				{
					if(node.get(cheminReverse.get(i).town).getRelations().get(h).getEndNode().town.equals(cheminReverse.get(i+1).town))
					{
						cheminReverse.get(i).line = node.get(cheminReverse.get(i).town).getRelations().get(h).getmodeTransport();
					}
				}
			}
			
			/*if(i == 0)
			{
				cheminReverse.get(i).line = "7B";	
			}*/
			
			if(i == cheminReverse.size() - 1)
			{
				for(int h = 0 ; h < node.get(cheminReverse.get(i).town).getRelations().size() ; h++)
				{
						if(node.get(cheminReverse.get(i).town).getRelations().get(h).getEndNode().town.equals(cheminReverse.get(i-1).town))
						{
							cheminReverse.get(i).line = node.get(cheminReverse.get(i).town).getRelations().get(h).getmodeTransport();
						}
				}
			}
			
			System.out.println(cheminReverse.get(i) + " Ligne : " + cheminReverse.get(i).line);
		}
			
		return cheminReverse;
	}
		
	/**
	 * check if the list of nodes  contains a specific node 
	 * 
	 * @param closed_set the list of nodes
	 * @param neighbor   the node to check 	
	 * @return true or false
	 */
	public boolean inClosedset(List<Node> closed_set,Node neighbor)
	{
		for(int i = 0 ; i < closed_set.size();i++)
		{
			if(closed_set.get(i).town.equals(neighbor.town))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * check if the list of nodes  contains a specific node 
	 * 
	 * @param open_set the list of nodes
	 * @param neighbor   the node to check 	
	 * @return true or false
	 */
	public boolean inOpenset(List<Node>  open_set,Node neighbor)
	{
		for(int i = 0 ; i < open_set.size();i++)
		{
			if(open_set.get(i).town.equals(neighbor.town))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * calculate the slant distance between two location 
	 * 
	 * @param lat1  the latitude of the first location
	 * @param lon1  the longitude of the first location
	 * @param lat2  the latitude of the second location
	 * @param lon2  the longitude of the second location
	 * @return the distance 
	 */
	public static double distanceVolOiseauEntre2Points(double lat1, double lon1, double lat2, double lon2) 
	{
        return
        Math.acos(
            Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)
        );
    }
	
	/**
	 * the astar method
	 * find the pathway between two points 
	 * with the geolocalization as parameter
	 * 
	 * @param graph   the graph
	 * @param start
	 * @param end
	 * @param modeTransport 
	 * @param critere       the criteria
	 * @return list of node which represent the pathway 
	 */
	public List<Node> AStar(Map<String, Node> graph,String start,String end,String modeTransport,String critere)
	{
		// Permet de preremplir les weight ( pout les heuristique )
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
	/**
	 * the  Dijkstra method
	 * find the most effective pathway between two subway stops 
	 * with the time as parameter 
	 * 
	 * @param graph   the subway graph 
	 * @param start   the departure stop
	 * @param end     the arrival stop
	 * @param modeTransport the transportation mode
	 * @param critere       the time 
	 * @return list of node which represent the pathway from start stop to the end stop 
	 */
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
						int n1RiEndNodeWeight   = graph.get(n1.town).getRelations().get(i).getEndNode().weight;
						int n1RiStartNodeWeight = graph.get(n1.town).getRelations().get(i).getStartNode().weight;
						Node n1RiEndNode        = graph.get(n1.town).getRelations().get(i).getEndNode();
						Node n1RiStartNode      = graph.get(n1.town).getRelations().get(i).getStartNode();
						int n1sumNodesWeight = n1RiStartNodeWeight + Weight(n1RiStartNode,n1RiEndNode);
						if( n1RiEndNodeWeight > n1sumNodesWeight )
						{
							graph.get(n1.town).getRelations().get(i).getEndNode().weight = n1sumNodesWeight ;
							graph.get(n1.town).getRelations().get(i).getEndNode().previousNode = n1RiStartNode ;
													
							try
							{
								// checking the transportation mode???
								graph.get(n1.town).getRelations().get(i).getEndNode().line = 
										graph.get(n1.town).getRelations().get(i).getmodeTransport();
								
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
	
	/**
	 * find the with the minimum weight between a list of nodes
	 * 
	 *  @param listGraph the list of nodes
	 *  @return the node with the minimum
	 */
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

	/**
	 * print the path useless
	 */
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
	
		
		//Parse parse = new Parse();
		//Graph g = parse.getGraph();
		
		
		GraphSerialized deserialized  = null;
		try {
			deserialized = GraphSerialized.deSerialise("ressources/graph2") ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"failed to load the graph");
		}
		Graph g = deserialized.getSerializedGraph();
		
		List<Node> chemin = g.Dijkstra(g.node,"Republique","Balard","TOUS","OK");
		//g.AStar(g.node,"La Courneuve-8-Mai-1945","Montparnasse-Bienvenue","TOUS","OK");
		// List<Node> chemin = g.Dijkstra(g.node,"Balard","Pointe du Lac","TOUS","OK");
		

	}


}