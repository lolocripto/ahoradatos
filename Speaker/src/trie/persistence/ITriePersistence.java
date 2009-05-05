package trie.persistence;

import trie.core.Nodo;

public interface ITriePersistence {

	
	public Nodo getNodo(int i);
	
	public void saveNodo(Nodo unNodo);
	
	public void updateNodo (Nodo unNodo);
	
}
