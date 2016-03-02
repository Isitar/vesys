package bank.sockets;

import bank.Account;

public class TransferCommand implements java.io.Serializable {
	private static final long serialVersionUID = 6587926756375198621L;

	private Account acc1;
	private Account acc2;
	private double amount;

	public Account getAcc1() {
		return acc1;
	}

	public void setAcc1(Account acc1) {
		this.acc1 = acc1;
	}

	public Account getAcc2() {
		return acc2;
	}

	public void setAcc2(Account acc2) {
		this.acc2 = acc2;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
