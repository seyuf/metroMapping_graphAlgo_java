package algo.graph.implementation;

import java.util.ArrayList;
import java.util.List;

import algo.graph.interfaces.IRelation;
import algo.graph.interfaces.Inode;

public class Node  implements Inode {

	public List<IRelation> relationList;
	public String town;
	
	public Node(String townIn){
		this.town = townIn;
		relationList = new ArrayList<IRelation>();
	}
	
	public Node(){
		town = null;
		relationList = new ArrayList<IRelation>();
	}
	@Override
	public List<IRelation> getRelation() {
		// TODO Auto-generated method stub
		return this.relationList;
	}

	public void addRelation(Relation r) {
		// TODO Auto-generated method stub
		try{
		this.relationList.add(r);
		}catch(Exception e){
			System.out.println("relation is not set");
		}
	}
}
