package com.linyi.test.coap.advance;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.californium.core.CoapResource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

// 5 : Create a Resources Loader 
//(class that will load the specs definition of the resources 
//		and instantiate them independently instead of creating a 
//		Tree on the server)

public class ResourcesLoader {

	private final static String Path = new File(".").getAbsolutePath() + File.separator + "resources";

	private List<ProxyRes> resourcesList;

	public ResourcesLoader() throws Exception {
		resourcesList = new ArrayList<ProxyRes>();

		File resources = new File(Path);
		for (String resName : resources.list()) {
			File resFile = new File(resources, resName);
			InputStream is = new FileInputStream(resFile);
			
			 
			JsonObject o = (JsonObject) new JsonParser().parse((new JsonReader(new InputStreamReader(is, "UTF-8"))));

//			resourcesArr.add(o);
			resourcesList.add(buildObject(o));
		}
	}

	private ProxyRes buildObject(JsonObject o)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ProxyRes r = new ProxyRes();
		r.setPath(o.get("endpoint").toString().replace("\"", ""));
		
		Class<?> clazz = Class.forName(o.get("class").toString().replace("\"", ""));
		CoapResource coapRes = (CoapResource) clazz.newInstance();
		r.setCoapRes(coapRes);

		return r;
	}

	public List<ProxyRes> getResourcesList() {
		return resourcesList;
	}
}
