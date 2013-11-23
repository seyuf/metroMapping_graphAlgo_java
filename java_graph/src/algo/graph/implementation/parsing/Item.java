package algo.graph.implementation.parsing;

public class Item 
{
	String depart;
	String date;
	String modeTransport;
	String ligne;
	
	public Item(String dep,String date,String modeDeTransport,String line)
	{
		depart = dep;
		this.date = date;
		modeTransport = modeDeTransport;
		ligne = line;
	}
}
