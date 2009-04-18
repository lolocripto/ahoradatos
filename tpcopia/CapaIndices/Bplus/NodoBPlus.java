package tpdatos.CapaIndices.BPlus;
import tpdatos.CapaIndices.Common.Nodo;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: NodoBPlus (Implementa nodos de Arbol B+)
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:49:29 a.m.
 */
public class NodoBPlus extends Nodo {

	public NodoBPlus(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructores/Destructores
	 * 
	 * @param refNodo
	 * @param nivel
	 * @param tamanio
	 */
	public NodoBPlus(unsigned int refNodo, unsigned char nivel, unsigned short tamanio){

	}

	/**
	 * 
	 * @param refNodo
	 * @param nivel
	 * @param clave
	 * @param tamanio
	 */
	public NodoBPlus(unsigned int refNodo, unsigned char nivel, Clave* clave, unsigned short tamanio){

	}

	public Nodo* copiar(){
		return null;
	}

	public unsigned short getTamanioEspacioClaves(){
		return null;
	}

	public unsigned short getTamanioMinimo(){
		return null;
	}

	public abstract ~NodoBPlus();

	/**
	 * 
	 * @param nodoHnoDer
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public bool puedePasarClaveHaciaDer(Nodo* nodoHnoDer, Nodo* nodoPadre, Clave* clavePadre){
		return null;
	}

	/**
	 * 
	 * @param nodoHnoIzq
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public bool puedePasarClaveHaciaIzq(Nodo* nodoHnoIzq, Nodo* nodoPadre, Clave* clavePadre){
		return null;
	}

}