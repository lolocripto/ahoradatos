/**
 * 
 */
package ar.com.datos.grupo5.interfaces;

import ar.com.datos.grupo5.excepciones.SessionException;

/**
 * @author Led Zeppelin
 *
 */
public interface Compresor {

	/**
	 * Inicia una session de compresion/descompresion.
	 */
	public void iniciarSesion();
	
	/**
	 * Comprime una cadena, este metodo se debe llamar en un bucle.
	 * 
	 * @param cadena
	 *            La cadena a comprimir.
	 * @return La cadena comprida en binario.
	 * @throws SessionException 
	 */
	public String comprimir(String cadena) throws SessionException;

	/**
	 * Finaliza la sesion actual y deja todo lista para comprimir/descomprimir
	 * otro archivo.
	 */
	public String finalizarSession();
	
	/**
	 * @param datos Los datos comprimidos en binario.
	 * @return la cadena descomprimida.
	 */
	public String descomprimir(StringBuffer datos) throws SessionException;
	
	/**
	 * Imprime los valores de la lista de Contexto
	 */
	public void imprimirHashMap();
}
