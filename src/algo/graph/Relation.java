package algo.graph;

import algo.graph.interfaces.INode;
import algo.graph.interfaces.IRelation;

public class Relation implements IRelation
{
	private Node nodeEnd;
	private Node nodeStart;
	private int weight;
	public String ligneTransport;
	public String modeTransport;
	
	public Relation(Node Node1,Node Node2,int w,String ligne,String mode)
	{
		nodeStart = Node1;
		nodeEnd = Node2;
		weight = w;
		ligneTransport = ligne;
		modeTransport = mode;
	}
	
	public String getLigneTransport() 
	{
		return ligneTransport;
	}
	
	public void setLigneTransport(String ligne)
	{
		try
		{
			ligneTransport = ligne;
		}
		catch(Exception E)
		{
			
		}
	}
	
	public String getmodeTransport() 
	{
		return modeTransport;
	}
	
	public void setmodeTransport(String mode)
	{
		try
		{
			modeTransport = mode;
		}
		catch(Exception E)
		{
			
		}
	}
	
	public Node getStartNode() 
	{
		return nodeStart;
	}
	
	public void setStartNode(Node nodeStart)
	{
		try
		{
			this.nodeStart = nodeStart;
		}
		catch(Exception E)
		{
			
		}
	}

	public Node getEndNode() 
	{
		return nodeEnd;
	}
	
	public void setEndNode(Node nodeEnd)
	{
		try
		{
			this.nodeEnd = nodeEnd;
		}
		catch(Exception E)
		{
			
		}
	}
	
	public void setWeight(int Weight)
	{
		try
		{
			weight = Weight;
		}
		catch(Exception E)
		{
			
		}
	}


	public int getWeight() 
	{
		return weight;
	}

}
