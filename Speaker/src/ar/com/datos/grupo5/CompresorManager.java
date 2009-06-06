/**
 * 
 */
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.compresion.CompresorFactory;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.MetodoCompresion;

/**
 * @author Led Zeppelin
 *
 */
public class CompresorManager {

	private Compresor compresor;
	
	private String pathArchivoOrigen;
	
	private String pathArchivoDestino;
	
	public CompresorManager(final String metodo, final String pathArchivoOrigenExt, final String pathArchivoDestinoExt){
		this.compresor = CompresorFactory.getCompresor(metodo);
		this.pathArchivoDestino = pathArchivoDestinoExt;
		this.pathArchivoOrigen = pathArchivoOrigenExt;
	}
	
	public final String ComprimirArchivo() throws SessionException{
		byte[] salidaCompresor;
		//Abrir archivo y leer linea por linea.
		String linea = "";
		salidaCompresor = null;
		this.compresor.comprimir(linea);
		//escribo salida y vuelvo a leer.
		return "Compresión exitosa";
	}

	public final String DesComprimirArchivo() {
		// TODO Auto-generated method stub
		return "Descompresión exitosa";
	}
	
}
