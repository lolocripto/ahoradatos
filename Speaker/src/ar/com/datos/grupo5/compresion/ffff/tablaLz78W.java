/**
 * 
 */
package ar.com.datos.grupo5.compresion.lz78;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;

/**
 * @author Led Zeppelin
 *
 */
public class tablaLz78W {
	
	/*
	private HashMap<nodoTablaLZ78W,Integer> tablaCompresion;

	private HashMap<Integer,nodoTablaLZ78W> tablaDescompresion;
	*/
	
	private HashMap<String,Integer> tablaCompresion;

	private HashMap<Integer,String> tablaDescompresion;
	
	private boolean esCompresion;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(tablaLz78W.class);
	
	/**
	 * Contructor de la clase.
	 */
	public tablaLz78W(){
		/*
		this.tablaCompresion = new HashMap<nodoTablaLZ78W,Integer>();
		this.tablaDescompresion = new HashMap<Integer,nodoTablaLZ78W>();
		*/
		this.tablaCompresion = new HashMap<String,Integer>();
		this.tablaDescompresion = new HashMap<Integer,String>();
		this.inicializarTabla();
		
	}

	/**
	 * Inicializa la tabla con los caracteres conocidos.
	 */
	private void inicializarTabla() {
		Iterator<Character> it = Constantes.LISTA_CHARSET_LATIN.iterator();
		Character letra;
		int i = 0;
			while (it.hasNext()) {
				letra = it.next();
				//logger.debug("\nLetra: " + letra);
				/*
				this.tablaCompresion.put(new nodoTablaLZ78W(letra.toString()),i);
				this.tablaDescompresion.put(i,new nodoTablaLZ78W(letra.toString()));
				*/
				this.tablaCompresion.put(letra.toString(),i);
				this.tablaDescompresion.put(i,letra.toString());				
				i++;
			}	
	}

	private void aplicarClearing() {
		
	}
	
	/**
	 * Agrega un elemento a la tabla de descompresion o compresion
	 * @param nodo
	 */
	public final void add(final String nodo) {
		/*
		if (!this.esCompresion) { 
			this.tablaDescompresion.put(this.tablaDescompresion.size() + 1, new nodoTablaLZ78W(nodo));
		} else {
			this.tablaCompresion.put(new nodoTablaLZ78W(nodo), this.tablaCompresion.size() + 1);
		}
		*/

		//FIXME: Validar la necesidad de sumarle uno al tamaño de la tabla al momento de insertar el elemento
		if (!this.esCompresion) { 
			logger.debug("\nNodo a insertar: " + nodo + ", Posicion: " + this.tablaDescompresion.size());
			this.tablaDescompresion.put(this.tablaDescompresion.size(), nodo);
		} else {
			logger.debug("\nNodo a insertar: " + nodo + ", Posicion: " + this.tablaCompresion.size());
			this.tablaCompresion.put(nodo, this.tablaCompresion.size());
		}
	}
	
	/**
	 * Obtiene el numero relacionado al String de la tabla de Compresion
	 * @param cadena
	 * @return
	 */
	public final Integer get(String cadena) {
		if (this.esCompresion) { 
			//return this.tablaCompresion.get(new nodoTablaLZ78W(cadena));
			return this.tablaCompresion.get(cadena);
		} else {
			return -1;
		}
	}
	
	/**
	 * Obtiene el nodo relacionado al int de la tabla de Descompresion
	 * @param index
	 * @return
	 */
//	public final nodoTablaLZ78W get(int index) {
	public final String get(int index) {
		if (!this.esCompresion) {
			return this.tablaDescompresion.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Verifica si existe el key en la tabla de descompresion
	 * @param index
	 * @return
	 */
	public final boolean existKey(int index){
		if (!this.esCompresion) {
			return this.tablaDescompresion.containsKey(index);
		} else {
			return false;
		}
	}

	/**
	 * Verifica si existe el key en la tabla de compresion
	 * @param index
	 * @return
	 */
	public final boolean existKey(String cadena){
		if (this.esCompresion) {
			//nodoTablaLZ78W nodo = new nodoTablaLZ78W(cadena);
			//return this.tablaCompresion.containsKey(nodo);
			return this.tablaCompresion.containsKey(cadena);
		} else {
			return false;
		}
	}

	/**
	 * Verifica si existe el value en la tabla de descompresion
	 * @param index
	 * @return
	 */
	public final boolean existValue(String cadena){
		if (!this.esCompresion) {
			//return this.tablaDescompresion.containsValue(new nodoTablaLZ78W(cadena));	
			return this.tablaDescompresion.containsValue(cadena);
		} else {
			return false;
		}
	}
	
	/**
	 * Verifica si existe el value en la tabla de compresion
	 * @param index
	 * @return
	 */
	public final boolean existValue(int index){
		if (this.esCompresion) {
			return this.tablaCompresion.containsValue(index);	
		} else {
			return false;
		}
	}
	
	/**
	 * Obtiene el proximo indice de la tabla de descompresion o compresion.
	 * @return el proximo indice.
	 */
	public final int getProxIndice(){
		if (this.esCompresion) {
			return this.tablaCompresion.size();	
		} else {
			return this.tablaDescompresion.size();
		}
		
	}

	/**
	 * Obtiene el tamaño de la tabla de descompresion o compresion.
	 * @return el tamaño de la tabla.
	 */
	public final int getSize() {
		if (this.esCompresion) {
			return this.tablaCompresion.size();	
		} else {
			return this.tablaDescompresion.size();
		}
	}

	/**
	 * @return the esCompresion
	 */
	public boolean isEsCompresion() {
		return esCompresion;
	}

	/**
	 * @param esCompresion the esCompresion to set
	 */
	public void setEsCompresion(boolean esCompresion) {
		this.esCompresion = esCompresion;
	}
}
