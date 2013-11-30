package hibernateLocal;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.onebusaway.gtfs.services.HibernateGtfsFactory;

public class HibernateSession {
	private static final String KEY_CLASSPATH = "classpath:";

	private static final String KEY_FILE = "file:";
	
	
	protected String resource = null;

	 public HibernateSession() {
		super();
		this.resource = "file:src/hibernateLocal/hibernate-configuration.xml";
	}

	public HibernateGtfsFactory createHibernateGtfsFactory() {

		    Configuration config = new Configuration();
             
		    
		    if (resource.startsWith(KEY_CLASSPATH)) {
		      resource = resource.substring(KEY_CLASSPATH.length());
		      config = config.configure(resource);
		    } else if (resource.startsWith(KEY_FILE)) {
		      resource = resource.substring(KEY_FILE.length());
		      config = config.configure(new File(resource));
		    } else {
		      config = config.configure(new File(resource));
		    }

		    SessionFactory sessionFactory = config.buildSessionFactory();
		    return new HibernateGtfsFactory(sessionFactory);
		  }

}
