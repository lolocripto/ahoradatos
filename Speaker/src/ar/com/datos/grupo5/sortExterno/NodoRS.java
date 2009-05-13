package ar.com.datos.grupo5.sortExterno;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:57 p.m.
 */
public class NodoRS implements Comparable<NodoRS> {

	/**
	 * Id del documento.
	 */
	private Long idDocumento;

	/**
	 * id del termino.
	 */
	private Long idTermino;

	/**
	 * Tamaño del nodo al persistirse.
	 */
	private int tamanio;

	/**
	 * Contructor de la clase.
	 */
	public NodoRS() {
		this.idTermino = 0L;
		this.idDocumento = 0L;
		this.tamanio = Constantes.SIZE_OF_LONG * 2;
	}

	/**
	 * @throws Throwable
	 *             d.
	 */
	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param idT
	 *            id del termino
	 * @param idD
	 *            id del documento
	 */
	public NodoRS(final Long idT, final Long idD) {
		this.idTermino = idT;
		this.idDocumento = idD;
		this.tamanio = Constantes.SIZE_OF_LONG * 2;
	}

	/**
	 * @param idDocumentoExt
	 *            the idDocumento to set
	 */
	public final void setIdDocumento(final Long idDocumentoExt) {
		this.idDocumento = idDocumentoExt;
	}

	/**
	 * @return the idDocumento
	 */
	public final Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idTerminoExt
	 *            the idTermino to set
	 */
	public final void setIdTermino(final Long idTerminoExt) {
		this.idTermino = idTerminoExt;
	}

	/**
	 * @return the idTermino
	 */
	public final Long getIdTermino() {
		return idTermino;
	}

	/**
	 * Setea los datos del objeto a partir de un buffer.
	 * 
	 * @param buffer
	 *            Datos leidos.
	 */
	public final void setBytes(byte[] buffer) {

		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
		DataInputStream dis = new DataInputStream(bis);

		try {
			this.idTermino = dis.readLong();
			this.idDocumento = dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final byte[] getBytes() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {

			byte[] campoBytes = Conversiones.longToArrayByte(this.idTermino);
			dos.write(campoBytes, 0, campoBytes.length);

			campoBytes = Conversiones.longToArrayByte(this.idDocumento);
			dos.write(campoBytes, 0, campoBytes.length);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bos.toByteArray();

	}

	/**
	 * @param tamanio
	 *            the tamanio to set
	 */
	@SuppressWarnings("unused")
	private void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}

	/**
	 * @return the tamanio
	 */
	public int getTamanio() {
		return tamanio;
	}

	/**
	 * 
	 * Devuelve 1 si this es mayor al argumento, 0 si son iguales y -1 si this
	 * es menor que el argumento.
	 * Se compara primero por numero de termino y en caso de igualdad por numero
	 * de documento.
	 * 
	 * @param nRS objeto argumento
	 *            
	 * @return 1= this mayor | 0=iguales | -1=this menor
	 */
	public int compareTo(NodoRS nRS) {

		int result = this.idTermino.compareTo(nRS.idTermino);

		switch (result) {
		case -1:
			return -1;

		case 0:
			return this.idDocumento.compareTo(nRS.idDocumento);

		case 1:
			return 1;

		default: 
			return 2;
		}
	}

}