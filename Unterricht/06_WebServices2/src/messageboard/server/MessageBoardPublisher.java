package messageboard.server;

import javax.xml.ws.Endpoint;

public class MessageBoardPublisher {

	public static void main(String[] args) {
		String url = "http://127.0.0.1:9999/messages";
		Endpoint.publish(url, new MessageBoardSOAP());
		System.out.println("service published");
		System.out.println("WSDL available at " + url + "?wsdl");
	}

}
