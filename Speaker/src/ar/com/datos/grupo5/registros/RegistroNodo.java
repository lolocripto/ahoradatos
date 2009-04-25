
package ar.com.datos.grupo5.registros;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.ListasInvertidas;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Esta clase implementa el registro para los nodos de árboles.
 * 
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class RegistroNodo implements Registro {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ListasInvertidas.class);

	/**
	 * Cuantos bytes puedo pasar.
	 */
	private Long moreBytes;
	
	/**
	 * Es la clave del nodo.
	 */
	private Clave claveNodo;
	
	/**
	 * Nodo al que apunta por la izquierda.
	 */
	private Nodo nodoIzquierdo;
	
	/**
	 * Nodo al que apunta por la derecha.
	 */
	private Nodo nodoDerecho;
	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * 
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {
		
		if (moreBytes > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {
		//TODO Hacer este método!!!!!!
		byte[] arraybytes = null;
		return arraybytes;
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param offset id del termino que se busca.
	 */
	public final void setBytes(final byte[] buffer, final Long offset) {
	// TODO Hacer este método!!!!	
	}

	/**
	 * En el caso de ocupar varios bloques esta función agrega mas datos
	 * al registro a partir de los datos de otros bloques.
	 * @param buffer
	 * 			Datos leidos de un bloque que no es el primero.
	 * @param offset
	 * 			Offset dentro de les array de byte donde empiezan
	 * 			los datos.
	 */
	public final void setMoreBytes(final byte[] buffer, final int offset) {
		//TODO Hacer este método!!!!!!!!!		
	}

	/**
	 * @param clave the clave to set
	 */
	public final void setClaveNodo(final Clave clave) {
		this.claveNodo = clave;
	}

	/**
	 * @return the claveNodo
	 */
	public final Clave getClaveNodo() {
		return claveNodo;
	}

	/**
	 * @return the nodoIzquierdo
	 */
	public final Nodo getNodoIzquierdo() {
		return nodoIzquierdo;
	}

	/**
	 * @param nodo the nodoIzquierdo to set
	 */
	public final void setNodoIzquierdo(final Nodo nodo) {
		this.nodoIzquierdo = nodo;
	}

	/**
	 * @return the nodoDerecho
	 */
	public final Nodo getNodoDerecho() {
		return nodoDerecho;
	}

	/**
	 * @param nodo the nodoDerecho to set
	 */
	public final void setNodoDerecho(final Nodo nodo) {
		this.nodoDerecho = nodo;
	}
	
}
