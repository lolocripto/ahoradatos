/**
 * 
 */
package ar.com.datos.grupo5.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Interface para manejar Archivos.
 * @author Diego
 *
 */
public interface Archivo {
	/**
	 * Metodo para Intentar abrir un archivo, pasado por parámetro.
	 * @param archivo Path completo del archivo.
	 * @param modo Es el modo en el que se abrirá el archivo {R,W,R+,A}.
	 * @return devuelve el resultado de la operación.
	 * @throws FileNotFoundException 
	 */
	boolean abrir(final String archivo, final String modo) throws FileNotFoundException;
	
	/**
	 * Metodo para Intentar crear un archivo, pasado por parámetro.
	 * @param archivo Path completo del archivo.
	 * @throws FileNotFoundException si no se puede crear el archivo.
	 */
	void crear(final String archivo) throws FileNotFoundException;
	
	/**
	 * Metodo para cerrar el archivo que se está manipulando.
	 * @throws IOException 
	 */
	void cerrar() throws IOException;
		
	/**
	 * Metodo para buscar en un archivo, una cadena pasada por parámetro.
	 * @param registro : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 * puntero en la posición donde debería estar el patrón buscado.
	 */
	boolean buscar(final Registro registro);
		
	/**
	 * Metodo para Insertar la cadena en el archivo 
	 * en el que se está trabajando.
	 * @param registro Es el registro que se va a agregar al archivo.
	 * @throws IOException 
	 */
	void insertar(final Registro registro) throws IOException;
	
	/**
	 * Metodo para borrar una cadena en el archivo en el que se está trabajando.
	 * @param registro Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la operación,
	 * y queda posicionado el puntero
	 * en la posición donde se encontraba la cadena buscada.
	 * En caso de no encontrar la cadena, devuelve false
	 *  y el puntero queda donde
	 * debería estar la cadena buscada.
	 */
	boolean borrar(final Registro registro);
}
