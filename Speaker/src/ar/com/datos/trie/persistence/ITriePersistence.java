package ar.com.datos.trie.persistence;

import ar.com.datos.trie.core.Nodo;

public interface ITriePersistence {

	
	public Nodo getNodo(int i);
	
	public void saveNodo(Nodo unNodo);
	
	public void updateNodo (Nodo unNodo);
	
}
