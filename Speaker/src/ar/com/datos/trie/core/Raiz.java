package ar.com.datos.trie.core;

public class Raiz extends Nodo {

	private int ultimoNodo;
	
	public int getUltimoNodo() {
		return ultimoNodo;
	}

	public void sumarUnNodo() {
		this.ultimoNodo ++;

	}

	public Raiz() {
		super();

		this.contenido = " ";
	}
	
	public void setContenido(String nuevoContenido){
	}
	
	
	
}
