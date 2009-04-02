
package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Esta Clase implementa la interfaz del registro para audio.
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author Diego
 */
public class RegistroAudio implements Registro {

	/**
	 * Cuantos bytes puedo pasar.
	 */
	private long moreBytes;
	
	/**
	 * 
	 */
	private int longDato;
	
	/**
	 * 
	 */
	private ByteArrayOutputStream dato;

	
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
			
			int longDatosAdic = Constantes.SIZE_OF_INT;
			
			byte[] datosByte = dato.toByteArray();
			
			if (moreBytes == (dato.size() + longDatosAdic)) {
				byte[] longDatoBytes = Conversiones.intToArrayByte(longDato);

				dos.write(longDatoBytes, 0, longDatoBytes.length);
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
	 * @param dato the dato to set
	 */
	public final void setDato(final ByteArrayOutputStream dato) {
	 	this.dato = dato;
		this.longDato = dato.size();
		this.moreBytes = (long) (this.longDato + Constantes.SIZE_OF_INT); 
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#getLongDatos()
	 * @return Devuelve la longitud del dato almacenado.
	 */
	public final long getLongDatos() {
		return longDato;
	}
		
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
	public final void setBytes(final byte[] buffer, final Long offset) {

		dato.write(buffer, 0, buffer.length);
	}

}
