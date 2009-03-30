package ar.com.datos.UnidadesDeExpresion;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.parser.BufferRecharger;

public class BufferedCollection implements Collection {


	
	private ArrayList lista_unidades;
	private BufferRecharger recargador;

	public BufferedCollection(BufferRecharger recarga){
	    lista_unidades= new ArrayList();
		recargador = recarga;
		
	}
	public boolean add(Object arg0) {
		return lista_unidades.add(arg0);
	}

	public boolean addAll(Collection arg0) {
		return lista_unidades.addAll(arg0);
	}

	public void clear() {
		lista_unidades.clear();

	}

	public boolean contains(Object arg0) {
		return lista_unidades.contains(arg0);
	}

	public boolean containsAll(Collection arg0) {
		return lista_unidades.containsAll(arg0);
	}

	public boolean isEmpty() {
	    return lista_unidades.isEmpty();
	}

	public Iterator iterator() {
		// TODO Auto-generated method stub
		return new BufferedCollectionIterator(this);
	}

	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	public void recargar(){
		recargador.recargarBuffer(lista_unidades,Constantes.BUFFER_LECTURA_TEXT_INPUT);
	}

	public boolean removeAll(Collection c) {
		return lista_unidades.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return lista_unidades.retainAll(c);
	}

	public int size() {
		return lista_unidades.size();
	}

	public Object[] toArray() {
	     return lista_unidades.toArray();
	}

	public Object[] toArray(Object[] a) {
		return lista_unidades.toArray(a) ;
		
	}

}
