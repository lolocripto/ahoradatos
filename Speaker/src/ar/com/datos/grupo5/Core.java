package ar.com.datos.grupo5;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.UnidadesDeExpresion.Palabra;
import ar.com.datos.grupo5.compresion.CompresorConsola;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.grupo5.parser.ITextInput;
import ar.com.datos.grupo5.parser.PalabrasFactory;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.grupo5.utils.MetodoCompresion;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;


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

	private FTRSManager ftrsManager;
	
	private ArrayList<SimilitudDocumento> ranking;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	
	private long tiempoConsulta;

	private String metodo;
	
	
	/** 
	 * Permite al usuario agregar un documento sin especificar el compresor a utilizar
	 * utilizando el compresor por defecto definido en la configuracion. La carga del 
	 * documento se realizará sin la carga de audio.
	 * @param invocador Comunicación con el usuario
	 * @param pathDocumento Dirección del archivo que se leerá
	 * @return String con mensaje del resultado final.
	 */
	public final String loadWithoutAudio(final InterfazUsuario invocador,
			final String pathDocumento) {
		return this.loadWithoutAudio(invocador, pathDocumento, this.metodo);
	}
	
	/** 
	 * Permite al usuario agregar un documento utilizando el compresor definido por el usuario. 
	 * La carga del documento se realizará sin la carga de audio.
	 * @param invocador Comunicación con el usuario
	 * @param pathDocumento Dirección del archivo que se leerá
	 * @param metodoExt Método de compresión elegido por el usuario.
	 * @return String con mensaje del resultado final.
	 */
	public final String loadWithoutAudio(final InterfazUsuario invocador,
			final String pathDocumento, final String metodoExt) {
		logger.debug("Entre en loadWithAudio");
		
		
		MetodoCompresion met = Conversiones.metodoDeCompresion(metodoExt);
		
		if (met == null){
			DocumentsManager.getInstance().setTipoCompresion(this.metodo.toUpperCase());
			invocador.mensaje("Método de compresión no se reconoce, se utilizará: " + this.metodo);
		} else {
			DocumentsManager.getInstance().setTipoCompresion(metodoExt.toUpperCase());
		}
		
		try {
			Iterator<IunidadDeHabla> iterador;
			// Cargo el parser con el documento en modo aprendizaje
			try {
				//Inicio la grabación del documento.
				contenedor = this.parser.modolecturaYalmacenamiento(pathDocumento);
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			} 
			/*catch (FileNotFoundException e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "El archivo solicitado no existe.";
			}
			*/
			logger.debug("tengo el contenedor de palabras.");
			
			if (!abrirArchivo(invocador)) {
				return "Intente denuevo";
			}
						
			Long offsetDoc = DocumentsManager.getInstance().getOffsetUltDoc();
		
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {

				elemento = iterador.next();
		//		logger.debug("Termino: " + elemento.getTextoEscrito());
				this.logger.debug(elemento.getTextoEscrito());
				// Si no es StopWord entonces utilizo el Ftrs.
				if (!elemento.isStopWord()) {
					ftrsManager.validarTermino(elemento
							.getTextoEscrito(), offsetDoc);
				}

			}
			DocumentsManager.getInstance().finalizaEscritura();
			this.ftrsManager.generarListasInvertidas();

			//cerrarArchivo(invocador);
		}  catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			
			return "Error inesperado";
		}
		
		logger.debug("Sali de al funcion load");
		return "Las palabras han sido correctamente ingresadas.";
	}
	
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
			final String pathDocumento, final String metodoExt) {
		
		logger.debug("Entre en load");
		
		MetodoCompresion met = Conversiones.metodoDeCompresion(metodoExt);
		
		if (met == null){
			DocumentsManager.getInstance().setTipoCompresion(this.metodo.toUpperCase());
			invocador.mensaje("Método de compresión no se reconoce, se utilizará: " + this.metodo);
		} else {
			DocumentsManager.getInstance().setTipoCompresion(metodoExt.toUpperCase());	
		}
		try {
			Iterator<IunidadDeHabla> iterador;
			// Cargo el parser con el documento en modo aprendizaje
			try {
				//Inicio la grabación del documento.
				contenedor = this.parser.modolecturaYalmacenamiento(pathDocumento);
			}
			catch (FileNotFoundException e) {
				return "El archivo solicitado no existe.";
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			} 

			logger.debug("tengo el contenedor de palabras.");
			
			Long offsetRegistroAudio;
			
			if (!abrirArchivo(invocador)) {
				return "Intente denuevo";
			}
						
			Long offsetDoc = DocumentsManager.getInstance().getOffsetUltDoc();
		
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {

				elemento = iterador.next();
				logger.debug("Termino: " + elemento.getTextoEscrito());
				
				// Si no es StopWord entonces utilizo el Ftrs.
				if (!elemento.isStopWord()) {
					ftrsManager.validarTermino(elemento
							.getTextoEscrito(), offsetDoc);
				}
				
				/*
				 * Si es una palabra pronunciable la proxima palabra, sino pido
				 * el audio para la misma. 
				 * Si, es pronunciable, si la encuntra
				 * sigo con la proxima palabra, sino pido el audio para la
				 * misma.
				 */
				if (this.diccionario.buscarPalabra(elemento
						.getEquivalenteFonetico()) != null) {
					logger.debug("existe en el archivo de texto.");
					continue;
				}
				
				logger.debug("No esta en el archivo de texto.");
				
				
				// Si no encontro la palabra pido ingresar el audio
				String mensaje; 
				String respuesta = "0";

				int resultado;
				//pido que grabe hasta que sea correcta la grabación
				while (!respuesta.equalsIgnoreCase("S")) {
					
					mensaje = new String(
							"Para ingresar el audio para la palabra: "
							+ elemento.getTextoEscrito());
					
					invocador.mensaje(mensaje);
					
					// Protocolo de Grabación
					resultado = this.iniciarGrabacion(invocador);
					// Segun el resultado
					switch(resultado) {
						case -1:
							return "Operacion cancelada.";
						case -2:
							logger.debug("error, resultado = -2. respuesta es N, pide el termino de nuevo.");
						//	respuesta = "N";
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
			DocumentsManager.getInstance().finalizaEscritura();
			this.ftrsManager.generarListasInvertidas();

			//cerrarArchivo(invocador);
		}  catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			return "Error inesperado";
		}
		
		logger.debug("Sali de al funcion load");
		return "Las palabras han sido correctamente ingresadas.";
	}

	/** 
	 * Permite al usuario agregar un documento sin especificar el compresor a utilizar
	 * utilizando el compresor por defecto definido en la configuracion.
	 * @param invocador
	 * @param pathDocumento
	 * @return
	 */
	public final String load(final InterfazUsuario invocador,
			final String pathDocumento) {
		return this.load(invocador, pathDocumento, this.metodo);
	}
	
	/**
	 * Permite comprimir un archivo de texto cualquiera especificando los path's
	 * de los archivos de origen y el de destino.
	 * @param invodador
	 * @param metodoExt Metodo que se usara para la compresión.
	 * @param pathArchivoOrigen Ubicación del archivo que se va a comprimir.
	 * @param pathArchivoDestino Ubicación y nombre del archivo que tendrá
	 * el archivo comprimido. 
	 * @return Aviso del resultado de la compresión.
	 */
	public final String comprimir(final InterfazUsuario invocador, final String metodoExt,
					final String pathArchivoOrigen,final String pathArchivoDestino) {

		MetodoCompresion met = Conversiones.metodoDeCompresion(metodoExt);
		
		if (met == null || met.equals(MetodoCompresion.NINGUNO)){
			invocador.mensaje("no se reconoce el tipo de compresion, intente con alguno de los siguientes:");
			invocador.mensaje("ppmc, aritmetico, aritmetico-1, lzp, lz78");
			return null;
			
		}else{
		
		try{
		CompresorConsola.comprimir(met, pathArchivoOrigen, pathArchivoDestino);
		return "\nArchivo: "+pathArchivoDestino+ " creado";
		
		}catch(Exception e){
			invocador.mensaje("hubo un error al comprimir el archivo, se aborta.\nVerifique la codificación del archivo.");
			return null;
		}
		}
	}
	
	/**
	 * Permite descomprimir un archivo previamente comprimido por este programa 
	 * especificando los path's de los archivos de origen y el de destino.
	 * @param invodador
	 * @param metodoExt Metodo con el cual esta comprimido el archivo.
	 * @param pathArchivoOrigen Ubicación del archivo que se va a descomprimir.
	 * @param pathArchivoDestino Ubicación y nombre del archivo que tendrá
	 * el archivo descomprimido
	 * @return Aviso del resultado de la descompresión.
	 */
	public final String descomprimir(final InterfazUsuario invocador, final String metodoExt,
			final String pathArchivoOrigen,final String pathArchivoDestino) {
	
		MetodoCompresion met = Conversiones.metodoDeCompresion(metodoExt);
		
		if (met == null || met.equals(MetodoCompresion.NINGUNO)){
			
			invocador.mensaje("no se reconoce el tipo de compresion, intente con alguno de los siguientes:");
			invocador.mensaje("ppmc, aritmetico, lzp, lz78");
			return null;
			
		}else{
		
		try{
		CompresorConsola.descomprimir(met, pathArchivoOrigen, pathArchivoDestino);
		return "\nArchivo: "+pathArchivoDestino+ " creado";
		
		}catch(Exception e){
			invocador.mensaje("hubo un error al Descomprimir el archivo, se aborta");
			return null;
		}
		}
	}
	
	/**
	 * 
	 * @param invocador .
	 */
	public final void help(final InterfazUsuario invocador) {
		String mensaje = "Funcion: load \n"
			+ "Caracteristicas: carga un documento para almacenar las palabras "
			+ "desconocidas y almacena al documento lo almacena comprimido.\n"
			+ "Los metodos de compresión disponibles son: ppmc, lzp, lz78, aritmetico, aritmetico-1.\n"
			+ "En el caso de no elegir un metodo de compresión se comprimirá\n"
			+ "con el metodo especificado por default.\n"
			+ "Uso: load <\"path_absoluto_del_documento\"> [<\"metodo_para_comprimir\">]\n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \"ppmc\" \n\n"

			+ "Funcion: loadWithoutAudio \n"
			+ "Caracteristicas: Carga un documento para almacenar las palabras "
			+ "desconocidas.\n"
			+ "En el caso de no elegir un metodo de compresión se comprimirá\n"
			+ "con el metodo especificado por default.\n"
			+ "Uso: loadWithoutAudio <\"path_absoluto_del_documento\"> [<\"metodo_para_comprimir\">]\n"
			+ "Ej: loadWithoutAudio \"/home/usuario/Escritorio/prueba.txt\" \n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \"ppmc\" \n\n"
			
			+ "Funcion: playDocument \n"
			+ "Caracteristicas: carga un documento reproduciendo las "
			+ "palabras reconocidas \n"
			+ "Uso: playDocument <\"path_absoluto_del_documento\"> \n"
			+ "Ej: playDocument \"/home/usuario/Escritorio/prueba.txt\" \n\n"

			+ "Funcion: playText \n"
			+ "Caracteristicas: reproduce el texto ingresado, omitiendo las "
			+ "palabras que no conoce \n"
			+ "Uso: playText <\"texto ingresado\"> \n"
			+ "Ej: playText \"hola, como estas\" \n\n"

			+ "Funcion: clear \n"
			+ "Caracteristicas: borra la pantalla \n"
			+ "Uso: clear \n"
			+ "Ej: clear \n\n"

			+ "Funcion: help \n"
			+ "Caracteristicas: muestra los comandos disponibles para su ejecución \n"
			+ "Uso: help \n" + "Ej: help \n\n"

			+ "Funcion: query \n" 
			+ "Caracteristicas: Ejecuta una consulta en el indice \n"
			+ "Uso: query <\"texto ingresado\"> \n"
			+ "Ej: query \"hola como estas\" \n\n"

			+ "Funcion: comprimir\n"
			+ "Caracteristicas: Comprime un archivo según el método elegido. \n"
			+ "Uso: comprimir <\"metodo_para_comprimir\"> <\"path_archivo_origen\"> <\"path_archivo_destino\"> \n"
			+ "Ej: comprimir \"lzp\" \"/home/usuario/Escritorio/prueba.txt\" \"/home/usuario/Escritorio/pruebaComprimida.tsp\" \n\n"
			
			+ "Funcion: descomprimir\n"
			+ "Caracteristicas: Descomprime un archivo según el método elegido. \n"
			+ "Uso: descomprimir <\"metodo_para_descomprimir\"> <\"path_archivo_origen\"> <\"path_archivo_destino\"> \n"
			+ "Ej: descomprimir \"lzp\" \"/home/usuario/Escritorio/pruebaComprimida.zip\" \"/home/usuario/Escritorio/pruebaDescomprimida.txt\" \n\n"
						
			+ "Funcion: fin \n" 
			+ "Caracteristicas: sale del programa \n"
			+ "Uso: fin \n" + "Ej: fin \n\n";	

		this.clear(invocador);
		invocador.mensaje(mensaje);
	}
	
	
	/**
	 * 
	 * @param invocador .
	 */
	public final void clear(final InterfazUsuario invocador) {
		
		String clear = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n";
		
		invocador.mensaje(clear);
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
	 * 
	 * @param invocador
	 *            .
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	private final String playDocumentInterno(final InterfazUsuario invocador,
			final String indice) {
		
		try {
			
			Iterator<IunidadDeHabla> iterador;
			
			SimilitudDocumento simDocs = this.ranking.get(Integer.parseInt(indice) - 1);
			
			// Mando a parsear el documento y obtengo un collection
			try {

				contenedor = this.parser.modoLecturaDocAlmacenado(simDocs.getDocumento());
				
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
				if (elemento.esPronunciable()) {

				Long offsetAudio = this.diccionario
					.buscarPalabra(elemento.getEquivalenteFonetico());

				invocador.mensajeSinSalto(elemento.getTextoEscrito() + " ");
				if (offsetAudio != null) {
					playWord(this.audioFileManager.leerAudio(offsetAudio));
					audioManager.esperarFin();
				}

				}
			}
			logger.debug("Sali de la funcion playDocument");

			invocador.mensaje("");
			//cerrarArchivo(invocador);
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage());
			return "Error en dispositivo de audio.";
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
			return "Error inesperado.";
		}
			
		return "Reproduccion finalizada";
	}

	/**
	 * Reproduce un documento entero.
	 * 
	 * @param invocador
	 *            .
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String playDocument(final InterfazUsuario invocador,
			final String rutaDocumento) {
		
		try {
			
			Iterator<IunidadDeHabla> iterador;
			
			// Mando a parsear el documento y obtengo un collection
			try {
				contenedor = this.parser.modoLecturaSinAlmacenamiento(rutaDocumento, true);
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
				if (elemento.esPronunciable()) {

					Long offsetAudio = this.diccionario
						.buscarPalabra(elemento.getEquivalenteFonetico());
	
					if (offsetAudio != null) {
						invocador.mensajeSinSalto(elemento.getTextoEscrito() + " ");
						playWord(this.audioFileManager.leerAudio(offsetAudio));
						audioManager.esperarFin();
					}
				}
			}
			logger.debug("Sali de la funcion playDocument");

			invocador.mensaje("");
			//cerrarArchivo(invocador);
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage());
			return "Error en dispositivo de audio.";
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage());
			return "Error inesperado.";
		}
			
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
	
				contenedor = this.parser.modoLecturaSinAlmacenamiento(textoAReproducir, false);
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
				if (elemento.esPronunciable()) {

					Long offsetAudio = this.diccionario
							.buscarPalabra(elemento.getEquivalenteFonetico());
					if (offsetAudio != null) {
						invocador.mensajeSinSalto(elemento.getTextoEscrito()
								+ " ");
						playWord(this.audioFileManager.leerAudio(offsetAudio));
						audioManager.esperarFin();
	
					} else {
						logger.debug("No se encontró la palabra ["
								+ elemento.getEquivalenteFonetico()
								+ "] en el diccionario.");
					}
				}
			}
			logger.debug("Sali de al funcion playText");
			
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage(), e);
			invocador.mensaje("Error en dispositivo de audio");
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			invocador.mensaje("Error inesperado");
		}
		
		invocador.mensaje("");
	}

	/**
	 * Reproduce la última palabra leida.
	 * 
	 * @throws SimpleAudioPlayerException .
	 */
	public final void playWord() throws SimpleAudioPlayerException {
		this.audioManager.reproducir();
	}

	/**
	 * Reproduce el audio que recibe.
	 * 
	 * @param audioAReproducir
	 *            es el audio que se va a reproducir.
	 * @throws SimpleAudioPlayerException .
	 */
	public final void playWord(final InputStream audioAReproducir)
			throws SimpleAudioPlayerException {
		this.audioManager.reproducir(audioAReproducir);
	}
	
	/**
	 * Constructor de la clase.
	 */
	public Core() {
		this.audioManager = new AudioManager();
		this.parser = new TextInterpreter();
		this.audioFileManager = new AudioFileManager();
		try {
			this.diccionario = new Diccionario();
			this.ftrsManager = new FTRSManager();
			
			this.ftrsManager.abrirArchivos(); //TODO esto se puso por una prueba, probablemente haya que sacarlo
			
		} catch (Exception e) {
			logger.debug("No se ha podido crear el FTRS.");
		}
		this.ranking = null;
		this.metodo = Constantes.METODO;
	}
	
	/**
	 * Se llama al terminar, para dar un mensaje.
	 * 
	 * @param invocador .
	 */
	public final void quit(final InterfazUsuario invocador) {
		try {
			this.ftrsManager.cerrarArchivos();
			this.diccionario.cerrar();
			invocador.mensaje("gracias por usar TheSpeaker");
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Cierra los archivos.
	 * 
	 * @param invocador
	 *            .
	 * @return true si pudo cerrar los archivos.
	 */
	private boolean cerrarArchivo(final InterfazUsuario invocador) {
		
		try {

			if (this.audioFileManager != null) {
				this.audioFileManager.cerrar();
			}
			this.ftrsManager.cerrarArchivos();
			return true;

		} catch (Exception e) {
			
			try {
				if (this.audioFileManager != null) {
					this.audioFileManager.cerrar();
				}
			} catch (Exception g) {
				invocador.mensaje("Error al cerrar el archivo de audio.");
			}

			
			invocador.mensaje("Error al cerrar el archivo de diccionario.");
			
			this.ftrsManager.cerrarArchivos();
			return false;
		}
	}
	
	/**
	 * Abre los archivo.
	 * 
	 * @param invocador .
	 * @return true si pudo abrir los archivos.
	 */
	private boolean abrirArchivo(final InterfazUsuario invocador) {

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
		
		logger.debug("Abrio el archivo Audio");
				
		this.ftrsManager.abrirArchivos();
		
		return true;
	}
	

	/**
	 * Metodo que se encarga de administrar la consulta del usuario.
	 * @param invocador 
	 * 			.
	 * @param query cadena de caracteres que debe tener los documentos.
	 * @return
	 * 		Devuelve un mensaje con el estado final del proceso.
	 */
	public final String query(final InterfazUsuario invocador, final String query) {
		Float tiempoFinal = new Float(0.0);
		try {
			
			logger.debug("se ingresa la palabra: "+query);
			this.tiempoConsulta = System.currentTimeMillis();
			Long cantidadDocs = DocumentsManager.getInstance().getCantidadDocsAlmacenados();
			if (cantidadDocs == 0) {
				return "No hay documentos cargados al sistema";
			}
			
			
			//chequeo de stop word
			Palabra consulta = PalabrasFactory.getPalabra(query);
			if (consulta.isStopWord()){
				return "Su consulta es un StopWord. No presenta documentos relevantes";
			}else{
			//fin chequeo stop word
			
			logger.debug("pero se consulta por la palabra: "+consulta.getTextoEscrito());
				
			try {
				
				ranking = this.ftrsManager.consultaRankeada(consulta.getTextoEscrito(), cantidadDocs);
				tiempoFinal = (float)(System.currentTimeMillis() - this.tiempoConsulta) / 1000;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (this.ranking == null) {
				invocador.mensaje("No se encontraron documentos.");
				return tiempoFinal.toString() + " segundos";
			}

			  Iterator<SimilitudDocumento> it;
			  it = this.ranking.iterator();
			  SimilitudDocumento nodo;
			  Integer i = 1;
			  invocador.mensaje("Seleccione un de los documentos para ser reproducido:");
			  while (it.hasNext()) {
				  nodo = it.next();
				  String mensaje = i.toString() + ". " + DocumentsManager.getInstance().getNombreDoc(nodo.getDocumento());  
				  invocador.mensaje(mensaje);
				  i++;
			  }
			 int opcion = -1;
			 String documento = "0";
			 
			  while (opcion < 1 || opcion > (i-1)){
				  documento = invocador.obtenerDatos("Elija el documento a reproducir: ");
				  try {
					  opcion = Integer.parseInt(documento);
				  } catch (NumberFormatException e) {
					  invocador.mensaje("Opción incorrecta. Intente nuevamente.");
				  }  
			  }
			  
			  
			  this.playDocumentInterno(invocador, documento);
			
			return tiempoFinal.toString() + " segundos";
		} 
		}
			catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			return "Error inesperado.";
		}
	}



	@Override
	protected void finalize() throws Throwable {
		try {

			if (this.audioFileManager != null) {
				this.audioFileManager.cerrar();
			}
			
			
			this.ftrsManager.cerrarArchivos();
			

		} catch (Exception e) {
			
			try {
				if (this.audioFileManager != null) {
					this.audioFileManager.cerrar();
				}
			} catch (Exception g) {
				
			}
			
			this.ftrsManager.cerrarArchivos();
			
		}
		super.finalize();
	}	
}
