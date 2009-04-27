package ar.com.datos.grupo5;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Nodo de la lista de espacios libres del archivo de bloques.
 * Permite administras de una manera mas sencilla el par Bloque-EspacioLibre
 * @author Led Zeppelin
 *
 */
public class NodoListaEspacioLibre implements Comparable<NodoListaEspacioLibre>{

	/**
	 * Contiene el espacio libre dentro del bloque asociado.
	 */
	private Short espacio;
	
	/**
	 * Contiene el número del bloque que tiene espacio libre.
	 */
	private Integer nroBloque;
	
	/**
	 *  Permite saber si hay mas bytes para copiar.
	 */
	private int moreBytes;
	
	/**
	 * El tamaño que ocupa en archivo.
	 */
	private int tamanioNodo = Constantes.SIZE_OF_INT + Constantes.SIZE_OF_SHORT;
	
	
	/**
	 * Permite obtener el espacio libre para el bloque asociado.
	 * @return
	 * 		El espacio libre del bloque.
	 */
	public final short getEspacio() {
		return espacio;
	}
	
	/**
	 * Permite obtener el Número del bloque que tiene espacio libre.
	 * @return
	 * 		El número del bloque.
	 */
	public final int getNroBloque() {
		return nroBloque;
	}
	
	/**
	 * Permite setear el número del bloque. 
	 * @param nroBloqueExt
	 * 			El número de bloque.
	 */
	public final void setNroBloque(final int nroBloqueExt) {
		this.nroBloque = nroBloqueExt;
		this.moreBytes = Constantes.SIZE_OF_INT + Constantes.SIZE_OF_SHORT;
	}
	
	/**
	 * Permite setear el espacio libre dentro del bloque.
	 * @param espacioExt
	 * 			Espacio libre dentro del bloque.
	 */
	public final void setEspacio(final short espacioExt) {
		this.espacio = espacioExt;
		this.moreBytes = Constantes.SIZE_OF_INT + Constantes.SIZE_OF_SHORT;
	}

	/**
	 * Determina cuando nodo es igual a otro.
	 * @param nodo .
	 * @return true si son iguales.
	 */
	@Override
	public final boolean equals(final Object nodo) {
		
		if (nodo instanceof NodoListaEspacioLibre) {
			NodoListaEspacioLibre c = (NodoListaEspacioLibre) nodo;
			return ( (c.getEspacio() == this.espacio) && (c.getNroBloque() == this.nroBloque) );
		}
		
		return false;
	}
	
	/**
	 * Para el equals.
	 * @return al hash code.
	 */
	public final int hashCode() {
		return 0;
	}
	
	/**
	 * Este método convierte el objeto en una tira de bytes.
	 * 
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {

			
			byte[] espacioBytes = Conversiones.shortToArrayByte(this.espacio);
			byte[] nroBloqueByte = Conversiones.intToArrayByte(this.nroBloque);
			if (moreBytes == (espacioBytes.length + nroBloqueByte.length)) {
				
				dos.write(espacioBytes, 0, espacioBytes.length);
				moreBytes -= espacioBytes.length;
				dos.write(nroBloqueByte, 0, nroBloqueByte.length);
				moreBytes -= nroBloqueByte.length;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	/**
	 * Este método toma la tira de bytes y setea al registro.
	 * 
	 * @param buffer
	 *            la tira de bytes.
	 */
	public final void setBytes(final byte[] buffer) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dis = new DataInputStream(bis);

		try {
			this.espacio = dis.readShort();
			this.nroBloque = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param tamanioNodo the tamanioNodo to set
	 */
	public void setSize(int tamanioNodo) {
		this.tamanioNodo = tamanioNodo;
	}

	/**
	 * @return the tamanioNodo
	 */
	public int getSize() {
		return tamanioNodo;
	}

	/**
	 * Para comparar los nodos.
	 * @param nodo nodo a comparar.
	 * @return 0, 1, -1.
	 */
	public int compareTo(NodoListaEspacioLibre nodo) {
		
		return this.espacio.compareTo(nodo.getEspacio());
	}

}
