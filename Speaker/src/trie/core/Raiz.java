package trie.core;

public class Raiz extends Nodo {

	private int ultimoNodo;
	
	public int getUltimoNodo() {
		return ultimoNodo;
	}

	public void sumarUnNodo() {
		this.ultimoNodo ++;
		this.isDirty = true;
	}

	public Raiz() {
		super();
		this.isDirty =false;
		this.contenido = " ";
	}
	
	public void setContenido(String nuevoContenido){
	}
	
	
	
}
