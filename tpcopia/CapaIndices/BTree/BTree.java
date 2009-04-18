package tpdatos.CapaIndices.BTree;
import tpdatos.CapaIndices.Manager.IndiceManager.IndiceArbolManager;

/**
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:55:11 a.m.
 */
public class BTree {

	/**
	 * Objeto utilizado para cargar y guardar los nodos desde disco
	 */
	protected IndiceArbolManager& indiceManager;
	/**
	 * Tamanio máximo (en bytes) de un nodo
	 */
	protected unsigned short tamanioNodo;

	public BTree(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param indiceManager
	 * @param tamanioNodo
	 */
	public BTree(IndiceManager& indiceManager, unsigned short tamanioNodo){

	}

	public abstract ~BTree();

	/**
	 * 
	 * @param clave
	 */
	public abstract Clave* buscar(Clave* clave);

	/**
	 * 
	 * @param clave
	 */
	public abstract bool eliminar(Clave* clave);

	/**
	 * 
	 * @param clave
	 */
	public abstract bool insertar(Clave* clave);

	/**
	 * 
	 * @param clave
	 */
	public abstract void mayor(Clave* clave);

	/**
	 * 
	 * @param clave
	 */
	public abstract void mayorOIgual(Clave* clave);

	/**
	 * 
	 * @param claveVieja
	 * @param claveNueva
	 */
	public abstract int modificar(Clave* claveVieja, Clave* claveNueva);

	public abstract void primero();

	public abstract Clave* siguiente();

	public abstract bool vacio();

}