package tpdatos.CapaFisica.ArchivoIndice;
import tpdatos.CapaFisica.ArchivoEL.ArchivoEL;
import tpdatos.CapaFisica.ArchivoBase.ArchivoBase;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: ArchivoIndice (Define el comportamiento de las clases de manejo de
 * archivos de indices en disco).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 11:07:06 a.m.
 */
public class ArchivoIndice extends ArchivoBase {

	private ArchivoEL* archivoEL;
	private static unsigned int contador;

	public ArchivoIndice(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param nombreArchivo
	 * @param tamBloque
	 */
	public CapaFisicaArbol(string nombreArchivo, unsigned short tamBloque){

	}

	public abstract ~CapaFisicaArbol();

	/**
	 * 
	 * @param numBloque
	 */
	public abstract char eliminarBloque(unsigned short numBloque);

	/**
	 * Metodos publicos
	 * 
	 * @param bloque
	 */
	public abstract short escribirBloque(final void* bloque);

	/**
	 * 
	 * @param bloque
	 * @param numBloque
	 */
	public abstract char escribirBloque(final void* bloque, unsigned short numBloque);

	public ArchivoEL* getArchivoEL(){
		return null;
	}

	/**
	 * 
	 * @param bloque
	 * @param numBloque
	 */
	public abstract char leerBloque(void* bloque, unsigned short numBloque);

}