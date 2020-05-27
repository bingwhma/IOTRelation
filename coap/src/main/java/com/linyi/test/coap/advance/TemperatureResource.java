package com.linyi.test.coap.advance;

import org.eclipse.californium.core.server.resources.CoapExchange;

// 3 : Create a Temperature Resource extending AbstractResource
public class TemperatureResource extends AbstractResource { 

    public TemperatureResource() { 
     super ("/houses/*/rooms/*/sensors/temperature"); 

     getAttributes().setTitle ("Temperature resource !"); 
    } 

    @Override 
    public void handleGET (CoapExchange exchange) { 
     String response = "The temperature"; 
     if (getWildcards() != null) { 
      response += " of the " + getWildcards().get (0) + " on the " + getWildcards().get (1); 
     } 
     response += " is : 25 degree C"; 

     exchange.respond (response); 
    } 
}