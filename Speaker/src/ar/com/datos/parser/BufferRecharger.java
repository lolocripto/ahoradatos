package ar.com.datos.parser;

import java.util.Collection;

/**
 * Interfaz que permite usar la colección BufferizableCollection. El objeto que
 * implementa esta interfaz podrá ofrecer al usuario un metodo de acceso a sus
 * items transparentemente bufferisados mediante una colección que se
 * auto-recarga al agotarse su buffer.
 * 
 * @author LedZeppeling
 */
public interface BufferRecharger<E> {
	
	/**
	 * 
	 * @return .
	 */
	Collection<E> listar();

	/**
	 * Método que es llamado por la colección bufferizable cuando agota su
	 * buffer por defecto.
	 * 
	 * @param coleccion
	 *            colección bufferisable
	 * @param MaxBufferedObjects
	 *            cantidad máxima de objetos que entran en el buffer
	 */
	void recargarBuffer (Collection<E> coleccion, int MaxBufferedObjects);

	/**
	 * Permite saber si hay mas elementos para devolver
	 * 
	 * @return boolean que dice si hay mas elementos
	 */
	boolean hasMoreItems(); 
}
