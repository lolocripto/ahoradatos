package ar.com.datos.grupo5.btree;

import java.io.IOException;

import ar.com.datos.grupo5.registros.RegistroNodo;

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
	RegistroNodo buscar(final Clave clave);
	
	/**
	 * Inserta un registro en el Arbol.
	 * 
	 * @param registro
	 *            El registro para insertar.
	 * @return true si lo inserta.
	 * @throws IOException 
	 */
	boolean insertar(final RegistroNodo registro) throws IOException;
	
	/**
	 * true si lo modifica.
	 * @param registro El registro que se quiere modificar.
	 * @return true si lo modifica.
	 */
	boolean modificar(final RegistroNodo registro);
	
	/**
	 * @return El registro siguiente. Null si no existe siguiente.
	 */
	RegistroNodo siguiente();
	
}
