package ch.fhnw.ds.mep.server;

import javax.xml.ws.Endpoint;

public class ServerPublisher {

	public static void main(String[] args){
		String url = "http://127.0.0.1:9875/mep";
		Endpoint.publish(url, new Server());
		System.out.println("service published");
		System.out.println("WSDL available at "+url+"?wsdl");
	}

}
