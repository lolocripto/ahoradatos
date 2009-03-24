package ar.com.datos.grupo5;

import java.util.*;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.grupo5.interfaces.Audio;
import ar.com.datos.reproduccionaudio.core.SimpleAudioPlayer;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;
import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import javax.sound.sampled.AudioFileFormat;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;

/**
 * @author xxvkue
 *
 */
public class Core {

	/** Atributos de la Clase.
	 *  
	 */
	private String palabraActual;

	/**
	 * 
	 */
	private ITextInput parser;
	
	/**
	 * 
	 */
	private Audio manipularAudio;
	
	private OutputStream oStream;
	/**
	 * 
	 */
	private Collection<IunidadDeHabla>  contenedor; 
	/* Hilo para la parte de grabacion */

	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	/**
	 * 
	 */
	private InterfazUsuario interfazConUsuarios;
	
	/**
	 * 
	 * @param pathDocumento Agrergar comentario.
	 * @return lo mismo.
	 */
	public final String load(final String pathDocumento) {
		
		Iterator iterador;
		// Mando a parsear el documento y obtengo un collection
		contenedor = this.parser.archivoDeCarga(pathDocumento);
		
		System.out.println(pathDocumento);
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		System.out.println("Voy a iterar");
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			System.out.println("Entre en el while");
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
			if (true==true) continue;
			
			// Si no lo encontro pido ingresar el audio
			String mensaje = new String("Para ingresar el audio para la palabra: ");
			String respuesta = "0";
			this.interfazConUsuarios.mensaje(mensaje);
			
			//pido que grabe hasta que sea correcta la grabación
			while (respuesta.compareToIgnoreCase("S") != 0) {
				
				// Protocolo de Grabacion
				this.iniciarGrabacion();
				
				// Protocolo para terminar la grabacion
				this.finalizarGrabacion();
				
			    //this.playWord(this.oStream);
			    
			    mensaje = "La grabación ha sido correcta? S/N:";
			    respuesta = this.interfazConUsuarios.obtenerDatos(mensaje);
			    }
			 
			//
		}
		System.out.println("Sali de al funcion load");
		return "";
	}

	/**
	 * 
	 * @return coemntar
	 */
	public final int i() {
		return 0;
	}

	/**
	 * 
	 * @return comentar.
	 */
	public final int f() {
		return 0;
	}

	private int iniciarGrabacion(){
		String mensaje;
		String respuesta;
		
		mensaje = "Para empezar la grabación ingrese la tecla i y luego enter";
		respuesta = this.interfazConUsuarios.obtenerDatos(mensaje);
		
		while (respuesta.compareToIgnoreCase("i") != 0 && respuesta.compareToIgnoreCase("c") != 0) {
			respuesta = this.interfazConUsuarios.obtenerDatos("Comando incorrecto. Ingrese I para grabar.");
		}
		
		if (respuesta.compareToIgnoreCase("i") == 0){
			// Pido grabar el audio 
			this.manipularAudio.grabar(AudioFileFormat.Type.AU, oStream);
			return 0;
		} else {
			//Detengo la ejecucion del programa y dejo la consola activa
			return 1;
		}
	}
	
	private int finalizarGrabacion() {
		String mensaje;
		String respuesta;
		
		mensaje = "Para detener la grabación ingrese la tecla f y luego enter";
		respuesta = this.interfazConUsuarios.obtenerDatos(mensaje);
		
		while (respuesta.compareToIgnoreCase("f") != 0 && respuesta.compareToIgnoreCase("c") != 0) {
			respuesta = this.interfazConUsuarios.obtenerDatos("Comando incorrecto. Ingrese f para terminar.");
		}
		
		if (respuesta.compareToIgnoreCase("i") == 0) {
			//Termino la grabacion
			this.manipularAudio.terminargrabacion();
			return 0;
		} else {
			//Detengo la ejecucion del programa y dejo la consola activa
			return 1;
	    }
	}
	/**
	 * 
	 * @param pathDocumento comentar
	 * @return comentar.
	 */
	public final int playDocument(final String pathDocumento) {
		return 0;
	}

	/**
	 * 
	 * @param textoAReproducir comentar
	 * @return comentar
	 */
	public final int playText(final String textoAReproducir) {
		return 0;
	}

	/**
	 * @param audio
	 * @return
	 */
	public final int playWord(final InputStream audio) {
		this.manipularAudio.reproducir(audio);
		return 0;
	}
	
	/**.
	 * Puta funcion para probar
	 * @param consola
	 * @return
	 */
	public final String test(final InterfazUsuario consola) {
		
		System.out.println("Funciono la invocacion al metodo");
		
		this.interfazConUsuarios.mensaje("Este mensaje esta escrito desde el objeto que invoca a la consola.");
		
		String nombre = this.interfazConUsuarios.obtenerDatos("Ingrese su nombre: ");
		
		System.out.println("Recibi esto: " + nombre);
		return "Todo OK";
	}

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(final String[] args) {
		
		//Creo la consola y le paso la clase que ejecuta los metodos.
		Consola consola = new Consola(Core.class);
		
		//Me quedo leyendo la entrada.
		consola.leer();
		
	}
}
