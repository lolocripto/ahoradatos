package ar.com.datos.UnidadesDeExpresion;

import java.util.Collection;
import java.util.Iterator;

public class BufferedCollectionIterator<E> implements Iterator<E> {

	
	private BufferedCollection<E> coleccionPabras;
	private Iterator<E> buffer;
	
	public BufferedCollectionIterator(BufferedCollection<E> lista){
		coleccionPabras=lista;
		buffer = lista.lista_unidades.iterator();
	}
	
	/**
	 * Este metodo permite saber si existen mas elementos para ser devueltos,
	 * esta lógica implica saber, en primer lugar, si el buffer se agotó y
	 * en segundo lugar si existen mas objetos para recargar.
	 * @return boolean que indica si hay mas elementos.
	 */
	public boolean hasNext() {
//si el buffer tiene mas objetos devuelve true
		if (buffer.hasNext()){
			return true;
		}else{  //si el buffer se agotó, se fija si hay mas para recargar
		    if (coleccionPabras.recargador.hasMoreItems()){  // si hay mas
		    	coleccionPabras.clear();     // se vacia la lista
		    	coleccionPabras.recargar();  // se recarga
			buffer = coleccionPabras.lista_unidades.iterator();  //se renueva el iterador
			return buffer.hasNext();    //se pregunta si fue realmente recargado
		}
	}
		return false;
	}
	/**
	 * método que devuelve el siguente objeto del buffer. Antes de hacerlo
	 * quita el ultimo devuelto para vaciarlo en tiempo real.
	 * @return Object con el siguente objeto
	 */
	public E next() {
	//	buffer.remove();
	    return buffer.next();	
	}

	public void remove() {
		// TODO Auto-generated method stub
	}

}
