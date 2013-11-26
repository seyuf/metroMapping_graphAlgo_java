package algo.graph.parsing;

import hibernateLocal.HibernateSession;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs.services.HibernateGtfsFactory;

import algo.graph.Graph;

public class Parse 
{
	public static ArrayList<Item> relationString = new ArrayList<Item>();
	public static ArrayList<Geo> coordonneesStops = new ArrayList<Geo>();
	public static ArrayList<Routes> routes = new ArrayList<Routes>();
	public static ArrayList<StopTimes> stop_times = new ArrayList<StopTimes>();
	public static ArrayList<Trips> trips = new ArrayList<Trips>();
	public static ArrayList<Stops> stops = new ArrayList<Stops>();
	Graph g = new Graph();
	
	public Parse()
	{
		
	}
	
	public String getTransportation(int type){
		if(type == 1)
			return "METRO";
		else if(type == 2)
			return "RER";
		else if(type == 3)
			return "BUS";
		else if (type == 0)
			return "TRAM";
		else
			return "NONE";

		
	}
	public Graph getGraph()
	{
		// Cr�ation du Graph a retourner
		try
		{
		
		    HibernateSession hyber = new HibernateSession();
		    HibernateGtfsFactory factory =  hyber.createHibernateGtfsFactory();
		    GtfsMutableRelationalDao dao = factory.getDao();
		    
		    /** fill routes **/
		    Collection<Route> routesDB = dao.getAllRoutes();
		    for( Route route: routesDB){
		    	routes.add(new Routes(route.getId().getId(), route.getShortName(),getTransportation(route.getType())));
		    }
		    
		    /** fill stops **/
		    Collection<Stop> stopsDB = dao.getAllStops();
		    for( Stop stop: stopsDB){
		    	
		    	stops.add(new Stops(stop.getId().getId(), stop.getName(),
		    			Double.toString(stop.getLat()), Double.toString(stop.getLon()), stop.getParentStation()));
		    	//StopTime stopTime = dao.getStopTimeForId(Integer.parseInt(stop.getId().getId()));
		    	//stop_times.add( new StopTimes(stopTime.getTrip().getId().getId(),
		    		//	stopTime.getStop().getId().getId(),Integer.toString( stopTime.getArrivalTime())));
		    	
		    }
		    
		    
			// Chargement du fichier permettant de r�cup�rer les correspondances et les d�lais entre les stations 
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
			
			
			/*** fill stop times *****/
			/* fill stops 
		    Collection<StopTime> stopTimesDB = dao.getAllStopTimes();
		    for( StopTime stopTime: stopTimesDB){
		    	
		    	stop_times.add( new StopTimes(stopTime.getTrip().getId().getId(),
		    			stopTime.getStop().getId().getId(),Integer.toString( stopTime.getDepartureTime())));
		    	
		    }
		    */
			
			
			/* fill trips **/
			 Collection<Trip> tripsDB = dao.getAllTrips();
			    for( Trip trip: tripsDB){
			    	trips.add(new Trips(trip.getRoute().getId().getId(), trip.getId().getId()));
			    	
			    }

		
			
			String fichierGeo= "geo.csv";
			InputStream ipsGeo = new FileInputStream(fichierGeo); 
			InputStreamReader ipsrGeo = new InputStreamReader(ipsGeo);
			BufferedReader brGeo = new BufferedReader(ipsrGeo);
			
			String ligneGeo;
			brGeo.readLine();
			while ((ligneGeo = brGeo.readLine()) != null)
			{
				String [] splitGeo = ligneGeo.split("#");
				coordonneesStops.add(new Geo(splitGeo[0],splitGeo[1],splitGeo[2]));
			}
			brGeo.close();
			

			for(int i = 0 ; i < stops.size() ; i++)
			{
				for(int j = 0 ; j < coordonneesStops.size() ; j++)
				{
					if(coordonneesStops.get(j).id.equals(stops.get(i).parent))
					{						
						stops.get(i).stop_lat = coordonneesStops.get(j).latitude;
						stops.get(i).stop_long = coordonneesStops.get(j).longitude;
					}
				}
			}
			
			/* System.out.println("Nombre de routes : " + routes.size());
			System.out.println("Nombre de Stop times : " + stop_times.size());
			System.out.println("Nombre de trips : " + trips.size());
			System.out.println("Nombre d'arr�t : " + stops.size()); */
			
			System.out.println("Parsing termin�");
			System.out.println("");
			
			// Boucle permettant de stockers les diff�rentes correspondance
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
				
				Stops depart = null;
				Stops arrive = null;

				for(int i = 0 ; i < relationString.size() ; i++)
				{
					if(i < relationString.size() - 1)
					{
						for(Stops stop : stops)
						{
							if(stop.stop_id.equals(relationString.get(i).depart))
								depart = stop;
							
							if(stop.stop_id.equals(relationString.get(i+1).depart))
								arrive = stop;
						}
						
						int hDepart =  Integer.parseInt(relationString.get(i).date.substring(0,2));	
						int minDepart =  Integer.parseInt(relationString.get(i).date.substring(3,5));
						int hFinal = Integer.parseInt(relationString.get(i+1).date.substring(0,2));
						int minFinal =  Integer.parseInt(relationString.get(i+1).date.substring(3,5));
												 
						// conversion heures et minutes => minutes
						int mTotal1 = hDepart*60+minDepart;
						int mTotal2 = hFinal*60+minFinal;
						 
						// calcul de l'�cart en minutes
						int ecart = mTotal2-mTotal1;
									
						g.addRoute(depart,arrive,ecart,relationString.get(i).modeTransport,relationString.get(i).ligne);
					}
				}
				relationString.clear();
			}

		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return g;
	}
}
