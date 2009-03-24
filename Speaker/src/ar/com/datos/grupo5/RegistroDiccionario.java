
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Esta clase implementa el registro para el diccionario.
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author Diego
 */
public class RegistroDiccionario implements Registro {
	

	private byte[] buffer;
	private Long longitud;
	private Long offset;
	private String dato;
	
	/* (non-Javadoc)
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] toBytes() {
		String cadena;
		/**
		 * 1° Agrego el offset.
		 */
		cadena = this.offset.toString();
		this.longitud = (long)this.dato.length();
		/**
		 * Luego agrego la longitud.
		 */
		cadena += this.longitud.toString();
		/**
		 * Por ultimo, el dato.
		 */
		cadena += this.dato;
		this.buffer = cadena.getBytes();
		return this.buffer;
	}
	
	/**
	 * Método que llena el registro con la información del buffer
	 */
	public void llenar() {
		// TODO Llenar este método
	}
	
	/**
	 * @return the longitud
	 */
	public Long getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(Long longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the offset
	 */
	public Long getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Long offset) {
		this.offset = offset;
	}

	/**
	 * @return the dato
	 */
	public String getDato() {
		return dato;
	}

	/**
	 * @param dato the dato to set
	 */
	public void setDato(String dato) {
		this.dato = dato;
	}

}
