/**
 * 
 */
package ar.com.datos.grupo5.archivos;


import java.io.FileNotFoundException;
import java.io.IOException;


import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;


/**
 * @author Led Zeppelin
 * 
 */
public class ArchivoDocumentos extends Directo {

	/**
	 * Mantiene la longitud del documento actual.
	 */
	private long longitudDoc;
	
	private long offsetLastDoc;

	private byte modoEnEjecucion;  //0 esperando setear modo - 1 lectura - 2 escritura
	
	private byte codRedundancia = -128;
	
	private boolean masLineasLeer;

	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger milogueador = Logger
			.getLogger(ArchivoDocumentos.class);

	// private String rutaArchivo = Constantes.ARCHIVO_DOCUMENTOS;

	private String rutaArchivo = "/home/zeke/Escritorio/archivoDocs.dat";

	/**
	 * Constructor de la clase.
	 * 
	 * @throws Exception
	 */
	/*
	 * public ArchivoDocumentos() throws Exception { this.buffer = null; }
	 */

	public ArchivoDocumentos() {
	
	}
	
	
	public void iniciarSession(){
		try {
			this.modoEnEjecucion = 0;
			this.abrir(this.rutaArchivo,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			System.out
					.println("no se ha podido abrir el archivo de documentos");
			e.printStackTrace();
		}
	}

	/**
	 * permite setear el documento a leer mediante un offset
	 * 
	 * @param offset
	 * @return
	 */
	public boolean setDocumentToRead(Long offset){
		try {
			
			//hago el chequeo del modo de ejecucion, si se trata mal puede corromperse el archivo
			if (this.modoEnEjecucion != 0){
				
				System.out.println("el archivo de documentos no esta preparado para lectura");
				return false;
			}
			
			//verifico que no quiera posicionarme mas alla de los limites del archivo,
			//es una verificacion para evitar que se corrompa.
			if (file.length() < offset){
				System.out.println("se ha querido posicionar mas alla de los limites del archivo");
				return false;
			}else {
				
			//seteo el modo de ejecucion	
			this.modoEnEjecucion = 1;
			
			//cargo en la variable local el offset pasado por parametro
			this.offsetLastDoc = offset;
			
			//me posiciono en el offset pasado por parametro
			this.file.seek(offset);
			
			//leo la longitud del documento guardado y la almaceno en una variable local
			this.longitudDoc = this.file.readLong();
			
			if (this.longitudDoc ==0){
				System.out.println("el documento tenia extension cero");
				return false;
			}
			this.masLineasLeer = true;
			
			byte lecturaDeCod = this.file.readByte();
			
			if (lecturaDeCod !=this.codRedundancia){
				System.out.println("ha fallado la comprobacion de redundancia del archivo");
				return false;
			}
			
			return true;
			}
			}
	        catch (IOException e) {
			System.out.println("no se ha podido posicionar el documento en el archivo");
			e.printStackTrace();
			return false;
		}
		}
	
	/**
	 * permite setear el archivo en modo carga
	 * @return
	 */
	public boolean setDocumentToWrite(){
		
		try{
			
			if (this.modoEnEjecucion != 0){
				System.out.println("el archivo de documentos no esta preparado para lectura");
				return false;
			}
			//me posiciono en el final del documento
			this.file.seek(this.file.length());
			//cargo el offset del documento (la siguiente posicion a la ultima utilizada) en una variable local
			this.offsetLastDoc = this.file.getFilePointer();
			
			//reservo espacio para la longitud, que aun no la conozco
			Long reservaDeEspacio = new Long(0);
			this.file.writeLong(reservaDeEspacio);
			
			//guardo el codigo de redundancia, para detectar rapidamente si hubo algo mal almacenado
			this.file.writeByte(codRedundancia);
			
			//seteo el modo de ejecucion
			this.modoEnEjecucion = 2;
			
			return true;
			
		}catch (Exception e){
			System.out.println("no se ha podido setear el documento de archivos en modo escritura");
			e.printStackTrace();
			return false;
		}
	}
	

	public boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		return super.abrir(archivo, modo);
	}


	public boolean escribirLinea(String linea) {

		if (this.modoEnEjecucion == 2){
			
			try {
				//la linea recibida si bien es una linea, no contiene el caracter de salto, lo agrego a mano
				linea.concat("\\n");
				
				this.file.writeUTF(linea);
			//	this.file.writeBytes(linea);
				return true;
			} catch (IOException e) {
				System.out.println("no se ha podido escribir la linea");
				e.printStackTrace();
				return false;
			}
		}else {
			System.out.println("no se ha podido escribir la linea, el documento noe estaba en modo escritura");
			return false;
		}
	}
	
	
	public long getLongitudDoc() {
		return longitudDoc;
	}

	public void setLongitudDoc(long longitudDoc) {
		this.longitudDoc = longitudDoc;
	}

	public long getOffsetLastDoc() {
		return offsetLastDoc;
	}

	public void setOffsetLastDoc(long offsetLastDoc) {
		this.offsetLastDoc = offsetLastDoc;
	}
	
	
	public String leerLinea (){
		if (this.modoEnEjecucion == 1 && this.masLineasLeer){
			String linea;
			try {
				linea= this.file.readLine();
				
				if ((this.offsetLastDoc + this.longitudDoc) >= this.file.getFilePointer() ){
					this.masLineasLeer = false;
				}
				return linea;
			} catch (IOException e) {
				System.out.println("no se ha podido leer la linea");
				e.printStackTrace();
				return null;
			}
		}else{
			System.out.println("no se ha podido leer la linea, el archivo no estaba en modo lectura");
			return null;
		}
	}
	
	public boolean masLineasParaLeer (){
		return this.masLineasLeer;
	}
	
	
	public void terminarSession (){
		
		if (this.modoEnEjecucion ==2){
			
			try {
				//obtengo la longitud del documento que no sabia con la posicion actual
				//menos el offset con el que empezamos
				long longitudDocumentoAlm = this.file.getFilePointer() - this.offset;
				
				//asigno la variable local de longitud con el valor obtenido
				this.longitudDoc = longitudDocumentoAlm;
				
				//me pongo nuevamente al comienzo del doc
				this.file.seek(this.offset);
				//y almaceno su longitud, porque al empezar la escritura solo reserve el espacio
				this.file.writeLong(this.longitudDoc);
				
				this.modoEnEjecucion = 0;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			this.masLineasLeer = false;
			this.modoEnEjecucion =0;
		}
		
		try {
			this.cerrar();
		} catch (IOException e) {
			System.out.println("no se ha podido cerrar el archivo de documentos");
			e.printStackTrace();
		}
	}
}
