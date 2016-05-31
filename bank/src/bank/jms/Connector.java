package bank.jms;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

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
				queue = (Queue) getContext().lookup("/queue/BANK");
			} catch (Exception e) {
			}
		}
		return queue;
	}

	public static Context getContext() {
		if (jndiContext == null) {
			try {
				jndiContext = new InitialContext();
			} catch (Exception e) {
			}
		}
		return jndiContext;
	}

	public static ConnectionFactory getFactory() throws NamingException {
		if (factory == null) {
			factory = (ConnectionFactory) getContext().lookup("ConnectionFactory");
		}
		return factory;
	}

	public static String CallService(CommandType c, String Args) {
		try {

			Queue queue = Connector.getQueue();
			try (JMSContext context = Connector.getFactory().createContext()) {
				TemporaryQueue tempQueue = context.createTemporaryQueue();

				JMSProducer sender = context.createProducer().setJMSReplyTo(tempQueue);
				JMSConsumer receiver = context.createConsumer(tempQueue);

				// Convert to utf-8 for queue

				ByteBuffer bf = Charset.forName("UTF-8").encode(Args);
				String convertedArgs = new String(bf.array());

				if (Args == null || Args == "") {
					sender.send(queue, c.ordinal() + "");
				} else {
					sender.send(queue, c.ordinal() + ";" + convertedArgs);
				}
				byte[] by = receiver.receiveBody(String.class).getBytes();
				String res = new String(by, "UTF-8");

				res = res.replace("%00", "");
				return res;
			}
		} catch (Exception e) {
			return "";
		}
	}

}
