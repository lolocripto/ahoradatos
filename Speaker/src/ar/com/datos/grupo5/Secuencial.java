package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.grupo5.interfaces.Registro;

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
	 * Metodo para Intentar abrir un archivo, pasado por parámetro.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#abrir(String, String)
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
	 * @see ar.com.datos.grupo5.interfaces.Archivo#borrar(Registro)
	 */
	public final boolean borrar(final Registro registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para buscar en un archivo, una cadena pasada por parámetro.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#buscar(Registro)
	 */
	public final boolean buscar(final Registro registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Metodo para cerrar el archivo que se está manipulando.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public void cerrar() throws IOException {
		
			file.close();
	}

	/**
	 * Metodo para Intentar crear un archivo, pasado por parámetro.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#crear(String)
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
	 * @see ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
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
