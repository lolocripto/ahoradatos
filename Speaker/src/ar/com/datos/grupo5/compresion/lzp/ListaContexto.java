package ar.com.datos.grupo5.compresion.lzp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:19 a.m.
 */
public class ListaContexto {

	private Map<String, Integer> mapaContexto;

	public ListaContexto(){
		mapaContexto = new HashMap<String, Integer>();
	}

	public Integer getPosicion(String contexto) {
		return mapaContexto.get(contexto);
	}
	
	public void setPosicion(String contexto, Integer posicion) {
		mapaContexto.put(contexto, posicion);
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

}