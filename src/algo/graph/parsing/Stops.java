package algo.graph.parsing;

public class Stops 
{
	public String stop_id;
	public String stop_name;
	public String stop_lat;
	public String stop_long;
	public String parent;
	public String nameLigne;
	
	public Stops(String id,String name,String lat,String lon,String paren)
	{
		stop_id = id;
		stop_name = name;
		stop_lat = lat;
		stop_long = lon;
		parent = paren;
		nameLigne = "";
	}
}
