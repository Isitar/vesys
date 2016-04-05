package messageboard.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBoard {
	private ConcurrentMap<Integer, Message> messages;
	private AtomicInteger mapKey;

	public MessageBoard() {
		messages = new ConcurrentHashMap<>();
		mapKey = new AtomicInteger();
	}

	public Map<Integer, Message> getMessages() {
		return this.messages;
	}

	public int addMessage(Message p) {
		int id = mapKey.incrementAndGet();
		p.setId(id);
		messages.put(id, p);
		return id;
	}

	public Message getMessage(int id) {
		return messages.get(id);
	}

	public List<Message> getAllMessagesFrom(String owner) {
		Message[] msgs = messages.values().toArray(new Message[]{});
		Arrays.sort(msgs);

		List<Message> list = new ArrayList<Message>();
		for (Message m : msgs) {
			if(m.getOwner().equals(owner)) {
				list.add(m);
			}
		}
		return list;
	}

}
