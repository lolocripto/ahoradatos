package tpdatos.CapaIndices.Manager;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: Manager (Abstracta. Define el comportamiento y la interfaz de todas las
 * clases Manager).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:54:31 a.m.
 */
public class Manager {

	/**
	 * Nombre del archivo a acceder.
	 */
	private string nombreArchivo;
	/**
	 * Tamaño de un bloque dentro del archivo.
	 */
	private unsigned int tamanioBloque;

	public Manager(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param tamanioBloque
	 * @param nombreArchivo
	 */
	public Manager(unsigned int tamanioBloque, string nombreArchivo){

	}

	public string getNombreArchivo(){
		return "";
	}

	public unsigned int getTamanioBloque(){
		return null;
	}

	/**
	 * Metodo protegido
	 */
	protected ComuDatos* instanciarPipe(){
		return null;
	}

	public abstract ~Manager();

	/**
	 * 
	 * @param nombre
	 */
	public void setNombreArchivo(string nombre){

	}

	/**
	 * 
	 * @param tamanio
	 */
	public void setTamanioBloque(unsigned int tamanio){

	}

}