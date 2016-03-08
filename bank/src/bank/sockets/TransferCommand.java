package bank.sockets;

public class TransferCommand implements java.io.Serializable {
	private static final long serialVersionUID = 6587926756375198621L;

	private String accNo1;
	private String accNo2;
	private double amount;


	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccNo1() {
		return accNo1;
	}

	public void setAccNo1(String accNo1) {
		this.accNo1 = accNo1;
	}

	public String getAccNo2() {
		return accNo2;
	}

	public void setAccNo2(String accNo2) {
		this.accNo2 = accNo2;
	}
}
