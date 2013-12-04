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
	
	public Graph()
	{
		node = new HashMap<String, Node>();
		listRelation = new ArrayList<Compare>();
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
			
			/*
			if(critere.equals("CORRESPONDANCE"))
			{
				System.out.println("OK RE INITIALISATION");
				// Récupération de la ligne de la station de fin
				if(getRelation(fin) == 1)
				{
						ligneEnd = graph.get(fin).getRelations().get(0).getmodeTransport();
				}
				
				// Si la station de départ fait parti de la même ligne que la station de fin
				for(int i = 0 ; i < graph.get(depart).getRelations().size() ;i++)
				{
					if(!graph.get(depart).getRelations().get(i).getmodeTransport().equals(ligneEnd))
					{
						graph.get(depart).getRelations().get(i).setWeight(graph.get(depart).getRelations().get(i).getWeight()-10000);
					}
				}
			}*/
			
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
		
		/* now we can work with the serialized graph it take less than some milliseconds*/
		//Parse parse = new Parse();
		//Graph g = parse.getGraph();
		Graph g = null;
		// deserialize graph
		GraphSerialized deserialized  = null;
		try {
			deserialized = GraphSerialized.deSerialise("ressources/graph") ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"failed to load the graph");
		}
		g = deserialized.getSerializedGraph();
		
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
		List<Node> chemin = g.Dijkstra(g.node,"La Courneuve-8-Mai-1945","Buttes-Chaumont","TOUS","OK");
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
		
		String messageDisplay = "";
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
		
		System.out.println(" jusqua : " + chemin.get(0).town);
	}


}
