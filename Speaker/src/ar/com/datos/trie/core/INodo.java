package ar.com.datos.trie.core;

import ar.com.datos.trie.persistence.TrieNodeRegistry;


public interface INodo {

	
		public Long getNumeroNodo();
   	
		public String getContenido();
		
		public PunteroSonido getPuntero();
		
		public void setContenido(String nuevoContenido);
		
		public void setPuntero (PunteroSonido nuevoPuntero);
		
		public INodo getHijo(String letraOpalabra);
		
		public INodo getHijo (char letra);
		
		public void setNuevoHijo(INodo nuevoNodo);
		
		public void setNumeroNodo(long numeroNodo);
		
		public TrieNodeRegistry getRegistroAsociado();
		
		public void setRegistroAsociado(TrieNodeRegistry t);

	}

