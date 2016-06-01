package bank.jms;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import bank.Account;
import bank.Bank;
import bank.local.Driver;

public class Server {

	// local Bank
	private static Bank bank;

	public static void main(String[] args) throws Exception {

		// JMS related initialization
		Context jndiContext = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
		Queue queue = (Queue) jndiContext.lookup("/queue/BANK");


		// JMS topic initialization
		Topic topic = (Topic) jndiContext.lookup("/topic/BANK");


		// Bank related initialization
		Driver localDriver = new Driver();
		localDriver.connect(new String[] { "" });
		bank = localDriver.getBank();

		try (JMSContext context = factory.createContext()) {
			JMSConsumer consumer = context.createConsumer(queue);
			JMSProducer sender = context.createProducer();

			System.out.println("Bank server is running...");
			while (true) {
				Message request = consumer.receive();

				// Handle request

				String message;

				if (request.isBodyAssignableTo(String.class))
					message = request.getBody(String.class);
				else {
					message = CommandType.skip.ordinal() + "";
					System.out.println("Error occoured: Message not assignable");
				}
				String[] arguments = message.split(";");
				CommandType ct = CommandType.values()[Integer.parseInt(arguments[0])];
				StringBuilder sb = new StringBuilder();
				switch (ct) {
				case getAccountNumbers:
					Set<String> accountNumbers = bank.getAccountNumbers();
					sb.append(ReturnType.Successful.ordinal());
					for (String accNum : accountNumbers) {
						sb.append(';');
						sb.append(accNum);
					}
					// produces 0;accNum1;accNum2;...
					break;
				case createAccount:
					String number = bank.createAccount(URLDecoder.decode(arguments[1]));
					sb.append(ReturnType.Successful.ordinal());
					sb.append(';');
					sb.append(number);
					// produces 0;number
					sender.send(topic, number); // notify listeners
					break;
				case closeAccount:
					boolean accountClosed = bank.closeAccount(arguments[1]);
					sb.append(ReturnType.Successful.ordinal());
					sb.append(';');
					sb.append(accountClosed);
					// produces 0;true on successful close
					sender.send(topic, arguments[1]); // notify listeners
					break;
				case getAccount:
					if (arguments.length < 2) {
						sb.append(ReturnType.IOException.ordinal());
						break;
					}

					if (bank.getAccount(arguments[1]) != null)
						sb.append(ReturnType.Successful.ordinal());
					else
						sb.append(ReturnType.IOException.ordinal());

					break;
				case transfer:
					Account from = bank.getAccount(arguments[1]);
					Account to = bank.getAccount(arguments[2]);
					double amount = Double.parseDouble(arguments[3]);
					bank.transfer(from, to, amount);
					sb.append(ReturnType.Successful.ordinal());
					sender.send(topic, arguments[1]); // notify listeners about "from" account
					sender.send(topic, arguments[2]); // notify listeners about "to" account
					break;
				case deposit:
					bank.getAccount(arguments[1]).deposit(Double.parseDouble(arguments[2]));
					sb.append(ReturnType.Successful.ordinal());
					// produces 0 on success
					sender.send(topic, arguments[1]); // notify listeners
					break;
				case withdraw:
					bank.getAccount(arguments[1]).withdraw(Double.parseDouble(arguments[2]));
					sb.append(ReturnType.Successful.ordinal());
					// produces 0 on success
					sender.send(topic, arguments[1]); // notify listeners
					break;
				case getBalance:
					double balance = bank.getAccount(arguments[1]).getBalance();
					sb.append(ReturnType.Successful.ordinal());
					sb.append(';');
					sb.append(balance);
					// produces 0;123.0 for example
					break;
				case getOwner:
					String owner = bank.getAccount(arguments[1]).getOwner();
					sb.append(ReturnType.Successful.ordinal());
					sb.append(';');
					sb.append(URLEncoder.encode(owner));
					// produces 0;owner
					break;
				case isActive:
					boolean isActive = bank.getAccount(arguments[1]).isActive();
					sb.append(ReturnType.Successful.ordinal());
					sb.append(';');
					sb.append(isActive);
					// produces 0;true if active
					break;
				case inactivate:
					bank.getAccount(arguments[1]).setActive(false);
					sb.append(ReturnType.Successful.ordinal());
					// produces 0 on success
					sender.send(topic, arguments[1]); // notify listeners
					break;
				}

				String returnMessage = sb.toString();

				// send back
				sender.send(request.getJMSReplyTo(), returnMessage);
			}
		}
	}
}
