
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.interfaces.Registro;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

/**
 * @author Diego
 *
 */
public class RegistroAudio implements Registro {

	private byte[] buffer;
	private long offset;
	private long longitud;
	private AudioFileFormat.Type dato;
	
	/* (non-Javadoc)
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
