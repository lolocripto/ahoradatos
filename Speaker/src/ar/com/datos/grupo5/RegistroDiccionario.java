
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
	
	/**
	 * Método toBytes(). Nos sirve para devoler la cadena de bytes a escribir.
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
	 * Método que llena el registro con la información del buffer.
	 */
	public void llenar() {
		// TODO Llenar este método
	}
	
	/**
	 * Método que devuelve la Longitud.
	 * @return La longitud del dato.
	 */
	public Long getLongitud() {
		return longitud;
	}

	/**
	 * Método para setear la longitud.
	 * @param longitud La longitud a setear.
	 */
	public void setLongitud(Long longitud) {
		this.longitud = longitud;
	}

	/**
	 * Método que devuelve el offset.
	 * @return El offset en el archivo de audio.
	 */
	public Long getOffset() {
		return offset;
	}

	/**
	 * Método para cargar el offset.
	 * @param offset El offset a cargar.
	 */
	public void setOffset(Long offset) {
		this.offset = offset;
	}

	/**
	 * Método para devolver el dato.
	 * @return El dato.
	 */
	public String getDato() {
		return dato;
	}

	/**
	 * Método para cargar el dato.
	 * @param dato El dato a setear.
	 */
	public void setDato(String dato) {
		this.dato = dato;
	}

}
