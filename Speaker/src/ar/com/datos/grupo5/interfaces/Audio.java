package ar.com.datos.grupo5.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;

/**
 * Interfaz para poder realizar las grabaciones y repoducciones de audio.
 * @author xxvkue
 *
 */
public interface Audio {

	/**
	 * 
	 * @param targetType .
	 * @param output .
	 */
	void grabar(OutputStream output);

	
	/**
	 * 
	 */
	void terminarGrabacion();
	
	/**
	 * Reproduce lo ulimo que se grabó.
	 * @param input .
	 */
	void reproducir();
	
	/**
	 * Reproduce un Stram.
	 * @param input .
	 */
	void reproducir(InputStream audio);
	
	/**
	 * Termina la reproduccion de un audio.
	 */
	void terminarReproduccion();
	
	/**
	 * 
	 * @return El outputstream para obtener los datos grabados.
	 */
	OutputStream getAudio();
}
