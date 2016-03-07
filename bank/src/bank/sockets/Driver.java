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

		private final Map<String, Account> accounts = new HashMap<>();

		public Bank(String host, int port) throws IOException {
			s = new Socket(host, port, null, 0);
			os = new ObjectOutputStream(s.getOutputStream());
			is = new ObjectInputStream(s.getInputStream());
		}

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
			return (bank.Account) tcpRequest(number, "getAccount");
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

			Command c = new Command();
			c.setCommand(command);
			c.setAssignedObject(o);
			try {
				os.writeObject(c);

				c = (Command) is.readObject();
				return c.getReturnObject();
			} catch (IOException | ClassNotFoundException e) {
				return null;
			}
		}

	}
/*
	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		private static int lastGeneratedNumber = 0;

		// number generation values
		private final int accountNumberLength = 7;
		private final int dashPosition = 3;

		Account(String owner) {
			this.owner = owner;

			// build account number, 000-0000 style
			String strNumber = Integer.toString(++lastGeneratedNumber);
			StringBuilder sb = new StringBuilder();
			int zeros = accountNumberLength - strNumber.length();
			for (int i = 0; i < zeros; i++) {
				sb.append('0');
			}
			sb.append(strNumber);
			sb.insert(dashPosition, '-');

			number = sb.toString();
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
		public void deposit(double amount) throws InactiveException {
			if (!active) {
				throw new InactiveException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if (!active) {
				throw new InactiveException();
			}
			if (amount > balance) {
				throw new OverdrawException();
			}
			balance -= amount;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

	}
*/
}