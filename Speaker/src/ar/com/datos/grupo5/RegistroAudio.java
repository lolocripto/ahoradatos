
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Esta Clase implementa la interfaz del registro para audio.
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author Diego
 */
public class RegistroAudio implements Registro {

	private byte[] buffer;
	private Long longitud;
	private byte[] dato;
	
	
	public boolean hasMoreBytes() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* Método toBytes(). Nos sirve para devoler la cadena de bytes a escribir.
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] getBytes() {
		String cadena;
		/**
		 * 1° Agrego la longitud.
		 */
		this.longitud = (long)this.dato.toString().length();
		cadena = this.longitud.toString();
		/**
		 * Por ultimo, el dato.
		 */
		cadena += this.dato;
		this.buffer = cadena.getBytes();
		return this.buffer;
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
	 * @return the dato
	 */
	public byte[] getDato() {
		return dato;
	}

	/**
	 * @param dato the dato to set
	 */
	public void setDato(byte[] dato) {
		this.dato = dato;
	}

}
