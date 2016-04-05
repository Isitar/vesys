/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.soap;

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

		private Map<String, Account> getAccounts() {
			return new HashMap<String, Account>();
		} // todo soap call

		@Override
		public Set<String> getAccountNumbers() {
			HashSet<String> accountNumbers = new HashSet<String>();
			getAccounts().values().forEach(a -> {
				if (a.isActive()) {
					accountNumbers.add(a.getNumber());
				}
			});
			return accountNumbers;
		}

		@Override
		public String createAccount(String owner) {
			Account account = new Account(owner);
			// call to SOAP
			return "";
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = getAccounts().get(number);
			if (acc.isActive() && acc.getBalance() == 0 && !(acc == null)) {
				acc.setActive(false);
				return true; // account is closed
			}

			return false; // account is not closed

		}

		@Override
		public bank.Account getAccount(String number) {
			return getAccounts().get(number);
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

		private String number;
		private String owner;

		Account(String owner) {
			this.owner = owner;
		}

		@Override
		public double getBalance() {
			// call to SOAP
			return 0;
		}

		@Override
		public String getOwner() {
			// call to SOAP
			return "";
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			// call to SOAP
			return true;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if (!isActive()) {
				throw new InactiveException();
			}
			if (amount < 0) {
				throw new IllegalArgumentException();
			}
			// call to SOAP

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
			// call to SOAP

		}

		public void setActive(boolean active) {
			// call to SOAP
		}

	}

}