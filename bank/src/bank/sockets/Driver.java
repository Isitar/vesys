/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

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
		try {
			bank.getSocket().close();
		} catch (IOException e) {
			System.err.println("could not close socket");
			e.printStackTrace();
		} finally {
			bank = null;
			System.out.println("disconnected...");
		}
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
			if (!(from.isActive() && to.isActive())) {
				throw new InactiveException();
			}
			TransferCommand tc = new TransferCommand();
			tc.setAccNo1(from.getNumber());
			tc.setAccNo2(to.getNumber());
			tc.setAmount(amount);
			tcpRequest(tc, "transfer");
			Driver.Account a1 = (Account) from;
			Driver.Account a2 = (Account) to;
			a1.withdrawLocal(amount);
			a2.depositLocal(amount);
		}

		private Object tcpRequest(Object o, String command) throws IOException {
			return Helper.tcpRequest(o, command, os, is);
		}

		protected Socket getSocket() {
			return s;
		}
	}

	static class Helper {
		public static Object tcpRequest(Object o, String command, ObjectOutputStream os, ObjectInputStream is)
				throws IOException {

			os.reset();
			Command c = new Command();
			c.setCommand(command);
			c.setAssignedObject(o);
			try {
				os.writeObject(c);
				os.flush();
				c = (Command) is.readObject();
				return c.getReturnObject();
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
	}

	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		private transient ObjectOutputStream os;
		private transient ObjectInputStream is;

		public Account(ObjectOutputStream os, ObjectInputStream is) {
			this.os = os;
			this.is = is;
		}

		public void depositLocal(double amount) {
			this.balance += amount;
		}

		public void withdrawLocal(double amount) {
			this.balance -= amount;
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
