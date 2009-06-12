/**
 * 
 */
package ar.com.datos.grupo5.excepciones;

/**
 * @author Led Zeppelin
 *
 */
public class CodePointException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CodePointException() {
		super("CodePoint fuera del intervalo permitido.");
	}

	public CodePointException(String message) {
		super(message);
	}

	public CodePointException(Throwable cause) {
		super(cause);
	}

	public CodePointException(String message, Throwable cause) {
		super(message, cause);
	}

}
