package ar.com.datos.trie.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;


public class TrieArchiveIndex {

	private int tamaioPar = Constantes.SIZE_OF_INT * 2;
	
	/**
	 * Archivo en disco.
	 */
	protected RandomAccessFile file = null;
	
	/**
	 * Ruta del archivo.
	 */
	private String nombreArchivo;
	
	/**
	 * Posicion en el archivo.
	 */
	protected long posicionActual = 0;
	
	
	
	public TrieArchiveIndex() throws FileNotFoundException {
	}
	
	
	
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
			this.file = new RandomAccessFile(archivo, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);

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
	public void insertar(long nodo, long bloque) throws IOException {
		
		if (file == null) {
			throw new IOException("No se creo o abrio el archivo.");
		}

		// Me posiciono al final de archivo.
		file.seek(file.length());
		file.write(Conversiones.longToArrayByte(nodo));
		file.seek(file.length());
		file.write(Conversiones.longToArrayByte(bloque));
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


	public Long buscar(long NumeroNodo){
		
		try {
			
			file.seek((tamaioPar * NumeroNodo)+Constantes.SIZE_OF_LONG);  //me posiciono en el nodo que quiero y le sumo lo que ocupa el valor del nodo
			byte[] bufferBloque = new byte[Constantes.SIZE_OF_LONG]; //creo un buffer para leer el bloque
	        file.read(bufferBloque, 0, Constantes.SIZE_OF_LONG); //leo el bloque partiendo de donde termina el int
	        return Conversiones.arrayByteToLong(bufferBloque); //convierto los bytes al long y devuelvo.
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		

        
	}


}
