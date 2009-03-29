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
	public boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {

		try {

			file = new RandomAccessFile(archivo, modo);

		} catch (FileNotFoundException e) {
			throw e;
		}
		
		return true;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#borrar(Registro)
	 */
	public final boolean borrar(final Registro registro) {
		
		//file.close();
		//File archivo = new File();
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#buscar(Registro)
	 */
	public final boolean buscar(final Registro registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public void cerrar() throws IOException {
		
			file.close();
	}

	/**
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
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
	 */
	public void insertar(final Registro registro) throws IOException {

		if (file == null) {
			throw new IOException("No se creo o abrio el archivo.");
		}

		int offset = 0;
		byte[] bytes = null;
		int bytesTotal = 0;
		int bytesEnviar = 0;
		
		//Me posiciono al final de archivo.
		file.seek(file.length());
		
		while (registro.hasMoreBytes()) {
			
			offset = 0;
			bytes = registro.getBytes();
			bytesTotal = bytes.length;
			bytesEnviar = Constantes.TAMANIO_BUFFER_ESCRITURA;
			if (bytesTotal < bytesEnviar) {
				bytesEnviar = bytesTotal;
			}
			
			//Escribo la cantidad que me permite.
			while (offset < bytesTotal) {

				if ((bytesTotal - offset) < bytesEnviar) {
					bytesEnviar = bytesTotal - offset;
				}

				file.write(bytes, offset, bytesEnviar);

				offset += bytesEnviar;
			}
		}
	}
}
