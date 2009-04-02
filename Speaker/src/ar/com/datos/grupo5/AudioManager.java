/**
 * 
 */
package ar.com.datos.grupo5;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;

import org.apache.log4j.Logger;

import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;
import ar.com.datos.capturaaudio.exception.SimpleAudioRecorderException;
import ar.com.datos.grupo5.interfaces.Audio;
import ar.com.datos.reproduccionaudio.core.SimpleAudioPlayer;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;

/**
 * @author cristian
 *
 */
public class AudioManager implements Audio {

	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(AudioManager.class);
	
	/**
	 * Objeto para reproducir Audio.
	 */
	private SimpleAudioPlayer player;
	
	/**
	 * Objeto para grabar Audio.
	 */
	private SimpleAudioRecorder recorder;
	
	/**
	 * Ultima grabacion que se realizó.
	 */
	private OutputStream lastOutput;
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#getAudio()
	 */
	public final OutputStream getAudio() {
		
		return recorder.getOutput();
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#grabar(javax.sound.sampled.AudioFileFormat.Type,
	 *      java.io.OutputStream)
	 */
	public final void grabar(OutputStream output) {

		lastOutput = output;

		try {

			recorder = new SimpleAudioRecorder(AudioFileFormat.Type.AU,
					lastOutput);

			recorder.init();

			recorder.startRecording();

		} catch (SimpleAudioRecorderException e) {
			logger.error("Error al grabar: " + e.getMessage());
			e.printStackTrace();
		}

		logger.info("Grabando...");
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#reproducir()
	 */
	public final void reproducir() {
		
		this.reproducir(null);

		esperarFin();
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#reproducir(java.io.InputStream)
	 */
	public final void reproducir(final InputStream audio) {
		
		InputStream is = null;
		
		if (audio == null) {
			is = new ByteArrayInputStream(
				((ByteArrayOutputStream) lastOutput).toByteArray());
		} else {
			is = audio;
		}

		BufferedInputStream buffer = new BufferedInputStream(is);
		
		player = new SimpleAudioPlayer(buffer);
		
		try {
			
			player.init();
			
			player.startPlaying();
			
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error en la reproduccion: " + e.getMessage(), e);
		}
		try {
			player.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Reproduciendo...");
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#terminarGrabacion()
	 */
	public final void terminarGrabacion() {
		
		logger.info("Terminando la grabacion.");
		
		recorder.stopRecording();

	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Audio#terminarReproduccion()
	 */
	public final void terminarReproduccion() {
		
		logger.info("Terminando reproduccion.");
		
		player.stopPlaying();

	}
	
	/**
	 * Para esperar el fin de la reproduccion.
	 */
	public final void esperarFin() {
		
		try {
			player.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
