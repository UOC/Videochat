package org.red5.server.webapp.calculadora;

import org.red5.server.adapter.ApplicationAdapter;

public class Application extends ApplicationAdapter {
	
	public Double add(Double a, Double b){
		return a + b;
	}
		
	public Double sub(Double a, Double b){
		return a - b;
	}
	
}