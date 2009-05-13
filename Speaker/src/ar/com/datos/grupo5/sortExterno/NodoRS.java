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
	 * 0 - disponible 1 - congelado.
	 */
	private int flag;
	
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
		this.flag = 0;
		this.tamanio = Constantes.SIZE_OF_LONG * 2;
	}

	/**
	 *	@throws Throwable d. 
	 */
	public void finalize() throws Throwable {
		
	}

	/**
	 * 
	 * @param idT id del termino
	 * @param idD id del documento
	 */
	public NodoRS(final Long idT, final Long idD) {
		this.idTermino = idT;
		this.idDocumento = idD;
		this.flag = 0;
		this.tamanio = Constantes.SIZE_OF_LONG * 2;
	}

	/**
	 * Comparar es una funcion que devuelve: 0 si this = NRS 1 si this > NRS -1 si
	 * this < NRS Siempre y cuando tenga flag == 0 sino devuelve 2 (si esta mal).
	 * 
	 * @param nRS c
	 * @return 0
	 */
	public final int comparar(final NodoRS nRS) {
		if (this.flag == 1 && nRS.getFlag() == 0) return 1;
		if ((nRS.getFlag()==1)&&(this.flag==0)) return -1;
		if ((nRS.getFlag()==1)&&(this.flag==1)) return 0;
		if ((nRS.getFlag()==0)&&(this.flag==0)) {
		   int comp = this.idTermino.compareTo(nRS.getIdTermino());
		   int compi2;
		if (comp != 0) {
		//	System.out.println("it1="+this.getIdTermino()+" it2="+nRS.getIdTermino());
			   return comp;
		   }
		   else
			   compi2 = this.idDocumento.compareTo(nRS.getIdDocumento());
			  // System.out.println("compi2 dio "+compi2+" d1="+this.getIdDocumento()+" d2="+nRS.getIdDocumento());
			   return compi2;
		}
		
        return 2;
	}
	/**
	 * @param flagExt the flag to set
	 */
	public final void setFlag(final int flagExt) {
		this.flag = flagExt;
	}

	/**
	 * @return the flag
	 */
	public final int getFlag() {
		return flag;
	}

	/**
	 * @param idDocumentoExt the idDocumento to set
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
	 * @param idTerminoExt the idTermino to set
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
	 * @param buffer Datos leidos.
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
	 * @param tamanio the tamanio to set
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
	 * Comparar es una funcion que devuelve: 0 si this = NRS 1 si this > NRS -1 si
	 * this < NRS Siempre y cuando tenga flag == 0 sino devuelve 2 (si esta mal).
	 * 
	 * @param nRS c
	 * @return 0
	 */
	public int compareTo(NodoRS nRS) {
		if (this.flag == 1 && nRS.getFlag() == 0) return 1;
		if ((nRS.getFlag()==1)&&(this.flag==0)) return -1;
		if ((nRS.getFlag()==1)&&(this.flag==1)) return 0;
		if ((nRS.getFlag()==0)&&(this.flag==0)) {
		   int comp = this.idTermino.compareTo(nRS.getIdTermino());
		   int compi2;
		if (comp != 0) {
		//	System.out.println("it1="+this.getIdTermino()+" it2="+nRS.getIdTermino());
			   return comp;
		   }
		   else
			   compi2 = this.idDocumento.compareTo(nRS.getIdDocumento());
			  // System.out.println("compi2 dio "+compi2+" d1="+this.getIdDocumento()+" d2="+nRS.getIdDocumento());
			   return compi2;
		}
		
        return 2;
	}
	
}