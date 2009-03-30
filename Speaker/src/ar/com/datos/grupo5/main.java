package ar.com.datos.grupo5;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;

import org.apache.log4j.Logger;

import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;
import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.reproduccionaudio.core.SimpleAudioPlayer;

/**
 * Esta clase es de ejemplo.
 */
public class main {

	/**
	 * El constructor lo agrego para que checkstyle no me rompa las bolas.
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
		
		
		try {
			
			//Creo la consola y le paso la clase que ejecuta los metodos.
//			Consola consola = new Consola(Ejecutador.class);
//			
//			consola.start();
//			
//			consola.join();
			
			
//			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//
//			SimpleAudioRecorder rec = new SimpleAudioRecorder(
//					AudioFileFormat.Type.AU, byteArray);
//		
//			rec.init();
//			
//			logger.debug("Grabando...");
//			
//			rec.startRecording();
//			
//			Thread.sleep(5000);
//			
//			rec.stopRecording();
//			
//			logger.debug("Terminando de grabar");
//
//			InputStream is = new ByteArrayInputStream(
//					((ByteArrayOutputStream) rec.getOutput()).toByteArray());
//
//			File soundFile = new File("/home/cristian/Desktop/audio_real.au");
//			InputStream inf = new FileInputStream(soundFile);
//			BufferedInputStream b = new BufferedInputStream(inf);
//			
//			SimpleAudioPlayer player = new SimpleAudioPlayer(b);
//			
//			player.init();
//
//			logger.debug("reproduciendo....");
//			
//			player.startPlaying();
//			
//			Thread.sleep(5000);
//			
//			player.stopPlaying();
//			
//			logger.debug("Terminó");

			Archivo archivo = new Secuencial();
			
			archivo.crear("/home/cristian/Desktop/test.txt");
			
			RegistroDiccionario registro = new RegistroDiccionario();
			
			registro.setDato("hola");
			registro.setOffset(123L);
			archivo.insertar(registro);
			registro.setDato("que");
			registro.setOffset(124L);
			archivo.insertar(registro);
			registro.setDato("tal");
			registro.setOffset(124L);
			archivo.insertar(registro);

			archivo.cerrar();

			archivo.abrir("/home/cristian/Desktop/test.txt",
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			RegistroDiccionario reg = (RegistroDiccionario) archivo.primero();
			logger.debug("Dato [" + reg.getDato() + "] offset [" + reg.getOffset() + "]");
			
			reg = (RegistroDiccionario) archivo.siguiente();
			logger.debug("Dato [" + reg.getDato() + "] offset [" + reg.getOffset() + "]");
			
			reg = (RegistroDiccionario) archivo.siguiente();
			logger.debug("Dato [" + reg.getDato() + "] offset [" + reg.getOffset() + "]");
			
//			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//			
//			AudioManager manager = new AudioManager();
//			
//			manager.grabar(byteArray);
//			
//			Thread.sleep(6000);
//			
//			manager.terminarGrabacion();
//			
//			//Este reproducir no se puede parar, reproduce todo lo que hay.
//			manager.reproducir();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
