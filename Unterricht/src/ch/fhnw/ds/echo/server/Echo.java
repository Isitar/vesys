package ch.fhnw.ds.echo.server;

import java.util.Date;

import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@HandlerChain(file = "handler-chain.xml")
public class Echo {

	public String sayHello(@WebParam(name = "name") String name) {
		if ("ex".equals(name)) throw new IllegalStateException("ex not expected");
		return "Hello " + name + " from SOAP at " + new Date();
	}

}
