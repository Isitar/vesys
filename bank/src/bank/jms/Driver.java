/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.jms;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import bank.InactiveException;
import bank.OverdrawException;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Driver implements bank.BankDriver2 {
	private Bank bank = null;

	@Override
	public void connect(String[] args) throws MalformedURLException {
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		try {
			Context jndiContext = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
			Topic topic = (Topic) jndiContext.lookup("/topic/BANK");

			JMSContext context = factory.createContext();
			JMSConsumer listener = context.createConsumer(topic);
			listener.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					try {
						handler.accountChanged(message.toString());
					} catch (IOException e) {
					}
				}
			});

		} catch (NamingException e) {
		}
	}

	static class Bank implements bank.Bank {

		@Override
		public Set<String> getAccountNumbers() {
			String[] accNumbers = Connector.CallService(CommandType.getAccountNumbers, "").split(";");

			if (ReturnType.values()[Integer.parseInt(accNumbers[0])] != ReturnType.Successful) {
				// no exception Handling
				return null;
			}
			if (accNumbers.length == 1) {
				return new HashSet<String>();
			} else {
				return new HashSet<String>(Arrays.asList(accNumbers).stream().skip(1).collect(Collectors.toList()));
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public String createAccount(String owner) {
			System.out.println("Creating Account: " + owner);
			owner = URLEncoder.encode(owner);

			String[] retVal = Connector.CallService(CommandType.createAccount, owner).split(";");
			if (ReturnType.values()[Integer.parseInt(retVal[0])] != ReturnType.Successful) {
				// no exception Handling
				return null;
			} else {
				return retVal[1];
			}
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = getAccount(number);
			if (acc.isActive() && acc.getBalance() == 0 && !(acc == null)) {
				acc.inactivate();
				return true; // account is closed
			}
			return false; // account is not closed
		}

		@Override
		public Account getAccount(String number) {
			if (ReturnType.values()[Integer.parseInt(
					Connector.CallService(CommandType.getAccount, number).split(";")[0])] == ReturnType.Successful)
				return new Account(number);

			return null;

		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
			if (!(from.isActive() && to.isActive())) {
				throw new InactiveException();
			}
			if (amount > from.getBalance()) {
				throw new OverdrawException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			Connector.CallService(CommandType.transfer, from.getNumber() + ";" + to.getNumber() + ";" + amount);
		}

	}

	static class Account implements bank.Account {

		public Account(String number) {
			this.number = number;
		}

		public Account() {
		};

		private String number;

		@Override
		public double getBalance() {
			ReturnType rt;
			String[] returnVals = Connector.CallService(CommandType.getBalance, number).split(";");
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Successful)
				return Double.parseDouble(returnVals[1]);
			else
				return 0;

		}

		@SuppressWarnings("deprecation")
		@Override
		public String getOwner() {
			String[] returnVals = Connector.CallService(CommandType.getOwner, number).split(";");
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Successful)
				return URLDecoder.decode(returnVals[1]);
			// System.out.println("Converted owner to: " + owner);
			// return owner;

			else
				return "";
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			String[] returnVals = Connector.CallService(CommandType.isActive, number).split(";");
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Successful)
				return Boolean.parseBoolean(returnVals[1]);
			else
				return false;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if (!isActive()) {
				throw new InactiveException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}

			String[] returnVals = Connector.CallService(CommandType.deposit, number + ";" + amount).split(";");
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if (!isActive()) {
				throw new InactiveException();
			}
			if (amount > getBalance()) {
				throw new OverdrawException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}

			Connector.CallService(CommandType.withdraw, number + ";" + amount);

		}

		public void inactivate() {
			Connector.CallService(CommandType.inactivate, number);
		}

		// needed for another project but not here.
		@Override
		public void setActive(boolean active) throws IOException {

		}

	}

}