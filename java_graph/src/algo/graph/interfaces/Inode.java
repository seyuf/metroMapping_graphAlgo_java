package algo.graph.interfaces;
import java.util.List;

import algo.graph.implementation.Relation;
import algo.graph.interfaces.IRelation;

public interface Inode {
	public List<IRelation> getRelation();
	public void addRelation(Relation r);

}
