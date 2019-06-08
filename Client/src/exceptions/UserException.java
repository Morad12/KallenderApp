package exceptions;

import java.io.Serializable;

public class UserException extends Exception implements Serializable {
	
	private String message;

	public UserException(String message) {
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
