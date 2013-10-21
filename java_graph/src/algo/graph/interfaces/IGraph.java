package algo.graph.interfaces;
import java.util.List;

import algo.graph.interfaces.IRelation;

public interface IGraph {
	public void addRoute( String n1, String n2, int Weight);
	public void findRoute2( String start, String target);
	public List<IRelation> findRoute(String start, String end);

}
