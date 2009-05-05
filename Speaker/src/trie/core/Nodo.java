package trie.core;

import java.util.HashMap;

import javax.swing.text.StyledEditorKit.BoldAction;



public class Nodo implements INodo{

	protected String contenido;
	protected PunteroSonido puntero; 
	protected HashMap<String, INodo> hijo;
	protected Boolean isDirty =false;
	private Long NumeroNodo;
	
	public Long getNumeroNodo() {
		return NumeroNodo;
	}

	public void setNumeroNodo(long numeroNodo) {
		NumeroNodo = numeroNodo;
	}

	protected boolean eshoja;
	

	public Nodo()
	{
		puntero=null;
		hijo = new HashMap<String, INodo>(); 
		this.isDirty = true;
	}
		
	public Nodo(String letra)
	{
		
		contenido = letra;
		puntero=null;
		hijo =  new HashMap<String, INodo>();
		this.isDirty = true;
	}
	
	public Nodo(char letra)
	{
	
		Character casteo = new Character(letra);
		contenido = casteo.toString();
		puntero=null;
		hijo =  new HashMap<String, INodo>();
		this.isDirty = true;
	}


	public String getContenido(){
		return this.contenido;
	}
	
	public PunteroSonido getPuntero(){
		return this.puntero;
	}
	
	public void setContenido(String nuevoContenido){
		this.contenido = nuevoContenido;
		this.isDirty = true;
	}
	
	public void setPuntero (PunteroSonido nuevoPuntero){
		this.puntero = nuevoPuntero;
		this.isDirty =true;
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


	public boolean esHoja() {
		return this.eshoja;
	}
	
	public void setHoja(Boolean a){
		this.eshoja = a;
	}

}
