package serialize;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import algo.graph.Graph;


/**
 * this class allows the serialization / de-serialization of an {@link Graph} object form a file
 * @author  ESGI Students COULIBALY // DA-COSTA // BEKAERT 
 * @version 1.0
 */
public class GraphSerialized implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1517789967740989086L;
	protected  Graph graph = null ;

	/**
	 * constructor set the serialization class
	 * 
	 */
	public GraphSerialized ( Graph graphIn )  {

		graph = graphIn;

	}
	/**
	 * return the de-serialized graph
	 * 
	 *  @return the graph 
	 */
	public Graph getSerializedGraph() {
		return graph;
	}


	public void setGraph(Graph graphIn) {
		this.graph = graphIn;
	}

	/**
	 * serialize the graph is some file
	 * 
	 * @param fileOfSerie the name of the file in which will be stored
	 * serialized graph
	 * @exception IOException will be thrown if the serialization fails
	 * @return none
	 */
	public void serialise( String fileOfSerie) throws IOException{

		FileOutputStream fileIn = new FileOutputStream(fileOfSerie) ;
		ObjectOutputStream Oos = new ObjectOutputStream(fileIn) ;
		Oos.writeObject(this) ;
		Oos.close() ;

	}
	

	/**
	 * this method will de-serialize an {@link GraphSerialized} object
	 * from the file allowing us to retrieve the graph in it
	 * 
	 * @param fileOfSerie the file in which the object have been serialized
	 * @return an {@link GraphSerialized}
	 */
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
