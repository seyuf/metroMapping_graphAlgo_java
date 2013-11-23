package algo.graph.implementation.interfaces;
import algo.graph.implementation.Node;

public interface IRelation
{
	public Node getStartNode();
	public Node getEndNode();
	public int getWeight();
	public String getmodeTransport();
	public void setStartNode(Node nodeStart);
	public void setEndNode(Node nodeEnd);
	public String getLigneTransport();
}