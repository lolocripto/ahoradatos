/**
 * 
 */
package ar.com.datos.grupo5.interfaces;

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
	 */
	public String comprimir(String cadena);

	/**
	 * Finaliza la sesion actual y deja todo lista para comprimir/descomprimir
	 * otro archivo.
	 */
	public void finalizarSession();
	
	/**
	 * 
	 * @return la cadena descomprimida.
	 */
	public String descomprimir(String datos);
}
