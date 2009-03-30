package ar.com.datos.UnidadesDeExpresion;

import java.util.Collection;
import java.util.Iterator;

public class BufferedCollectionIterator implements Iterator {

	
	private BufferedCollection palabras;
	private Iterator iterador;
	public BufferedCollectionIterator(BufferedCollection lista){
		palabras=lista;
		iterador = lista.iterator();
	}
	public boolean hasNext() {
		if (!iterador.hasNext())
			  palabras.recargar();
		   return true;
	}

	public Object next() {
		if (!iterador.hasNext())
			 return null;
	    return iterador.next();
			
	}

	public void remove() {
		// TODO Auto-generated method stub
	}

}
