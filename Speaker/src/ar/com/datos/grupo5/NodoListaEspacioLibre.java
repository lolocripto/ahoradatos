package ar.com.datos.grupo5;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Nodo de la lista de espacios libres del archivo de bloques.
 * Permite administras de una manera mas sencilla el par Bloque-EspacioLibre
 * @author Led Zeppelin
 *
 */
public class NodoListaEspacioLibre {

	/**
	 * Contiene el espacio libre dentro del bloque asociado.
	 */
	private short espacio;
	
	/**
	 * Contiene el número del bloque que tiene espacio libre.
	 */
	private int nroBloque;
	
	/**
	 *  Permite saber si hay mas bytes para copiar.
	 */
	private int moreBytes;
	
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

	/*
	public final boolean equals(final NodoListaEspacioLibre n1) {
		return (this.espacio == n1.getEspacio() 
					&& this.nroBloque == n1.getNroBloque());
	}
	*/
	
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

}
