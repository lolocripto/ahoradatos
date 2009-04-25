/**
 * 
 */
package ar.com.datos.grupo5.btree;

import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Clase que implementa el nodo de los árboles B*
 * @author Led Zeppelin
 */
public final class BAsterisk implements BTree {
	/**
	 * Nodo
	 */
	private Nodo nodo;

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#buscar(ar.com.datos.grupo5.btree.Clave)
	 * @return retorna el Registro, si pudo encontrarlo.
	 */
	public RegistroNodo buscar(Clave clave) {
		// TODO Auto-generated method stub
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

	/**
	 * @param nodo the nodo to set
	 */
	public void setNodo(final Nodo nodo) {
		this.nodo = nodo;
	}

	/**
	 * @return the nodo
	 */
	public Nodo getNodo() {
		return nodo;
	}

}
