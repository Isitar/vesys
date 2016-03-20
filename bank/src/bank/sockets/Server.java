package bank.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.local.Driver;

public class Server {

	public static void main(String[] args) throws IOException {
		Driver localDriver = new Driver();
		localDriver.connect(new String[] { "" });
		int port = 6789;
		BankHandler.b = localDriver.getBank();
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Started Bank Server on port " + port);
			while (true) {
				Socket s = server.accept();
				Thread t = new Thread(new BankHandler(s));
				t.start();
			}
		}
	}

}

class BankHandler implements Runnable {
	private final Socket s;
	public static Bank b;

	public BankHandler(Socket s) {
		this.s = s;
	}

	public void run() {
		System.out.println("connection from " + s);
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			while (s.isConnected()) {
				Object obj = in.readObject();

				if (obj instanceof Command) {
					Command c = (Command) obj;
					switch (c.getCommand()) {
					case "createAccount":
						c.setReturnObject(b.createAccount((String) c.getAssignedObject()));
						break;
					case "closeAccount":
						c.setReturnObject(b.closeAccount((String) c.getAssignedObject()));
						break;
					case "getAccount":
						bank.Account acc = b.getAccount((String) c.getAssignedObject());
						// debug not directly
						c.setReturnObject(acc);
						break;
					case "transfer":
						TransferCommand tc = (TransferCommand) c.getAssignedObject();
						try {
							b.transfer(b.getAccount(tc.getAccNo1()), b.getAccount(tc.getAccNo2()), tc.getAmount());
							c.setReturnObject(null);
						} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
							c.setError(e.getMessage());
							c.setReturnObject(e);
						}
						break;
					case "getAccountNumbers":
						c.setReturnObject(b.getAccountNumbers());
						break;
					case "deposit":
						DepositCommand dc = (DepositCommand) c.getAssignedObject();
						bank.Account depositAcc = b.getAccount(dc.getAccountNo());
						try {
							depositAcc.deposit(dc.getAmount());
							c.setReturnObject(null);
						} catch (InactiveException x) {
							c.setReturnObject(x);
							c.setError(x.getMessage());
						}

						break;
					case "withdraw":
						WithdrawCommand wc = (WithdrawCommand) c.getAssignedObject();
						bank.Account withdrawAcc = b.getAccount(wc.getAccountNo());
						try {
							withdrawAcc.withdraw(wc.getAmount());
							c.setReturnObject(null);
						} catch (OverdrawException e) {
							c.setError(e.getMessage());
						}
						break;
					case "activate":
						bank.Account activateAcc = b.getAccount((String) c.getAssignedObject());
						activateAcc.setActive(true);
						c.setReturnObject(null);
						break;
					case "deactivate":
						bank.Account deactivateAcc = b.getAccount((String) c.getAssignedObject());
						deactivateAcc.setActive(false);
						c.setReturnObject(null);
						break;
					}
					out.reset();
					out.writeObject(c);
				}
			}
			System.out.println("done serving " + s);
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InactiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
