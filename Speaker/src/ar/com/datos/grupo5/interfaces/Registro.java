/**
 * Esta clase representa a los registros que se graban en un archivo.
 */
package ar.com.datos.grupo5.interfaces;

/**
 * @author cristian
 *
 */
public interface Registro {

	/**
	 * Este metodo convierte el objeto en una tira de bytes.
	 * @return la tira de bytes.
	 */
	byte[] toBytes();
}
