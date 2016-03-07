/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args) throws NumberFormatException, IOException {
		bank = new Bank(args[0], Integer.parseInt(args[1]));
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
		private Socket s;
		private ObjectOutputStream os;
		private ObjectInputStream is;

		public Bank(String host, int port) throws IOException {
			s = new Socket(host, port, null, 0);
			os = new ObjectOutputStream(s.getOutputStream());
			is = new ObjectInputStream(s.getInputStream());
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<String> getAccountNumbers() throws IOException {
			return (Set<String>) tcpRequest(null, "getAccountNumbers");
		}

		@Override
		public String createAccount(String owner) throws IOException {
			return (String) tcpRequest(owner, "createAccount");
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			return (boolean) tcpRequest(number, "closeAccount");
		}

		@Override
		public bank.Account getAccount(String number) throws IOException {
			bank.Account acc = (bank.Account) tcpRequest(number, "getAccount");
			Driver.Account a = new Driver.Account(os, is);
			a.active = acc.isActive();
			a.balance = acc.getBalance();
			a.number = acc.getNumber();
			a.owner = acc.getOwner();
			return a;
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
			TransferCommand tc = new TransferCommand();
			tc.setAcc1(from);
			tc.setAcc2(to);
			tc.setAmount(amount);
			tcpRequest(tc, "transfer");

		}

		private Object tcpRequest(Object o, String command) throws IOException {
			return Helper.tcpRequest(o, command, os, is);
		}
	}

	static class Helper {
		public static Object tcpRequest(Object o, String command, ObjectOutputStream os, ObjectInputStream is)
				throws IOException {

			Command c = new Command();
			c.setCommand(command);
			c.setAssignedObject(o);
			try {
				os.writeObject(c);
				c = (Command) is.readObject();
				return c.getReturnObject();
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
	}

	static class Account implements bank.Account, java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 103983981303735184L;
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		private ObjectOutputStream os;
		private ObjectInputStream is;

		public Account(ObjectOutputStream os, ObjectInputStream is) {
			this.os = os;
			this.is = is;
		}

		@Override
		public double getBalance() {
			return balance;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void deposit(double amount) throws InactiveException, IOException {
			if (!active) {
				throw new InactiveException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			DepositCommand dc = new DepositCommand();
			dc.setAccountNo(this.number);
			dc.setAmount(amount);
			tcpRequest(dc, "deposit");
			this.balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
			if (!active) {
				throw new InactiveException();
			}
			if (amount > balance) {
				throw new OverdrawException();
			}
			WithdrawCommand wc = new WithdrawCommand();
			wc.setAccountNo(this.number);
			wc.setAmount(amount);
			Command com = (Command) tcpRequest(wc, "withdraw");
			balance -= amount;
		}

		public void setActive(boolean active) throws IOException {
			if (active)
				tcpRequest(this.number, "activate");
			else
				tcpRequest(this.number, "deactivate");
			this.active = active;
		}

		private Object tcpRequest(Object o, String command) throws IOException {
			return Helper.tcpRequest(o, command, os, is);
		}
	}

}
