package test;

import java.util.HashMap;
import java.util.Map;

import algo.graph.implementation.Gragh;
import algo.graph.implementation.Node;

public class Application {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String , Node> nodesTest = new HashMap<String,Node>();
		nodesTest.put("Paris", new Node("Paris"));
		nodesTest.put("Nantes", new Node("Nantes"));
		nodesTest.put("Bordeaux", new Node("Bordeau"));
		nodesTest.put("Mulhouse", new Node("Mulhouse"));
		nodesTest.put("Lyon", new Node("Lyon"));
		nodesTest.put("Valence", new Node("Valence"));
		nodesTest.put("Montpellier", new Node("Montpellier"));
		nodesTest.put("Biarritz", new Node("Biarritz"));
		nodesTest.put("Grenoble", new Node("Grenoble"));
		nodesTest.put("Geneve", new Node("Geneve"));
		nodesTest.put("Gap", new Node("Gap"));
		nodesTest.put("Nice", new Node("Nice"));
		
		
		Gragh carte = new Gragh(nodesTest);
//		carte.addRoute("Paris", "Nantes", 50);
//		carte.addRoute("Paris", "Lyon", 450);
//		carte.addRoute("Paris", "Mulhouse", 200);
//		carte.addRoute("Lyon", "Valence", 15);
//		carte.addRoute("Lyon", "Grenoble", 100);
//		carte.addRoute("Mulhouse", "Geneve", 50);
//		carte.addRoute("Gap", "Nice", 3);
//		carte.addRoute("Bordeaux", "Biarritz", 20);
		
		carte.addRoute("Lille", "Paris", 100);
		carte.addRoute("Paris", "Mulhouse", 200);	
		carte.addRoute("Mulhouse", "Strasbourg", 15);
		carte.addRoute("Mulhouse", "Genève", 50);
		carte.addRoute("Genève", "Grenoble", 10);
		carte.addRoute("Grenoble", "Gap", 5);
		carte.addRoute("Gap", "Nice", 3);
		carte.addRoute("Grenoble", "Lyon", 100);
		carte.addRoute("Lyon", "Valence", 15);
		carte.addRoute("Valence", "Montpellier", 30);
		carte.addRoute("Paris", "Nantes", 50);
		carte.addRoute("Nantes", "Bordeaux", 500);
		carte.addRoute("Bordeaux", "Biarritz", 20);
		carte.addRoute("Biarritz", "Affrique du sud", 200000);
		
		carte.showRelations();
		System.out.println("\n");
		carte.findRoute("Paris", "Valence");
	}

}
