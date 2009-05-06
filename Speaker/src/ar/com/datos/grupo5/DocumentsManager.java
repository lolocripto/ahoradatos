/**
 * 
 */
package ar.com.datos.grupo5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.archivos.Archivo;
import ar.com.datos.grupo5.archivos.ArchivoDocumentos;
import ar.com.datos.grupo5.archivos.Directo;
import ar.com.datos.grupo5.registros.RegistroDocumento;
import ar.com.datos.parser.ITextInput;


/**
 * @author xxvkue
 *
 */
public class DocumentsManager {


	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private ArchivoDocumentos archivo;
	
	/**
	 * Collection con bytes.
	 */
	private Collection<IunidadDeHabla> terminos; 
	
	/**
	 * 
	 */
	private ITextInput parser;
	
	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger milogueador = Logger.getLogger(DocumentsManager.class);
	
	/**
	 * Permite saber cual es la ruta del archivo pasado por parámetro.
	 */
	private String rutaArchivo;
	
	private boolean moreLines;
	
	/**
	 * Constructor de la clase AudioFileManager.
	 *
	 */
	public DocumentsManager() {
		try {
			this.archivo = new ArchivoDocumentos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public final Long agregarDocumento(final String Direccion) {
		return this.archivo.escribirDocumento(Direccion.getBytes());
	}
	
	/**
	 * Devuelve el contenido del documento
	 * @param offset
	 * @return
	 */
	public String leerDocumento(final Long offset){
		
/*
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
		*/
		return "";
	}

	/**
	 * Metodo que devuelve la proxima linea a procesar.
	 * @throws IOException
	 */
	private String leerLinea( final BufferedReader buffer) {

		String linea = "";

		while ((linea != null) && (linea.isEmpty())) {
			try {
				linea = (buffer.readLine());
			} catch (IOException e) {
				e.printStackTrace();
				this.moreLines = false;
				return null;
			}
		}
		if (linea != null) {
			this.moreLines = true;
			return linea;
		} else {
			moreLines = false;
			return null;
		}
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	
}
