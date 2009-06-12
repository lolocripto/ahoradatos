/**
 * 
 */
package ar.com.datos.grupo5.excepciones;

/**
 * @author Led Zeppelin
 *
 */
public class insufficientBitsException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public insufficientBitsException() {
		super("Bits insoficientes para realizar la descompresion.");
	}

	public insufficientBitsException(String message) {
		super(message);
	}

	public insufficientBitsException(Throwable cause) {
		super(cause);
	}

	public insufficientBitsException(String message, Throwable cause) {
		super(message, cause);
	}
}
