package tpdatos.CapaIndices.Manager.IndiceManager;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: IndiceArbolManager ( Clase que sirve de abstraccion de la capa fisica
 * para los indices en arboles de la capa de indices).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:53:05 a.m.
 */
public class IndiceArbolManager extends IndiceManager {

	/**
	 * Header del Nodo
	 * @author PowerData
	 * @version 1.0
	 * @created 18-Abr-2009 10:53:05 a.m.
	 */
	public class HeaderNodo {

		public unsigned short espacioLibre;
		public unsigned char nivel;
		public unsigned int refNodo;

		public HeaderNodo(){

		}

		public void finalize() throws Throwable {

		}

	}

	public IndiceArbolManager(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructor
	 * 
	 * @param tamNodo
	 * @param nombreArchivo
	 * @param tipoIndice
	 */
	public IndiceArbolManager(unsigned int tamNodo, string nombreArchivo, unsigned char tipoIndice){

	}

	/**
	 * Metodos publicos
	 * 
	 * @param clave
	 * @param puntero
	 */
	private abstract void copiarClaveHoja(Clave* clave, char*& puntero);

	/**
	 * 
	 * @param clave
	 * @param puntero
	 */
	private abstract void copiarClaveNoHoja(Clave* clave, char*& puntero);

	/**
	 * 
	 * @param posicion
	 */
	public abstract int eliminarBloque(unsigned short posicion);

	/**
	 * 
	 * @param posicion
	 */
	public abstract int eliminarBloqueDoble(unsigned short posicion);

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

	/**
	 * 
	 * @param bloqueNuevo
	 */
	public abstract int escribirBloqueDoble(BloqueIndice* bloqueNuevo);

	/**
	 * 
	 * @param numBloque
	 * @param bloqueModif
	 */
	public abstract int escribirBloqueDoble(unsigned short numBloque, BloqueIndice* bloqueModif);

	public static int getTamanioHeader(){
		return 0;
	}

	public abstract ~IndiceArbolManager();

	/**
	 * 
	 * @param numeroBloque
	 * @param bloqueLeido
	 */
	public abstract int leerBloque(unsigned int numeroBloque, BloqueIndice* bloqueLeido);

	/**
	 * 
	 * @param numBloque
	 * @param bloqueLeido
	 */
	public abstract int leerBloqueDoble(unsigned short numBloque, BloqueIndice* bloqueLeido);

	/**
	 * 
	 * @param buffer
	 */
	private abstract Clave* leerClaveHoja(char*& buffer);

	/**
	 * 
	 * @param buffer
	 */
	private abstract Clave* leerClaveNoHoja(char*& buffer);

}