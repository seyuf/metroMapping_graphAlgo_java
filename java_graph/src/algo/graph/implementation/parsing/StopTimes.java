package algo.graph.parsing;

public class StopTimes 
{
	public String trips_id;
	public String stop_id;
	public String date_depart;

	public StopTimes(String trips,String stop,String dateDepart)
	{
		trips_id = trips;
		stop_id = stop;
		date_depart = dateDepart;
	}
}
