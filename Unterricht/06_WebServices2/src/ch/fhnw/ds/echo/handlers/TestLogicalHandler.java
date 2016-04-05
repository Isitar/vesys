package ch.fhnw.ds.echo.handlers;

import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

public class TestLogicalHandler implements LogicalHandler<LogicalMessageContext> {

	@Override
	public boolean handleMessage(LogicalMessageContext context) {
		System.out.println("TestLogicalHandler.handleMessage " + context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY));
		return true;
	}

	@Override
	public boolean handleFault(LogicalMessageContext context) {
		System.out.println("TestLogicalHandler.handleFault " + context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY));
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("TestLogicalHandler.close");
	}
}