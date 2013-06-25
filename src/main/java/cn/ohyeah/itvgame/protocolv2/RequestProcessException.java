package cn.ohyeah.itvgame.protocolv2;

public class RequestProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestProcessException() {
		super();
	}

	public RequestProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestProcessException(String message) {
		super(message);
	}

	public RequestProcessException(Throwable cause) {
		super(cause);
	}

}
