package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;

public class TerminoDocumentoManager {

	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private Archivo archivo;
	
	
	/**
	 * Constructor de la clase AudioFileManager.
	 *
	 */
	public TerminoDocumentoManager() {
		//TODO: Revisar que el constructor de BloqueBSharp exista
		this.archivo = new BloqueBsharp();
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
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
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
	public Long insertar(final Collection<Long> frecuencia, final Collection<Long> listaOffsetDocumentos){
		
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		
		/*
		ByteArrayOutputStream audioByteArray = (ByteArrayOutputStream) audio;
		
		reg.setDato(audioByteArray);
		reg.setBytes(audioByteArray.toByteArray(), 0L);
		*/
		
		//TODO: Cargar el registro
		
		try {
			this.archivo.insertar(reg);
			return this.archivo.getOffset();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Lee el registro del archivo y devuelve el resultado de la operacion
	 * quedando accesible la lista de offset a documentos y la frecuencia.
	 * @param offset
	 * @return
	 */
public RegistroTerminoDocumentos leer(final Long offset){
		
//COMMENT: Tengo que leer bloques
		byte[] bufferDato;
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();			
			
		try {
			 bufferDato = this.archivo.leerBloque(offset);
		} catch (IOException e) {
			return null;
		} catch (UnImplementedMethodException e1) {
			return null;
		}
		/*
		is = new ByteArrayInputStream(reg.getBytes());
		return is;
	*/
	return reg;
	}

	
}
