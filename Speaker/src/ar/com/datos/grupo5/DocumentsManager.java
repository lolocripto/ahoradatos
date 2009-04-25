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

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.archivos.Archivo;
import ar.com.datos.grupo5.archivos.Directo;
import ar.com.datos.grupo5.registros.RegistroDocumento;


/**
 * @author xxvkue
 *
 */
public class DocumentsManager {


	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private Archivo archivo;
	
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
		this.archivo = new Directo();
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
	public Long agregarDocumento(final String Direccion) throws Exception, FileNotFoundException{
		
		BufferedReader buffer;
		File archivoInput = new File(Direccion);

		if (!archivoInput.exists()) {
			FileNotFoundException e = new FileNotFoundException(
					"No existe el archivo pasado al Parser");
			milogueador.error("no existe el archivo", e);
			throw e;
		}
		
		try {
			FileInputStream fis = new FileInputStream(archivoInput);
			InputStreamReader isr = new InputStreamReader(fis,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			buffer = new BufferedReader(isr);
		} catch (Exception e) {
			milogueador.error("Error: " + e.getMessage());
			throw e;
		}
		
		String Documento = new String();
		while(moreLines) {
			Documento += leerLinea(buffer);
		}
		
		RegistroDocumento reg = new RegistroDocumento();
		
		//reg.setDato(Documento);
		
		// Ahora debería agregar el registro al archivo
		
		
		
		/*
		RegistroAudio reg = new RegistroAudio();
		
		ByteArrayOutputStream audioByteArray = (ByteArrayOutputStream) Direccion;
		
		reg.setDato(audioByteArray);
		reg.setBytes(audioByteArray.toByteArray(), 0L);
		try {
			this.archivo.insertar(reg);
			return this.archivo.getOffset();
		} catch (Exception e) {
			return null;
		}
		*/		
		return 0L;
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
