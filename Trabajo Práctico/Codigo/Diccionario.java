package ar.com.datos.grupo5;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;

/**
 * Clase que permite manipular el diccionario.
 * @author LedZeppeling
 */
public class Diccionario {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(Diccionario.class);

	/**
	 * Archivo que contendrá las palabras y que será manejado por el
	 * diccionario.
	 */
	private Archivo archivo;
	
	/**
	 * Metodo para cargar el diccionario, accediendo al archivo.
	 * 
	 * @param archivo
	 *            La ruta completa del archivo a cargar.
	 * @param modo
	 *            El modo en el cual se debe abrir el archivo.
	 * @return true si pudo abrir el archivo.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cargar()
	 */
	public final boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	
	/**
	 * Método que cierra el diccionario.
	 * 
	 * @throws IOException
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		this.archivo.cerrar();
	}
	
	/**
	 * Busca en el diccionario la palabra que recibe.
	 * 
	 * @param palabra
	 *            La palabra que se quiere buscar.
	 * @return null si no encuentra la palabra, si no devuelve elregitro.
	 */
	public final RegistroDiccionario buscarPalabra(final String palabra) {
		
		RegistroDiccionario reg = null;
		
		try {
			reg = (RegistroDiccionario) archivo.primero();
			
			while (reg != null) {
				if (reg.getDato().equals(palabra)) {
					break;
				}
				reg = (RegistroDiccionario) archivo.siguiente();
			}
			
		} catch (UnImplementedMethodException e) {
			logger.error("Error: " + e.getMessage());
		}
		
		return reg;
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
		RegistroDiccionario reg = new RegistroDiccionario();
		
		reg.setOffset(offset);
		reg.setDato(palabra);
		try {
			this.archivo.insertar(reg);
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	
	/**
	 * Constructor de la clase.
	 *
	 */
	public Diccionario() {
		this.archivo = new Secuencial();
	}
}

