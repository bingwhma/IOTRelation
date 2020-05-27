package com.linyi.test.coap.advance;

import java.util.LinkedList;

import org.eclipse.californium.core.CoapResource;

// 2 : Create an abstract CoapResource that should inject the Wildcards list
public abstract class AbstractResource extends CoapResource { 

    private LinkedList<String> wildcards; 

    protected AbstractResource (String name) { 
     super (name); 
    } 

    protected AbstractResource (String name, boolean visible) { 
     super (name, visible); 
    } 

    public LinkedList<String> getWildcards() { 
     return wildcards; 
    } 

    public void setWildcards(LinkedList<String> wildcards) { 
     this.wildcards = wildcards; 
    } 
}
