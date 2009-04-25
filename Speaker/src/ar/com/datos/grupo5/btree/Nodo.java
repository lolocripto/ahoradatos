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
		
		this.nodoSiguiente = null;
		this.nodoPadre = null;
		this.registros = new ArrayList<RegistroNodo>();
		this.nodos = new ArrayList<Nodo>();
	}
	
	/**
	 * Constructor.
	 * @param nodo Nodo padre.
	 */
	public Nodo(final Nodo nodo) {
		
		this.nodoSiguiente = null;
		this.nodoPadre = nodo;
		this.registros = new ArrayList<RegistroNodo>();
		this.nodos = new ArrayList<Nodo>();
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
	 * Lista de nodos.
	 */
	private ArrayList<Nodo> nodos;
	
	/**
	 * Lista de registros.
	 */
	private ArrayList< RegistroNodo > registros;
	
	/**
	 * El nodo padre.
	 */
	private Nodo nodoPadre;
	
	/**********************
	 * Metodos
	 **********************/
	
	/**
	 * Agrega un nodo a la lista de nodos.
	 * @param nodo El nodo a insertar.
	 */
	public final void insertarNodo(final Nodo nodo) {
		this.nodos.add(nodo);
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
	 * 
	 * @param clave
	 *            .
	 * @return El indice en el array en donde esta el registro que contiene la
	 *         clave.
	 *         -1 - si no se encuentra la clave.
	 */
	public final int buscarRegistro(final Clave clave) {

		//FIXME: ver si esto funciona(deberia). 
		return registros.indexOf(clave);
		
		/*int pos = -1;
		boolean encontrado = false;
		
		for (RegistroNodo reg : this.registros) {
			pos++;
			if (reg.getClaveNodo().equals(clave)) {
				encontrado = true;
				break; 
			}
		}
		
		return encontrado?pos:-1;*/
	}
	
	/**
	 * Agrega un nodo.
	 * @param registro El reistro para insertar.
	 */
	public final void insertarRegistro(final RegistroNodo registro) {
		registros.add(registro);
	}
	
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
	 * @return El primer registro del nodo.
	 */
	public final RegistroNodo getPrimerRegistro() {
		return this.registros.get(0);
	}
	
	/**
	 * @return El ultimo registro del nodo.
	 */
	public final RegistroNodo getUltimoRegistro() {
		return this.registros.get(registros.size() - 1);
	}
}
