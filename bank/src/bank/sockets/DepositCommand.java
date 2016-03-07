package bank.sockets;

import java.io.Serializable;

public class DepositCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3568874600122964166L;
	private String accountNo;
	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}
