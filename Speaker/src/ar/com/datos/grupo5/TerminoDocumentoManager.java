package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.SortedMap;
import java.util.Map;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;

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
		this.archivo = new ArchivoBloques();
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
	 * @param frecuencia
	 *            Un vector con las frecuencias de la palabra en el documento.
	 * @param listaOffsetDocumentos
	 *            Offset de los documentos en que se encuntran las palabras.
	 * @return retorna el offset donde fue insertado el registro con la lista.
	 * @throws FileNotFoundException
	 */
	public Long insertar(final Collection<ParFrecuenciaDocumento> Lista){
		
		// Voy a insertar OffsetProxRegistro + Bytes del registroTerminoDocumentos
		
		
		
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		
		reg.setDatosDocumentos(Lista);
	
		/*
		ByteArrayOutputStream audioByteArray = (ByteArrayOutputStream) audio;
		
		reg.setDato(audioByteArray);
		reg.setBytes(audioByteArray.toByteArray(), 0L);
		*/
		
		//TODO: Cargar el registro
		
		//reg = reg.set(frecuencia, listaOffsetDocumentos);
		
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

	private boolean cargarRegistro(Collection<Long> frecuencia, Collection<Long> listaOffsetDocumentos){
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		return true;
	}
}
