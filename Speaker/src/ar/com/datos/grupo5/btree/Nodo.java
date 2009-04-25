package ar.com.datos.grupo5.btree;

import java.util.ArrayList;

import ar.com.datos.grupo5.registros.RegistroNodo;

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
		
		this.nodoIzquierdo = null;
		this.nodoDerecho = null;
		this.nodoSiguiente = null;
		this.nodoPadre = null;
		this.registros = new ArrayList<RegistroNodo>();
	}
	
	/**
	 * Constructor.
	 * @param nodoPadre Nodo padre.
	 */
	public Nodo(final Nodo nodoPadre) {
		
		this.nodoIzquierdo = null;
		this.nodoDerecho = null;
		this.nodoSiguiente = null;
		this.nodoPadre = nodoPadre;
		this.registros = new ArrayList<RegistroNodo>();
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
	private Nodo nodoSiguiente;
	
	/**
	 * Si el nodo es hoja o no.
	 */
	private boolean esHoja = false;
	
	/**
	 * Lista de registros.
	 */
	private ArrayList< RegistroNodo > registros;
	
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
	private Nodo nodoPadre;
	
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
		return nodoSiguiente;
	}
	
	/**
	 * @return the esHoja
	 */
	public final boolean isEsHoja() {
		return esHoja;
	}
	
	/**
	 * Verifica la existencia de una clave.
	 * @param clave .
	 * @return true si la clave existe.
	 */
	public final boolean existeClave(final Clave clave) {
		return this.registros.contains(clave);
	}
	
	/**
	 * Busca una clave. Sirve para algo??
	 * @param clave .
	 * @return la clave buscada.
	 */
	public final Clave buscarClave(final Clave clave) {
		for (int i = 0; i < this.registros.size(); i++) {
			if (clave.equals(this.registros.get(i).getClaveNodo())) {
				return clave;
			}
		}
		return null;
	}
	
	/**
	 * Agrega un nodo.
	 * @param registro El nodo para insertar.
	 */
	public final void insertarRegistro(final RegistroNodo registro) {
		registros.add(registro);
	}

	/**
	 * @return the padre
	 */
	public final Nodo getPadre() {
		return nodoPadre;
	}

	/**
	 * @param nodo the padre to set
	 */
	public final void setPadre(final Nodo nodo) {
		this.nodoPadre = nodo;
	}

	/**
	 * @return the registros
	 */
	public final ArrayList<RegistroNodo> getRegistros() {
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
	 * @param nodo the siguiente to set.
	 */
	public final void setSiguiente(final Nodo nodo) {
		this.nodoSiguiente = nodo;
	}

	/**
	 * @param hoja the esHoja to set
	 */
	public final void setEsHoja(final boolean hoja) {
		this.esHoja = hoja;
	}

	/**
	 * @return the nodoIzquierdo
	 */
	public final Nodo getNodoIzquierdo() {
		return nodoIzquierdo;
	}

	/**
	 * @param nodo the nodoIzquierdo to set
	 */
	public final void setNodoIzquierdo(final Nodo nodo) {
		this.nodoIzquierdo = nodo;
	}

	/**
	 * @return the nodoDerecho
	 */
	public final Nodo getNodoDerecho() {
		return nodoDerecho;
	}

	/**
	 * @param nodo the nodoDerecho to set
	 */
	public final void setNodoDerecho(final Nodo nodo) {
		this.nodoDerecho = nodo;
	}
	
}
