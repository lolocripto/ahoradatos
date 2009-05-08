package ar.com.datos.grupo5.trie.persistence;

import ar.com.datos.grupo5.trie.core.Nodo;

public interface ITriePersistence {

	
	public Nodo getNodo(Long i);
	
	public void saveNodo(Nodo unNodo);
	
	public void flush();
	
	public void agregarNodoSucio(Nodo unNodo);
	
	
	
}
