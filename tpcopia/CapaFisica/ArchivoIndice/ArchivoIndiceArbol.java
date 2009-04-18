package tpdatos.CapaFisica.ArchivoIndice;

/**
 * Clase -----------------------------------------------------------------------
 * Nombre: ArchivoIndiceArbol (Permite el manejo de archivos empleados en la
 * persistencia de datos a traves de indices con estructura de arbol B+ o B*).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 11:07:33 a.m.
 */
public class ArchivoIndiceArbol extends ArchivoIndice {

	public ArchivoIndiceArbol(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param nombreArchivo
	 * @param tamNodo
	 */
	public ArchivoIndiceArbol(string nombreArchivo, unsigned short tamNodo){

	}

	public abstract ~ArchivoIndiceArbol();

	/**
	 * 
	 * @param numNodo
	 */
	public char eliminarBloqueDoble(unsigned short numNodo){
		return 0;
	}

	/**
	 * Metodos publicos
	 * 
	 * @param nodo
	 */
	public short escribirBloqueDoble(final void* nodo){
		return 0;
	}

	/**
	 * 
	 * @param nodo
	 * @param numBloque
	 */
	public char escribirBloqueDoble(final void* nodo, unsigned short numBloque){
		return 0;
	}

	/**
	 * 
	 * @param nodo
	 * @param numNodo
	 */
	public char leerBloqueDoble(void* nodo, unsigned short numNodo){
		return 0;
	}

}