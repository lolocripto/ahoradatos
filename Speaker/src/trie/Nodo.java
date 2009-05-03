package trie;

import java.util.HashMap;


public class Nodo {

	private String contenido;
	private PunteroSonido puntero; 
	private HashMap<String, Nodo> hijo;
	
	private Boolean isDirty =false;
	

	public Nodo()
	{
		puntero=null;
		// One child corresponding to one lowercase alphabet
		hijo = new HashMap<String, Nodo>(); 
		this.isDirty = true;
	}
	
	public Nodo(String letra)
	{
		// Cell 0 corresponds to the character 'a'
		// Cell 1 corresponds to the character 'b'
		// ... and so on
		contenido = letra;
		puntero=null;
		hijo =  new HashMap<String, Nodo>();
		this.isDirty = true;
	}
	
	public Nodo(char letra)
	{
		// Cell 0 corresponds to the character 'a'
		// Cell 1 corresponds to the character 'b'
		// ... and so on
		Character casteo = new Character(letra);
		contenido = casteo.toString();
		puntero=null;
		hijo =  new HashMap<String, Nodo>();
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
	
	public Nodo getHijo(String letraOpalabra){
		return this.hijo.get(letraOpalabra);
	}
	
	public Nodo getHijo (char letra){
		Character casteo = new Character(letra);
		return this.hijo.get(casteo.toString());
	}
	
	public void setNuevoHijo(Nodo nuevoNodo){
		this.hijo.put(nuevoNodo.getContenido(), nuevoNodo);
	}
	
	public void reHidratar (){
		//implementar
	}
	
	public void serializar(){
		//implementar
	}

}
