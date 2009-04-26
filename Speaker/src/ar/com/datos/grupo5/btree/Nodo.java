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
		this.nodoAnterior = null;
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
		this.nodoAnterior = null;
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
	 * Puntero al nodo anterior para pasarse registros como lista.
	 */
	private Nodo nodoAnterior;
	
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
		//FIXME: Esto es cualquiera!!!!!!!
		return this.registros.contains(clave);
	}
	
	/**
	 * Busca una clave en el nodo.
	 * 
	 * @param clave
	 *            .
	 * @return * El indice en el array en donde esta el registro que contiene la
	 *         clave o del primer elemento que es mayor a la clave.
	 *         * -1 si la clave es menor a la clave del primer registro.
	 *         * -2 si la clave es mayor a la clave del ultimo registro.
	 */
	public final int buscarRegistro(final Clave clave) {

		int pos = -1;
		int resultado = 0;
		
		if (registros.size() == 0) {
			return 0;
		}
		
		// Si la clave es menor a la primera, no est�.
		if (clave.compareTo(registros.get(0).getClave()) < 0) {
			return -1;
		}

		// Si la clave es mayor a la ultima, no est�.
		if (clave.compareTo(registros.get(registros.size() - 1)
				.getClave()) > 0) {
			return -2;
		}
		
		//Recorro los nodos en busca de la clave.
		for (RegistroNodo reg : this.registros) {
			pos++;
			
			resultado = reg.getClave().compareTo(clave);
			switch (resultado) {
				//Si es igual o mayor, devuelvo el indice.
				case 0:
				case 1:
					return pos;
				//Sigo buscando;
				default:
					break;
			}
		}
		
		return pos;
	}
	
	/**
	 * Agrega un nodo.
	 * @param registro El reistro para insertar.
	 */
	public final void insertarRegistro(final RegistroNodo registro) {
		
		//Obtengo la posicion en donde debo insertarlo.
		int pos = this.buscarRegistro(registro.getClave());
		switch (pos) {
		case -1:
			this.registros.add(0, registro);
			break;
		case -2:
			// FIXME Cuando esta lleno, revienta -> pasar al hno
			this.registros.add(registros.size(), registro);
			break;
		default:
			this.registros.add(pos, registro);
			break;
		}
			
	}
	
	/**
	 * @param registro the registro to set
	 * @return .
	 */
	public final boolean setRegistro(final RegistroNodo registro) {
		int pos = this.buscarRegistro(registro.getClave());
		if (pos >= 0) {
			this.registros.set(pos, registro);
			return true;
		}
		return false;
	}

	
	/**
	 * @param registro the registro to set
	 * @return .
	 */
	public final boolean PasarRegistro(final RegistroNodo registro, 
			final boolean lugar) {
		// FIXME Hacer los m�todos para saber si hay lugar en los nodos!!
		if(lugar == false){
			// Pasar al hno Anterior
			
		}
		else{
			// Pasar al hermano siguiente
			
			
		}
		return false;
	}
	
	/**
	 * Para testear.
	 */
	public final void listar() {
		
		System.out.println();
		System.out.println("Contenido del nodo:");
		for (RegistroNodo reg : registros) {
			System.out.println("==== " + reg.getClave().getClave());
		}
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
	 * @param nodo the anterior to set.
	 */
	public final void setAnterior(final Nodo nodo) {
		this.nodoAnterior = nodo;
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

	/**
	 * @return the nodos
	 */
	public final ArrayList<Nodo> getNodos() {
		return nodos;
	}

	/**
	 * @param nodos the nodos to set
	 */
	/*public final void setNodos(final ArrayList<Nodo> nodos) {
		this.nodos = nodos;
	}*/
	
	/**
	 * @return the nodos
	 */
	public final Nodo splitNodo() {
		Nodo nodo = new Nodo();
		
		if (this.nodoPadre == null) {
			// Es la raiz!!!!!!
			// Partir en 2 o en 3 y uno de los otros es nuevo raiz

		}
		if (this.nodoSiguiente != null) {
			// Junto con el siguiente

			// Busco posici�n donde tengo que partir el nodo
			// contabilizando bytes
		} else {
			// Junto con el anterior

		}
		//Luego ver si tengo que generar el padre!!!!	
		return nodo;
	}
}
