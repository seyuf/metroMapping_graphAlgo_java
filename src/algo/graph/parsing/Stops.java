package algo.graph.parsing;

import java.io.Serializable;

import algo.graph.interfaces.IGraph;

public class Stops implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String stop_id;
	public String stop_name;
	public String stop_lat;
	public String stop_long;
	public String parent;
	
	public Stops(String id,String name,String lat,String lon,String paren)
	{
		stop_id = id;
		stop_name = name;
		stop_lat = lat;
		stop_long = lon;
		parent = paren;
	}

}
