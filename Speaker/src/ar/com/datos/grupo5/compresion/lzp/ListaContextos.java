package ar.com.datos.grupo5.compresion.lzp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:19 a.m.
 */
public class ListaContextos {
	/**
	 * 
	 */
	private Map<String, Integer> mapaContexto;
	
	/**
	 * 
	 */
	public final int size(){
		return mapaContexto.size();
	}

	/**
	 * 
	 */
	public ListaContextos(){
		mapaContexto = new HashMap<String, Integer>();
	}

	/**
	 * 
	 */
	public Integer getPosicion(String contexto) {
		return mapaContexto.get(contexto);
	}

	/**
	 * 
	 */
	public void setPosicion(String contexto, Integer posicion) {
		mapaContexto.put(contexto, posicion);
	}

	/**
	 * 
	 */	
	public byte[] getBytes(){
		return null;
	}

	/**
	 * 
	 * @param datos
	 */
	public void setBytes(byte[] datos){

	}

	/**
	 * 
	 * @param datos
	 */
	public final String toString(){
		String result = new String();
		for (Entry<String, Integer> entry : mapaContexto.entrySet()) {
			   result += entry.getKey() + "->" + entry.getValue();
			  }
		return result;
	}

}