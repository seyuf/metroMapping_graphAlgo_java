package algo.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import algo.graph.interfaces.INode;
import algo.graph.interfaces.IRelation;

public class Node implements INode, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1858797871135600787L;
	private List<IRelation> relation;
	public String town;
	public Node previousNode;
	public String latitude;
	public String longitude;
	public Integer weight;
	public String line;
	
	public Node()
	{
		relation = new ArrayList<IRelation>();
		weight = new Integer(0);
	}
	
	public String toString()
	{
		return town;
	}
	
	public Node(String ville,String lat,String lon)
	{
		town = ville;
		latitude = lat;
		longitude = lon;
		relation = new ArrayList<IRelation>();
	}
	
	public String getTown()
	{
		return town;
	}
	
	public List<IRelation> getRelations() 
	{
		return relation;
	}
	
	public void addRelation(IRelation rel) 
	{
		relation.add(rel);
	}
	
	public void setRelation(List<IRelation> relat)
	{
		try
		{
			relation = relat;
		}
		catch(Exception E)
		{
			
		}
	}
}
