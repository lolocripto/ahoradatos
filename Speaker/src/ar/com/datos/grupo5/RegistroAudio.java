
package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	private Long moreBytes;
	private Long longDato;
	private DataOutputStream dato;
	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {		
		if (moreBytes>0)
			return true;
		return false;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {

		byte[] datosByte = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (moreBytes == (dato.size() + Constantes.SIZE_OF_LONG)) {
				dato.write(datosByte, 0, Constantes.TAMANIO_BUFFER_ESCRITURA);
				byte[] longDatoBytes = Conversiones.longToArrayByte(longDato);

				dos.write(longDatoBytes, 0, longDatoBytes.length);
				moreBytes -= longDatoBytes.length;
			} else {
				dato.write(datosByte, dato.size() + Constantes.SIZE_OF_LONG,
						Constantes.TAMANIO_BUFFER_ESCRITURA);
			}
			dos.write(datosByte, 0, datosByte.length);
			moreBytes -= Constantes.TAMANIO_BUFFER_ESCRITURA;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}

	/**
	 * @return the dato
	 */
	public DataOutputStream getDato() {
		return dato;
	}

	/**
	 * @param dato the dato to set
	 */
	public void setDato(DataOutputStream dato) {
	 	this.dato = dato;
		this.longDato = (long) dato.size();
		// Acá considero el offset (long).
		this.moreBytes = (long) (this.longDato + Constantes.SIZE_OF_LONG); 
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#getLongDatos()
	 * @return Devuelve la longitud del dato almacenado.
	 */
	public long getLongDatos() {
		return longDato;
	}
		
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
	public void setBytes(final byte[] buffer, final Long offset) {

		// this.setDato(buffer.);
		try {
			dato.write(buffer, 0, buffer.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
