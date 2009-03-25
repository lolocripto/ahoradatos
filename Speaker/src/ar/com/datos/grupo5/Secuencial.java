package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.IOException;
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
	 * @throws FileNotFoundException 
	 */
	public boolean abrir(final String archivo, final String modo) throws FileNotFoundException {
		
		try {

			file = new RandomAccessFile(archivo, modo);

		} catch (FileNotFoundException e) {
			throw e;
		}
		
		return true;
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
	 * @throws IOException 
	 */
	public void cerrar() throws IOException {
		
			file.close();
	}

	/**
	 * Metodo para Intentar crear un archivo, pasado por parámetro.
	 * Si el archivo existe lo reemplaza.
	 * @param archivo Path completo del archivo.
	 * @throws FileNotFoundException si no se puede crear el archivo.
	 */
	public final void crear(final String archivo)
			throws FileNotFoundException {
		
		try {
			
			file = new RandomAccessFile(archivo,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			//Si existe lo trunco.
			try {
				file.setLength(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * Metodo para Insertar la cadena en el archivo
	 * en el que se está trabajando.
	 * @param registro Es el registro que se va a agregar al archivo.
	 * @throws IOException 
	 */
	public void insertar(final Registro registro) throws IOException {

		if (file == null) {
			throw new IOException("No se creo o abrio el archivo.");
		}
	
		int offset = 0;
		byte[] bytes = registro.toBytes();
		int bytesTotal = registro.toBytes().length;
		int bytesEnviar = Constantes.TAMANIO_BUFFER_ESCRITURA;
		
		if (bytesTotal < bytesEnviar) {
			bytesEnviar = bytesTotal;
		}
		
		file.seek(file.length());
		
		while (offset < bytesTotal) {
			
			if ((bytesTotal - offset) < bytesEnviar) {
				bytesEnviar = bytesTotal - offset; 
			}
			
			file.write(bytes, offset, bytesEnviar);
			
			offset += bytesEnviar;
		}
	}
}
