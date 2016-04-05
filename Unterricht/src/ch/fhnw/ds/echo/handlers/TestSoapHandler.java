package ch.fhnw.ds.echo.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class TestSoapHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("TestSoapHandler.handleMessage " + context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY));
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("TestSoapHandler.handleFault " + context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY));
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("TestSoapHandler.close");
	}

	@Override
	public Set<QName> getHeaders() {
		System.out.println("TestSoapHandler.getHeaders");
		return null;
	}
}