/**
 * 
 */
package ar.com.datos.grupo5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Led Zeppelin
 *
 */
public class ListaEspacioLibre {

	/**
	 * Lista con los espacios libres.
	 */
	private ArrayList<NodoListaEspacioLibre> espacioLibrePorBloque;
	
	/**
	 * El indice dentro del arrayList espacioLibrePorBloque del 
	 * último accedido.
	 */
	private int index;
	
	/**
	 * Factor de carga que limita la carga de los bloques para 
	 * poder agrandar los registros que contiene. 
	 */
	private Float factorCarga;
	
	/**
	 * Constructor de la clase.
	 */
	public ListaEspacioLibre() {
		this.espacioLibrePorBloque = new ArrayList<NodoListaEspacioLibre>();
		this.factorCarga = Constantes.FACTOR_CARGA_BLOQUES;
		this.setIndex(0);
	}
	
	/**
	 * Se encarga de actualizar un nodo o agregar un nodo en la 
	 * lista de espacios libres
	 * para mantener actualizada la lista.
	 * @param index
	 * 			Lugar dentro de la lista.
	 * @param nroBloqueExt
	 * 			Numero de bloque que tiene el espacio libre.
	 * @param espacio
	 * 			Espacio libre que tiene el bloque.
	 */
	public final void actualizarListaEspacioLibre(final int index, final int nroBloqueExt, final short espacio) {
		NodoListaEspacioLibre nodo;
		switch(index) {
			case -1:
				/* Agrego un elementos mas a la lista */
				nodo = new NodoListaEspacioLibre();
				nodo.setEspacio(espacio);
				nodo.setNroBloque(nroBloqueExt);
				this.espacioLibrePorBloque.add(nodo);
			break;
			default:
				/* Modifico un elemento mas a la lista */
				nodo = this.espacioLibrePorBloque.get(index);
				nodo.setEspacio(espacio);
			break;
		}	
	}
	
	public final int getSize() {
		return this.espacioLibrePorBloque.size();
	}
	
	public final void agregarNodo(NodoListaEspacioLibre nodo) {
		this.espacioLibrePorBloque.add(nodo);
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Permite cargar una lista con pares Bloque-EspacioLibre.
	 * @param espacioLibrePorBloqueExt
	 * 		Lista con los pares Bloque-EspacioLibre.
	 */
	public final void setEspacioLibrePorBloque(
			final ArrayList<NodoListaEspacioLibre> espacioLibrePorBloqueExt) {
		this.espacioLibrePorBloque = espacioLibrePorBloqueExt;
	}

	/**
	 * Permite obtener la lista con pares Bloque-EspacioLibre.
	 * @return
	 * 		Lista con los pares Bloque-EspacioLibre.
	 */
	public final List<NodoListaEspacioLibre> getEspacioLibrePorBloque() {
		return espacioLibrePorBloque;
	}

	/**
	 * Busca el primer bloque que contiene el espacio suficiente para 
	 * almacenar el elemento teniendo en cuenta el factor de carga.
	 * @param espacioNecesario Espacio necesario para poder guardar el elemento.
	 * @return
	 * 		Devuelve el numero de bloque en el cual puedo insertar
	 * 		el elemento. Si no encuentra un lugar devuelve -1. 
	 */
	public final int buscarEspacio(final Short espacioNecesario) {
		NodoListaEspacioLibre nodo;
		Iterator<NodoListaEspacioLibre> it = this.espacioLibrePorBloque.iterator();
		
		this.index = -1;
		Short espacioLibreTotal = (short) (espacioNecesario + (Constantes.SIZE_OF_LIST_BLOCK * this.factorCarga));
		
		while (it.hasNext()) {
			nodo = it.next();
			this.index++;
			if (nodo.getEspacio() >= espacioLibreTotal) {
				return nodo.getNroBloque();
			}
		}
		return -1;
	}
	
	
	/**
	 * Actualiza el espacio del ultimo elemento accedido 
	 * por buscarEspacio.
	 * @param espacioActual Espacio que queda en el bloque.
	 */
	public final void actualizarEspacio(final Short espacioActual) {
		NodoListaEspacioLibre nodo;
		
		nodo = this.espacioLibrePorBloque.get(this.index);
		
		nodo.setEspacio(espacioActual);
		
		//this.espacioLibrePorBloque.add(this.index, nodo);
	}
	
	public final Iterator<NodoListaEspacioLibre> obtenerIterador() {
		return this.espacioLibrePorBloque.iterator();
	}

	/**
	 * Busca el elemento con numero de bloque igual a nroBloqueExt
	 * @param nroBloqueExt Numero de bloque que quiero.
	 * @return
	 * 		True si lo encuentra, false sino lo encuentra.
	 */
	public boolean buscarBloque(int nroBloqueExt) {
		return true;
	}
}
