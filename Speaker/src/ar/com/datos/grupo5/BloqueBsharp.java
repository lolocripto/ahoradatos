package ar.com.datos.grupo5;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Clase que permite manipular el arbol B# a nivel fisico.
 * @author LedZeppeling
 */
public class BloqueBsharp extends Directo{
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(BloqueBsharp.class);
	
	/**
	 * Tamanio del Bloque en disco.
	 */
	private static int  tamanio = Constantes.SIZE_OF_INDEX_BLOCK;

	/**
	 * Offset del ultimo registro insertado.
	 */
	private long offset = 0L;

	/**
	 * @return El offset del ultimo registro insertado.
	 */
	public final long getOffset() {
		return offset;
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
	 *              La posición en la cual empieza el registro buscado.
	 * @return 
	 *        Retorna el registro que se encuentra en la posición offset.
	 * @throws IOException .
	 */
	public final byte[] leerBloque(final Long offset) throws IOException {
		
		byte[] bufferDato = new byte[Constantes.SIZE_OF_INDEX_BLOCK];
		file.seek(offset);

        file.read(bufferDato, 0, Constantes.SIZE_OF_INDEX_BLOCK);

		return bufferDato;
	}
	
	/**
	 * Metodo para Insertar la cadena en el archivo en el que se está
	 * trabajando.
	 * 
	 * @param registro
	 *            Es el registro que se va a agregar al archivo.
	 * @param offset
	 *            Es la posición en donde comienza el bloque a modificar.
	 * @throws IOException
	 *             Excepcion de extrada/salida.
	 */
	public void insertar(final Registro registro, final Long offset) throws IOException {
		
		if (file == null) {
			throw new IOException("No se creo o abrio el archivo.");
		}
		
		byte[] bytes = new byte[Constantes.SIZE_OF_INDEX_BLOCK];
		// Me posiciono al comienzo del bloque.
		file.seek(offset);
		file.write(bytes, 0, Constantes.SIZE_OF_INDEX_BLOCK);
	}
}
