package ar.com.datos.grupo5;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Archivo;



/**
 * Clase que permite manipular el diccionario
 * @see ar.com.datos.grupo5.interfaces.Archivo.
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
	 * @param archivo Path completo del archivo.
	 * @param modo Es el modo en el que se abrirá el archivo {R,W,R+,A}.
	 * @return devuelve el resultado de la operación.
	 * @throws FileNotFoundException 
	 */
	public boolean  cargar(final String archivo, final String modo) throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	/**
	 * Método que devuelve el archivo referenciado.
	 * @return El archivo diccionario.
	 */
	public Archivo getArchivo() {
		return archivo;
	}
	/**
	 * Método que cierra el diccionario.
	 * @throws IOException 
	 */
	public void cerrar() throws IOException {
		this.archivo.cerrar();
	}

}
