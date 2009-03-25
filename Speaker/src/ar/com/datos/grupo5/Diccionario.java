package ar.com.datos.grupo5;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Archivo;



/**
 * Clase que permite manipular el diccionario
 * @see ar.com.datos.grupo5.interfaces.Archivo
 * @author Diego
 *
 */
public class Diccionario {
	
	private Archivo archivo;
	
	/**
	 * @param archivo El archivo físico diccionario.
	 */
	private void setArchivo(final Archivo archivo) {
		this.archivo = archivo;
	}
	/**
	 * Metodo para cargar el diccionario, accediendo al archivo.
     * @see ar.com.datos.grupo5.interfaces.Archivo#cargar()
	 * @throws FileNotFoundException 
	 */

	public boolean  cargar(final String archivo, final String modo) throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	/**
	 * Método que devuelve el archivo referenciado.
     * @see ar.com.datos.grupo5.interfaces.Archivo#getArchivo()
	 */
	public Archivo getArchivo() {
		return archivo;
	}
	/**
	 * Método que cierra el diccionario.
     * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public void cerrar() throws IOException {
		this.archivo.cerrar();
	}

}
