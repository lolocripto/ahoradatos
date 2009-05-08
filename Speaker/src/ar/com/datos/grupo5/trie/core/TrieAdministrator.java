package ar.com.datos.grupo5.trie.core;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.trie.persistence.AdministrationRegistry;
import ar.com.datos.grupo5.trie.persistence.TrieArchiveHandler;
import ar.com.datos.grupo5.trie.persistence.TriePersistenceImpl;
import ar.com.datos.grupo5.trie.proxy.PersistentNodeFactory;

public class TrieAdministrator {

	private Trie mitrie;
	private TrieArchiveHandler admArchivo;
	private TriePersistenceImpl persistencia;
	
	
	public TrieAdministrator() {
		
		//instancio el administrador del archivo
		admArchivo = new TrieArchiveHandler();
		
		//instancio el administrador de persistencia
		persistencia = TriePersistenceImpl.getPersistenceSession(this.admArchivo);
		
		//recupero el registro administrativo
		this.recuperarRegistroAdministrativo();
		
		//verifico si es la primera vez que se ejecuta
		if (AdministrationRegistry.getCantidadNodosExistentes() ==0){ //primera apertura
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
	
	private void recuperarRegistroAdministrativo(){
		
		admArchivo.iniciarRegistroAdministrativo();
		
		int a = Constantes.SIZE_OF_TRIE;
		if (AdministrationRegistry.getCantidadNodosExistentes() ==0){
			AdministrationRegistry.setProfundidadActual(a);
		}
		int b = AdministrationRegistry.getProfundidadCreacionTrie();
		
		if (a != b){
			System.out.println("la profundidad de creacion del trie es diferente a la del archivo de configuracion, se aborta");
		}
	}
	
	
	public boolean agregarPalabra(String pal, Long puntero){
		
		PunteroSonido punt = new PunteroSonido(puntero);
		
		if (mitrie.search(pal) == null ){
			
		System.out.println("palabra: " + pal + "  no encontrada se almacena");	
		
		this.mitrie.insert(pal, punt);
		return true;
		}else{
			return false;
		}
		
	}
	
	public Long buscarPalabra (String pal){
		
		PunteroSonido punt = this.mitrie.search(pal);
		if (punt == null){
			System.out.println("palabra no encontrada");
			return null;
		}else{
			System.out.println("palabra: " + pal + "  encontrada se devuelve puntero");	
			return punt.getOffset();
		}
	}
	
	
	public void terminarSesion(){
		
		persistencia.flush();
		admArchivo.closeArchivo();
		persistencia =null;
		admArchivo = null;
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
