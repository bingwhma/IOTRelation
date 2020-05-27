package com.linyi.test.lwm2m.client;

//import static org.eclipse.leshan.LwM2mId.DEVICE;
//import static org.eclipse.leshan.LwM2mId.LOCATION;
//import static org.eclipse.leshan.LwM2mId.SECURITY;
//import static org.eclipse.leshan.LwM2mId.SERVER;

import static org.eclipse.leshan.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

import java.io.File;
import java.util.List;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.BindingMode;




public class LwM2MClient {

    private static final int OBJECT_ID_TEMPERATURE_SENSOR = 3303;
    private final static String DEFAULT_ENDPOINT = "LeshanClientDemo";
    
    private final static String[] modelPaths = new String[] { "3303.xml" };
    
	public static void main(String[] args) {
		
		String serverURI = "coap://127.0.0.1:" + LwM2m.DEFAULT_COAP_PORT;
		
        // Initialize model
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));
        
        // 48.131
        Float latitude = 48.131f;
        // 11.459
        Float longitude = 11.459f;
        Float scaleFactor = 1.0f;
        
        MyLocation locationInstance = new MyLocation(latitude, longitude, scaleFactor);
        
        // Initialize object list
        ObjectsInitializer initializer = new ObjectsInitializer(new LwM2mModel(models));
        
//        initializer.setInstancesForObject(SECURITY, noSecBootstap(serverURI));
//        initializer.setClassForObject(SERVER, Server.class);
        
        initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
        initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
        
        initializer.setInstancesForObject(DEVICE, new MyDevice());
        initializer.setInstancesForObject(LOCATION, locationInstance);
        initializer.setInstancesForObject(OBJECT_ID_TEMPERATURE_SENSOR, new RandomTemperatureSensor());
        List<LwM2mObjectEnabler> enablers = initializer.createAll();
        
        // Create CoAP Config
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanClientBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }

        // Create client
        String endpoint = DEFAULT_ENDPOINT;
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
//        builder.setLocalAddress(localAddress, localPort);
        builder.setObjects(enablers);
        builder.setCoapConfig(coapConfig);
        final LeshanClient client = builder.build();

        // Start the client
        client.start();

        // De-register on shutdown and stop client.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                client.destroy(true); // send de-registration request before destroy
            }
        });

	}
}
