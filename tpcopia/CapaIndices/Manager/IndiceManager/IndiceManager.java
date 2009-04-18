package tpdatos.CapaIndices.Manager.IndiceManager;
import tpdatos.CapaIndices.Manager.Manager;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: IndiceManager (Abstracta. Clase que sirve de abstraccion de la capa
 * fisica para los indices de la capa de indices).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:53:17 a.m.
 */
public class IndiceManager extends Manager {

	/**
	 * Atributo
	 */
	private unsigned char tipoIndice;

	public IndiceManager(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param tamanioBloque
	 * @param nombreArchivo
	 * @param tipoIndice
	 */
	public IndiceManager(unsigned int tamanioBloque, string nombreArchivo, unsigned char tipoIndice){

	}

	/**
	 * 
	 * @param posicion
	 */
	public abstract int eliminarBloque(unsigned short posicion);

	/**
	 * 
	 * @param bloqueNuevo
	 */
	public abstract int escribirBloque(BloqueIndice* bloqueNuevo);

	/**
	 * 
	 * @param numBloque
	 * @param bloqueModif
	 */
	public abstract int escribirBloque(unsigned short numBloque, BloqueIndice* bloqueModif);

	public final unsigned int getTipoIndice(){
		return null;
	}

	public abstract ~IndiceManager();

	/**
	 * 
	 * @param numeroBloque
	 * @param bloqueLeido
	 */
	public abstract int leerBloque(unsigned int numeroBloque, BloqueIndice* bloqueLeido);

}