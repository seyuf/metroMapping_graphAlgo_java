package hibernateLocal;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Pathway;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Transfer;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.model.calendar.ServiceDate;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.onebusaway.gtfs.services.GtfsDao;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs.services.HibernateGtfsFactory;
import org.onebusaway.gtfs.services.calendar.CalendarService;

import algo.graph.parsing.StopTimes;






public class TestReading {


	private static final String KEY_CLASSPATH = "classpath:";

	  private static final String KEY_FILE = "file:";

	  @SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		  /*
	    if (!(args.length == 1 || args.length == 2)) {
	      System.err.println("usage: gtfsPath [hibernate-config.xml]");
	      System.exit(-1);
	    }
	    */
	    
	    
	   //String gtfsFiles = "src/ressources/";
	    //if (args.length == 2)
	    //resource = args[1];
		  // there are 99847 trips for metro
		
	    HibernateSession hyber = new HibernateSession();
	    HibernateGtfsFactory factory =  hyber.createHibernateGtfsFactory();
	    GtfsMutableRelationalDao dao = factory.getDao();
	    //Collection<Route> routes = dao.getAllRoutes();
	    //Collection<Trip> trips = dao.getAllTrips();
	    Collection<Stop> stops = dao.getAllStops();
	    for(Stop stop:stops){
	    	int tmp = Integer.parseInt(stop.getId().getId());
	    	int tmp1 = Integer.parseInt(stop.getId().getId())+1;
	    	int tmp2 = Integer.parseInt(stop.getId().getId())+2;
	    	
	    	Stop stop1 = dao.getStopForId(new AgencyAndId("100", String.valueOf(tmp1)));
	    	Stop stop2 = dao.getStopForId(new AgencyAndId("100", String.valueOf(tmp2)));
	    	//System.out.println(stop.getId().getId()+"     "+stop.getName());
	    	try{
	    	if(!stop.getName().equals(stop1.getName())){
	    		System.out.println(stop.getName()+"  "+stop1.getName());
	    		
	    		//dao.getStopTimesForStop(stop);
	    	}
	    	else if(!stop.getName().equals(stop2.getName())){
	    		System.out.println(stop.getName()+"  "+stop2.getName());
	    		
	    	}
	    	}catch(Exception e){
	    		
	    	}
	    }
	    
	   
	    /*
	     * 
	    int i = 0;
	    int sumTrips = 0;
	    //Collection<Stop> colStop = null;
	    //ArrayList<Stop> listStop = null;
	    List<StopTime> list = null;
	    try{
	    	for( Route route : routes ){

	    	
	    		if( route.getType() == 1)
	    		{
	    			//System.out.println("route:   "+route.getId().getId());
	    			//trips = dao.getAllTrips();
	    			for( Trip trip : trips){
	    				 
	    				//System.out.println("trip:   "+trip.getRoute().getId().getId());
	    				int tripId = Integer.parseInt(trip.getRoute().getId().getId());
	    				int routeId =Integer.parseInt(route.getId().getId());
	    				if( tripId == routeId ){
	    					sumTrips++;
	    					//System.out.println("trps "+tripId+"  route"+routeId);
	    					
	    					
	    					list = dao.getStopTimesForTrip(trip);
	    					
	    					
	    					
	    					//System.out.println(dao.getStopForId(route.getId()).getName());
	    				}
	    			}
	    			for(StopTime lis : list){
	    				System.out.println(lis.getStop().getName());
	    			}
	    		}
	    		

	    	}
	    	System.out.println("sumTrips"+sumTrips);
	    }catch( Exception e){
	    	e.printStackTrace();
	    	
	    }
	    
	    */
	    
	    
	    //GtfsReader reader = new GtfsReader();
	   // reader.setInputLocation(new File(gtfsFiles));

	   
	   
	    //reader.setEntityStore(dao);
	    //reader.run();
	    
	    /*
	     * 
	     Collection<Stop> stops = dao.getAllStops(); 
	    //Collection<StopTime> timeList  = null ;
	   
	    
	    try{
	    for(Stop stop : stops ){
	    	
	    	//System.out.println(stop.getId());
	    	//System.out.println(stop.getId().getId());
	    	//System.out.println(stop.getId().getAgencyId());
	    	 //for(Stop stop2 : stops ){
	    	//boolean b = timeList.add( dao.getStopTimeForId( Integer.parseInt(stop.getId().getAgencyId()))) ;
	    	//System.out.println("bool check " + b);
	    	System.out.println("stop times are: "+dao.getStopTimeForId(Integer.parseInt(stop.getId().getId())));
	    	//System.out.println("--------------------------------------------------------");
	    	//System.out.println( stop.getName()+"  <--------->   "
	    	//+(dao.getStopTimeForId(Integer.parseInt(stop.getId().getId()))).getStop().getName());
	       // int stopId = stop.getId().toString()
	    	
	    	//if( Integer.parseInt(stop2.getId().getId()) == Integer.parseInt(stop.getId().getId())+2){
	    	//	System.out.println(stop.getName()+ "     <--------------->     "+ stop2.getName());
	    	//	break;
	    	//}
	    	 //}
	    }
	    }catch( Exception e){
	    	
	    }  
	    
	    
	    Collection<Trip> trips = dao.getAllTrips();
	    Collection<Stop> stops = null;
	    int i = 0;
	    
	    try{
		    for(Trip trip : trips ){
		    	if( trip.getRoute().getType() == 1){
		    		//System.out.println( dao.getStopTimeForId(Integer.parseInt(trip.getId().getId())).getStop().getName());
		    		//System.out.println(dao.getStopTimeForId(Integer.parseInt(trip.getId().getId())).getStop().getName());
		    		//System.out.println(trip.getId().getAgencyId());
		    		i++;
		    	//System.out.println((dao.getStopTimesForTrip(trip)).get(2).getStop().getName());
		    		
		    		
		    		System.out.println(i);
		    	
		    	}
		    	//System.out.println(trip);
		    }
		    //System.out.println("taille"+stops.size());
	    }
	    catch( Exception e){
	    	System.out.println(e.toString());
	    }
	    */
	    

	    /*
	   
	 
	  Collection<Trip> trips = dao.getAllTrips();
	    
	    try{
		    for(Trip trip : trips ){
		    	//if( trip.getRoute().getType() == 1){
		    		//System.out.println( dao.getStopTimeForId(Integer.parseInt(trip.getId().getId())).getStop().getName());
		    	//}
		    	System.out.println(trip);
		    }
	    }
	    catch( Exception e){
	    	
	    }
	    // check if the station is a subway or something else 
	    
	    // check the pathways
	    Collection<Pathway> paths = dao.getAllPathways();

	    try{
	    	for( Pathway path : paths ){
	    		
	    		System.out.println(path.toString());

	    		
	    	}
	    }catch( Exception e){
	    	
	    }
	    
	  
	   
	    Collection<Trip> trips = dao.getAllTrips();

	    try{
	    	for(Trip trip : trips ){

	    		System.out.println(Integer.parseInt((trip.getRoute().getId().getAgencyId())));

	    	}
	    }catch( Exception e){

	    }

	    
	    
	    try{
	    	//System.out.println( timeList.get(0).getArrivalTime());
	    	for(StopTime time : timeList ){
	    		System.out.println( "this is stop time"+time.getArrivalTime());
	    	}
	    }catch( Exception e){

	    }
	  
	 
	  
	    for (Stop stop : stops)
	      System.out.println(stop.getName());
	    
	    for(Trip trip : trips)
	      System.out.println(trip.getRoute());
	    */
	    
	  }
	  
	    /*

	    CalendarService calendarService = factory.getCalendarService();
	    Set<AgencyAndId> serviceIds = calendarService.getServiceIds();

	    for (AgencyAndId serviceId : serviceIds) {
	      Set<ServiceDate> dates = calendarService.getServiceDatesForServiceId(serviceId);
	      ServiceDate from = null;
	      ServiceDate to = null;
	      for (ServiceDate date : dates) {
	        from = min(from, date);
	        to = max(to, date);
	      }

	      System.out.println("serviceId=" + serviceId + " from=" + from + " to="
	          + to);
	    }
	  }

	  private static ServiceDate min(ServiceDate a, ServiceDate b) {
	    if (a == null)
	      return b;
	    if (b == null)
	      return a;
	    return a.compareTo(b) <= 0 ? a : b;
	  }

	  private static ServiceDate max(ServiceDate a, ServiceDate b) {
	    if (a == null)
	      return b;
	    if (b == null)
	      return a;
	    return a.compareTo(b) <= 0 ? b : a;
	  }
*/
	  
	 

}