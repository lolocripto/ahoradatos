package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * 
 * @author xxvkue
 *
 */
public class RegistroDocumento implements Registro {

	/**
	 * Cuantos bytes puedo pasar.
	 */
	private long moreBytes;
	
	/**
	 * Longitud del contenido del documento.
	 */
	private int longContenido;
	
	/**
	 * Contenido del documento.
	 */
	private String contenido;
	
	/**
	 * Longitud del nombre del Documento.
	 */
	private int longNombreDocumento;

	/**
	 * Tiene el nombre del Documento.
	 */
	private String nombreDocumento;

	
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
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			
			int longDatosAdic = Constantes.SIZE_OF_INT * 2;
			int tamanioTotal = this.longContenido 
						+ longDatosAdic + this.longNombreDocumento;
			
			byte[] contenidoByte = this.contenido.getBytes();
			byte[] nombreDocumentoByte = this.nombreDocumento.getBytes();
			
			if (moreBytes == tamanioTotal) {
				
				byte[] longContenidoBytes = Conversiones.intToArrayByte(this.longContenido);
				byte[] longNombreDocumentoBytes = Conversiones.intToArrayByte(this.longNombreDocumento);
				byte[] longTotal = Conversiones.intToArrayByte(tamanioTotal);
				
				dos.write(longTotal, 0, longTotal.length);
				
				dos.write(longContenidoBytes, 0, longContenidoBytes.length);
				moreBytes -= longContenidoBytes.length;
				dos.write(contenidoByte, 0, contenidoByte.length);
				moreBytes -= contenidoByte.length;
				
				dos.write(longNombreDocumentoBytes, 0, longNombreDocumentoBytes.length);
				moreBytes -= longNombreDocumentoBytes.length;
				dos.write(nombreDocumentoByte, 0, nombreDocumentoByte.length);
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}

	/**
	 * @param datos the dato to set
	 */
	public final void setContenido(final String datos) {
	 	this.contenido = datos;
		this.longContenido = this.contenido.getBytes().length;
		this.moreBytes = (long) (this.longContenido 
				+ Constantes.SIZE_OF_INT * 2 + this.longNombreDocumento); 
	}
	
	/**
	 * @param datos the dato to set
	 */
	public final void setNombreDocumento(final String datos) {
	 	this.nombreDocumento = datos;
		this.longNombreDocumento = this.nombreDocumento.getBytes().length;
		this.moreBytes = (long) (this.longContenido 
				+ Constantes.SIZE_OF_INT * 2 + this.longNombreDocumento); 
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#getLongDatos()
	 * @return Devuelve la longitud del dato almacenado.
	 */
	public final long getLongContenido() {
		return longContenido;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#getLongDatos()
	 * @return Devuelve la longitud del dato almacenado.
	 */
	public final long getLongNombreDocumento() {
		return longNombreDocumento;
	}
		
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 */
	public final void setBytesContenido(final byte[] buffer) {

		this.longContenido = buffer.length;
		contenido = new String(buffer);
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 */
	public final void setBytesNombreDocumento(final byte[] buffer) {

		this.longNombreDocumento = buffer.length;
		nombreDocumento = new String(buffer);
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
	public final void setBytes(final byte[] buffer, final Long offset) {
		setBytesNombreDocumento(buffer);
		setBytesContenido(buffer);
	}
	
}
