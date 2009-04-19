
package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.grupo5.Constantes;

/**
 * Esta clase implementa el registro para el diccionario.
 * 
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class RegistroTerminoDocumentos implements Registro {
	
	/**
	 * Cuantos bytes puedo pasar.
	 */
	private Long moreBytes;
		
	private Collection<Long> listaOffset;
	
	private Collection<Long> frecuenciaPorDocumento;
	
	private int	cantidadDocumentos;
	
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
	 * Método que devuelve el offset.
	 * 
	 * @return El offset en el archivo de audio.
	 */
	public final Collection<Long> getlistaOffset() {
		return listaOffset;
	}

	/**
	 * Método para cargar el offset.
	 * 
	 * @param offset
	 *            El offset a cargar.
	 */
	public final void setListaOffset(final Collection<Long> offset) {
		this.listaOffset = offset;
		// Acá considero el tamaño (int) y el offset (long).
		this.moreBytes = (long) dato.getBytes().length + Constantes.SIZE_OF_INT
				+ Constantes.SIZE_OF_LONG;
	}

	/**
	 * Método para cargar el offset.
	 * 
	 * @param offset
	 *            El offset a cargar.
	 */
	public final void setFrecuenciaPorDocumento(final Collection<Long> frecuencias) {
		this.frecuenciaPorDocumento = frecuencias;
		// Acá considero el tamaño (int) y el offset (long).
		this.moreBytes = (long) dato.getBytes().length + Constantes.SIZE_OF_INT
				+ Constantes.SIZE_OF_LONG;
	}
	
	/**
	 * Método para devolver el dato.
	 * 
	 * @return El dato.
	 */
	public final Collection<Long> getFrecuenciaPorDocumento() {
		return this.frecuenciaPorDocumento;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
/*	
	public final void setBytes(final byte[] buffer, final Long offset) {
		this.setFrecuenciaPorDocumento(offset);
		this.setListaOffset(new String(buffer));
	}
*/	
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			int longDatosAdic = Constantes.SIZE_OF_INT
					+ Constantes.SIZE_OF_LONG;
			byte[] datosByte = dato.getBytes();
			
			if (moreBytes == (datosByte.length + longDatosAdic)) {
				byte[] longDatoBytes = Conversiones
						.intToArrayByte(datosByte.length);
				byte[] offsetBytes = Conversiones.longToArrayByte(offset);

				dos.write(offsetBytes, 0, offsetBytes.length);
				dos.write(longDatoBytes, 0, longDatoBytes.length);
				moreBytes -= offsetBytes.length;
				moreBytes -= longDatoBytes.length;
			}
			dos.write(datosByte, 0, datosByte.length);
			moreBytes -= datosByte.length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena el registro con la información del buffer.
	 */
	public void llenar() {
		// TODO Llenar este método
	}

	 

}
