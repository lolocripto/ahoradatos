package ar.com.datos.grupo5.btree;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * 
 * @author cristian
 *
 */
public interface BTree {

	/**
	 * Busca un registro.
	 * 
	 * @param clave
	 *            la clave del registro.
	 * @return El registro buscado o el siguiente inmediatamente mayor en el
	 *         caso del b+, b#.
	 */
	Registro buscar(final Clave clave);
	
	/**
	 * Inserta un registro en el Arbol.
	 * 
	 * @param registro
	 *            El registro para insertar.
	 * @return true si lo inserta.
	 */
	boolean insertar(final Registro registro);
	
	/**
	 * true si lo modifica.
	 * @param registro El registro que se quiere modificar.
	 * @return true si lo modifica.
	 */
	boolean modificar(final Registro registro);
	
	/**
	 * @return El registro siguiente. Null si no existe siguiente.
	 */
	Registro siguiente();
	
}
