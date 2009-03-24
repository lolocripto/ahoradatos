package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;

import org.apache.log4j.Logger;

import sun.audio.AudioStream;

import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;
import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.grupo5.interfaces.Registro;
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
//			File file = new File("/home/cristian/Desktop/audio.au");
//			OutputStream oStream = new FileOutputStream(file);
//			
//			SimpleAudioRecorder rec = new SimpleAudioRecorder(AudioFileFormat.Type.AU, oStream);
//		
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
//			oStream.flush();
//			oStream.close();
//			
//			logger.debug("Terminando de grabar");
//			
//			File audio = new File("/home/cristian/Desktop/audio_real.au");         
//
//			InputStream input = new FileInputStream(audio);
//			
//			AudioStream as = new AudioStream(input);
//			
//			byte[] lectura = new byte[10000];
//			input.read(lectura, 0, 10000);
//			
//			SimpleAudioPlayer player = new SimpleAudioPlayer(new ByteArrayInputStream(lectura));
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
			
			//archivo.crear("/home/cristian/Desktop/test.txt");
			archivo.abrir("/home/cristian/Desktop/test.txt",Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			RegistroDiccionario registro = new RegistroDiccionario();
			
			registro.setDato("hola");
			
			archivo.insertar(registro);
			registro.setDato(" que tal");
			archivo.insertar(registro);
			
			archivo.cerrar();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
