
package ar.com.datos.grupo5.interfaces;

/**
 * Esta clase representa a los registros que se graban en un archivo.
 * @author Cristian, Diego
 *
 */
public interface Registro {
	
	/**
	 * Este método convierte el objeto en una tira de bytes.
	 * @return la tira de bytes.
	 */
	byte[] getBytes();
	
	/**
	 * Este método toma la tira de bytes y setea al registro.
	 * @param buffer la tira de bytes.
	 * @param offset El offset en el que se encuentra el dato de audio asociado.
	 */
	void setBytes(final byte[] buffer, final Long offset);
	
	/**
	 * Si hay mas bytes.
	 * @return true si hay mas bytes para devolver con getBytes()
	 */
	boolean hasMoreBytes();
	
	/**
	 * Longitud del dato almacenado.
	 * @return Devuelve la longitud del dato almacenado.
	 */
	long getLongDatos();
}
