package ar.com.datos.grupo5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;
/**
 * Clase Para el manejo de archivos secuenciales.
 * @see ar.com.datos.grupo5.Archivo
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class Secuencial extends Archivo {

	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(Secuencial.class);

	/**
	 * Cache de registros.
	 */
	private List < Registro > cacheRegistros = null;
	
	/**
	 * Si la cache esta vacia, la carga.
	 */
	private void recargarCache() {
		
		if (cacheRegistros.size() != 0) {
			return;
		}
		
		Registro registro = null;       
        byte[] bufferLong = new byte[Constantes.SIZE_OF_LONG];
        byte[] bufferInt = new byte[Constantes.SIZE_OF_INT];
        byte[] bufferDato = null;
        Long offset = 0L;
        int longitud = 0;
        int cantidadLeida = 0;
        
        try {
        	
    		long tamanioArchivo = file.length();
        	file.seek(posicionActual);
        	
        	for (int i = 0; i < Constantes.TAMANO_CACHE; i++) {
    			
        		if (posicionActual >= tamanioArchivo) {
	            	break;
	            }
        		
	        	registro = new RegistroDiccionario();

				cantidadLeida += file.read(bufferLong, 0,
						Constantes.SIZE_OF_LONG);

				offset = Conversiones.arrayByteToLong(bufferLong);
				cantidadLeida += file
						.read(bufferInt, 0, Constantes.SIZE_OF_INT);
	            
	            longitud = Conversiones.arrayByteToInt(bufferInt);
	            logger.debug("Lei:" + longitud);
	            bufferDato = new byte[longitud];
	            cantidadLeida += file.read(bufferDato, 0, longitud);
	            
	            registro.setBytes(bufferDato, offset);
	            
	            cacheRegistros.add(registro);
	            
	            posicionActual += cantidadLeida;
	            cantidadLeida = 0;
	            logger.info("Pase por aca");
        	}
            
        } catch (IOException e) {
            logger.equals("" + e.getMessage());
            e.printStackTrace();
        }
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#borrar(Registro)
	 * @param registro
	 *            : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la operación.
	 */
	public final boolean borrar(final Registro registro) {

		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#buscar(Registro)
	 * @param registro
	 *            : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 *         puntero en la posición donde debería estar el patrón buscado.
	 */
	public final boolean buscar(final Registro registro) {
		
		return false;
	}

	/**
	 * devuelve el primer registro.
	 * 
	 * @return EL registro.
	 */
	public final Registro primero() {

		// Limpio todo y cargo la cache.
		posicionActual = 0;
		cacheRegistros = new ArrayList<Registro>();

		recargarCache();

		if (cacheRegistros.size() == 0) {
			return null;
		}

		Registro registro = cacheRegistros.remove(0);

		return registro;
	}

	/**
	 * Devuelve el registro siguiente al actual.
	 * 
	 * @return EL registro.
	 */
	public final Registro siguiente() {

		recargarCache();

		if (cacheRegistros.size() == 0) {
			return null;
		}

		Registro registro = cacheRegistros.remove(0);

		return registro;
	}

	/**
	 * @throws UnImplementedMetodException .
	 * @see Archivo#posicionar(long)
	 */
	protected final void posicionar(final long pos)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException(
				"Funcion no valida en este tipo de archivo");

	}

	/**
	 * @throws UnImplementedMetodException .
	 * @see Archivo#leerBloque(Long)
	 */
	public final byte[] leerBloque(final Long offset)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException(
				"Funcion no valida en este tipo de archivo");

	}
	
	/**
	 * @see Archivo#getOffset()
	 */
	@Override
	public final long getOffset() throws UnImplementedMethodException {

		throw new UnImplementedMethodException(
				"Funcion no valida en este tipo de archivo");
	}

	/**
	 * @see Archivo#leer(Long)
	 */
	@Override
	public final Registro leer(final Long offset) throws IOException,
			UnImplementedMethodException {

		throw new UnImplementedMethodException(
				"Funcion no valida en este tipo de archivo");
	}
}
