package algo.graph.interfaces;

import java.util.List;
import java.util.Map;

import algo.graph.Node;
import algo.graph.parsing.Stops;

public interface IGraph
{
	public void addRoute(Stops start, Stops target, int weight,String ligne, String mode);
	public void findRoute(String start,String target);
	public void Init(Map<String, Node> node,Node depart);
	public Integer Weight(Node start,Node end);
	public Node Trouve_min(List<Node> listGraph);
	List<Node> Dijkstra(Map<String, Node> graph,String depart,String fin,String modeTransport,String critere);
	public void createListGraph(Map<String, Node> graphTotal,List<Node> listNode,String modeTransport);
}