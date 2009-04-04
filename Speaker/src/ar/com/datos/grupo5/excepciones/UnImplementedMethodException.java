/**
 * 
 */
package ar.com.datos.grupo5.excepciones;

/**
 * @author LedZeppeling
 *
 */
public class UnImplementedMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param msg Mensaje de la excepcion.
	 */
	public UnImplementedMethodException(final String msg) {
		super(msg);
	}
	
	/**
	 * 
	 */
	public UnImplementedMethodException() {
		super("SimpleAudioRecorder Exception");
	}
	
	/**
	 * 
	 * @param e causa.
	 */
	public UnImplementedMethodException(final Exception e) {
		super(e);
	}

	
}
