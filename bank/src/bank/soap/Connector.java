package bank.soap;

import java.net.MalformedURLException;
import java.net.URL;

import bank.soap.client.ServiceImplService;

public class Connector {
	private static bank.soap.client.ServiceImpl port;

	public static bank.soap.client.ServiceImpl getPort() {
		if (port == null) {
			port = new ServiceImplService().getServiceImplPort();
		}
		return port;
	}

	public static void createNewInstance(String wsdlLocation) throws MalformedURLException {
		port = new ServiceImplService(new URL(wsdlLocation)).getServiceImplPort();
	}
}
