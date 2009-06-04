/**
 * 
 */
package ar.com.datos.grupo5.interfaces;

/**
 * @author Led Zeppelin
 *
 */
public interface Compresor {

	public byte[] comprimir(String cadena);
	
	public byte[] finalizarCompresion();
	
	public String descomprimir();
}
