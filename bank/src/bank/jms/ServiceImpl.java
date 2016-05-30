package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jws.WebService;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.local.Driver;

@WebService
public class ServiceImpl implements Service {

	// local Bank
	private static Bank bank;

	public ServiceImpl() {

		Driver localDriver = new Driver();
		localDriver.connect(new String[] { "" });
		bank = localDriver.getBank();

	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		return bank.getAccountNumbers();
	}

	@Override
	public String createAccount(String owner) throws IOException {
		return bank.createAccount(owner);
	}

	@Override
	public void deposit(String number, double amount) throws IllegalArgumentException, IOException, InactiveException {
		bank.getAccount(number).deposit(amount);
	}

	@Override
	public void withdraw(String number, double amount)
			throws IllegalArgumentException, IOException, OverdrawException, InactiveException {
		bank.getAccount(number).withdraw(amount);

	}

	@Override
	public boolean setActive(String number, boolean active) throws IOException {
		bank.getAccount(number).setActive(active);
		return true;
	}

	@Override
	public String getOwner(String number) throws IOException {
		return bank.getAccount(number).getOwner();
	}

	@Override
	public boolean isActive(String number) throws IOException {
		return bank.getAccount(number).isActive();
	}

	@Override
	public double getBalance(String number) throws IOException {
		return bank.getAccount(number).getBalance();
	}

	@Override
	public boolean AccountExists(String number) throws IOException {
		return (bank.getAccount(number) != null);
	}

}
