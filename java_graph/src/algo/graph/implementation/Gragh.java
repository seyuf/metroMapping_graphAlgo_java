package algo.graph.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import algo.graph.implementation.Node;
import algo.graph.interfaces.IGraph;
import algo.graph.interfaces.IRelation;

public class Gragh implements IGraph {
	
	public Map<String , Node> nodes ;

	public Gragh(){
		nodes = new HashMap<String,Node>();
	}
	public Gragh( Map<String, Node> graphIn ){
		this.nodes = graphIn;
	}
	@Override
	public void addRoute(String n1, String n2, int weightIn) {
		// TODO Auto-generated method stub
		Node nd1 = this.nodes.get(n1);
		Node nd2 = this.nodes.get(n2);
		
		if(null == nd1){
			nd1 = new Node(n1);
			nodes.put(n1, nd1);
		}
		if(null == nd2){
			nd2 = new Node(n2);
			nodes.put(n2, nd2);
		}
		nd1 = this.nodes.get(n1);
		nd2 = this.nodes.get(n2);
		
		Relation r = new Relation(nd1, nd2, weightIn);
		nd1.addRelation(r);
		nd2.addRelation(r);
	}
	
	
	
	
	
	public List<IRelation> findRoute(String start, String end ){
		
		Node tmp_node = this.nodes.get(start);
		PriorityQueue<Relation> pq = new PriorityQueue<Relation>(
				10, new Comparator<Relation>() {
					@Override
					public int compare(Relation o1, Relation o2) {
						System.out.println(" | Comparing "+((Node)o1.getStartNode()).town+" <-> "+((Node)o1.getEndNode()).town+" ("+o1.getWeight()+
													") to "+((Node)o2.getStartNode()).town+" <-> "+((Node)o2.getEndNode()).town+" ("+o2.getWeight()+
													") : "+(o1.weight>o2.weight?o2.weight:o1.weight));
						return Math.max(o1.weight, o2.weight);
					}
				});
		ArrayList<Relation> solutionPath = new ArrayList<Relation>(),
							consideredPath = new ArrayList<Relation>();
		
		boolean arrived = false;
		int w = 0;
		// tant qu'on est pas arrivé
		while(!arrived) {
			// On met toute les relations de la node actuelle dans la PriorityQueue
			System.out.println("We are in "+tmp_node.town);
			for(IRelation r:tmp_node.getRelation()) {
				if (!solutionPath.contains(r)) {
					if(!consideredPath.contains(r)) {
						pq.add((Relation) r);
					}
				}
			}
			// On récupère la meilleure Relation dans la PriorityQueue
			Relation tmp_r = pq.poll();
			
			w+=tmp_r.getWeight();
			solutionPath.add(tmp_r);
			consideredPath.add(tmp_r);
			
			// On "se déplace" sur la node indiquée par la meilleure relation pour ce tour
			if (((Node) tmp_r.nodeEnd).town.equals(tmp_node.town)) {
				System.out.println(" +-- Moving to "+((Node)tmp_r.nodeEnd).town+" ("+w+")");
				tmp_node = (Node) tmp_r.nodeStart;
			} else {
				System.out.println(" +-- Moving to "+((Node)tmp_r.nodeEnd).town+" ("+w+")");
				tmp_node = (Node) tmp_r.nodeEnd;
			}
			
			// Si on est sur la ville d'arrivée, on est arrivé
			if (((Node) tmp_node).town.equals(end))
				arrived=true;
		}
		System.out.println("We are now in "+((Node)tmp_node).town+" !\n");
		return null;
	}

	@Override
	public void findRoute2(String start, String target) {
		// TODO Auto-generated method stub
		int min = 0;
		Node tmpNode = new Node();
		
		while( tmpNode != null){
			tmpNode = this.nodes.get(start);
			 System.out.println("size Relations:"+tmpNode.getRelation().size());;
			
			int size = tmpNode.getRelation().size();
	
			for(int i = 0; i < size; i++){
				System.out.println("Relations for:"+tmpNode.town+"\n");
				tmpNode = (Node) tmpNode.getRelation().get(i).getEndNode();
			}
			for(int i = 0; i < size; i++){
				System.out.println();
			}
			int tmpSize = size;
				while(size-- != 0){
					if(min == 0){
						min = tmpNode.getRelation().get(size).getWeight();
					}else{
						if(min > tmpNode.getRelation().get(size).getWeight()){
							min = tmpNode.getRelation().get(size).getWeight();
						}
					}
				}
				size = tmpSize;
				while( min != tmpNode.getRelation().get(size).getWeight() ){
					size--;
				}
				tmpNode = (Node) tmpNode.getRelation().get(size).getStartNode();
				start = tmpNode.town;
				System.out.println(tmpNode.town);
		}	
	}
	
	public void showRelations( ){
			for(Node iter:nodes.values()){
				System.out.println("Relation of "+iter.town);
				for(IRelation v:iter.getRelation()){
					System.out.println(" + " + ((Node)v.getStartNode()).town + " <- " + v.getWeight() + " -> " +((Node)v.getEndNode()).town);
				}
			}
	}
}
