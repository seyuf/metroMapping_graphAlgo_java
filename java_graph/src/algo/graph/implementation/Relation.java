package algo.graph.implementation;

import algo.graph.interfaces.IRelation;
import algo.graph.interfaces.Inode;

public class Relation implements IRelation {
	
	
	public Inode 	 nodeEnd;
	public Inode     nodeStart;
	public int 		 weight;
	
	public Relation(Inode nodeStartIn, Inode nodeEndIn, int weightIn){
		this.nodeStart = nodeStartIn;
		this.nodeEnd   = nodeEndIn; 
		this.weight	   = weightIn;
	}

	@Override
	public Inode getStartNode() {
		// TODO Auto-generated method stub
		return this.nodeStart;
		
	}

	@Override
	public Inode getEndNode() {
		// TODO Auto-generated method stub
		return this.nodeEnd;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}
	

}
