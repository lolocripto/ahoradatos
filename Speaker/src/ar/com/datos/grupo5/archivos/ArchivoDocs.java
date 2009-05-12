package ar.com.datos.grupo5.archivos;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.registros.RegistroFTRS;

public class ArchivoDocs {


	/**
	 * Mantiene la longitud del documento actual.
	 */
	private long longitudDoc;
	
	private Directo miArchivo;
	
	private long offsetLastDoc;

	private byte modoEnEjecucion;  //0 esperando setear modo - 1 lectura - 2 escritura
	
	private byte codRedundancia = -128;
	
	private boolean masLineasLeer;
	
	private long cantDocsAlmacenados;
	
	private String NombreDoc;
	
	private long offsetNombreDocActual;

	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger LOG = Logger
			.getLogger(ArchivoDocumentos.class);

    private String rutaArchivo = Constantes.ARCHIVO_DOCUMENTOS;

    
    public ArchivoDocs() {
    	miArchivo = new Directo();
    	try {
			miArchivo.abrir(rutaArchivo, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			LOG.error("no se ha podido abrir el archivo de documentos", e);
			e.printStackTrace();
		}
	}
	
    
    public long getCantidadDocs(){
    	
    	long adevolver = 0;
    	try {
			this.miArchivo.posicionar(0);
			adevolver = this.miArchivo.file.readLong();
			
		} catch (IOException e) {
			LOG.error("no se pudo leer la cantidad de documentos",e);
			e.printStackTrace();
		}
		
		this.cantDocsAlmacenados =adevolver;
		return adevolver;

    }
	
	private void escribirCantidadDocs(){
		
		try{
			this.miArchivo.posicionar(0);
			this.miArchivo.file.writeLong(this.cantDocsAlmacenados);

		} catch (IOException e) {
			LOG.error("no se pudo escribir la cantidad de documentos",e);
			e.printStackTrace();

		}

	}
	
	
	
	public void DocumentToWrite(String nombre, long longitud){
		
		try {
			
			//
			this.miArchivo.file.seek(miArchivo.file.length());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
	
}
