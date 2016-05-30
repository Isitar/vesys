/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.jms;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;

import bank.InactiveException;
import bank.OverdrawException;
import bank.soap.client.IOException_Exception;
import bank.soap.client.InactiveException_Exception;
import bank.soap.client.OverdrawException_Exception;

public class Driver implements bank.BankDriver {
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

	static class Bank implements bank.Bank {

		@Override
		public Set<String> getAccountNumbers() {
			Queue queue = Connector.getQueue();
			try (JMSContext context = Connector.getFactory().createContext()) {
				TemporaryQueue tempQueue = context.createTemporaryQueue();

				JMSProducer sender = context.createProducer().setJMSReplyTo(tempQueue);
				JMSConsumer receiver = context.createConsumer(tempQueue);

				sender.send(queue, CommandType.getAccountNumbers);

				String res = receiver.receiveBody(String.class);
				System.out.println(res);
			} catch (Exception e) {
			}

		}

		@Override
		public String createAccount(String owner) {
			try {
				return Connector.getPort().createAccount(owner);
			} catch (IOException_Exception e) {
				return "";
			}
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = getAccount(number);
			if (acc.isActive() && acc.getBalance() == 0 && !(acc == null)) {
				acc.setActive(false);
				return true; // account is closed
			}

			return false; // account is not closed

		}

		@Override
		public Account getAccount(String number) {
			try {
				if (Connector.getPort().accountExists(number))
					return new Account(number);
			} catch (IOException_Exception e) {
			}
			return null;

		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
			if (!from.isActive() || !to.isActive()) {
				throw new InactiveException();
			}
			if (amount > from.getBalance()) {
				throw new OverdrawException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			from.withdraw(amount);
			to.deposit(amount);
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
			try {
				return Connector.getPort().getBalance(number);
			} catch (IOException_Exception e) {
				return 0;
			}
		}

		@Override
		public String getOwner() {
			try {
				return Connector.getPort().getOwner(number);
			} catch (IOException_Exception e) {
				return "";
			}
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			try {
				return Connector.getPort().isActive(number);
			} catch (IOException_Exception e) {
				return false;
			}
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if (!isActive()) {
				throw new InactiveException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}

			try {
				Connector.getPort().deposit(number, amount);
			} catch (IOException_Exception e) {
			} catch (InactiveException_Exception e) {
				// is this cast really needed?
				throw new InactiveException();
			}

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

			try {
				Connector.getPort().withdraw(number, amount);
			} catch (IOException_Exception e) {
			} catch (InactiveException_Exception e) {
				throw new InactiveException();
			} catch (OverdrawException_Exception e) {
				throw new OverdrawException();
			}

		}

		public void setActive(boolean active) {
			try {
				Connector.getPort().setActive(number, active);
			} catch (IOException_Exception e) {
			}
		}

	}

}