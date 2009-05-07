/**
 * 
 */
package ar.com.datos.grupo5;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.archivos.ArchivoDocumentos;


/**
 * @author zeke
 *
 */
public class DocumentsManager {


	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private ArchivoDocumentos archivo;
	
	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger milogueador = Logger.getLogger(DocumentsManager.class);
	
	static private DocumentsManager singleton = null;
	
	/**
	 * Constructor de la clase AudioFileManager.
	 *
	 */
	private DocumentsManager() {
		try {
			this.archivo = new ArchivoDocumentos();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	public static DocumentsManager getInstance(){
		
		if (singleton == null){
			singleton = new DocumentsManager();
		}
		return singleton;
		
	}
	
	public void initReadSession(Long offsetArchivo){
		this.archivo.iniciarSession();
		this.archivo.setDocumentToRead(offsetArchivo);
	}
	
	
	public long getLongDocUltDoc(){
		return this.archivo.getLongitudDoc();
	}
	
	public long getOffsetUltDoc(){
		return this.archivo.getOffsetLastDoc();
	}
	
	/**
	 * Metodo que lee lineas del archivo, si no hay mas, devuelve null
	 * @return
	 */
	public String leerLinea(){
		
		if (this.archivo.masLineasParaLeer()){
		return this.archivo.leerLinea();
		}
		return null;
	}
	
	public void initWriteSession(){
		this.archivo.iniciarSession();
		this.archivo.setDocumentToWrite();
	}
	
	public void escribirLinea(String linea){	
		this.archivo.escribirLinea(linea);
	}
	
	
	public void cerrarSesion(){
		this.archivo.terminarSession();
	}
	
}
