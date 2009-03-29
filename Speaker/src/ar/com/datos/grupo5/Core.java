package ar.com.datos.grupo5;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.interfaces.Archivo;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.parser.ITextInput;



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
	 * @param invocador .
	 * @param pathDocumento Agrergar comentario.
	 * @return lo mismo.
	 */
	public final String load(final InterfazUsuario invocador,
			final String pathDocumento) {
		
		Iterator iterador;
		// Mando a parsear el documento y obtengo un collection
		contenedor = this.parser.archivoDeCarga(pathDocumento);
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
			if (1 == 2) {
				continue;
			}
			
			// Si no lo encontro pido ingresar el audio
			String mensaje = new String(
					"Para ingresar el audio para la palabra: "
							+ elemento.toString());

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
		}
		
		logger.debug("Sali de al funcion load");
		return "";
	}

	/**
	 * Para test.
	 * @param invocador
	 * @return
	 */
	public final String loadTest(final InterfazUsuario invocador){
		
		String cadena = "Hola";
		String mensaje = "Para ingresar el audio para la palabra: " + cadena;
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
	private void iniciarGrabacion(final InterfazUsuario invocador){

		String mensaje;
		String respuesta;
	
		mensaje = "Para iniciar la grabación ingrese la tecla i y luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("i")) {
			mensaje = "Comando incorrecto, por favor presione la tecla i.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		OutputStream byteArray = new ByteArrayOutputStream();
		try {
			// Pido grabar el audio 
			this.manipularAudio.grabar(byteArray);
		} catch (Exception e) {
			logger.error("Error, no puedo entrar en la inter audio"
					+ e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param invocador .
	 * @return ??
	 */
	private int finalizarGrabacion(final InterfazUsuario invocador) {
		
		String mensaje, respuesta;

		mensaje = "Para detener la grabación ingrese la tecla f y luego enter";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("f")) {
			mensaje = "Comando incorrecto, por favor presione la tecla f.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		//Termino la grabacion
		this.manipularAudio.terminarGrabacion();
		return 0;
	}
	
	/**
	 * 
	 * @param pathDocumento comentar
	 */
	public final void playDocument(final OutputStream pathDocumento) {
		//TODO implementar.
	}

	/**
	 * 
	 * @param textoAReproducir comentar
	 */
	public final void playText(final String textoAReproducir) {
		//TODO Implementar.
	}

	/**
	 * @param audio
	 */
	public final void playWord() {
		this.manipularAudio.reproducir();
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
	
	/**
	 * .
	 */
	public Core() {
		archivo = new Secuencial();
		this.manipularAudio = new AudioManager();
		this.oStream = new ByteArrayOutputStream();
		this.diccionario = new Diccionario();
		try {
			archivo.crear("/home/xxvkue/Desktop/test.txt");
		} catch (Exception e) {
			logger.error(e.getMessage());
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
