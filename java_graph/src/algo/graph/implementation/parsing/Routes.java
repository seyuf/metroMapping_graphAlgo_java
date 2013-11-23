package algo.graph.implementation.parsing;

public class Routes 
{
	public String route_id;
	public String line;
	public String modeTransport;
	
	public Routes(String routes,String ligne,String modeDeTransport)
	{
		route_id = routes;
		line = ligne;
		modeTransport = modeDeTransport;
	}
}