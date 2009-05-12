/**
 * 
 */
package ar.com.datos.grupo5;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.archivos.ArchivoDocs;
import ar.com.datos.grupo5.archivos.ArchivoDocumentos;


/**
 * @author zeke
 *
 */
public class DocumentsManager {

	long offsetUltDocEscrito;

	/**
	 * Objeto que maneja las operaciones sobre archivos.
	 */
	private ArchivoDocs archivo;
	
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
			this.archivo = new ArchivoDocs();
			this.offsetUltDocEscrito = 0;
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
		
		this.archivo.documentToRead(offsetArchivo);
	}
	
	public long getOffsetUltDoc(){
		return this.offsetUltDocEscrito;
	}
	
	/**
	 * Metodo que lee lineas del archivo, si no hay mas, devuelve null
	 * @return
	 */
	public String leerLinea(){
		
		if (this.archivo.masLineasParaLeer()){
		return this.archivo.leerLinea();
		}else{
		return null;
		}
	}
	
	public void initDocWriteSession(String nombre, long longitud){
		
		this.offsetUltDocEscrito = this.archivo.documentToWrite(nombre, longitud);
	}
	
	public Long getCantidadDocsAlmacenados(){
		return (long) this.archivo.getCantidadDocs();
	}
	
	/*
	 * Para usar esta funcion debe haberse iniciado una sesion de lectura
	 */
	public String getNombreDoc(Long offsetDoc){
		
		return this.archivo.nombreDoc(offsetDoc);
		
	}
	
	public void escribirLinea(String linea){	
		this.archivo.escribirLinea(linea);
	}

	public void cerrarSesion(){
		this.archivo.cerrarArchivo();
	}
	
}
