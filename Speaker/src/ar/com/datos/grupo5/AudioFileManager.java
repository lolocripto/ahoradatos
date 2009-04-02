/**
 * 
 */
package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;



/**
 * @author xxvkue
 *
 */
public class AudioFileManager {

	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private Archivo archivo;
	
	/**
	 * Constructor de la clase AudioFileManager.
	 *
	 */
	public AudioFileManager(){
		this.archivo = new Directo();
	}
	
	/**
	 * @param archivo
	 *            El archivo físico diccionario.
	 */
	private void setArchivo(final Archivo archivo) {
		this.archivo = archivo;
	}
	
	/**
	 * Metodo para preparar el archivo de datos audios, 
	 * accediendo al archivo.
	 * 
	 * @throws FileNotFoundException
	 */
	public boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	
	/**
	 * Método que cierra el diccionario.
     * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		this.archivo.cerrar();
	}

	/**
	 * Metodo para agregar una palabra al diccionario.
	 * @param palabra La palabra que se quiere agregar al diccionario.
	 * @param offset Es la posición de la palabra dentro del archivo de audio.
	 * @return retorna TRUE si pudo agregr la palabra, o FALSE en caso contrario.
	 * @throws FileNotFoundException
	 * @link ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
	 */
	public Long agregar(final OutputStream audio){
		
		RegistroAudio reg = new RegistroAudio();
		
		ByteArrayOutputStream audioByteArray = (ByteArrayOutputStream) audio;
		
		reg.setDato(audioByteArray);
		reg.setBytes(audioByteArray.toByteArray(), 0L);
		try {
			this.archivo.insertar(reg);
			return this.archivo.getOffset();
		} catch (Exception e) {
			return null;
		}		
	}
	
	public InputStream leerAudio(final Long offset){
		

		InputStream is = null;			
			
		RegistroAudio reg = new RegistroAudio();
		try {
			reg = (RegistroAudio) this.archivo.leer(offset);
		} catch (IOException e) {
			return null;
		} catch (UnImplementedMethodException e1) {
			return null;
		}
		
		is = new ByteArrayInputStream(reg.getBytes());
		return is;
	}

}
