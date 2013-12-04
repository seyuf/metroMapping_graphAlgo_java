package algo.graph;

import java.io.Serializable;

public class Compare implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2202438351223634341L;
	public String depart;
	public String arrive;
	
	public Compare(String dep, String arr)
	{
		depart = dep;
		arrive = arr;
	}
}
