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
import ar.com.datos.parser.TextInterpreter;
import java.io.FileNotFoundException;
import java.io.IOException;


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
	 * 
	 */
	private RegistroDiccionario registro;
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
			
			this.registro = new RegistroDiccionario();
			this.registro.setDato(elemento.toString());
			
			// Si lo encontro sigo en el bucle
//			if (this.archivo.buscar(this.registro)) {
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
			    
			    mensaje = "La grabación ha sido correcta? S/N: ";
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
			    
			    mensaje = "La grabación ha sido correcta? S/N: ";
			    respuesta = invocador.obtenerDatos(mensaje);
			    }
			
			//TODO: Grabar audio en el archivo de audio
			
			// Genero el nuevo registro del diccionario
			RegistroDiccionario registro = new RegistroDiccionario();
			registro.setDato(cadena);
			registro.setOffset(123L);
return "";	
	}
	
	/**
	 * 
	 * @param invocador
	 * @return
	 */
	private void iniciarGrabacion(final InterfazUsuario invocador) {

		String mensaje;
		String respuesta;
	
		mensaje = "Para iniciar la grabación ingrese la tecla i " +
				"y luego enter: ";
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

		mensaje = "Para detener la grabación ingrese la tecla f y luego enter: ";
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

	/**
	 * .
	 */
	public Core() {
		this.diccionario = new Diccionario();
		this.manipularAudio = new AudioManager();
		this.oStream = new ByteArrayOutputStream();
		this.diccionario = new Diccionario();
		this.parser = new TextInterpreter();
		
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
