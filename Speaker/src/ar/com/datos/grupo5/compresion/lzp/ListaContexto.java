package ar.com.datos.grupo5.compresion.lzp;

import java.util.ArrayList;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:19 a.m.
 */
public class ListaContexto {

	private ArrayList<ParCtxUbicacion> lista;

	public ListaContexto(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param contexto
	 */
	public void buscarContexto(String contexto){

	}

	public byte[] getBytes(){
		return null;
	}

	/**
	 * 
	 * @param datos
	 */
	public void setBytes(byte[] datos){

	}

	public String siguienteContexto(){
		return "";
	}

	/**
	 * @param lista the lista to set
	 */
	public void setLista(ArrayList<ParCtxUbicacion> lista) {
		this.lista = lista;
	}

	/**
	 * @return the lista
	 */
	public ArrayList<ParCtxUbicacion> getLista() {
		return lista;
	}

}