package serialize;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import algo.graph.Graph;

public class GraphSerialized implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1517789967740989086L;
	protected  Graph graph = null ;


	public GraphSerialized ( Graph graphIn )  {

		graph = graphIn;

	}
	public Graph getSerializedGraph() {
		return graph;
	}


	public void setGraph(Graph graphIn) {
		this.graph = graphIn;
	}





	public void serialise( String fileOfSerie) throws IOException{

		FileOutputStream fileIn = new FileOutputStream(fileOfSerie) ;
		ObjectOutputStream Oos = new ObjectOutputStream(fileIn) ;
		Oos.writeObject(this) ;
		Oos.close() ;

	}

	public static GraphSerialized deSerialise( String fileOfSerie) throws IOException{
		GraphSerialized ov1 = null ;
		FileInputStream fileIn = new FileInputStream(fileOfSerie) ;
		System.out.println(" openning deserialisation file ... ") ;
		ObjectInputStream Oos = new ObjectInputStream(fileIn) ;
		try {
			ov1 = ( GraphSerialized ) Oos.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(" failed to deserialize") ;
			e.printStackTrace();
		}
		Oos.close() ;
		return ov1 ;

	}

}
