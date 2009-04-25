package ar.com.datos.grupo5.registros;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
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
	 * Contenido del documento.
	 */
	private String contenido;

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
			
			byte[] contenidoByte = this.contenido.getBytes();
			byte[] nombreDocumentoByte = this.nombreDocumento.getBytes();
			
			int longContenido = contenidoByte.length;
			int longNombreDocumento = nombreDocumentoByte.length;
			int longDatosAdic = Constantes.SIZE_OF_INT * 2;
			int tamanioTotal = longContenido 
						+ longDatosAdic + longNombreDocumento;
			
			if (moreBytes == tamanioTotal) {
				
				byte[] longContenidoBytes = Conversiones
						.intToArrayByte(longContenido);
				byte[] longNombreDocumentoBytes = Conversiones
						.intToArrayByte(longNombreDocumento);
				byte[] longTotal = Conversiones.intToArrayByte(tamanioTotal);
				
				dos.write(longTotal, 0, longTotal.length);
				
				dos.write(longContenidoBytes, 0, longContenidoBytes.length);
				moreBytes -= longContenidoBytes.length;
				dos.write(contenidoByte, 0, contenidoByte.length);
				moreBytes -= contenidoByte.length;
				
				dos.write(longNombreDocumentoBytes, 0,
						longNombreDocumentoBytes.length);
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
		this.moreBytes = (long) (this.contenido.getBytes().length
				+ Constantes.SIZE_OF_INT * 2 + this.nombreDocumento.getBytes().length); 
	}
	
	/**
	 * @param datos the dato to set
	 */
	public final void setNombreDocumento(final String datos) {
	 	this.nombreDocumento = datos;
		this.moreBytes = (long) (this.contenido.getBytes().length 
				+ Constantes.SIZE_OF_INT * 2 + this.nombreDocumento.getBytes().length); 
	}
		
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 */
	public final void setBytesContenido(final byte[] buffer) {

		contenido = new String(buffer);
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 */
	public final void setBytesNombreDocumento(final byte[] buffer) {

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
