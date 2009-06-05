package ar.com.datos.grupo5.compresion.lzp;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:57:27 a.m.
 */
public class ParCtxUbicacion {

	private String contexto;
	private int posicion;

	public ParCtxUbicacion(){

	}

	public void finalize() throws Throwable {

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

	/**
	 * @param contexto the contexto to set
	 */
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	/**
	 * @return the contexto
	 */
	public String getContexto() {
		return contexto;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

}