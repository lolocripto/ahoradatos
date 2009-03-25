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

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

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
//			
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
//			SimpleAudioPlayer player = new SimpleAudioPlayer(is);
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
			
			
//			Archivo archivo = new Secuencial();
//			
//			archivo.crear("/home/cristian/Desktop/test.txt");
//			//archivo.abrir("/home/cristian/Desktop/test.txt",Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
//			
//			RegistroDiccionario registro = new RegistroDiccionario();
//			
//			registro.setDato("hola");
//			registro.setOffset(123L);
//			archivo.insertar(registro);
//			registro.setDato(" que tal");
//			registro.setOffset(124L);
//			archivo.insertar(registro);
//			
//			archivo.cerrar();
			
//			long a = 125L;
//			byte[] bytes = Constantes.longToArrayByte(a);
//			logger.debug(bytes);
//			long b = Constantes.arrayByteToLong(bytes);
//			logger.debug(b);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
