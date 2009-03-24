package ar.com.datos.grupo5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;

import org.apache.log4j.Logger;

import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;
import ar.com.datos.reproduccionaudio.core.SimpleAudioPlayer;

/**
 * Esta clase es de ejemplo.
 */
public class main {

	/**
	 * El constructor lo agrego para que checstyle no me rompa las bolas.
	 */
	public main() {
		super();
	}
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(main.class);

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(String[] args) {
		
		//Creo la consola y le paso la clase que ejecuta los metodos.
		//Consola consola = new Consola(Ejecutador.class);
		
		//Me quedo leyendo la entrada.
		//consola.leer();
		
		try {
			File file = new File("/home/cristian/Desktop/audio.au");
			OutputStream oStream = new FileOutputStream(file);
			
			SimpleAudioRecorder rec = new SimpleAudioRecorder(AudioFileFormat.Type.AU, oStream);
			
			rec.init();
			
			logger.debug("Grabando...");
			
			rec.startRecording();
			
			Thread.sleep(5000);
			
			rec.stopRecording();
			
			oStream.flush();
			oStream.close();
			
			logger.debug("Terminando de grabar");
			
			logger.debug("reproduciendo....");
			
			InputStream input = new FileInputStream(file);
			
			SimpleAudioPlayer player = new SimpleAudioPlayer(input);
			
			player.init();
			
			player.startPlaying();
			
			Thread.sleep(5000);
			
			player.stopPlaying();
			
			logger.debug("Terminó");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
