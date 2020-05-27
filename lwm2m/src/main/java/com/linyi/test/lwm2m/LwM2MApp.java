package com.linyi.test.lwm2m;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.codec.DefaultLwM2mNodeDecoder;
import org.eclipse.leshan.core.node.codec.DefaultLwM2mNodeEncoder;
import org.eclipse.leshan.core.node.codec.LwM2mNodeDecoder;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.impl.FileSecurityStore;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;
import org.eclipse.leshan.server.security.EditableSecurityStore;

/**
 * Hello world!
 *
 */
public class LwM2MApp {
	
    public static void main( String[] args ){
    	
    	  // Prepare LWM2M server
        LeshanServerBuilder builder = new LeshanServerBuilder();
//        builder.setEncoder(new DefaultLwM2mNodeEncoder());
//        LwM2mNodeDecoder decoder = new DefaultLwM2mNodeDecoder();
//        builder.setDecoder(decoder);
//        
//    	String modelsFolderPath = null;
//    	// Create Models
//        List<ObjectModel> models = ObjectLoader.loadDefault();
//        if (modelsFolderPath != null) {
//            models.addAll(ObjectLoader.loadObjectsFromDir(new File(modelsFolderPath)));
//        }
    	
        // Set securityStore & registrationStore
        EditableSecurityStore securityStore = new FileSecurityStore(); //use file persistence
        builder.setSecurityStore(securityStore);

        // Create and start LWM2M server
        LeshanServer lwServer = builder.build();
        
        lwServer.getRegistrationService().addListener(new RegistrationListener() {

            @Override
            public void updated(RegistrationUpdate update, Registration updatedRegistration,
                    Registration previousRegistration) {
            	System.out.println("***********updated***********");
            }

            @Override
            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                    Registration newReg) {
            	System.out.println("***********unregistered***********");
            }

            @Override
            public void registered(Registration registration, Registration previousReg,
                    Collection<Observation> previousObsersations) {
            	System.out.println("***********registered***********");
            }
        });
    	
        // Start Jetty & Leshan
        lwServer.start();
    }
}
