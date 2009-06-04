package ar.com.datos.grupo5.archivos;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.ppmc.Ppmc;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class ArchivoDocs {


	private Directo miArchivo;
	
	private long longDocActualRead; //longitud en bytes del texto que debo leer
	private long offsetInicio;      //offset donde empieza la seccion de datos del documento a leer
	
	private int cantDocsAlmacenados;

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
	
	
	
	public long documentToWrite(String nombre, MetodoCompresion metodo, long longitud){
		
		long offsetDelNuevoArchivo=0;
		
		try {
			//sumo uno a la cantidad de docs
			this.cantDocsAlmacenados++;
			//guardo dicha cantidad
			this.escribirCantidadDocs();
			
			offsetDelNuevoArchivo = miArchivo.file.length();
			
			//me voy al final del archivo
			this.miArchivo.file.seek(offsetDelNuevoArchivo);
			//escribo el nombre y la longitud
			this.escribirNombreYlong(nombre, metodo, longitud);
			//ya queda preparado para escribir informacion;

		} catch (IOException e) {
			LOG.error("no se pudo setear el Archivo de almacenamiento de documentos para escribir",e);
			e.printStackTrace();
		}
		return offsetDelNuevoArchivo;

	}
	
	
	public void documentToRead(long offset){
		
		try {
			//voy al offset que me dicen
			this.miArchivo.file.seek(offset);
			byte longNombre = this.miArchivo.file.readByte(); //leo a long del nombre
			byte[] nombre =new byte[longNombre];
			this.miArchivo.file.read(nombre);  //leo el nombre
			
			this.miArchivo.file.readByte();	//Leo el tipo de compresion
			
			this.longDocActualRead = this.miArchivo.file.readLong(); //cargo la longitud que debo leer
			this.offsetInicio = this.miArchivo.file.getFilePointer();//guardo la posicion desde donde empiezo a leer lineas
			
		} catch (IOException e) {
			LOG.error("no se pudo setear el Archivo de almacenamiento de documentos para leer",e);
			e.printStackTrace();
		}
	}
	
	//escribe el nombre y la longitud del documento desde la posicion actual en el archivo
	private void escribirNombreYlong(String nombre, MetodoCompresion metodo, long longitud){
		
		byte[] nombreEnBytes = nombre.getBytes();
		byte longNombre = (byte) nombreEnBytes.length;
		
		byte tipoCompresion = (new Integer(0)).byteValue();
		
		try{
			//escribo la longitud del nombre
			this.miArchivo.file.writeByte(longNombre);
			//escribo el nombre
			this.miArchivo.file.write(nombreEnBytes);
			
			//Escribo el tipo de compresion
			//0 => Ninguno, 1=> LZP, 2 => PPMC, 3 => LZ78, 4 => ARTIMETICO
			if (metodo == MetodoCompresion.PPMC) {
				tipoCompresion = (new Integer(2)).byteValue();
			} else {
				if (metodo == MetodoCompresion.LZP) {
					tipoCompresion = (new Integer(1)).byteValue();
				} else {
					if (metodo == MetodoCompresion.LZ78) {
						tipoCompresion = (new Integer(3)).byteValue();
					} else {
						if (metodo == MetodoCompresion.ARIT) {
							tipoCompresion = (new Integer(4)).byteValue();
						}					
					}
				}
			}
			
			this.miArchivo.file.writeByte(tipoCompresion);
			
			//escribo la longitud del archivo que voy a escribir a continuacion
			this.miArchivo.file.writeLong(longitud);
			//reservo espacio en la logitud del archivo, de esta forma si se rompe, se rompe solo un documento.
			this.miArchivo.file.setLength(miArchivo.file.length() + longitud);
			
		} catch (Exception e) {
			
			LOG.error("no se pudo escribir nombre y longitud del documento, en archivo documentos",e);
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
		
		long finDeDoc = this.offsetInicio + this.longDocActualRead;
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
		
		try {
			//la linea recibida si bien es una linea, no contiene el caracter de salto, lo agrego a mano
			linea.concat("\\n");
			this.miArchivo.file.writeUTF(linea);
			
		} catch (IOException e) {
			LOG.error("no se ha podido escribir la linea",e);
			e.printStackTrace();
		}
			
	}
	
	
	public String leerLinea(){
		
		String linea;
		try {
			linea= this.miArchivo.file.readUTF();
			return linea;
		} catch (IOException e) {
			LOG.error("no se ha podido leer la linea",e);
			e.printStackTrace();
			return null;
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
