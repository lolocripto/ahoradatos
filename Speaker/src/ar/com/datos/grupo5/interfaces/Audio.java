package ar.com.datos.grupo5.interfaces;

import javax.sound.sampled.AudioFileFormat;
import java.io.OutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
/**
 * Interfaz para poder realizar las grabaciones y repoducciones de audio
 * @author xxvkue
 *
 */
public interface Audio {

	/**
	 * 
	 * @param targetType
	 * @param output
	 */
	void grabar(AudioFileFormat.Type targetType, OutputStream output);
	
	/**
	 * 
	 * @param input
	 */
	void reproducir(InputStream input);
}
