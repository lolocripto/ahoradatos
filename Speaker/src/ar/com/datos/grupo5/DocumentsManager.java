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
		
		if(this.archivo.getModoEnEjecucion() !=0){
			this.archivo.terminarSession();
		}
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
	
	public void initDocWriteSession(){
		
		if(this.archivo.getModoEnEjecucion() !=0){
			this.archivo.terminarSession();
		}
		
		this.archivo.iniciarSession();
		this.archivo.setDocumentToWrite();
	}
	
	
	public void escribirOtroDocumento(){
		this.cerrarSesion();
		this.initDocWriteSession();
	}
	
	public boolean setNombreDoc(String nombreDoc){
		return this.archivo.setCurrentDocName(nombreDoc);
	}
	
	public Long getCantidadDocsAlmacenados(){
		return this.archivo.getCantDocsAlmacenados();
	}
	
	/*
	 * Para usar esta funcion debe haberse iniciado una sesion de lectura
	 */
	public String getNombreDoc(Long offsetDoc){
		return this.archivo.getDocName(offsetDoc);
		
	}
	
	public void escribirLinea(String linea){	
		this.archivo.escribirLinea(linea);
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		this.cerrarSesion();
		super.finalize();
	}


	public void cerrarSesion(){
		this.archivo.terminarSession();
	}
	
}
