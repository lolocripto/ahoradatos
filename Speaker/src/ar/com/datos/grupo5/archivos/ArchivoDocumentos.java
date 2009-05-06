/**
 * 
 */
package ar.com.datos.grupo5.archivos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.registros.RegistroAudio;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.parser.Parser;

/**
 * @author Led Zeppelin
 *
 */
public class ArchivoDocumentos extends Directo {

	/**
	 * Buffer de lectura de archivo de texto.
	 */
	private BufferedReader buffer;

	/**
	 * Mantiene la longitud del documento actual.
	 */
	private long longitudDoc;
	
	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger milogueador = Logger.getLogger(ArchivoDocumentos.class);
	
	/**
	 * Constructor de la clase.
	 * @throws Exception
	 */
	public ArchivoDocumentos() throws Exception {
		this.buffer = null;
	}
	
	public final boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
			return super.abrir(archivo, modo);
	}
			
	/**
	 * @see Archivo#siguiente()
	 */
	@Override
	public final Registro siguiente() throws UnImplementedMethodException {
		
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}
	
	public final Long escribirDocumento(byte[] buffer) {
		try {
			//Tomo el offset a escribir
			this.offset = this.file.length();
			
			//Escribo el tamaño que tiene el nombre del documento
			byte[] longBuffer = Conversiones.intToArrayByte(buffer.length);
			this.file.write(longBuffer, 0, longBuffer.length);
			//Escribo el documento.
			this.file.write(buffer, 0, buffer.length);
			//Escribo 0 para la longitud del documento.
			longBuffer = Conversiones.longToArrayByte(0L);
			this.file.write(longBuffer, 0, longBuffer.length);
			
			//Actualizo la longitud del documento actual
			this.longitudDoc = 0L;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0L;
		}
		return this.offset;
	}
}
