package exceptions;

import java.io.Serializable;

public class TerminException extends Exception implements Serializable {
	
	private String message;

	public TerminException(String message) {
		super();
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
