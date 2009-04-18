package tpdatos.CapaIndices.BPlus;
import tpdatos.CapaIndices.BTree.BTree;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: BPlusTree (Implementa Arboles B+ en disco)
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:55:55 a.m.
 */
public class BPlusTree extends BTree {

	private SetClaves::iterator iterClavesActual;
	/**
	 * Atributos privados
	 */
	private NodoBPlus* nodoActual;
	private NodoBPlus* nodoRaiz;

	public BPlusTree(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Contructor/Destructor
	 * 
	 * @param indiceManager
	 * @param tamanioNodo
	 */
	public BPlusTree(IndiceManager& indiceManager, unsigned short tamanioNodo){

	}

	public abstract ~BPlusTree();

	/**
	 * 
	 * @param clave
	 */
	public Clave* buscar(Clave* clave){
		return null;
	}

	/**
	 * 
	 * @param clave
	 */
	private NodoBPlus* buscarLugar(Clave* clave){
		return null;
	}

	/**
	 * 
	 * @param nodo
	 * @param clave
	 */
	private NodoBPlus* buscarLugarRecursivo(NodoBPlus* nodo, Clave* clave){
		return null;
	}

	/**
	 * 
	 * @param padre
	 * @param hijo
	 * @param claveNodoHijo
	 */
	private NodoBPlus* buscarPadre(NodoBPlus* padre, NodoBPlus* hijo, Clave* claveNodoHijo){
		return null;
	}

	/**
	 * 
	 * @param clave
	 */
	public bool eliminar(Clave* clave){
		return null;
	}

	/**
	 * 
	 * @param nodoTarget
	 * @param codigo
	 * @param claveEliminada
	 */
	private void eliminarInterno(NodoBPlus*& nodoTarget, char* codigo, Clave* claveEliminada){

	}

	private NodoBPlus* getRaiz(){
		return null;
	}

	/**
	 * 
	 * @param clave
	 */
	public bool insertar(Clave* clave){
		return null;
	}

	/**
	 * 
	 * @param nodoDestino
	 * @param codigo
	 * @param claveInsertada
	 */
	private void insertarInterno(NodoBPlus*& nodoDestino, char* codigo, Clave* claveInsertada){

	}

	/**
	 * 
	 * @param clave
	 */
	public void mayor(Clave* clave){

	}

	/**
	 * 
	 * @param clave
	 */
	public void mayorOIgual(Clave* clave){

	}

	/**
	 * 
	 * @param nodoIzq
	 * @param nodoDer
	 * @param separador
	 */
	private void merge(NodoBPlus* nodoIzq, NodoBPlus*& nodoDer, Clave* separador){

	}

	/**
	 * 
	 * @param claveVieja
	 * @param claveNueva
	 */
	public int modificar(Clave* claveVieja, Clave* claveNueva){
		return 0;
	}

	/**
	 * 
	 * @param nodoDestino
	 * @param nodoPadre
	 * @param nodoHnoIzq
	 * @param clavePadre
	 */
	private void pasarClaveHaciaDerecha(NodoBPlus* nodoDestino, NodoBPlus* nodoPadre, NodoBPlus* nodoHnoIzq, Clave* clavePadre){

	}

	/**
	 * 
	 * @param nodoDestino
	 * @param nodoPadre
	 * @param nodoHnoDer
	 * @param clavePadre
	 */
	private void pasarClaveHaciaIzquierda(NodoBPlus* nodoDestino, NodoBPlus* nodoPadre, NodoBPlus* nodoHnoDer, Clave* clavePadre){

	}

	/**
	 * 
	 * @param nodoDestino
	 * @param nodoPadre
	 * @param nodoHnoIzq
	 * @param clavePadre
	 */
	private void pasarMaximoPosibleHaciaDerecha(NodoBPlus* nodoDestino, NodoBPlus* nodoPadre, NodoBPlus* nodoHnoIzq, Clave*& clavePadre){

	}

	/**
	 * 
	 * @param nodoDestino
	 * @param nodoPadre
	 * @param nodoHnoDer
	 * @param clavePadre
	 */
	private void pasarMaximoPosibleHaciaIzquierda(NodoBPlus* nodoDestino, NodoBPlus* nodoPadre, NodoBPlus* nodoHnoDer, Clave*& clavePadre){

	}

	public void primero(){

	}

	/**
	 * 
	 * @param nodoIzq
	 * @param nodoDer
	 * @param separador
	 */
	private bool puedeMerge(NodoBPlus* nodoIzq, NodoBPlus* nodoDer, Clave* separador){
		return null;
	}

	public Clave* siguiente(){
		return null;
	}

	/**
	 * 
	 * @param nodoTarget
	 */
	private Clave* split(NodoBPlus* nodoTarget){
		return null;
	}

	public bool vacio(){
		return null;
	}

}