package ar.com.datos.grupo5.archivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.CompresorFactory;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.compresion.ppmc.Ppmc;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class ArchivoDocs {


	private Directo miArchivo;
	
	private MetodoCompresion tipoCompresion;
	
	//longitud en bytes del texto que debo leer
	private long longTextoDoc; 				
	
	//offset donde empieza la sección de datos del documento a leer
	private long offsetInicioRegistro; 
	
	//offset desde donde comienza el documento actual sera el file.lenght para
	//docs en escritura o el offset pasado por parametro en docs para lectura
	private long offsetInicioTexto; 
	
	//cantidad de bytes empleados para datos administrativos del documento
	private int bytesParaHeader;       
	
	private int cantDocsAlmacenados;
	
	private Compresor comp;
	
	private RandomAccessFile archivoTemp;
	
	private File sourceArchivoTemp;
	
	private conversionBitToByte conversor;

	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger LOG = Logger
			.getLogger(ArchivoDocs.class);

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
	
    
    public int getCantidadDocs(){
    	
    	int adevolver = 0;
    	
    	try {
    		if (this.miArchivo.file.length() == 0) {
    			adevolver = 0;
    		} else {
	    		//guardo la posicion actual
	    		long posOriginal = this.miArchivo.file.getFilePointer();
	    		//voy al cero para leer la cantidad de documentos
				this.miArchivo.posicionar(0);
				adevolver = this.miArchivo.file.readInt();
				//vuelvo a la posicion original
				this.miArchivo.file.seek(posOriginal);
    		}
		} catch (IOException e) {
			LOG.error("no se pudo leer la cantidad de documentos",e);
			e.printStackTrace();
		}
		
		this.cantDocsAlmacenados =adevolver;
		return adevolver;

    }
	
	private void escribirCantidadDocs(){
		
		try{
			//guardo la posicion actual
    		long posOriginal = this.miArchivo.file.getFilePointer();
    		//voy al cero para leer la cantidad de documentos
			this.miArchivo.posicionar(0);
			this.miArchivo.file.writeInt(this.cantDocsAlmacenados);
			//vuelvo a la posicion original
			this.miArchivo.file.seek(posOriginal);

		} catch (IOException e) {
			LOG.error("no se pudo escribir la cantidad de documentos",e);
			e.printStackTrace();

		}

	}
	
	
	/**
	 * 
	 * @param nombre
	 * @param metodoCompresion "0" Ninguno, "1" LZP, "2" PPMC, "3" LZ78, "4" ARTIMETICO
	 * @param longitud
	 * @return
	 */
	public long documentToWrite(String nombre, MetodoCompresion metodoCompresion, long longitud){
		
		this.tipoCompresion = metodoCompresion;
		
		this.conversor = new conversionBitToByte();
		
		this.comp = CompresorFactory.getCompresor(this.tipoCompresion);
		
		//chequeo que esté activada la compresion
		if (this.comp != null){
		this.comp.iniciarSesion();
		}
		
		try {
			//sumo uno a la cantidad de docs
			this.cantDocsAlmacenados++;
			//guardo dicha cantidad
			this.escribirCantidadDocs();
			
		//	offsetDelNuevoArchivo = miArchivo.file.length();
			
			this.offsetInicioRegistro = miArchivo.file.length();
			
			//me voy al final del archivo
			this.miArchivo.file.seek(this.miArchivo.file.length());
			
			//escribo el nombre y la longitud
			this.escribirNombreLongyMetodo(nombre, metodoCompresion);
			//ya queda preparado para escribir informacion;
			
			//guardo en memoria cuantos bytes estoy usando para el header
			this.bytesParaHeader = (int) (miArchivo.file.length() - this.offsetInicioRegistro);
			
			//almaceno la posicion desde donde empieza la seccion de datos del doc
			this.offsetInicioTexto = miArchivo.file.length(); 

		} catch (IOException e) {
			LOG.error("no se pudo setear el Archivo de almacenamiento de documentos para escribir",e);
			e.printStackTrace();
		}
		return this.offsetInicioRegistro;

	}
	
	
	public void documentToRead(long offset){
		
		this.offsetInicioRegistro = offset;
		
		try {
			//voy al offset que me dicen
			this.miArchivo.file.seek(offset);
			byte longNombre = this.miArchivo.file.readByte(); //leo a long del nombre
			byte[] nombre =new byte[longNombre];
			this.miArchivo.file.read(nombre);  //leo el nombre
			
			this.longTextoDoc = this.miArchivo.file.readLong(); //cargo la longitud que debo leer
			
			byte compresion = this.miArchivo.file.readByte();	//Leo el tipo de compresion
			this.tipoCompresion = Conversiones.metodoDeCompresion(compresion); //la guardo
			
			this.offsetInicioTexto = this.miArchivo.file.getFilePointer();//guardo la posicion desde donde empiezo a leer lineas
			
			if (this.tipoCompresion != MetodoCompresion.NINGUNO){
			this.comp = CompresorFactory.getCompresor(this.tipoCompresion); //genero el compresor
			this.comp.iniciarSesion();
			
			this.generarArchivoTemp();
			
			}
			
			//guardo en memoria los bytes usados para el header
			this.bytesParaHeader = (int) (this.miArchivo.file.getFilePointer() - offset);
			
		} catch (IOException e) {
			LOG.error("no se pudo setear el Archivo de almacenamiento de documentos para leer",e);
			e.printStackTrace();
		}
	}
	
	//escribe el nombre y la longitud del documento desde la posicion actual en el archivo
	private void escribirNombreLongyMetodo(String nombre, MetodoCompresion metodo){
		
		byte[] nombreEnBytes = nombre.getBytes();
		byte longNombre = (byte) nombreEnBytes.length;
		
		byte tipoCompresion = (new Integer(0)).byteValue();
		
		try{
			//escribo la longitud del nombre
			this.miArchivo.file.writeByte(longNombre);
			//escribo el nombre
			this.miArchivo.file.write(nombreEnBytes);
			
			//reservo espacio para el campo longitud del archivo que voy a leer
			this.miArchivo.file.writeLong(0);
			
			//guardo el metodo de compresion usado
			this.miArchivo.file.writeByte(Conversiones.metodoDeCompresion(metodo));
			
//			//reservo espacio en la logitud del archivo, de esta forma si se rompe, se rompe solo un documento.
//			this.miArchivo.file.setLength(miArchivo.file.length() + longitud);
			
		} catch (Exception e) {
			
			LOG.error("no se pudo escribir nombre y longitud del documento, en archivo documentos",e);
			e.printStackTrace();
		}

	}
	
	private void escribirLongDoc(){
		
		try {
			//guardo a donde volver
			long marcaRetorno = this.miArchivo.file.getFilePointer();
			
			//voy al inicio del documento actual
			this.miArchivo.file.seek(this.offsetInicioRegistro);
			
			//leo la longitud del nombre y el nombre
			short longitudNombre = miArchivo.file.readByte();
			byte[] array = new byte[longitudNombre];
			//leo el nombre
			miArchivo.file.read(array);
			
			//escribo la longitud actual del archivo que será donde estamos menos
			//donde comenzaba la seccion de datos del doc
			miArchivo.file.writeLong(marcaRetorno - this.offsetInicioTexto);
			
			miArchivo.file.seek(marcaRetorno);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String nombreDoc(long offset){
		
		String name=null;
		
		try {
			
			//guardo la posicion actual
    		long posOriginal = this.miArchivo.file.getFilePointer();
			
			this.miArchivo.file.seek(offset);
			byte longNombre = this.miArchivo.file.readByte(); //leo a long del nombre
			byte[] nomEnBytes =new byte[longNombre];
			this.miArchivo.file.read(nomEnBytes);  //leo el nombre
			name = new String(nomEnBytes);
			
			this.miArchivo.file.seek(posOriginal);
			
		} catch (IOException e) {
			LOG.error("no se pudo leer nombre de documento, en archivo documentos",e);
			e.printStackTrace();
		}
		return name;
	}
	
	
	public boolean masLineasParaLeer (){
		
		if (this.tipoCompresion == MetodoCompresion.NINGUNO){
			return this.masLineasSinCompresion();
		}else{
			return this.masLineasConCompresion();
		}
	
	}
	
	
	
	private boolean masLineasConCompresion(){
		
		try {
			
			long posActual = this.archivoTemp.getFilePointer();
			long longitud = this.archivoTemp.length();
			
			if ( posActual >= longitud ){
				return false;
			}else
			{
				return true;
		}
			}
			catch (IOException e) {
			LOG.error("no se ha podido leer el file pointer",e);
			e.printStackTrace();
			return false;
		}
		
		}
	
	
	private boolean masLineasSinCompresion(){
		long finDeDoc = this.offsetInicioTexto + this.longTextoDoc;
		long posActual=0;
		try {
			posActual = this.miArchivo.file.getFilePointer();
			
		} catch (IOException e) {
			LOG.error("no se ha podido leer el file pointer",e);
			e.printStackTrace();
			return false;
		}
		if (posActual >= finDeDoc){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	public void escribirLinea(String linea){
		
		//la linea recibida si bien es una linea, no contiene el caracter de salto, lo agrego a mano
		linea = linea.concat("\n");
		
		if (this.tipoCompresion == MetodoCompresion.NINGUNO) 
			this.escribirLineaSinCompresion(linea);
		else this.escribirLineaComprimida(linea);
			
		//por cada linea que escribo actualizo la longitud del documento actual
		//se hace de esta forma porque no tengo forma de saber de antemano
		//cuanto se va a comprimir el archivo y por ende el tamaño final
		this.escribirLongDoc();
	}
	
	
	private void escribirLineaComprimida(String linea){
		try {
			
			//obtengo el string binario del compresor
			String stringComprimido =this.comp.comprimir(linea);
			
			this.conversor.setBits(stringComprimido);
			
			//obtengo el array de bits para guardar
			byte[] tirabits =  this.conversor.getBytes();
			
			//guardo la tira de bits
			this.miArchivo.file.write(tirabits);
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private void escribirLineaSinCompresion(String linea){
		
		try {
			
			this.miArchivo.file.writeUTF(linea);
			
		} catch (IOException e) {
			LOG.error("no se ha podido escribir la linea",e);
			e.printStackTrace();
		}
		
	}
	
	public void cerrarSesionDeEscritura(){
	
		if (this.tipoCompresion != MetodoCompresion.NINGUNO){
		
		try {
			//obtengo el string binario del compresor
			String stringComprimido =this.comp.finalizarSession();
			
			if (stringComprimido != null){
			//seteo los bits que obtengo 
			this.conversor.setBits(stringComprimido);
			
			//guardo los ultimos bits obtenidos
			this.miArchivo.file.write(this.conversor.getBytes());
			
			this.escribirLongDoc();
			
			}
			byte [] padding = this.conversor.finalizarConversion();
			
			if (padding != null){
			
			//pongo el padding final
			this.miArchivo.file.write(padding);
			
			//guardo nuevamente la longitud del documento
			this.escribirLongDoc();
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	
//	
//	/**
//	 * Cierra la sesion de lectura sobre el archivo temporal eliminandolo
//	 */
//	public void cerrarSesionDeLectura(){
//		
//	if (this.tipoCompresion != MetodoCompresion.NINGUNO){
//		
//		try {
//			this.archivoTemp.close();
//			this.sourceArchivoTemp.deleteOnExit();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	
//	}
//}
//	
	
	
	/**
	 * Verifica si se esta leyendo un documento comprimido, en cuyo caso invoca
	 * al metodo leerLineaDeArchivo pasandole como parametro el archivo 
	 * temporal con el texto descomprimido. En caso contrario invoca al metodo
	 * directamente con el archivo maestro de documentos del cual puede leer
	 * el texto normalmente.
	 * @return linea leida
	 */
	public String leerLinea(){
		if (this.tipoCompresion == MetodoCompresion.NINGUNO) 
			return this.leerLineaDeArchivo(this.miArchivo.file);
		else return this.leerLineaDeArchivo(this.archivoTemp);
	}
	
	
	private String leerLineaDeArchivo (RandomAccessFile archivo){
		
		String linea;
		try {
			linea= archivo.readUTF();
			return linea;
		} catch (IOException e) {
			LOG.error("no se ha podido leer la linea",e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Genera un archivo temporal con el texto descomprimido listo
	 * para ser parseado
	 */
	private void generarArchivoTemp (){

		
		try {
			//genero el archivo temporal
			this.sourceArchivoTemp = new File ("./descomp.temp");
			this.archivoTemp = new RandomAccessFile(this.sourceArchivoTemp, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			
			//dejo marcado el archivo para que se borre solito cuando termina la maquina virtual
			this.sourceArchivoTemp.deleteOnExit();
			
			//obtengo el offset donde terminar la lectura
			long terminoEn = this.offsetInicioTexto + this.longTextoDoc;
			
			long estoyEn = this.miArchivo.file.getFilePointer();
			
			//instancio todos las estructuras
			StringBuffer sb = new StringBuffer();
			StringBuffer desc = new StringBuffer();
			byte[] datos;
			String binario;
			String descomprimidos = new String();
			
			//genero las estructuras necesarias para escirbir en utf16
			
			//leo el archivo de documentos y genero el temporal
			while (this.miArchivo.file.getFilePointer() < terminoEn && !comp.isFinalizada()){
				
				long bytesRestantes = terminoEn - this.miArchivo.file.getFilePointer();
				
				//me fijo si puedo leer 10 bytes, para que array tenga ese tamaño
				//en caso contrario tendrá solo la cantidad restante
				if (bytesRestantes > 150){
					datos = new byte[150];
				}else{
					datos = new byte[(int)bytesRestantes];
				}

				//leo 10 bytes del archivo
				this.miArchivo.file.read(datos);
				
				//convierto los 10 bytes en un string binario
				
				
				binario = Conversiones.arrayByteToBinaryString(datos);
				
				//cargo ese string binario en un buffer
				sb.append(binario);
				
				//obtengo los datos descomprimidos
				
				if(this.tipoCompresion == MetodoCompresion.ARIT || this.tipoCompresion == MetodoCompresion.ARIT1){
					desc.append(((CompresorAritmetico)this.comp).StringCompleto(sb));
				}else{
					desc.append(this.comp.descomprimir(sb) + "\n");
				}
			
				//guardo en el temporal los datos en utf
				this.guardarEnSalto(this.archivoTemp, desc);
				if (tipoCompresion != MetodoCompresion.LZP) {
					sb.delete(0, sb.length());
				}
			}
			
		
			//cuando ya tengo toda la info cargada vuelvo al inicio
			this.archivoTemp.seek(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Permite guardar en el archivo temporal los string obtenidos de la descompresion
	 * pero de forma tal que no se corten las palabras.
	 * @param archivo
	 * @param linea
	 */
	private void guardarEnSalto(RandomAccessFile archivo, StringBuffer linea){
		
		int ultimaAparicionEspacio = linea.lastIndexOf("\n");
		
		if (ultimaAparicionEspacio != -1 ){
		try {
			archivo.writeUTF(linea.substring(0, ultimaAparicionEspacio));
			linea.delete(0, ultimaAparicionEspacio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
		
	}
	
			
	public void cerrarArchivo(){
		
		try {
			this.miArchivo.cerrar();
		} catch (IOException e) {
			LOG.error("no se ha podido cerrar el archivo de documentos",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene el tipo de compresion que tiene el documento almacenado.
	 * @param offset
	 * @return
	 */
	public MetodoCompresion getTipoCompresion(long offset){
    	
		MetodoCompresion metodo = null;
		byte tipoCom;
		
		try {
			
			//guardo la posicion actual
    		long posOriginal = this.miArchivo.file.getFilePointer();
			
			this.miArchivo.file.seek(offset);
			byte longNombre = this.miArchivo.file.readByte(); //leo a long del nombre
			byte[] nomEnBytes =new byte[longNombre];
			this.miArchivo.file.read(nomEnBytes);  //leo el nombre
			
			tipoCom = this.miArchivo.file.readByte();
			
			switch((new Integer(tipoCom)).intValue()){
				case 0:
						metodo = MetodoCompresion.NINGUNO;
					break;
				case 1:
						metodo = MetodoCompresion.LZP;
					break;
				case 2:
						metodo = MetodoCompresion.PPMC;
					break;
				case 3:
						metodo = MetodoCompresion.LZ78;
					break;
				case 4:
						metodo = MetodoCompresion.ARIT;
					break;
				default:
						metodo = MetodoCompresion.NINGUNO;
			}
			
			this.miArchivo.file.seek(posOriginal);
			
		} catch (IOException e) {
			LOG.error("no se pudo leer nombre de documento, en archivo documentos",e);
			e.printStackTrace();
		}
		return metodo;

    }

}
