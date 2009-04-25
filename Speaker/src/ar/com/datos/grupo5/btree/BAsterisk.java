/**
 * 
 */
package ar.com.datos.grupo5.btree;

import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Clase que implementa el nodo de los árboles B*.
 * @author Led Zeppelin
 */
public final class BAsterisk implements BTree {
	
	/**
	 * Nodo Actual.
	 */
	private Nodo nodoActual;
	
	/**
	 * Nodo Raiz.
	 */
	private Nodo nodoRaiz;

	/**
	 * Busca un registro.
	 * 
	 * @param clave
	 *            la clave del registro.
	 * @return El registro buscado o el siguiente inmediatamente mayor.
	 */
	public RegistroNodo buscar(final Clave clave) {
		
		if (nodoRaiz == null) {
			return null;
		}

		Nodo nodoAux = nodoRaiz;
		
		return null;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#insertar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public boolean insertar(final RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#modificar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public boolean modificar(final RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#siguiente()
	 */
	public RegistroNodo siguiente() {
		// TODO Auto-generated method stub
		return null;
	}

}
