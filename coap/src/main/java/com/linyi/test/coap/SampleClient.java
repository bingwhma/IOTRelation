package com.linyi.test.coap;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

public class SampleClient {

	 private static final String serverUrl = "coap://127.0.0.1:5683/hello";
	    public static void main(String[] args) throws Exception{

	        NetworkConfig networkConfig = NetworkConfig.createStandardWithoutFile();
	        CoapEndpoint coapEndpointBuilder = new CoapEndpoint(networkConfig);
//	        coapEndpointBuilder.setNetworkConfig(networkConfig);

	        CoapClient client = new CoapClient();
	        client.setEndpoint(coapEndpointBuilder);
	        client.setURI(serverUrl);

	        CoapResponse response = client.get();

	        if (response != null) {

	            System.out.println(response.getCode());
	            System.out.println(response.getOptions());
	            System.out.println(response.getResponseText());

	            System.out.println("\nADVANCED\n");
	            System.out.println(Utils.prettyPrint(response));
	        } else {
	            System.out.println("No response received.");
	        }
	    }

}
