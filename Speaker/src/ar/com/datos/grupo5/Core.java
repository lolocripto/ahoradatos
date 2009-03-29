package ar.com.datos.grupo5;


import java.util.*;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.grupo5.interfaces.Audio;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.Diccionario;
import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.capturaaudio.exception.SimpleAudioRecorderException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import javax.sound.sampled.AudioFileFormat;
import org.apache.log4j.Logger;



/**
 * @author xxvkue
 *
 */
public class Core {


	/** Atributos de la Clase.
	 *  
	 */
	private ITextInput parser;
	
	/**
	 * Contiene todas las palabras conocidas por el sistema.
	 */
	private Diccionario diccionario;
	/**
	 * 
	 */
	private Archivo archivo;
	/**
	 * 
	 */
	private AudioManager manipularAudio;
	/**
	 * 
	 */
	private ByteArrayOutputStream oStream;
		
	/**
	 * 
	 */
	private Collection<IunidadDeHabla>  contenedor; 
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	
	/**
	 * 
	 * @param pathDocumento Agrergar comentario.
	 * @return lo mismo.
	 */
	public final String load(final InterfazUsuario invocador, final String pathDocumento) {
		
		Iterator iterador;
		// Mando a parsear el documento y obtengo un collection
		contenedor = this.parser.archivoDeCarga(pathDocumento);
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
			if (1 == 2) continue;
			
			// Si no lo encontro pido ingresar el audio
			String mensaje = new String("Para ingresar el audio para la palabra: "+elemento.toString());

			String respuesta = "0";
			invocador.mensaje(mensaje);
			
			//pido que grabe hasta que sea correcta la grabación
			while (!respuesta.equalsIgnoreCase("S")) {
				
				// Protocolo de Grabacion
				this.iniciarGrabacion(invocador);
				
				// Protocolo para terminar la grabacion
				this.finalizarGrabacion(invocador);
				
			    //this.playWord(this.oStream);
			    
			    mensaje = "La grabación ha sido correcta? S/N:";
			    respuesta = invocador.obtenerDatos(mensaje);
			    }
			 
			//
		}
		
		logger.debug("Sali de al funcion load");
		return "";
	}

	
	public final String loadTest(final InterfazUsuario invocador){
		
		String cadena = "Hola";
		String mensaje = new String("Para ingresar el audio para la palabra: "+cadena);
		String respuesta = "0";
		invocador.mensaje(mensaje);
				
			//pido que grabe hasta que sea correcta la grabación
			while (!respuesta.equalsIgnoreCase("S")) {
				
				// Protocolo de Grabacion
				this.iniciarGrabacion(invocador);
				
				// Protocolo para terminar la grabacion
				this.finalizarGrabacion(invocador);
				
			    this.playWord();
			    
			    mensaje = "La grabación ha sido correcta? S/N:";
			    respuesta = invocador.obtenerDatos(mensaje);
			    }
			 
			return respuesta;
	
	}
	
	/**
	 * 
	 * @param invocador
	 * @return
	 */
	private int iniciarGrabacion(final InterfazUsuario invocador){

		String mensaje;
		String respuesta;
	
		mensaje = "Para iniciar la grabación ingrese la tecla i y luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("i")) {
			mensaje ="Comando incorrecto, por favor presione la tecla i.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		OutputStream byteArray = new ByteArrayOutputStream();
		try {
			// Pido grabar el audio 
			this.manipularAudio.grabar(byteArray);
			//
		} catch (Exception e) {
			System.out.println("Error, no puedo entrar en la inter audio"+e.toString());
		}		
		return 0;
	}
	
	/**
	 * 
	 * @param invocador
	 * @return
	 */
	private int finalizarGrabacion(final InterfazUsuario invocador) {
		String mensaje,respuesta;

		mensaje = "Para detener la grabación ingrese la tecla f y luego enter";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("f")) {
			mensaje ="Comando incorrecto, por favor presione la tecla f.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		//Termino la grabacion
		this.manipularAudio.terminarGrabacion();
		return 0;
	}
	/**
	 * 
	 * @param pathDocumento comentar
	 * @return comentar.
	 */
	public final int playDocument(final OutputStream pathDocumento) {

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
	public final int playWord() {
		this.manipularAudio.reproducir();
		return 0;
	}
	
	/**.
	 * Puta funcion para probar
	 * @param consola
	 * @return
	 */
	public final String test(final InterfazUsuario consola) {
		
		System.out.println("Funciono la invocacion al metodo");
/*		
		this.interfazConUsuarios.mensaje("Este mensaje esta escrito desde el objeto que invoca a la consola.");
		
		String nombre = this.interfazConUsuarios.obtenerDatos("Ingrese su nombre: ");
	*/	
		logger.debug("Recibi esto:");
		return "Todo OK";
	}
	
	public Core(){
		archivo = new Secuencial();
		this.manipularAudio = new AudioManager();
		this.oStream = new ByteArrayOutputStream();
		this.diccionario = new Diccionario();
		try {
			archivo.crear("/home/xxvkue/Desktop/test.txt");
		} catch (Exception e) {
			// TODO: handle exception
		}		
		
	}

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(final String[] args) {
		
		Consola consola = new Consola(Core.class);
		
		consola.start();
		
		try {
			consola.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
