package ar.com.datos.grupo5.btree;

import java.util.Vector;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Representa el nodo de arbol b.
 * @author LedZeppeling
 *
 */
public class Nodo {
	
	/**
	 * Constructor.
	 */
	public Nodo() {
		nodoIzquierdo = null;
		nodoDerecho = null;
		siguiente = null;
		padre = null;
	}
	
	/**
	 * Constructor.
	 * @param nodoPadre Nodo padre.
	 */
	public Nodo(final Nodo nodoPadre) {
		nodoIzquierdo = null;
		nodoDerecho = null;
		siguiente = null;
		padre = nodoPadre;
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
	private boolean esHoja = false;

	/**
	 * Lista de claves.
	 */
	private Vector < Comparable<Object> > claves;
	
	/**
	 * Lista de registros.
	 */
	private Vector < Registro > registros;
	
	/**
	 * Nodo al que apunta a la izquierda.
	 */
	private Nodo nodoIzquierdo;
	
	/**
	 * Nodo al que apunta a la derecha.
	 */
	private Nodo nodoDerecho;
	
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
	 * @return the espacioOcupado
	 */
	public final int getEspacioOcupado() {
		return espacioOcupado;
	}

	/**
	 * @return the siguiente
	 */
	public final Nodo getSiguiente() {
		return siguiente;
	}
	
	/**
	 * @return the esHoja
	 */
	public final boolean isEsHoja() {
		return esHoja;
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
	public final Comparable<Object> buscarClave(
			final Comparable<Object> clave) {
		
		for (Comparable<Object> c : this.claves) {
			if (c.equals(clave)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Agrega un nodo.
	 * @param registro El nodo para insertar.
	 */
	public final void insertarRegistro(final Registro registro) {
		registros.add(registro);
	}

	/**
	 * @return the padre
	 */
	public final Nodo getPadre() {
		return padre;
	}

	/**
	 * @param nodoPadre the padre to set
	 */
	public final void setPadre(final Nodo nodoPadre) {
		this.padre = nodoPadre;
	}

	/**
	 * @return the claves
	 */
	public final Vector<Comparable<Object>> getClaves() {
		return claves;
	}

	/**
	 * @return the registros
	 */
	public final Vector<Registro> getRegistros() {
		return registros;
	}

	/**
	 * @param espacio the espacioTotal to set
	 */
	public final void setEspacioTotal(final int espacio) {
		this.espacioTotal = espacio;
	}

	/**
	 * @param espacio the espacioOcupado to set
	 */
	public final void setEspacioOcupado(final int espacio) {
		this.espacioOcupado = espacio;
	}

	/**
	 * @param nodoSiguiente the siguiente to set
	 */
	public final void setSiguiente(final Nodo nodoSiguiente) {
		this.siguiente = nodoSiguiente;
	}

	/**
	 * @param hoja the esHoja to set
	 */
	public final void setEsHoja(final boolean hoja) {
		this.esHoja = hoja;
	}
	
}
