package algo.graph.parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import algo.graph.Graph;

public class Parse 
{
	ArrayList<Item> relationString = new ArrayList<Item>();
	ArrayList<Routes> routes = new ArrayList<Routes>();
	ArrayList<StopTimes> stop_times = new ArrayList<StopTimes>();
	ArrayList<Trips> trips = new ArrayList<Trips>();
	ArrayList<Stops> stops = new ArrayList<Stops>();
	Graph g = new Graph();
	
	public Parse()
	{
		
	}
	
	public Graph getGraph()
	{
		// Création du Graph a retourner
		try
		{
			// Chargement du fichier permettant de récupérer toutes les routes du réseaux RATP/SNCF
			String fichierRoutes ="routes.txt";
			InputStream ips = new FileInputStream(fichierRoutes); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligneRoutes;
			String numTrips_ID = "";
			
			br.readLine();
			String modeTransport = "";
			
			while ((ligneRoutes = br.readLine()) != null)
			{
				String [] splitRoutes = ligneRoutes.split(",");
				
				if(splitRoutes[2].contains("T"))
					modeTransport = "TRAM";
				
				if(splitRoutes[2].equals("A") || splitRoutes[2].equals("B"))
					modeTransport = "RER";
				
				else
					modeTransport = "METRO";
				
				// Traitement mode de transport
				routes.add(new Routes(splitRoutes[0],splitRoutes[2],modeTransport));		
			}
			br.close();
			
			// Chargement du fichier permettant de récupérer les correspondances et les délais entre les stations 
			String fichierStop_Times = "stop_times.txt";
			InputStream ipsStop_Times = new FileInputStream(fichierStop_Times); 
			InputStreamReader ipsrStop_Times = new InputStreamReader(ipsStop_Times);
			BufferedReader brStop_Times = new BufferedReader(ipsrStop_Times);
			
			String ligneStop_Times;
			brStop_Times.readLine();
			while((ligneStop_Times = brStop_Times.readLine()) != null)
			{
				String [] splitStop_Times  = ligneStop_Times.split(",");
				stop_times.add(new StopTimes(splitStop_Times[0],splitStop_Times[3],splitStop_Times[1]));
			}
			
			brStop_Times.close();
			
			// Parcourt du fichier permettant de recupérer les chemins des différentes lignes du reseau
			String fichierTrips = "trips.txt";
			InputStream ipsTrips = new FileInputStream(fichierTrips); 
			InputStreamReader ipsrTrips = new InputStreamReader(ipsTrips);
			BufferedReader brTrips = new BufferedReader(ipsrTrips);
			
			String ligneTrips;
			brTrips.readLine();
			while ((ligneTrips = brTrips.readLine()) != null)
			{
				String [] splitTrips = ligneTrips.split(",");
				trips.add(new Trips(splitTrips[0],splitTrips[2]));
			}
			brTrips.close();
			

			// Parcourt du fichier permettant de recupérer les chemins des différentes lignes du reseau
			String fichierStops = "stops.txt";
			InputStream ipsStops = new FileInputStream(fichierStops); 
			InputStreamReader ipsrStops = new InputStreamReader(ipsStops);
			BufferedReader brStops = new BufferedReader(ipsrStops);
			
			String ligneStops;
			brStops.readLine();
			while ((ligneStops = brStops.readLine()) != null)
			{
				String [] splitStops = ligneStops.split(",");
				stops.add(new Stops(splitStops[0],splitStops[2],splitStops[3],splitStops[4]));
			}
			brStops.close();
			
			System.out.println("Nombre de routes : " + routes.size());
			System.out.println("Nombre de Stop times : " + stop_times.size());
			System.out.println("Nombre de trips : " + trips.size());
			System.out.println("Nombre d'arrêt : " + stops.size());
			
			System.out.println("FINISH");
			
			// Boucle permettant de stockers les différentes correspondance
			for(Routes route : routes)
			{
				for(Trips trip : trips)
				{
					if(trip.route_id.equals(route.route_id))
					{
						for(StopTimes st : stop_times)
						{
							if(st.trips_id.equals(trip.trips_id))
							{
								Item item = new Item(st.stop_id,st.date_depart,route.modeTransport,route.line);
								relationString.add(item);
							}
						}
					}
				}
				
				String depart = "";
				String arrive = "";
				// System.out.println("DEBUT DU PARCOURT");
				for(int i = 0 ; i < relationString.size() ; i++)
				{
					if(i < relationString.size() - 1)
					{
						for(Stops stop : stops)
						{
							if(stop.stop_id.equals(relationString.get(i).depart))
								depart = stop.stop_name;
							
							if(stop.stop_id.equals(relationString.get(i+1).depart))
								arrive = stop.stop_name;
						}
						
						int hDepart =  Integer.parseInt(relationString.get(i).date.substring(0,2));	
						int minDepart =  Integer.parseInt(relationString.get(i).date.substring(3,5));
						int hFinal = Integer.parseInt(relationString.get(i+1).date.substring(0,2));
						int minFinal =  Integer.parseInt(relationString.get(i+1).date.substring(3,5));
												 
						// conversion heures et minutes => minutes
						int mTotal1 = hDepart*60+minDepart;
						int mTotal2 = hFinal*60+minFinal;
						 
						// calcul de l'écart en minutes
						int ecart = mTotal2-mTotal1;
						
						g.addRoute(depart,arrive,ecart,relationString.get(i).ligne);
						// System.out.println(depart + " => " + arrive + " Time : " + relationString.get(i+1).date + " - " + relationString.get(i).date + " Mode de transport : " + relationString.get(i).modeTransport + " Ligne : " + relationString.get(i).ligne + " Ecart : " + ecart);
					}
				}
				// System.out.println("FIN DU PARCOURT");
				relationString.clear();
			}
		// Parcourt des fichiers pour la création du Graph
		/*try
		{
			String fichierRoutes ="routes.txt";
			InputStream ips = new FileInputStream(fichierRoutes); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligneRoutes;
			String numTrips_ID = "";
			
			br.readLine();
			
			while ((ligneRoutes = br.readLine()) != null)
			{
				String [] splitRoutes = ligneRoutes.split(",");
				
				// System.out.println("Numero de la route : " + splitRoutes[0] + " numero de la ligne : " + splitRoutes[2]);
				
				// Ne pas tenir compte des bus
				if(splitRoutes[2].length() < 3)
				{
				// Parcourt du fichier Trips
				String fichierTrips = "trips.txt";
				InputStream ipsTrips = new FileInputStream(fichierTrips); 
				InputStreamReader ipsrTrips = new InputStreamReader(ipsTrips);
				BufferedReader brTrips = new BufferedReader(ipsrTrips);
				
				String ligneTrips;
				brTrips.readLine();
				while ((ligneTrips = brTrips.readLine())!=null)
				{
					String [] splitTrips = ligneTrips.split(",");
					// System.out.println(ligneTrips);
					
					if(splitTrips[0].equals(splitRoutes[0]))
					{
						numTrips_ID = splitTrips[2];
						break;
					}
				}
				
			    // System.out.println("Numero de trips : " + numTrips_ID);
				
				// Parcourt du fichier stop_times.txt
				String fichierStop_Times = "stop_times.txt";
				InputStream ipsStop_Times = new FileInputStream(fichierStop_Times); 
				InputStreamReader ipsrStop_Times = new InputStreamReader(ipsStop_Times);
				BufferedReader brStop_Times = new BufferedReader(ipsrStop_Times);
				
				String ligneStop_Times;
				brStop_Times.readLine();
				while ((ligneStop_Times = brStop_Times.readLine()) != null)
				{
					String [] splitStop_Times  = ligneStop_Times.split(",");

					if(splitStop_Times[0].equals(numTrips_ID))
					{
						//System.out.println("Num : " + splitStop_Times[3]);

						Item item = new Item(splitStop_Times[3],splitStop_Times[2]);
						relationString.add(item);*/
			
						/*int ordre = 0 ;
						while(Integer.parseInt(splitStop_Times[4]) > ordre)
						{
							
							ligneStop_Times = brStop_Times.readLine();
							splitStop_Times  = ligneStop_Times.split(",");
							
							ordre = Integer.parseInt(splitStop_Times[4]);
							System.out.println("Num : " + splitStop_Times[3]);
						}*/
						
						/*//System.out.println("Numero de station : " + splitStop_Times[3] + " ordre : " + splitStop_Times[4]);
						
						String numStation = splitStop_Times[3];
						int ordre = Integer.parseInt(splitStop_Times[4]);
							
						// Parcourt 
						ligneStop_Times = brStop_Times.readLine();
						splitStop_Times  = ligneStop_Times.split(",");
						
						if(splitStop_Times[0].equals(numTrips_ID))
						{
							System.out.println("Ordre : " + ordre);
							if(ordre < Integer.parseInt(splitStop_Times[4]))
							{
								System.out.println(numStation + " => " + splitStop_Times[3]);
							}
						}
						
						else 
							break;*/
				/*	}
				}
				
				System.out.println("Pour le voyage : " + numTrips_ID);
				for(int i = 0 ; i < relationString.size() ; i++)
				{
					if(i < relationString.size() - 1)
					{
						System.out.println(relationString.get(i).depart + " => " + relationString.get(i+1).depart + " Time : " + relationString.get(i+1).date + " - " + relationString.get(i).date);
					}
				}
				
				System.out.println();
				}
			}*/
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return g;
		
		
	}


}
