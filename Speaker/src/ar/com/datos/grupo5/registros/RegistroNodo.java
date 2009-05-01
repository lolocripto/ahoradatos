
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
	private Integer nroBloqueDerecho;
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			// TODO TESTEARME!!!!!!!!!
			byte[] claveBytes = claveNodo.getClave().getBytes();
			byte[] longClave = Conversiones
					.intToArrayByte(claveBytes.length);
			int longitud = 2 * Constantes.SIZE_OF_INT
					+ longClave.length;
			
			dos.write(Conversiones.intToArrayByte(nroBloqueIzquierdo));
			dos.write(Conversiones.intToArrayByte(longitud));
			dos.write(longClave);
			dos.write(claveBytes);
			dos.write(Conversiones.intToArrayByte(nroBloqueDerecho));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param bloqueAnt nro de bloque anterior.
	 */
	public void setBytes(final byte[] buffer, final int bloqueAnt) {
		//TODO TESTEARLO YA!!!!!!!!!!
		//Leo el numero de bloque Anterior.
		setNroBloqueIzquierdo(bloqueAnt);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dos = new DataInputStream(bis);
		byte[] datos = new byte[Constantes.SIZE_OF_INT];
		
		try {
			//Leo la longitud de la clave
			int longdato = dos.readInt();
			datos = new byte[longdato];
			//Leo la clave
			dos.read(datos, 0, longdato);
			claveNodo = new Clave(new String(datos));
			//Leo el numero de bloque posterior.
			setNroBloqueDerecho(dos.readInt());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public final Integer getNroBloqueDerecho() {
		return nroBloqueDerecho;
	}

	/**
	 * @param nroBloqueDerecha the nroBloqueDerecha to set
	 */
	public final void setNroBloqueDerecho(final Integer nroBloqueDerecha) {
		this.nroBloqueDerecho = nroBloqueDerecha;
	}

}
