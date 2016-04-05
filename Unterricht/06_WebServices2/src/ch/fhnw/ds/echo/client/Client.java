package ch.fhnw.ds.echo.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import ch.fhnw.ds.echo.client.jaxws.Echo;
import ch.fhnw.ds.echo.client.jaxws.EchoService;
import ch.fhnw.ds.echo.handlers.LogHandler;
import ch.fhnw.ds.echo.handlers.TestLogicalHandler;
import ch.fhnw.ds.echo.handlers.TestSoapHandler;

public class Client {

	public static void main(String[] args) throws Exception {
		EchoService service = new EchoService();
		service.setHandlerResolver(new ClientHandlerResolver());
		Echo port = service.getEchoPort();

		String result = port.sayHello("Dominik");
		System.out.println(result);
	}
}

class ClientHandlerResolver implements HandlerResolver {
	
	@SuppressWarnings("rawtypes")
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		List<Handler> chain = new ArrayList<Handler>();
		chain.add(new LogHandler());			// SOAP Handler
		chain.add(new TestLogicalHandler());	// Logical Handler
		chain.add(new TestSoapHandler());		// SOAP Handler
		return chain;
	}
	
}
