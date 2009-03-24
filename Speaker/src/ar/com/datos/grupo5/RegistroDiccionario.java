
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
	
	
	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getLongitud() {
		return longitud;
	}

	public void setLongitud(long longitud) {
		this.longitud = longitud;
	}

	public String getDato() {
		return dato;
	}

	public void setDato(String dato) {
		this.dato = dato;
	}
	
	/* (non-Javadoc)
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] toBytes() {
	
		return dato.getBytes();
	}
	
	/**
	 * Método que devuelve el buffer como cadena
	 * @return 
	 */
	public String toString() {
		return buffer.toString();
	}

}
