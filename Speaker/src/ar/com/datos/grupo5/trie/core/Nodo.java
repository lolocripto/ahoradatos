package ar.com.datos.grupo5.trie.core;

import java.util.HashMap;

import ar.com.datos.grupo5.trie.persistence.TrieNodeRegistry;





public class Nodo implements INodo{

	protected String contenido;
	protected PunteroSonido puntero; 
	protected HashMap<String, INodo> hijo;
	private Long NumeroNodo;
	private TrieNodeRegistry registroAsociado;
	
	
	public Long getNumeroNodo() {
		return NumeroNodo;
	}

	public void setNumeroNodo(long numeroNodo) {
		NumeroNodo = numeroNodo;
	}
	

	public Nodo()
	{
		puntero=null;
		hijo = new HashMap<String, INodo>(); 
	}
		
	public Nodo(String letra)
	{
		
		contenido = letra;
		puntero=null;
		hijo =  new HashMap<String, INodo>();
	}
	
	public Nodo(char letra)
	{
	
		Character casteo = new Character(letra);
		contenido = casteo.toString();
		puntero=null;
		hijo =  new HashMap<String, INodo>();
	}


	public String getContenido(){
		return this.contenido;
	}
	
	public PunteroSonido getPuntero(){
		return this.puntero;
	}
	
	public void setContenido(String nuevoContenido){
		this.contenido = nuevoContenido;

	}
	
	public void setPuntero (PunteroSonido nuevoPuntero){
		this.puntero = nuevoPuntero;

	}
	
	public INodo getHijo(String letraOpalabra){
		return this.hijo.get(letraOpalabra);
	}
	
	public INodo getHijo (char letra){
		Character casteo = new Character(letra);
		return this.hijo.get(casteo.toString());
	}
	
	public void setNuevoHijo(INodo nuevoNodo){
		this.hijo.put(nuevoNodo.getContenido(), nuevoNodo);
	}

	public TrieNodeRegistry getRegistroAsociado() {
		
		return this.registroAsociado;
	}


	public void setRegistroAsociado(TrieNodeRegistry t) {
		this.registroAsociado = t;
		
	}


}
