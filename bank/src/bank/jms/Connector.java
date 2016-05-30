package bank.jms;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.TemporaryQueue;

public class Connector {
	static Context jndiContext;
	static ConnectionFactory factory;
	static Queue queue;

	public static Queue getQueue() {
		if (queue == null) {
			try {
				jndiContext = new InitialContext();
				factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
				queue = (Queue) jndiContext.lookup("/queue/BANK");
			} catch (Exception e) {
			}
		}
		return queue;
	}
}
