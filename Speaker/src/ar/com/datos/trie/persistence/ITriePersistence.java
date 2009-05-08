package ar.com.datos.trie.persistence;

import ar.com.datos.trie.core.Nodo;

public interface ITriePersistence {

	
	public Nodo getNodo(Long i);
	
	public void saveNodo(Nodo unNodo);
	
	public void flush();
	
	public void agregarNodoSucio(Nodo unNodo);
	
	
	
}
