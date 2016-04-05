package messageboard.server;

import java.io.Serializable;

public class Message implements Serializable, Comparable<Message> {
	private int id;
	private String owner;
	private String text;
	
	public Message(String owner, String text) {
		this.owner = owner;
		this.text = text;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public int compareTo(Message other) {
		return this.id - other.id;
	}
}