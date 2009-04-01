package ar.com.datos.grupo5;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.parser.TextInterpreter;


/**
 * @author xxvkue
 *
 */
public class Core {


	/**
	 * Obtiene todas las posibles palabras a ser lidas. 
	 */
	private ITextInput parser;
	
	/**
	 * Contiene todas las palabras conocidas por el sistema.
	 */
	private Diccionario diccionario;
	/**
	 * Permite grabar y reproducir el audio correspondiente a un palabra.
	 */
	private AudioManager manipularAudio;
		
	/**
	 * Conteniene todas las palabras a grabar o a leer del documento ingresado.
	 */
	private Collection<IunidadDeHabla>  contenedor; 
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	
	/**
	 * Busca las palabras a grabar, graba el audio y lo guarda.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @param pathDocumento
	 *            Direccion donde se encuentra el archivo a ser examinado.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String load(final InterfazUsuario invocador,
			final String pathDocumento) {
		
		Iterator iterador;
		// Mando a parsear el documento y obtengo un collection
		contenedor = this.parser.modoCarga(pathDocumento, true);
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
//			if (this.archivo.buscar(this.registro)) {
			if (1 == 2) {
				continue;
			}
			
			// Si no lo encontro pido ingresar el audio
			String mensaje = new String(
					"Para ingresar el audio para la palabra: "
							+ elemento.getTextoEscrito());

			String respuesta = "0";
			invocador.mensaje(mensaje);
			
			//pido que grabe hasta que sea correcta la grabación
			while (!respuesta.equalsIgnoreCase("S")) {
				
				// Protocolo de Grabacion
				this.iniciarGrabacion(invocador);
				
				// Protocolo para terminar la grabacion
				this.finalizarGrabacion(invocador);
				
				this.playWord();
			    
			    mensaje = "La grabación ha sido correcta? S/N: ";
			    respuesta = invocador.obtenerDatos(mensaje);
			    }

		}
		logger.debug("Sali de al funcion load");
		return "";
	}

	/**
	 * Para test.
	 * 
	 * @param invocador
	 * @return
	 */
	public final String loadTest(final InterfazUsuario invocador) {

		String cadena = "Hola";
		String mensaje = "Para ingresar el audio para la palabra: " + cadena;
		String respuesta = "0";
		invocador.mensaje(mensaje);

		// pido que grabe hasta que sea correcta la grabación
		while (!respuesta.equalsIgnoreCase("S")) {

			// Protocolo de Grabacion
			this.iniciarGrabacion(invocador);

			// Protocolo para terminar la grabacion
			this.finalizarGrabacion(invocador);

			this.playWord();

			mensaje = "La grabación ha sido correcta? S/N: ";
			respuesta = invocador.obtenerDatos(mensaje);
		}

		// TODO: Grabar audio en el archivo de audio

		// Genero el nuevo registro del diccionario
		RegistroDiccionario registro = new RegistroDiccionario();
		registro.setDato(cadena);
		registro.setOffset(123L);
		return "";
	}
	
	/**
	 * Da comienzo a la grabación del audio de la palabra en cuestión.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @return 0 si la grabación termino bien, -1 si la grabación fue cancelada.
	 */
	private int iniciarGrabacion(final InterfazUsuario invocador) {

		String mensaje;
		String respuesta;
	
		mensaje = "Para iniciar la grabación ingrese la tecla i " +
				"y luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("i") && !respuesta.equalsIgnoreCase("c")) {
			mensaje = "Comando incorrecto, por favor presione la tecla i.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		
		if (respuesta.equalsIgnoreCase("c")) {
			return -1;
		}
		OutputStream byteArray = new ByteArrayOutputStream();
		try {
			// Pido grabar el audio 
			this.manipularAudio.grabar(byteArray);
			return 0;
		} catch (Exception e) {
			logger.error("Error, no puedo entrar en la inter audio"
					+ e.getMessage());
			return -2;
		}
		
	}
	
	/**
	 * Termina la grabación del audio, tanto con un final correcto como por una
	 * cancelación del usuario.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @return 0 si la grabación termino bien, -1 si la grabación fue cancelada.
	 */
	private int finalizarGrabacion(final InterfazUsuario invocador) {
		
		String mensaje, respuesta;

		mensaje = "Para detener la grabación ingrese la tecla f y " +
				"luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("f") && !respuesta.equalsIgnoreCase("c")) {
			mensaje = "Comando incorrecto, por favor presione la tecla f.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		//Termino la grabacion
		this.manipularAudio.terminarGrabacion();
		if (respuesta.equalsIgnoreCase("f")) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * Reproduce un documento entero.
	 * 
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String playDocument(final String pathDocumento) {
		//TODO implementar.
		Iterator iterador;
		// Mando a parsear el documento y obtengo un collection
		contenedor = this.parser.modoLectura(pathDocumento, true);
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		InputStream audioAReproducir;
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
			RegistroDiccionario registro = 
				this.diccionario.buscarPalabra(elemento.getEquivalenteFonetico());
			
			if (registro.getOffset() == 0L) {
				continue;
			}
			
			// Si lo encontro busco el audio
			
			//TODO: Leer el registro, obtener el offset y buscar el audio
							
			//this.playWord(audioAReproducir);

		}
		logger.debug("Sali de al funcion playDocument");
		return "Reproduccion finalizada";
	}

	/**
	 * Reproduce un texto introducido palabra por palabra.
	 * @param textoAReproducir Las palabras a leer.
	 */
	public final void playText(final String textoAReproducir) {
		//TODO Implementar.
	}

	/**
	 * Reproduce la última palabra leida.
	 */
	public final void playWord() {
		this.manipularAudio.reproducir();
	}

	/**
	 * Reproduce el audio que recibe.
	 * @param audioAReproducir es el audio que se va a reproducir.
	 */
	public final void playWord(final InputStream audioAReproducir) {
		this.manipularAudio.reproducir(audioAReproducir);
	}
	
	/**
	 * Constructor de la clase.
	 */
	public Core() {
		this.diccionario = new Diccionario();
		this.manipularAudio = new AudioManager();
		this.diccionario = new Diccionario();
		this.parser = new TextInterpreter();
	}

	/** Inicia el programa.
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
