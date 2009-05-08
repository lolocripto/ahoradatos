package ar.com.datos.UnidadesDeExpresion;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.parser.BufferRecharger;


/** Colection utlizada para el bufferizado.
 * @author LedZeppeling
 * @param <E>
 */
public class BufferedCollection<E> extends AbstractCollection<E> {
   
	/**
	 * 
	 */
	protected ArrayList<E> listaUnidades;
	
	/**
	 * .
	 */
	protected BufferRecharger<E> recargador;

	/**
	 * 
	 * @param recarga
	 */
	public BufferedCollection(final BufferRecharger<E> recarga) {
		listaUnidades = new ArrayList<E>();
		recargador = recarga;

	}
	
	/**
	 * 
	 */
	public final boolean add(final E e) {
		return listaUnidades.add(e);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final boolean addAll(final Collection arg0) {
		return listaUnidades.addAll(arg0);
	}

	/**
	 * 
	 */
	public final void clear() {
		listaUnidades.clear();

	}

	/**
	 * 
	 */
	public final boolean contains(final Object arg0) {
		return listaUnidades.contains(arg0);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final boolean containsAll(final Collection arg0) {
		return listaUnidades.containsAll(arg0);
	}

	/**
	 * 
	 */
	public final boolean isEmpty() {
	    return listaUnidades.isEmpty();
	}

	/**
	 * 
	 */
	public final boolean remove(final Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Metodo que recarga la collection utilizada
	 * 
	 */
	public final void recargar() {
		recargador.recargarBuffer(this, Constantes.BUFFER_LECTURA_TEXT_INPUT);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final boolean removeAll(final Collection c) {
		return listaUnidades.removeAll(c);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final boolean retainAll(final Collection c) {
		return listaUnidades.retainAll(c);
	}

	/**
	 * 
	 */
	public final int size() {
		return listaUnidades.size();
	}

	/**
	 * 
	 */
	public final Object[] toArray() {
		return listaUnidades.toArray();
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final Object[] toArray(final Object[] a) {
		return listaUnidades.toArray(a);

	}

	/**
	 * 
	 */
	public final Iterator<E> iterator() {
		BufferedCollectionIterator<E> iterador = 
			new BufferedCollectionIterator<E>(
				this);
		return iterador;
	}

	
	
	
	
}
