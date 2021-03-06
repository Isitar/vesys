/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args) {
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

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			HashSet<String> accountNumbers = new HashSet<String>();
			accounts.values().forEach(a -> {
				if (a.isActive()) {
					accountNumbers.add(a.getNumber());
				}
			});
			return accountNumbers;
		}

		@Override
		public String createAccount(String owner) {
			Account account = new Account(owner);
			accounts.put(account.getNumber(), account);
			return account.getNumber();
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = accounts.get(number);

			if (acc.isActive() && (acc.getBalance() == 0) && !(acc == null)) {
				acc.setActive(false);
				return true; // account is closed
			} else {
				return false; // account is not closed
			}
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
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

	static class Account implements bank.Account, java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4470185434146804658L;
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
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			balance -= amount;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

	}

}