
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.ListasInvertidas;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

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
	private Long moreBytes = 1L;
	
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
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * 
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public boolean hasMoreBytes() {
		
		if (moreBytes > 0) {
			return true;
		}
		moreBytes = 0L;
		return false;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			// FIXME me parece que así se complica lo de las longitudes
			byte[] longDatoBytes = Conversiones.
			intToArrayByte(claveNodo.getBytes().length);
			byte[] bloqueAnt = Conversiones.intToArrayByte(nroBloqueIzquierdo);
			byte[] bloquePos = Conversiones.intToArrayByte(nroBloqueDerecha);

			dos.write(bloqueAnt, 0, bloqueAnt.length);
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
	public void setBytes(final byte[] buffer, final Long offset) {
		//TODO HACERLO YA!!!!!!!!!!
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
		moreBytes = 1L;
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
	public final void setNroBloqueIzquierdo(Integer nroBloqueIzquierdo) {
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
	public final void setNroBloqueDerecha(Integer nroBloqueDerecha) {
		this.nroBloqueDerecha = nroBloqueDerecha;
	}

}
