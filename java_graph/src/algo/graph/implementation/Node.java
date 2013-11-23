package algo.graph.implementation;

import java.util.ArrayList;
import java.util.List;

import algo.graph.implementation.interfaces.INode;
import algo.graph.implementation.interfaces.IRelation;

public class Node implements INode
{
	private List<IRelation> relation;
	public String town;
	public Node nodePrecedent;
	public String latitude;
	public String longitude;
	public Integer poids;
	public String ligne;
	
	public Node()
	{
		relation = new ArrayList<IRelation>();
		poids = new Integer(0);
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
