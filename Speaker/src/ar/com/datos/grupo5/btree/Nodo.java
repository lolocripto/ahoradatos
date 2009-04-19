package ar.com.datos.grupo5.btree;

import java.util.Vector;

/**
 * Representa el nodo de arbol b.
 * @author cristian
 *
 */
public class Nodo {
	
	/**
	 * Constructor.
	 */
	public Nodo() {
		super();
	}
	
	/**
	 * Espacio en el nodo.
	 */
	private int espacioTotal;
	
	/**
	 * Espacio ocupado.
	 */
	private int espacioOcupado;
	
	/**
	 * Puntero al nodo siguiente para recorrer como lista.
	 */
	private Nodo siguiente;
	
	/**
	 * Si el nodo es hoja o no.
	 */
	private boolean esHoja;

	/**
	 * Lista de claves.
	 */
	private Vector < Comparable<Object> > claves;
	
	/**
	 * Lista de nodos. 
	 */
	private Vector < Nodo > nodos;
	
	/**
	 * El nodo padre.
	 */
	private Nodo padre;
	
	/**************************
	 * Getters and Setters
	 **************************/
	
	/**
	 * @return the espacioTotal
	 */
	public final int getEspacioTotal() {
		return espacioTotal;
	}

	/**
	 * @param espacioTotal the espacioTotal to set
	 */
	public final void setEspacioTotal(final int espacioTotal) {
		this.espacioTotal = espacioTotal;
	}

	/**
	 * @return the espacioOcupado
	 */
	public final int getEspacioOcupado() {
		return espacioOcupado;
	}

	/**
	 * @param espacioOcupado the espacioOcupado to set
	 */
	public final void setEspacioOcupado(final int espacioOcupado) {
		this.espacioOcupado = espacioOcupado;
	}

	/**
	 * @return the siguiente
	 */
	public final Nodo getSiguiente() {
		return siguiente;
	}

	/**
	 * @param siguiente the siguiente to set
	 */
	public final void setSiguiente(final Nodo siguiente) {
		this.siguiente = siguiente;
	}
	
	/**
	 * @return the esHoja
	 */
	public final boolean isEsHoja() {
		return esHoja;
	}

	/**
	 * @param esHoja the esHoja to set
	 */
	public final void setEsHoja(final boolean esHoja) {
		this.esHoja = esHoja;
	}
	
	/**
	 * Inserta una clave.
	 * @param clave la clave a insertar.
	 * @return false si no pudo insertar o la clave ya existia.
	 */
	public final boolean insertarClave(final Comparable<Object> clave) {
		
		return claves.add(clave);
	}
	
	/**
	 * Elimina una clave.
	 * @param clave La clave a eliminar.
	 * @return true si pudo eliminar la clave.
	 */
	public final boolean eliminarClave(final Comparable<Object> clave) {
		return claves.remove(clave);
	}
	
	/**
	 * Verifica la existencia de una clave.
	 * @param clave .
	 * @return true si la clave existe.
	 */
	public final boolean existeClave(final Comparable<Object> clave) {
		return this.claves.contains(clave);
	}
	
	/**
	 * Busca una clave. Sirve para algo??
	 * @param clave .
	 * @return la clave buscada.
	 */
	public final Comparable<Object> buscarClave(final Comparable<Object> clave) {
		
		for (Comparable<Object> c : this.claves) {
			if (c.equals(clave)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Agrega un nodo.
	 * @param nodo El nodo para insertar.
	 */
	public final void insertarNodo(final Nodo nodo) {
		nodos.add(nodo);
	}

	/**
	 * @return the padre
	 */
	public final Nodo getPadre() {
		return padre;
	}

	/**
	 * @param padre the padre to set
	 */
	public final void setPadre(final Nodo padre) {
		this.padre = padre;
	}
	
}
