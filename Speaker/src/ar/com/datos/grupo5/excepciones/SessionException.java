package ar.com.datos.grupo5.excepciones;

public class SessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionException() {
		super("Sesion no iniciada.");
	}

	public SessionException(String message) {
		super(message);
	}

	public SessionException(Throwable cause) {
		super(cause);
	}

	public SessionException(String message, Throwable cause) {
		super(message, cause);
	}

}
