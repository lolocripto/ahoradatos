package ar.com.datos.grupo5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;
/**
 * Clase Para el manejo de archivos secuenciales.
 * @see ar.com.datos.grupo5.interfaces.Archivo
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author Diego
 */
public class Secuencial implements Archivo {

	/**
	 * Archivo en disco.
	 */
	private RandomAccessFile file = null;
	
	/**
	 * Ruta del archivo.
	 */
	private String nombreArchivo;
	
	/**
	 * Posicion en el archivo.
	 */
	private long posicionActual = 0;
	
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
		
		
		//TODO ver que pasa si no hay tantos registros para leer.
		
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
        	
        	for (int i = 0; i < Constantes.TAMANO_CACHE; i++) {
    			
        		if (posicionActual == tamanioArchivo) {
	            	break;
	            }
        		
	        	registro = new RegistroDiccionario();
	        	
	            file.seek(posicionActual);
	            cantidadLeida += file.read(bufferLong, 0, Constantes.SIZE_OF_LONG);
	            
	            offset = Conversiones.arrayByteToLong(bufferLong);
	            cantidadLeida += file.read(bufferInt, 0, Constantes.SIZE_OF_INT);
	            
	            longitud = Conversiones.arrayByteToInt(bufferInt);
	            bufferDato = new byte[longitud];
	            cantidadLeida += file.read(bufferDato, 0, longitud);
	            
	            registro.setBytes(bufferDato, offset);
	            
	            cacheRegistros.add(registro);
	            
	            posicionActual += cantidadLeida;
	            cantidadLeida = 0;
        	}
            
        } catch (IOException e) {
            logger.equals("" + e.getMessage());
            e.printStackTrace();
        }
	}
	
	/**
	 * Metodo para Intentar abrir un archivo, pasado por parámetro.
	 * 
	 * @see ar.com.datos.grupo5.interfaces.Archivo#abrir(String, String)
	 */
	public boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		
		try {

			nombreArchivo = archivo;
			file = new RandomAccessFile(archivo, modo);

		} catch (FileNotFoundException e) {
			throw e;
		}
		
		return true;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#borrar(Registro)
	 * @param registro : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la operación.
	 */
	public final boolean borrar(final Registro registro) {
		
		//No se implemeta esta funcionalidad.
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#buscar(Registro)
	 * @param registro : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 * puntero en la posición donde debería estar el patrón buscado.
	 */
	public final boolean buscar(final Registro registro) {
		
		// TODO Auto-generated method stub
		byte[] buffer = null;
		byte[] bufferRegistro = registro.getBytes();
		
		//Buscar en el registro de datos
		try {
			this.file.read(buffer, 0, bufferRegistro.length);
			if (buffer.equals(bufferRegistro)) {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		
			file.close();
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
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
	 *Método para ver si tiene a continuación otro registro.
	 *@return Si tiene otro regitro, true, sino, false.
	 */
	public final boolean hasNext(){
		//TODO Function
		return true;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#crear(String)
	 */
	public final void crear(final String archivo) throws FileNotFoundException {

		try {

			nombreArchivo = archivo;
			file = new RandomAccessFile(nombreArchivo,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			// Si existe lo trunco.
			try {
				file.setLength(0);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
	 */
	public final void insertar(final Registro registro) throws IOException {

		if (file == null) {
			throw new IOException("No se creo o abrio el archivo.");
		}

		int offset = 0;
		byte[] bytes = null;
		int bytesTotal = 0;
		int bytesEnviar = 0;

		// Me posiciono al final de archivo.
		file.seek(file.length());

		while (registro.hasMoreBytes()) {

			offset = 0;
			bytes = registro.getBytes();
			bytesTotal = bytes.length;
			bytesEnviar = Constantes.TAMANIO_BUFFER_ESCRITURA;
			if (bytesTotal < bytesEnviar) {
				bytesEnviar = bytesTotal;
			}

			// Escribo la cantidad que me permite.
			while (offset < bytesTotal) {

				if ((bytesTotal - offset) < bytesEnviar) {
					bytesEnviar = bytesTotal - offset;
				}

				file.write(bytes, offset, bytesEnviar);

				offset += bytesEnviar;
			}
		}
	}

	/**
	 * Borra el archivo del disco.
	 * 
	 * @return true si pudo borrar el archivo.
	 * @throws IOException .
	 */
	public final boolean eliminar() throws IOException {

		file.close();
		File fileAux = new File(nombreArchivo);
		return fileAux.delete();

	}

	/**
	 * @see Archivo#siguiente()
	 */
	public final Registro siguiente() {

		recargarCache();

		if (cacheRegistros.size() == 0) {
			return null;
		}

		Registro registro = cacheRegistros.remove(0);

		return registro;
	}
}
