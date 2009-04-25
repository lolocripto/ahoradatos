/**
 * 
 */
package ar.com.datos.grupo5.archivos;

import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.registros.RegistroAudio;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author LedZeppeling
 *
 */
public class Directo extends Archivo {

	/**
	 * Offset del ultimo registro insertado.
	 */
	private long offset = 0L;
	
	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(Directo.class);
	
	/**
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#borrar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public final boolean borrar(final Registro registro)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @throws UnImplementedMetodException .
	 * @see Archivo#leerBloque(Long)
	 */
	public byte[] leerBloque(final Long offset)
			throws UnImplementedMethodException, IOException {

		throw new UnImplementedMethodException(
				"Funcion no valida en este tipo de archivo");

	}
	/**
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#buscar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public final boolean buscar(final Registro registro)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#insertar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public void insertar(final Registro registro) throws IOException {
		
		offset = file.length();
		super.insertar(registro);
	}

	/**
	 * Posiciona en puntero del archivo en la posicion deseada.
	 * 
	 * @param pos
	 *            La posicion a la cual sedesa apuntar.
	 * @throws IOException .
	 */
	public final void posicionar(final long pos) throws IOException {
		
		file.seek(pos);
	}

	/**
	 * @return El offset del ultimo registro insertado.
	 */
	public long getOffset() {
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
	 * Método para recuperar un registro de un archivo Directo.
	 * @param offset
	 *              La posición en la cual empieza el registro buscado.
	 * @return 
	 *        Retorna el registro que se encuentra en la posición offset.
	 * @throws IOException .
	 */
	public Registro leer(final Long offset) throws IOException {
		Registro reg = null;
		int longitud = 0;
		
		file.seek(offset);
		byte[] bufferInt = new byte[Constantes.SIZE_OF_INT];
        byte[] bufferDato = null;
        file.read(bufferInt, 0, Constantes.SIZE_OF_INT);
        longitud = Conversiones.arrayByteToInt(bufferInt);
        bufferDato = new byte[longitud];
        
        file.read(bufferDato, 0, longitud);
        reg = new RegistroAudio();
        reg.setBytes(bufferDato, (long) longitud);
		return reg;
	}

}
