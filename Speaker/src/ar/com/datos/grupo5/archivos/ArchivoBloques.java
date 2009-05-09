package ar.com.datos.grupo5.archivos;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Clase que permite manipular el arbol B# a nivel fisico.
 * @author LedZeppeling
 */
public class ArchivoBloques extends Directo {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ArchivoBloques.class);
	
	/**
	 * Tamanio del Bloque en disco.
	 */
	private int tamanio;

	/**
	 * Constructor de la clase.
	 * @param tamanioBloque
	 */
	public ArchivoBloques(final int tamanioBloque) {
		tamanio = tamanioBloque;
	}
	/**
	 * @see Archivo#primero()
	 */
	@Override
	public Registro primero() throws UnImplementedMethodException {
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see Archivo#siguiente()
	 */
	@Override
	public Registro siguiente() throws UnImplementedMethodException {
		
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}
	
	/**
	 * Método para recuperar un bloque de un archivo directo por bloques.
	 * @param offset
	 *              el numero de nodo buscado
	 * @return 
	 *        Retorna el registro que se encuentra en la posición offset.
	 * @throws IOException .
	 */
	@Override
	public final byte[] leerBloque(final int offset) throws IOException {

		//Si esta vacio no devualvo nada.
		if (file.length() == 0) {
			return null;
		}
		/*
		byte[] bufferDato = new byte[Constantes.SIZE_OF_INDEX_BLOCK];
		file.seek(offset * Constantes.SIZE_OF_INDEX_BLOCK);

        file.read(bufferDato, 0, Constantes.SIZE_OF_INDEX_BLOCK);
		 */
		byte[] bufferDato = new byte[tamanio];
		file.seek(offset * tamanio);

        if (file.read(bufferDato, 0, tamanio) > 0) {
        	return bufferDato;
        } else {
        	return null;
        }
		
	}
	
	/**
	 * Metodo para Insertar la cadena en el archivo en el que se está
	 * trabajando.
	 * 
	 * @param bytes
	 *            Es el registro que se va a agregar al archivo.
	 * @param nroBloque
	 *            Es la posición en donde comienza el bloque a modificar.
	 * @throws IOException
	 *             Excepcion de extrada/salida.
	 */
	public void escribirBloque(final byte[] bytes, final int nroBloque)
			throws IOException { 
		
		//este metodo esta fallando cuando por ej quiero poner el bloque numero 4 y solo hay 2 bloques, lo escribe en la posicion numero 3
		//habria que lanzar una excepcion o generar los bloques vacios que corresponda. para colocar en posicion mi bloque 4

		
		// Me posiciono al comienzo del bloque.
		if (file.length() <= (nroBloque * tamanio)) {
			file.seek(file.length());
		} else {
			file.seek(nroBloque * tamanio);
		}
		if (tamanio > (bytes.length)) {
			file.write(bytes, 0, bytes.length);
			byte[] bytesAux = new byte[tamanio - (bytes.length)];
			file.write(bytesAux);
		} else {
			file.write(bytes, 0, tamanio);
		}
		
	}

	
	public boolean estaVacio(){
		try {
			if (file.length() == 0){
				return true;
			}else return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

/**
 * Método que se diferencia de "escribirBloque" en la libertad de poder escribir bloques en posiciones
 * separadas del archivo, es decir, poder escribir el bloque 7 sin tener el bloque 6.
 * 
 * @param bytes
 *            Es el registro que se va a agregar al archivo.
 * @param nroBloque
 *            Es la posición en donde comienza el bloque a modificar.
 * @throws IOException
 *             Excepcion de extrada/salida.
 */
public void escribirBloqueSalteado(final byte[] bytes, final int nroBloque)
		throws IOException { 

		file.seek(nroBloque * tamanio);
	if (tamanio > (bytes.length)) {
		file.write(bytes, 0, bytes.length);
		byte[] bytesAux = new byte[tamanio - (bytes.length)];
		file.write(bytesAux);
	} else {
		file.write(bytes, 0, tamanio);
	}
	
}
}


