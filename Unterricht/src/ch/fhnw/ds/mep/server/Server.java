package ch.fhnw.ds.mep.server;

import java.util.Date;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class Server {

	@WebMethod
	public void send1(String msg) {
		execute("send1", msg);
	}

	@WebMethod
	@Oneway
	public void send2(String msg) {
		execute("send2", msg);
	}
	
	private void execute(String label, String msg) {
		System.out.printf(">> %s %s at %s with %s%n", label, msg, new Date(), Thread.currentThread());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("<< %s %s at %s with %s%n", label, msg, new Date(), Thread.currentThread());

	}

}
