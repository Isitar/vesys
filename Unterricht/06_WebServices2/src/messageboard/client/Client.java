package messageboard.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import messageboard.client.jaxws.Message;
import messageboard.client.jaxws.MessageBoardSOAP;
import messageboard.client.jaxws.MessageBoardSOAPService;

public class Client {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: " + Client.class.getName() + " <user> <password>");
		}
		final String user = args[0];
		final String password = args[1];

		MessageBoardSOAPService service = new MessageBoardSOAPService();

		service.setHandlerResolver(new HandlerResolver() {
			@SuppressWarnings("rawtypes")
			@Override
			public List<Handler> getHandlerChain(PortInfo portInfo) {
				List<Handler> chain = new ArrayList<Handler>();
				chain.add(new ClientHashHandler(user, password));
				return chain;

			}
		});
		MessageBoardSOAP port = service.getMessageBoardSOAPPort();

		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
				"http://localhost:8888/messages");
		

		System.out.println("Commands: ");
		System.out.println("get <id>");
		System.out.println("del <id>");
		System.out.println("add <message>");
		System.out.println("all <owner>");
		System.out.println();

		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		int id = 0;
		while (true) {
			String[] cmd = readCommand(r);
			try { id = Integer.parseInt(cmd[1]); } catch (NumberFormatException e) { id = 0; }
			switch (cmd[0]) {
			case "get":
				printMessage(port.getMessage(id));
				break;
			case "del":
				System.out.println(port.delete(id, user));
				break;
			case "add":
				System.out.println(port.create(user, cmd[1]));
				break;
			case "all":
				for(Message m : port.getAllMessagesFrom(cmd[1])) {
					printMessage(m);
				}
				break;
			default:
				System.out.println("unknown command: " + cmd[0]);
			}

		}
	}
	
	private static void printMessage(Message m) {
		if(m != null) {
			System.out.println("id    = " + m.getId());
			System.out.println("owner = " + m.getOwner());
			System.out.println("text  = " + m.getText());
		} else {
			System.out.println("null");
		}
	}

	private static String[] readCommand(BufferedReader r) throws IOException {
		System.out.print("> ");
		String line = r.readLine();
		if(line.length() < 3) System.exit(0);
		String cmd = line.substring(0, 3);
		String rest = line.length() > 4 ? line.substring(4) : null;
		return new String[] { cmd, rest };
	}

}
