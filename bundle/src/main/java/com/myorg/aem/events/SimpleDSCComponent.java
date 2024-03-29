package com.myorg.aem.events;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
 
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.util.ArrayList;
import java.util.Arrays;
 
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
 
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
 
//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ; 
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
 
import org.apache.sling.jcr.api.SlingRepository;
 
/**
 * Just a simple DS Component
 */
@Component(immediate=true)
@Service
public class SimpleDSCComponent implements EventListener {
     
    private Logger log = LoggerFactory.getLogger(this.getClass());
     
    private BundleContext bundleContext;
     
     
    @Reference
    private SlingRepository repository;
     
  //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;
     
    private Session session;
     
    private ObservationManager observationManager;
     
    public void run() {
        log.info("Running...");
    }
     
     
    //Place app logic here to define the AEM Custom Event Handler
    protected void activate(ComponentContext ctx) {
        this.bundleContext = ctx.getBundleContext();
         
        try
        {
                    
            //Invoke the adaptTo method to create a Session 
            // ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
             session = repository.loginAdministrative(null);
              
            // Setup the event handler to respond to a new claim under content/claim.... 
                observationManager = session.getWorkspace().getObservationManager();
                 final String[] types = { "nt:unstructured","sling:Folder" };
                 final String path = "/aayush"; // define the path
                 observationManager.addEventListener(this, Event.NODE_ADDED, path, true, null, null, false);
                 log.info("Observing property changes to {} nodes under {}", Arrays.asList(types), path);
                           
         }
        catch(Exception e)
        {
            e.printStackTrace(); 
         }
        }
 
         protected void deactivate(ComponentContext componentContext) throws RepositoryException {
              
             if(observationManager != null) {
                 observationManager.removeEventListener(this);
             }
             if (session != null) {
                 session.logout();
                 session = null;
               }
         }
 
        //Define app logic that is fired when the event occurs - simply track the time 
        //when the event occurred. 
         public void onEvent(EventIterator itr) {
              
             Calendar cal = Calendar.getInstance();
             cal.getTime();
             //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            
                        
            //log the time when the event occurred 
             log.info("A new node was added to /aayush at " + cal.getTime());
                          
         }
}