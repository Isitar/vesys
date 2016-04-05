package bank.soap;

import javax.xml.ws.Endpoint;

public class Publisher {

	public static void main(String[] args) {
		String url = "http://127.0.0.1:80/hs";
		//Endpoint.publish(url, new HelloServiceImpl());
		System.out.println("service published");
		System.out.println("WSDL available at " + url + "?wsdl");
	}
}
