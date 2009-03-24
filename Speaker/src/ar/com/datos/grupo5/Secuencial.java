package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Clase Para el manejo de archivos secuenciales.
 * @author Diego
 */
public class Secuencial implements Archivo {

	/**
	 * Archivo en disco.
	 */
	private RandomAccessFile file = null;
	
	/**
	 * Metodo para Intentar abrir un archivo, pasado por parámetro.
	 * @param archivo Path completo del archivo.
	 * @param modo Es el modo en el que se abrirá el archivo {R,W,R+,A}.
	 * @return devuelve el resultado de la operación.
	 */
	public boolean abrir(final String archivo, final String modo) {
		return false;
	}

	/**
	 * Metodo para borrar una cadena en el archivo en el que se está trabajando.
	 * @param registro Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la operación,
	 * y queda posicionado el puntero
	 * en la posición donde se encontraba la cadena buscada.
	 * En caso de no encontrar la cadena, devuelve false y el
	 * puntero queda donde
	 * debería estar la cadena buscada.
	 */
	public final boolean borrar(final Registro registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para buscar en un archivo, una cadena pasada por parámetro.
	 * @param registro Es el registro a buscar en el archivo.
	 * @return Devuelve el resultado de la búsqueda, quedando posicionado el
	 * puntero en la posición donde debería estar el patrón buscado.
	 */
	public final boolean buscar(final Registro registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para cerrar el archivo que se está manipulando.
	 */
	public void cerrar() {
		// TODO Auto-generated method stub

	}

	/**
	 * Metodo para Intentar crear un archivo, pasado por parámetro.
	 * Si el archivo existe lo reemplaza.
	 * @param archivo Path completo del archivo.
	 * @param tipo Es el tipo de archivo que se quiere crear {B,T}.
	 * @throws FileNotFoundException si no se puede crear el archivo.
	 */
	public final void crear(final String archivo, final String tipo)
			throws FileNotFoundException {
		
		try {
			
			file = new RandomAccessFile(archivo, "rw");

		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * Metodo para Insertar la cadena en el archivo
	 * en el que se está trabajando.
	 * @param registro Es el registro que se va a agregar al archivo.
	 */
	public void insertar(final Registro registro) {

		Integer a = 0;
	
		int start = 0;
		int bytesTotal = registro.toBytes().length;
		int bytesAux = Constantes.TAMANIO_BUFFER_ESCRITURA;
		int bytesEscritos = 0;
		
		if (bytesTotal < bytesAux) {
			bytesAux = bytesTotal;
		}
    
//        byte[] bytes = new byte[Constantes.TAMANIO_BUFFER_ESCRITURA];
//    
//        // Read in the bytes
//        int offset = 0;
//        int numRead = 0;
//        while (offset < bytes.length
//               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
//            offset += numRead;
//        }
//
//        return bytes;
		
	}
}
