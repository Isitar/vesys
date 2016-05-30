package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.local.Driver;

public class Server {

	// local Bank
	private static Bank bank;

	public static void main(String[] args) throws Exception {
		Context jndiContext = new InitialContext();

		ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
		Queue queue = (Queue) jndiContext.lookup("/queue/BANK");

		try (JMSContext context = factory.createContext()) {
			JMSConsumer consumer = context.createConsumer(queue);
			JMSProducer sender = context.createProducer();

			System.out.println("Bank server is running...");
			while (true) {
				Message request = consumer.receive();
				// Handle request

				// send back
				sender.send(request.getJMSReplyTo(), request.getBody(String.class));
			}
		}
	}
}
