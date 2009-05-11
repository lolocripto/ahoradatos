package ar.com.datos.grupo5.trie.core;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.trie.persistence.TrieArchiveHandler;
import ar.com.datos.grupo5.trie.persistence.TriePersistenceImpl;
import ar.com.datos.grupo5.trie.proxy.PersistentNodeFactory;

public class TrieAdministrator {

	private Trie mitrie;
	private TrieArchiveHandler admArchivo;
	private TriePersistenceImpl persistencia;
	private static Logger LOG  = Logger.getLogger(TrieAdministrator.class);
	
	public TrieAdministrator() {
		
		//instancio el administrador del archivo
		admArchivo = new TrieArchiveHandler();
		
		//instancio el administrador de persistencia
		persistencia = TriePersistenceImpl.getPersistenceSession(this.admArchivo);
		
		//verifico si es la primera vez que se ejecuta
		if (admArchivo.cantNodosExistentes() ==0){ //primera apertura 
			//si es la primera vez creo la raiz y se la paso al trie
			INodo raiz = PersistentNodeFactory.getNewNodo(" ");
			mitrie = new Trie(raiz);
		}else{
			Long raiz = new Long(1);
			INodo raizProxy;
			raizProxy = PersistentNodeFactory.getNewProxy(persistencia.getNodo(raiz));
			mitrie = new Trie(raizProxy);
		}
	}

	/*
	private void recuperarRegistroAdministrativo(){
		
		admArchivo.iniciarRegistroAdministrativo();
		
		int a = Constantes.SIZE_OF_TRIE;
		if (AdministrationRegistry.getCantidadNodosExistentes() ==0){
			AdministrationRegistry.setProfundidadActual(a);
		}
		int b = AdministrationRegistry.getProfundidadCreacionTrie();
		
		if (a != b){
		//	System.out.println("la profundidad de creacion del trie es diferente a la del archivo de configuracion, se aborta");
		}
	}
	*/
	
	public boolean agregarPalabra(String pal, Long puntero){
		
		PunteroSonido punt = new PunteroSonido(puntero);
		
		if (mitrie.search(pal) == null ){
		
		this.mitrie.insert(pal, punt);
		
		this.fuerzaPersistencia();
		
		return true;
		}else{
			
			this.fuerzaPersistencia();
			return false;
		}
		
	}
	
	public Long buscarPalabra (String pal){
		
		PunteroSonido punt = this.mitrie.search(pal);
		if (punt == null){
			
			return null;
		}else{
			
			return punt.getOffset();
		}
	}
	
	
	private void fuerzaPersistencia(){
		persistencia.flush();
	}
	
	
	public void terminarSesion(){
		persistencia.flush();
		admArchivo.closeArchivo();
	}
	
	public int cantNodos(){
		return this.admArchivo.cantNodosExistentes();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (this.persistencia != null && this.admArchivo !=null){
		persistencia.flush();
		admArchivo.closeArchivo();
		}
		
		super.finalize();
	}
	
	
}
