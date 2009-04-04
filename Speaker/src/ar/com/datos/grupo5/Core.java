package ar.com.datos.grupo5;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

import com.sun.management.GarbageCollectorMXBean;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.parser.TextInterpreter;


/**
 * @author LedZeppeling
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
	private AudioManager audioManager;
	
	/**
	 * 
	 */
	private AudioFileManager audioFileManager;
	
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
	 *            Dirección donde se encuentra el archivo a ser examinado.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String load(final InterfazUsuario invocador,
			final String pathDocumento) {
		
		logger.debug("Entre en load");
		
		Iterator<IunidadDeHabla> iterador;

		// Cargo el parser con el documento en modo aprendizaje
		try {
			contenedor = this.parser.modoCarga(pathDocumento, true);
		} catch (Exception e) {
			logger.error("Error al crear contenedor: " + e.getMessage(), e);
			return "Error inesperado, consulte al proveedor del software";
		}
		
		logger.debug("tengo el contenedor de palabras.");
	
		Long offsetRegistroAudio;
		
		if (!abrirArchivo(invocador)) {
			return "Intente denuevo";
		}
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
	
			elemento = iterador.next();
			logger.debug("Itero una vez.txt.");
			
			/* Si es una palabra pronunciable la 
			 * proxima palabra, sino pido el audio para la misma
			 */
			if (elemento.esPronunciable()) {
				logger.debug("Es pronunciable.");
				/* Si, es pronunciable, si la encuntra sigo con la 
				 * proxima palabra, sino pido el audio para la misma
				 */
				if (this.diccionario.buscarPalabra(elemento.getEquivalenteFonetico()) 
						!= null) {
					logger.debug("existe en el archivo de texto.");
					continue;
				}
				logger.debug("No esta en el archivo de texto.");
			} else {
				continue;
			}
			
			// Si no encontro la palabra pido ingresar el audio
			String mensaje = new String(
					"Para ingresar el audio para la palabra: "
							+ elemento.getTextoEscrito());

			String respuesta = "0";
			invocador.mensaje(mensaje);

			int resultado;
			//pido que grabe hasta que sea correcta la grabación
			while (!respuesta.equalsIgnoreCase("S")) {
				
				// Protocolo de Grabación
				resultado = this.iniciarGrabacion(invocador);
				// Segun el resultado
				switch(resultado) {
					case -1:
						return "Operacion cancelada.";
					case -2:
						continue;
					default:
				}
				
				// Protocolo para terminar la grabación
				resultado = this.finalizarGrabacion(invocador);
				
				//Segun el resultado
				switch(resultado) {
					case -1:
						return "Operacion cancelada.";
					default:
				}
				
				this.playWord();
			    
			    mensaje = "La grabación ha sido correcta? S/N: ";
			    respuesta = invocador.obtenerDatos(mensaje);
			    }

			offsetRegistroAudio = this.audioFileManager
					.agregar(this.audioManager.getAudio());
			
			//Agrego la palabra al diccionario 
			this.diccionario.agregar(elemento.getEquivalenteFonetico(),
					offsetRegistroAudio);
			
		}
		cerrarArchivo(invocador);
		
		logger.debug("Sali de al funcion load");
		return "ff";
	}

	/**
	 * 
	 * @param invocador .
	 */
	public final void help(final InterfazUsuario invocador) {
		
		String clear = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n";
		String mensaje = "Funcion: Load \n"
	+ "Caracteristicas: carga un documento para almacenar las palabras "
	+ "desconocidas \n"
	+ "Uso: load <\"path_absoluto_del_documento\"> \n"
	+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n\n"

	+ "Funcion: playDocument \n"
	+ "Caracteristicas: carga un documento reproduciendo las "
			+ "palabras reconocidas \n"
	+ "Uso: playDocument <\"path_absoluto_del_documento\"> \n"
	+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n\n"
	
	+ "Funcion: help \n"
	+ "Caracteristicas: muestra los comandos disponibles para su ejecución \n"
	+ "Uso: help \n"
	+ "Ej: help \n\n" 
	
	+ "Funcion: playText \n"
	+ "Caracteristicas: reproduce el texto ingresado, omitiendo las "
			+ "palabras que no conoce \n"
	+ "Uso: playText <\"texto ingresado\"> \n"
	+ "Ej: playText \"hola, como estas\" \n\n";
		
		invocador.mensaje(clear + mensaje);
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
	
		mensaje = "Para iniciar la grabación ingrese la tecla i "
				+ "y luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("i")
				&& !respuesta.equalsIgnoreCase("c")) {
			mensaje = "Comando incorrecto, por favor presione la tecla i.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		
		if (respuesta.equalsIgnoreCase("c")) {
			return -1;
		}
		
		OutputStream byteArray = new ByteArrayOutputStream();
		
		try {
			// Pido grabar el audio
			this.audioManager.grabar(byteArray);
			return 0;
		} catch (Exception e) {
			logger
				.error("Error, no se pudo grabar el audio intentelo nuevamente."
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

		mensaje = "Para detener la grabación ingrese la tecla f y "
				+ "luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("f")
				&& !respuesta.equalsIgnoreCase("c")) {
			
			mensaje = "Comando incorrecto, por favor presione la tecla f.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		//Termino la grabacion
		this.audioManager.terminarGrabacion();
		if (respuesta.equalsIgnoreCase("f")) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * Reproduce un documento entero.
	 * @param invocador .
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String playDocument(final InterfazUsuario invocador,
			final String pathDocumento) {
		
		Iterator<IunidadDeHabla> iterador;
		
		// Mando a parsear el documento y obtengo un collection
		try {
			contenedor = this.parser.modoLectura(pathDocumento, true);
		} catch (Exception e) {
			logger.error("Error al crear contenedor: " + e.getMessage(), e);
			return "Error inesperado, consulte al proveedor del software";
		}
		
		if (!abrirArchivo(invocador)) {
			return "Intente denuevo";
		}
		
		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		// Mientras tenga palabras para verificar consulto
		while (iterador.hasNext()) {
			
			elemento = (IunidadDeHabla) iterador.next();
			
			// Si lo encontro sigo en el bucle
			//RegistroDiccionario registro = this.diccionario
//					.buscarPalabra(elemento.getEquivalenteFonetico());
			
			RegistroDiccionario registro = this.diccionario
				.buscarPalabra(elemento.getTextoEscrito());
			if (registro == null) {
				continue;
			}
			
			// Si lo encontro busco el audio
			
			//TODO: Leer el registro, obtener el offset y buscar el audio
							
			playWord(this.audioFileManager.leerAudio(registro.getOffset()));
			
			audioManager.esperarFin();

		}
		logger.debug("Sali de al funcion playDocument");

		cerrarArchivo(invocador);
			
		return "Reproduccion finalizada";
	}

	/**
	 * Reproduce un texto introducido palabra por palabra.
	 * 
	 * @param textoAReproducir
	 *            Las palabras a leer.
	 * @param invocador .
	 */
	public final void playText(final InterfazUsuario invocador,
			final String textoAReproducir) {

		
	try { 	
		
		Iterator<IunidadDeHabla> iterador;
		// Mando a parsear el documento y obtengo un collection
		try {

			contenedor = this.parser.modoLectura(textoAReproducir, false);
		} catch (Exception e) {
			logger.error("Error al crear contenedor: " + e.getMessage(), e);
		}
		
		
		if (!abrirArchivo(invocador)) {
			invocador.mensaje("no eexisten los archivos de "
					+ "diccionario o datos ");
		}

		IunidadDeHabla elemento;
		iterador = contenedor.iterator();
		
		
			while (iterador.hasNext()) {
				
				elemento = iterador.next();
				RegistroDiccionario registro = this.diccionario
					.buscarPalabra(elemento.getTextoEscrito());
				if (registro == null) {
					continue;
				}
						
				playWord(this.audioFileManager.leerAudio(registro.getOffset()));
				audioManager.esperarFin();
			}
			logger.debug("Sali de al funcion playText");
		} catch (Exception e) {
			logger.debug("Error: " + e.getMessage(), e);
	}
	}

	/**
	 * Reproduce la última palabra leida.
	 */
	public final void playWord() {
		this.audioManager.reproducir();
	}

	/**
	 * Reproduce el audio que recibe.
	 * @param audioAReproducir es el audio que se va a reproducir.
	 */
	public final void playWord(final InputStream audioAReproducir) {
		this.audioManager.reproducir(audioAReproducir);
	}
	
	/**
	 * Constructor de la clase.
	 */
	public Core() {
		this.diccionario = new Diccionario();
		this.audioManager = new AudioManager();
		this.parser = new TextInterpreter();
		this.audioFileManager = new AudioFileManager();
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
	
	public final void quit (final InterfazUsuario invocador){
	//	this.cerrarArchivo(invocador);
		invocador.mensaje("gracias por usar TheSpeaker");
	}
	
	
	private boolean cerrarArchivo(InterfazUsuario invocador) {
		try {
			if (this.diccionario != null) this.diccionario.cerrar();
			if (this.audioFileManager != null)this.audioFileManager.cerrar();
			return true;
			
		} catch (Exception e) {
			try{
				if (this.audioFileManager != null)this.audioFileManager.cerrar();
			}catch (Exception g){
				invocador.mensaje("Error al cerrar el archivo de audio.");
			}
			invocador.mensaje("Error al cerrar el archivo de diccionario.");
			return false;
		}		
	}
	private boolean abrirArchivo(InterfazUsuario invocador) {
		
		/*
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		try {
			this.diccionario.abrir(Constantes.ARCHIVO_DICCIONARIO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			invocador.mensaje("No se pudo abrir el diccionario.");
			return false;
		}		
		
		logger.debug("Abrio el test.txt.");
		
		/*
		 * Abro el archivo para la carga y consulta de los audios
		 */
		try {
			this.audioFileManager.abrir(Constantes.ARCHIVO_AUDIO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			invocador.mensaje("No se pudo abrir el archivo de audio.");
			return false;
		}
		
		logger.debug("Abrio el testAudio.txt.");
		return true;
	}
	
}
