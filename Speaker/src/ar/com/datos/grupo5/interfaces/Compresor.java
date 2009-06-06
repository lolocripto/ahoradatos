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
	public void iniciarsesion();
	
	/**
	 * Comprime una cadena, este metodo se debe llamar en un bucle.
	 * @param cadena La cadena a comprimir.
	 * @return bytes.
	 */
	public void procesar(String cadena);

	/**
	 * Obtiene los datos comprimidos hasta el momento.
	 * @return bytes con los datos comprimidos.
	 */
	public byte[] obtenerBuffer();
	
	/**
	 * Finaliza la sesion actual y deja todo lista para comprimir/descomprimir otro archivo.
	 * @return los bytes que quedaron sin obtenerse aun. 
	 */
	public byte[] finalizarSession();
	
	/**
	 * 
	 * @return
	 */
	public String descomprimir(byte[] datos);
}
