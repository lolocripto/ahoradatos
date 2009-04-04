package ar.com.datos.grupo5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Interface para manejar Archivos.
 * @author Diego
 *
 */
public abstract class Archivo {
	
	/**
	 * Archivo en disco.
	 */
	protected RandomAccessFile file = null;
	
	/**
	 * Ruta del archivo.
	 */
	protected String nombreArchivo;
	
	/**
	 * Posicion en el archivo.
	 */
	protected long posicionActual = 0;
	
	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(Archivo.class);
	
	/**
	 * Método para Intentar abrir un archivo, pasado por parámetro.
	 * 
	 * @param archivo
	 *            Path completo del arc/home/xxvkue/Datos/ahoradatoshivo.
	 * @param modo
	 *            Es el modo en el que se abrirá el archivo {R,W,R+,A}.
	 * @return devuelve el resultado de la operación.
	 * @throws FileNotFoundException
	 *             Si no puede abrir el Archivo.
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
	 * Método para Intentar crear un archivo, pasado por parámetro.
	 * 
	 * @param archivo
	 *            Path completo del archivo.
	 * @throws FileNotFoundException
	 *             si no se puede crear el archivo.
	 */
	public void crear(final String archivo)
			throws FileNotFoundException {

		try {

			nombreArchivo = archivo;
			file = new RandomAccessFile(nombreArchivo,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA());
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
	 * Método para cerrar el archivo que se está manipulando.
	 * 
	 * @throws IOException
	 *             Excepcion de entrada/salida.
	 */
	public void cerrar() throws IOException {
		
		file.close();
	}

	/**
	 * Metodo para Insertar la cadena en el archivo en el que se está
	 * trabajando.
	 * 
	 * @param registro
	 *            Es el registro que se va a agregar al archivo.
	 * @throws IOException
	 *             Excepcion de extrada/salida.
	 */
	public void insertar(final Registro registro) throws IOException {
		
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
			bytesEnviar = Constantes.TAMANIO_BUFFER_ESCRITURA();
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
	 * Borra el archivo en disco.
	 * 
	 * @return true si lo pudo borrar.
	 * @throws IOException .
	 */
	public boolean eliminar() throws IOException {
		
		file.close();
		File fileAux = new File(nombreArchivo);
		return fileAux.delete();
	}

	/**
	 * Metodo para borrar una cadena en el archivo en el que se está trabajando.
	 * 
	 * @param registro
	 *            Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la operación, y queda posicionado el
	 *         puntero en la posición donde se encontraba la cadena buscada. En
	 *         caso de no encontrar la cadena, devuelve false y el puntero queda
	 *         donde debería estar la cadena buscada.
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract boolean borrar(final Registro registro)
			throws UnImplementedMethodException;
	
	/**
	 * Método para buscar en un archivo, una cadena pasada por parámetro.
	 * 
	 * @param registro
	 *            : Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 *         puntero en la posición donde debería estar el patrón buscado.
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract boolean buscar(final Registro registro)
			throws UnImplementedMethodException;

	/**
	 * devuelve el primer registro.
	 * 
	 * @return EL registro.
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract Registro primero() throws UnImplementedMethodException;

	/**
	 * Devuelve el registro siguiente al actual.
	 * 
	 * @return EL registro.
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract Registro siguiente() throws UnImplementedMethodException;

	/**
	 * Método para recuperar un registro de un archivo Directo.
	 * 
	 * @param offset
	 *            La posición en la cual empieza el registro buscado.
	 * @return Retorna el registro que se encuentra en la posición offset.
	 * @throws IOException .
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract Registro leer(final Long offset) throws IOException,
			UnImplementedMethodException;

	/**
	 * @return El offset del ultimo registro insertado.
	 * @throws UnImplementedMethodException
	 *             Si la clase que hereda no implementa el metodo.
	 */
	public abstract long getOffset() throws UnImplementedMethodException;
}
