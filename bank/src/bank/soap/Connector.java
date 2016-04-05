package bank.soap;

import bank.soap.client.ServiceImplService;

public class Connector {
	private static bank.soap.client.ServiceImpl port;

	public static bank.soap.client.ServiceImpl getPort() {
		if (port == null) {
			port = new ServiceImplService().getServiceImplPort();
		}
		return port;
	}

}
