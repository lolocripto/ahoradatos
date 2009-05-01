/**
 * 
 */
package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.archivos.ArchivoDeTerminos;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.registros.RegistroTerminoGlobal;



/**
 * @author LedZeppeling
 *
 */
public class TerminosGlobales {

	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private ArchivoDeTerminos archivo;
	
	/**
	 * Constructor de la clase AudioFileManager.
	 *
	 */
	public TerminosGlobales() {
		this.archivo = new ArchivoDeTerminos();
	}
	
	/**
	 * Metodo para preparar el archivo de datos audios, accediendo al archivo.
	 * 
	 * @throws FileNotFoundException
	 */
	public boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}

	/**
	 * Método que cierra el diccionario.
	 * 
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		this.archivo.cerrar();
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
	public Long agregar(final String termino){
		
		RegistroTerminoGlobal reg = new RegistroTerminoGlobal();
		reg.setDato(termino);
		try {
			this.archivo.insertar(reg);
			return this.archivo.getOffset();
		} catch (Exception e) {
			return null;
		}		
	}
	
	public String leerTermino(final Long offset){
		
		RegistroTerminoGlobal reg = new RegistroTerminoGlobal();
		try {
			reg = (RegistroTerminoGlobal) this.archivo.leer(offset);
		} catch (IOException e) {
			return null;
		}
		
		return reg.getDato();
	}

}
