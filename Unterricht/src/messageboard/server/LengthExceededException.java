package messageboard.server;

public class LengthExceededException extends Exception {
	private String details;

	public LengthExceededException(String reason, String details) {
		super(reason);
		this.details = details;
	}

	public String getFaultInfo() {
		return this.details;
	}
}