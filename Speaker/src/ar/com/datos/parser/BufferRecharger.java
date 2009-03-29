package ar.com.datos.parser;

import java.util.Collection;

public interface BufferRecharger {

	public void recargarBuffer (Collection<Object> coleccion, int MaxBufferLeght);

	public boolean hasMoreItems(); 
}
