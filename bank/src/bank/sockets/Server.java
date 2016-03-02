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
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Startet Bank Server on port " + port);
			while (true) {
				Socket s = server.accept();
				Thread t = new Thread(new BankHandler(s, localDriver.getBank()));
				t.start();
			}
		}
	}

}

class BankHandler implements Runnable {
	private final Socket s;
	private final Bank b;

	public BankHandler(Socket s, Bank b) {
		this.s = s;
		this.b = b;
	}

	public void run() {
		System.out.println("connection from " + s);
		try {
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			while (s.isConnected()) {
				Object obj = in.readObject();

				if (obj instanceof Command) {
					Command c = (Command) obj;
					switch (c.getCommand()) {
					case "create":
						c.setReturnObject(b.createAccount((String) c.getAssignedObject()));
						break;
					case "close":
						c.setReturnObject(b.closeAccount((String) c.getAssignedObject()));
						break;
					case "getAccount":
						c.setReturnObject(b.getAccount((String) c.getAssignedObject()));
						break;
					case "transfer":
						TransferCommand tc = (TransferCommand) c.getAssignedObject();
						try {
							b.transfer(tc.getAcc1(), tc.getAcc2(), tc.getAmount());
							c.setReturnObject(null);
						} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
							c.setError(e.getMessage());
							c.setReturnObject(e);
						}
						break;
					}

					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
					out.writeObject(c);
				}
			}
			System.out.println("done serving " + s);
		} catch (IOException e) {
			System.err.println(e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
