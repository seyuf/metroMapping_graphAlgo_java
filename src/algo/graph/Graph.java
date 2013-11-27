package algo.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algo.graph.Node;
import algo.graph.interfaces.IGraph;
import algo.graph.parsing.Parse;
import algo.graph.parsing.Stops;

public class Graph implements IGraph
{
	public Map<String, Node> node;
	private ArrayList<Compare> listRelation;
	
	public Graph()
	{
		node = new HashMap<String, Node>();
		listRelation = new ArrayList<Compare>();
	}
	
	public void addRoute(Stops start, Stops target, int weight,String ligne, String mode) 
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
				n1.poids = 0;
				node.put(start.stop_name,n1);
			}
			
			if(n2 == null)
			{
				n2 = new Node(target.stop_name,target.stop_lat,target.stop_long);

				int poidsn1 = 0;
				if(n1.poids != null)
					poidsn1 = n1.poids;
				
				n2.poids = poidsn1 + weight;
				node.put(target.stop_name,n2);
			}
			
			Relation r = new Relation(n1,n2,weight,ligne,mode);
			
			listRelation.add(comp);
			n1.addRelation(r);
			n2.addRelation(r);
		}
	}
	
	
	/*Initialisation(G,sdeb)
	1 pour chaque point s de G
	2    faire d[s] := infini              on initialise les sommets autres que sdeb � infini 
	3 d[sdeb] := 0 */
	
	public void Init(Map<String, Node> node,Node depart)
	{
		for(String ville : node.keySet())
		{
			if(!ville.equals(depart.town))
			{
				node.get(ville).poids = Integer.MAX_VALUE;
				node.get(ville).nodePrecedent = null;
			}
		}
		
		node.get(depart.town).poids = 0;
	}
	
	public Integer Poids(Node start,Node end)
	{
		for(int i = 0 ; i < node.get(start.town).getRelations().size() ; i++)
		{
			if(node.get(start.town).getRelations().get(i).getEndNode().toString().equals(end.town))
				return node.get(start.town).getRelations().get(i).getWeight();			
		}
		
		return 0;
	}

	public void createListGraph(Map<String, Node> graphTotal,List<Node> listNode,String modeTransport)
	{
		if(modeTransport == "TOUS")
		{
			for(String ville : graphTotal.keySet())
				listNode.add(graphTotal.get(ville));
		}
		
		else
		{	
			for(String ville : graphTotal.keySet())
			{
				//listNode.add(graphTotal.get(ville));
				for(int i = 0 ; i < graphTotal.get(ville).getRelations().size() ; i++)
				{
					if(graphTotal.get(ville).getRelations().get(i).getStartNode().toString().equals(ville))
					{
						if(graphTotal.get(ville).getRelations().get(i).getLigneTransport().equals(modeTransport))
							listNode.add((Node) graphTotal.get(ville).getRelations().get(i).getEndNode());
					}
				}
			}
		}
	}
	
	public List<Node> Dijkstra(Map<String, Node> graph,String depart,String fin,String modeTransport)
	{
		try
		{
			// Initialisation des sommets du graph
			Init(graph,graph.get(depart));
			
			// Creation liste avec tous les points
			List<Node> listNode = new ArrayList<Node>();
			createListGraph(graph,listNode,modeTransport);
			
			Node n1 = new Node();
			
			// for(Node node : listNode) System.out.println(node.town + " : " + node.poids);
			
			// Parcourt de la liste tant que celle-ci est vide
			while(listNode.size() != 0)
			{
				n1 = Trouve_min(listNode);
				listNode.remove(n1);
				
				for(int i = 0 ; i < graph.get(n1.town).getRelations().size() ; i++)
				{				
					if(graph.get(n1.town).getRelations().get(i).getStartNode().town.equals(n1.town))
					{
						if(graph.get(n1.town).getRelations().get(i).getEndNode().poids > graph.get(n1.town).getRelations().get(i).getStartNode().poids + Poids(graph.get(n1.town).getRelations().get(i).getStartNode(),graph.get(n1.town).getRelations().get(i).getEndNode()))
						{
							graph.get(n1.town).getRelations().get(i).getEndNode().poids = graph.get(n1.town).getRelations().get(i).getStartNode().poids + Poids(graph.get(n1.town).getRelations().get(i).getStartNode(),graph.get(n1.town).getRelations().get(i).getEndNode());
							graph.get(n1.town).getRelations().get(i).getEndNode().nodePrecedent = graph.get(n1.town).getRelations().get(i).getStartNode();
						}
					}
				}
				
			}
			
			List<Node> chemin = new ArrayList<Node>();
			Node n = graph.get(fin);
			
			while(n != graph.get(depart))
			{
				chemin.add(n);
				n = n.nodePrecedent;
			}
			chemin.add(graph.get(depart));
			
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
			if(min.poids > listGraph.get(i).poids)
				min = listGraph.get(i);
		}
		
		return min;
	}

	public void findRoute(String start, String target) 
	{
		// Algorithme de Dijkstra
		 for(String ville : node.keySet())
		 {
			System.out.println("Poids de " + ville + " : " + node.get(ville).poids);
			
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
		
		List<Node> chemin = g.Dijkstra(g.node,"Châtelet","Solférino","TOUS");
		
		String ligne = "";
		for(int i = chemin.size()-1 ; i >= 0  ; i--)
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
		}
		
		String messageDisplay = "";
		String ligneTemp = "";
		ligneTemp = chemin.get(chemin.size()-1).ligne;

		System.out.println("De " + chemin.get(chemin.size()-1).town + " : " + chemin.get(chemin.size()-1).ligne);
		for(int i = chemin.size()-1 ; i >= 0  ; i--)
		{
			if(!ligneTemp.equals(chemin.get(i).ligne))
			{
				System.out.println(" jusqua : " + chemin.get(i).town);

				System.out.println("De " + chemin.get(i).town + " : " + chemin.get(i).ligne);
			}
			ligneTemp = chemin.get(i).ligne;
		}
		
		System.out.println(" jusqua : " + chemin.get(0).town);
		
		//System.out.println(g.node.get("Chatelet").town);
		/*for(int i = 0 ; i < g.node.get("La Courneuve-Aubervilliers").getRelations().size() ; i++)
		{
			System.out.println(g.node.get("La Courneuve-Aubervilliers").getRelations().get(i).getStartNode().town + " => " + g.node.get("La Courneuve-Aubervilliers").getRelations().get(i).getEndNode().town);
		}*/
		
		//g.findRoute("","");
		//g.Dijkstra(g.node,"A","J");
		// g.Trouve_min(g.node,"C","J");
		// g.findRoute("Lyon","Nice");
		
		//Parse parse = new Parse();
		//Graph g = parse.getGraph();
		//g.findRoute("","");
	}

}
