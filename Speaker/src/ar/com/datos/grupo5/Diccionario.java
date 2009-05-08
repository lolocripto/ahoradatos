package ar.com.datos.grupo5;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.trie.core.TrieAdministrator;

/**
 * Clase que permite manipular el diccionario.
 * @author LedZeppeling
 */
public class Diccionario {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(Diccionario.class);

	private TrieAdministrator ta;
	
	/**
	 * Constructor de la clase.
	 *
	 */
	public Diccionario() {
		this.ta = new TrieAdministrator();
	}
	
	/**
	 * Busca en el diccionario la palabra que recibe.
	 * 
	 * @param palabra
	 *            La palabra que se quiere buscar.
	 * @return null si no encuentra la palabra, si no devuelve elregitro.
	 */
	public final Long buscarPalabra(final String palabra) {
	
		return this.ta.buscarPalabra(palabra);
	}
	
	/**
	 * Metodo para agregar una palabra al diccionario.
	 * 
	 * @param palabra
	 *            La palabra que se quiere agregar al diccionario.
	 * @param offset
	 *            Es la posición de la palabra dentro del archivo de audio.
	 * @return retorna TRUE si pudo agregr la palabra, o FALSE en caso
	 *         contrario.
	 * @throws FileNotFoundException
	 * @link ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
	 */
	public final boolean agregar(final String palabra, final Long offset) {		
		
		return this.ta.agregarPalabra(palabra, offset);
		
	}
	
	public final void cerrar(){
		this.ta.terminarSesion();
	}


}

