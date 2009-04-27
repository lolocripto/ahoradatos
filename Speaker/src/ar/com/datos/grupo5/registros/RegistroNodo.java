
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.ListasInvertidas;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Esta clase implementa el registro para los nodos de árboles.
 * 
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class RegistroNodo {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ListasInvertidas.class);
	
	/**
	 * Es la clave del nodo.
	 */
	private Clave claveNodo;
	
	/**
	 * El numero de bloque al que apunta a la izquierda.
	 */
	private Integer nroBloqueIzquierdo;
	
	/**
	 * El numero de bloque al que apunta a la izquierda.
	 */
	private Integer nroBloqueDerecha;
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			// FIXME me parece que así se complica lo de las longitudes
			int longitud =  2 * Constantes.SIZE_OF_INT + 
			claveNodo.getBytes().length;
			byte[] longDatoBytes = Conversiones.
			intToArrayByte(claveNodo.getBytes().length);
			byte[] bloqueAnt = Conversiones.intToArrayByte(nroBloqueIzquierdo);
			byte[] bloquePos = Conversiones.intToArrayByte(nroBloqueDerecha);
			byte[] longDato = Conversiones.intToArrayByte(longitud);
			
			dos.write(bloqueAnt, 0, bloqueAnt.length);
			dos.write(longDato, 0, longDato.length);
			dos.write(longDatoBytes, 0, longDatoBytes.length);
			dos.write(claveNodo.getBytes(), 0, claveNodo.getBytes().length);
			dos.write(bloquePos, 0, bloquePos.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param offset id del termino que se busca.
	 */
	public void setBytes(final byte[] buffer, final int offset) {
		//TODO HACERLO YA!!!!!!!!!!
	}

	/**
	 * @param clave the clave to set
	 */
	public final void setClave(final Clave clave) {
		this.claveNodo = clave;
	}

	/**
	 * @return the claveNodo
	 */
	public final Clave getClave() {
		return claveNodo;
	}

	/**
	 * @return the nroBloqueIzquierdo
	 */
	public final Integer getNroBloqueIzquierdo() {
		return nroBloqueIzquierdo;
	}

	/**
	 * @param nroBloqueIzquierdo the nroBloqueIzquierdo to set
	 */
	public final void setNroBloqueIzquierdo(final Integer nroBloqueIzquierdo) {
		this.nroBloqueIzquierdo = nroBloqueIzquierdo;
	}

	/**
	 * @return the nroBloqueDerecha
	 */
	public final Integer getNroBloqueDerecha() {
		return nroBloqueDerecha;
	}

	/**
	 * @param nroBloqueDerecha the nroBloqueDerecha to set
	 */
	public final void setNroBloqueDerecha(final Integer nroBloqueDerecha) {
		this.nroBloqueDerecha = nroBloqueDerecha;
	}

}
