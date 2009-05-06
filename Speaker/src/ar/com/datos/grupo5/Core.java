package ar.com.datos.grupo5;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.grupo5.registros.RegistroDiccionario;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.parser.TextInterpreter;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;
import ar.com.datos.sortExterno.Merge;
import ar.com.datos.sortExterno.NodoRS;
import ar.com.datos.sortExterno.ReplacementSelection;


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
	 * Se encarga de manejar los documentos en disco.
	 */
	private DocumentsManager documentManager;
	
	/**
	 * 
	 */
	private AudioFileManager audioFileManager;
	
	/**
	 * Se encarga de administrar los terminos.
	 */
	private TerminosGlobales terminosGlobalesManager;
	
	/**
	 * Conteniene todas las palabras a grabar o a leer del documento ingresado.
	 */
	private Collection<IunidadDeHabla>  contenedor; 
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	
	/**
	 * Archivo de trabajo.
	 */
	private RandomAccessFile archivoTrabajo;
	
	/**
	 * Administra las listas invertidas en el archivo de bloques.
	 */
	private ListasInvertidas listasInvertidas;
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
		
		try {
			Iterator<IunidadDeHabla> iterador;

			// Cargo el parser con el documento en modo aprendizaje
			try {
				contenedor = this.parser.modoCarga(pathDocumento, true);
			} catch (FileNotFoundException e) {
				return "No se pudo abrir el archivo: " + pathDocumento;
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			}
			
			logger.debug("tengo el contenedor de palabras.");
			
			Long offsetRegistroAudio;
			
			if (!abrirArchivo(invocador)) {
				return "Intente denuevo";
			}
			
			//TODO: Pisar el anterior!!
			archivoTrabajo = new RandomAccessFile(Constantes.ARCHIVO_TRABAJO,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			NodoRS idTerminoIdDocumento;
			/*
			 * Creo el documento en el archivo de documentos.
			 * TODO: Completar el tema de documentos.
			 */
			Long offsetDoc = 0L;
			//offsetDoc = this.documentManager.agregarDocumento(pathDocumento);
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			
			RegistroTerminoDocumentos regTerminoDocumentos = null;
			
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {

				elemento = iterador.next();
				logger.debug("Itero una vez.txt.");
				
				/*
				 * TODO: Cada palabra necesito agregarla al documento.
				 */
				
				// Si no es StopWord entonces utilizo el Ftrs.
				if (!elemento.isStopWord()) {
					/*
					 * TODO: Busco el termino en el FTRS.
					 * regTerminoDocumentos = busqueda(elemento.getTextoEscrito());
					 */
					
					/*
					 * Si es nulo entonces no existe el termino en el ftrs
					 * por lo tanto tampoco existe en el archivo de termino globales.
					 */
					if (regTerminoDocumentos == null) {
						//Se encarga de agregar el termino que no existe.
						regTerminoDocumentos 
							= this.agregaTermino(elemento.getTextoEscrito());
					}
					
					//Escribo el idTermino junto con el offsetDoc
					idTerminoIdDocumento = new NodoRS(regTerminoDocumentos.getIdTermino(), offsetDoc);
					archivoTrabajo.write(idTerminoIdDocumento.getBytes(), 0, idTerminoIdDocumento.getTamanio());

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
			
			this.archivoTrabajo.close();
			
			generarListasInvertidas();

			cerrarArchivo(invocador);
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage());
			return "Error en dispositivo de audio";
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			return "Error inesperado";
		}
		
		logger.debug("Sali de al funcion load");
		return "Las palabras han sido correctamente ingresadas.";
	}

	/**
	 * Se encarga de agregar el termino que no existe en el 
	 * archivo de terminos globales y en el FTRS.
	 * @param textoEscritoExt Termino a agregar.
	 */
	private RegistroTerminoDocumentos agregaTermino(final String textoEscritoExt) {
		// TODO Auto-generated method stub
		Long idTermino
			= this.terminosGlobalesManager.agregar(textoEscritoExt);
		/*
		 * agregar al FTRS el termino con idTermino y con el 
		 * offset a la lista en null.
		 */
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		reg.setIdTermino(idTermino);
		return reg;
	}

	/**
	 * 
	 * @param invocador .
	 */
	public final void help(final InterfazUsuario invocador) {
		
		String mensaje = "Funcion: load \n"
			+ "Caracteristicas: carga un documento para almacenar las palabras "
			+ "desconocidas \n"
			+ "Uso: load <\"path_absoluto_del_documento\"> \n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n\n"

			+ "Funcion: playDocument \n"
			+ "Caracteristicas: carga un documento reproduciendo las "
			+ "palabras reconocidas \n"
			+ "Uso: playDocument <\"path_absoluto_del_documento\"> \n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n\n"

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

			+ "Funcion: fin \n" + "Caracteristicas: sale del programa \n"
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
	public final String playDocument(final InterfazUsuario invocador,
			final String pathDocumento) {
		
		try {
			Iterator<IunidadDeHabla> iterador;
			
			// Mando a parsear el documento y obtengo un collection
			try {
				contenedor = this.parser.modoLectura(pathDocumento, true);
			} catch (FileNotFoundException e) {
				return "No se pudo abrir el archivo: " + pathDocumento;
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
				RegistroDiccionario registro = this.diccionario
					.buscarPalabra(elemento.getEquivalenteFonetico());
				
				if (registro != null) {
					invocador.mensajeSinSalto(elemento.getTextoEscrito() + " ");
					playWord(this.audioFileManager.leerAudio(registro
								.getOffset()));
					audioManager.esperarFin();
				}

				}
			}
			logger.debug("Sali de la funcion playDocument");

			invocador.mensaje("");
			cerrarArchivo(invocador);
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
				if (elemento.esPronunciable()) {
					RegistroDiccionario registro = this.diccionario
							.buscarPalabra(elemento.getEquivalenteFonetico());
					if (registro != null) {
						invocador.mensajeSinSalto(elemento.getTextoEscrito()
								+ " ");
						playWord(this.audioFileManager.leerAudio(registro
								.getOffset()));
						audioManager.esperarFin();
	
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
		this.diccionario = new Diccionario();
		this.audioManager = new AudioManager();
		this.parser = new TextInterpreter();
		this.audioFileManager = new AudioFileManager();
		this.documentManager = new DocumentsManager();
	}
	
	/**
	 * Se llama al terminar, para dar un mensaje.
	 * 
	 * @param invocador .
	 */
	public final void quit(final InterfazUsuario invocador) {

		invocador.mensaje("gracias por usar TheSpeaker");
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
			
			if (this.diccionario != null) {
				this.diccionario.cerrar();
			}
			if (this.audioFileManager != null) {
				this.audioFileManager.cerrar();
			}
			if (this.documentManager != null) {
				this.documentManager.cerrar();
			}
			return true;

		} catch (Exception e) {
			
			try {
				if (this.audioFileManager != null) {
					this.audioFileManager.cerrar();
				}
			} catch (Exception g) {
				invocador.mensaje("Error al cerrar el archivo de audio.");
			}
			try {
				if (this.documentManager != null) {
					this.documentManager.cerrar();
				}
			} catch (Exception g) {
				invocador.mensaje("Error al cerrar el archivo de documentos.");
			}
			try {
				if (this.terminosGlobalesManager != null) {
					this.terminosGlobalesManager.cerrar();
				}
			} catch (Exception g) {
				invocador.mensaje("Error al cerrar el archivo de terminos " 
						+ "Globales.");
			}
			invocador.mensaje("Error al cerrar el archivo de diccionario.");
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
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		try {
			this.diccionario.abrir(Constantes.ARCHIVO_DICCIONARIO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			invocador.mensaje("No se pudo abrir el diccionario.");
			return false;
		}

		logger.debug("Abrio el archivo Diccionario");

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
		
		/*
		 * Abro el archivo para la carga y consulta de los documentos
		 */
		try {
			this.documentManager.abrir(Constantes.ARCHIVO_DOCUMENTOS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			invocador.mensaje("No se pudo abrir el archivo de documentos.");
			return false;
		}
		
		logger.debug("Abrio el archivo documentos");
		
		try {
			this.terminosGlobalesManager.abrir(Constantes.ARCHIVO_TERMINOS_GLOBALES,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			invocador.mensaje("No se pudo abrir el archivo de terminos globales.");
			return false;
		}
		
		logger.debug("Abrio el archivo de terminos globales");
		return true;
	}
	
	/**
	 * Se encarga de la generacion y actualizacion 
	 * de las listas invertidas.
	 */
	private void generarListasInvertidas() {
		//Ahora tengo que realizar el replacement Selection
		ReplacementSelection remplacementP
					= new ReplacementSelection(Constantes.ARCHIVO_TRABAJO);
		
		remplacementP.ordenar();
		
		ArrayList<String> listaParticiones 
					= remplacementP.getListaParticiones();
		
		Merge mergeManager 
					= new Merge(listaParticiones, remplacementP.getArch());
		
		mergeManager.ejecutarMerge();
		
		long nRegistros = 0;
		NodoRS nodo = new NodoRS();
		byte[] dataNodo = new byte[nodo.getTamanio()];
		try {
			this.archivoTrabajo = new RandomAccessFile(
					Constantes.ARCHIVO_TRABAJO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			nRegistros = this.archivoTrabajo.length() / nodo.getTamanio();
		
			Long idTermino = 0L;
			Long idDocumento = 0L;
			Long frecuencia = 0L;
			int i = 0;
			this.archivoTrabajo.read(dataNodo, 0, dataNodo.length);
			nodo.setBytes(dataNodo);
			idTermino = nodo.getIdTermino();
			idDocumento = nodo.getIdDocumento();
			
			//El documento siempre es el mismo
			while (i < nRegistros) {
				while (idTermino == nodo.getIdTermino() && idDocumento == nodo.getIdDocumento()&& i < nRegistros) {
					frecuencia++;
					i++;
					this.archivoTrabajo.read(dataNodo, 0, dataNodo.length);
					nodo = new NodoRS();
					nodo.setBytes(dataNodo);
				}
				ParFrecuenciaDocumento parFrecDoc = new ParFrecuenciaDocumento();
				parFrecDoc.setFrecuencia(frecuencia);
				parFrecDoc.setOffsetDocumento(idDocumento);
				
				//Leo el termino
				String termino = this.terminosGlobalesManager.leerTermino(idTermino);
				//TODO:Pido al FTRS los datos del termino. Busco el termino
				
				
				RegistroTerminoDocumentos regTerminoDocumentos 
						= new RegistroTerminoDocumentos();
				
				//regTerminoDocumentos.
				
				
				
				idTermino = nodo.getIdTermino();
				idDocumento = nodo.getIdDocumento();
				frecuencia = 0L;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
