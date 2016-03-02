package bank.sockets;

public class Command implements java.io.Serializable {
	private static final long serialVersionUID = 5328231923103656003L;

	private String command;
	private Object assignedObject;
	private Object returnObject;
	private String error;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object getAssignedObject() {
		return assignedObject;
	}

	public void setAssignedObject(Object assignedObject) {
		this.assignedObject = assignedObject;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
}
