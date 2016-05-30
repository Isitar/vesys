/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.jms;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.naming.NamingException;

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
			String[] accNumbers = Connector.CallService(CommandType.getAccountNumbers, "").split(";");

			if (ReturnType.values()[Integer.parseInt(accNumbers[0])] == ReturnType.Error) {
				// no exception Handling
				return null;
			}
			return new HashSet<String>(Arrays.asList(accNumbers).stream().skip(1).collect(Collectors.toList()));
		}

		@Override
		public String createAccount(String owner) {
			String[] retVal = Connector.CallService(CommandType.createAccount, owner).split(";");
			if (ReturnType.values()[Integer.parseInt(retVal[0])] == ReturnType.Error) {
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
					Connector.CallService(CommandType.getAccount, number).split(";")[0])] == ReturnType.Answer)
				return new Account(number);

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
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Answer)
				return Double.parseDouble(returnVals[1]);
			else
				return 0;

		}

		@Override
		public String getOwner() {
			String[] returnVals = Connector.CallService(CommandType.getOwner, number).split(";");
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Answer)
				return returnVals[1];
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
			if (ReturnType.values()[Integer.parseInt(returnVals[0])] == ReturnType.Answer)
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
			Connector.CallService(CommandType.incativate, number);
		}

		
		// needed for another project but not here.
		@Override
		public void setActive(boolean active) throws IOException {

		}

	}

}