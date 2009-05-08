package ar.com.datos.trie.persistence;

import java.util.ArrayList;
import java.util.Iterator;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.trie.core.Nodo;

public class TriePersistenceImpl implements ITriePersistence {

	TrieArchiveHandler tah;
	ArrayList<Nodo> listaNodosSucios;
	private int cantNodosSucios;
	private int autoFlush = Constantes.AUTO_FLUSH_AT;
	

	static private TriePersistenceImpl singleton = null;

	private TriePersistenceImpl(TrieArchiveHandler administradorArchivo) {
		this.tah = administradorArchivo;
		this.listaNodosSucios = new ArrayList<Nodo>();
	}

	static public TriePersistenceImpl getPersistenceSession(
			TrieArchiveHandler administradorArchivo) {

		if (singleton == null) {
			singleton = new TriePersistenceImpl(administradorArchivo);
		}
		return singleton;
	}

	static public TriePersistenceImpl getPersistenceSession() {

		return singleton;
	}
	
	public void agregarNodoSucio(Nodo unNodo){
		this.listaNodosSucios.add(unNodo);
		this.cantNodosSucios++;
		
		if (this.cantNodosSucios > this.autoFlush){
			this.flush();
			this.cantNodosSucios =0;
		}
	}
	
	public Nodo nuevoNodo(char i) {

		Nodo unNodo;
		// creo un nuevo nodo con el valor del char pasado por parametro
		unNodo = new Nodo(i);
		// agrego al nodo un numero que voy a obtener del registro de
		// administracion
		unNodo.setNumeroNodo(AdministrationRegistry.sumarUnNodo());
		// creo un registro que lo represente
		TrieNodeRegistry t = new TrieNodeRegistry(unNodo);
		
		t.setDirty(true);
		this.agregarNodoSucio(unNodo);
		
		return unNodo;

	}
	
	public Nodo nuevoNodo(String i) {

		Nodo unNodo;
		// creo un nuevo nodo con el valor del char pasado por parametro
		unNodo = new Nodo(i);
		// agrego al nodo un numero que voy a obtener del registro de
		// administracion
		unNodo.setNumeroNodo(AdministrationRegistry.sumarUnNodo());
		// creo un registro que lo represente
		TrieNodeRegistry t = new TrieNodeRegistry(unNodo);
		
		t.setDirty(true);
		this.agregarNodoSucio(unNodo);
		
		return unNodo;

	}
	

	public Nodo getNodo(Long i) {

		TrieNodeRegistry registroNodo;
		registroNodo = tah.recuperarNodo(i);
		registroNodo.setDirty(false);
		return registroNodo.getNodo();

	}

	public void saveNodo(Nodo unNodo) {

		if (unNodo.getRegistroAsociado().isDirty()) {
			tah.guardarNodo(unNodo.getRegistroAsociado());
			unNodo.getRegistroAsociado().setDirty(false);

		}
	}

	/**
	 * Metodo que permite persistir todos los objetos de la lista de dirtys
	 */
	public void flush() {
		
		Iterator<Nodo> it = this.listaNodosSucios.iterator();
		
		while (it.hasNext()){
			this.saveNodo(it.next());
		}
		
		this.listaNodosSucios.clear();

	}

}
