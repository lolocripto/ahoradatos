
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Esta clase implementa el registro para el diccionario.
 * @author Diego
 *
 */
public class RegistroDiccionario implements Registro {
	
	private byte[] buffer;
	private long offset;
	private long longitud;
	private String dato;
	
	/* (non-Javadoc)
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Método que devuelve el buffer como cadena
	 * @return 
	 */
	public String toString() {
		return buffer.toString();
	}

}
