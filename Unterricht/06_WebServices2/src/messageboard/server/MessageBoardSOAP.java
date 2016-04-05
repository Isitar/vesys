package messageboard.server;

import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
@HandlerChain(file = "serviceHandler.xml")
public class MessageBoardSOAP {
	private static final MessageBoard messageBoard = new MessageBoard();
	private static final int maxLength = 140;

	@WebMethod
	public List<Message> getAllMessagesFrom(String owner) {
		return messageBoard.getAllMessagesFrom(owner);
	}

	@WebMethod
	public Message getMessage(int id) {
		return messageBoard.getMessage(id);
	}

	@WebMethod
	public String create(String owner, String text) throws LengthExceededException {
		if(text.length() > maxLength) {
			throw new LengthExceededException(text.length() + " chars is too long!", "Max characters: " + maxLength);
		}
		
		int id = messageBoard.addMessage(new Message(owner, text));
		return "Message " + id + " created.";
	}

	@WebMethod
	public String delete(int id, String owner) {
		String msg = "Message " + id + " not found.";
		Message p = messageBoard.getMessage(id);
		if (p != null) {
			if(!p.getOwner().equals(owner)) {
				msg = "Message can only be edited by its owner";
			} else {
				messageBoard.getMessages().remove(id);
				msg = "Message " + id + " removed.";
			}
		}
		return msg;
	}

}