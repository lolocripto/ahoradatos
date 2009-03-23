package ar.com.datos.grupo5;

import ar.com.datos.grupo5.interfaces.Archivo;

/**
 * @author Diego
 *
 */
public class Secuencial implements Archivo {
	/**
	 * Metodo para Intentar abrir un archivo, pasado por parámetro.
	 * @param archivo Path completo del archivo.
	 * @param modo Es el modo en el que se abrirá el archivo {R,W,R+,A}.
	 */
	public void abrir(final String archivo, final String modo) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metodo para borrar una cadena en el archivo en el que se está trabajando.
	 * @param patron Es la cadena a buscar en el archivo.
	 * @return Devuelve el resultado de la operación,
	 * y queda posicionado el puntero
	 * en la posición donde se encontraba la cadena buscada.
	 * En caso de no encontrar la cadena, devuelve false y el
	 * puntero queda donde
	 * debería estar la cadena buscada.
	 */
	public final boolean borrar(final String patron) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para buscar en un archivo, una cadena pasada por parámetro.
	 * @param patron Es la cadena a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 * puntero en la posición donde debería estar el patrón buscado.
	 */
	public final boolean buscar(final String patron) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para cerrar el archivo que se está manipulando.
	 */
	public void cerrar() {
		// TODO Auto-generated method stub

	}

	/**
	 * Metodo para Intentar crear un archivo, pasado por parámetro.
	 * @param archivo Path completo del archivo.
	 * @param tipo Es el tipo de archivo que se quiere crear {B,T}.
	 */
	public void crear(final String archivo, final String tipo) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metodo para Insertar la cadena en el archivo
	 * en el que se está trabajando.
	 * @param cadena Es la palabra o frase que se va a agregar al archivo.
	 */
	public void insertar(final String cadena) {
		// TODO Auto-generated method stub

	}

}
