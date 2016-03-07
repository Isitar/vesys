package bank.sockets;

import java.io.Serializable;

public class WithdrawCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6776486393075760664L;
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
