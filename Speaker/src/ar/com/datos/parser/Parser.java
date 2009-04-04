package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.BufferedCollection;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.Constantes;

/**
 * @author LedZeppeling
 * 
 */
public class Parser implements BufferRecharger<IunidadDeHabla> {

	/**
	 * Permite saber cual es la ruta del archivo pasado por parámetro.
	 */
	private String ruta_archivo;
	
	/**
	 * Contiene el archivo a ser leído.
	 */
	private File archivo;
	
	/**
	 * Atributo para administrar el nivel de logueo mediante Log4j.
	 */
	private static Logger milogueador;
	
	/**
	 * Buffer de lectura de archivo de texto.
	 */
	private BufferedReader buffer;
	
	/**
	 * permite saber si existen mas líneas para levantar desde el 
	 * archivo de texto.
	 */
	private boolean moreLines;
	
	/**
	 * Permite saber si se esta trabajando sobre un archivo o una línea pasada.
	 * desde la consola
	 */
	private boolean bArchivo;
	
	/**
	 * Si se trata de una línea simple, pasada desde la consola, se almacena en
	 * esta variable para su manipulación.
	 */
	private String lineaSimple;
	
	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(Parser.class);

	 /**
	 * Constructor. Toma una cadena o archivo y realiza el procesado. Si es una
	 * linea, se utiliza un buffer para su tratamiento. de palabras
	 * 
	 * @param rutaOlinea
	 * @param esArchivo
	 * @throws Exception 
	 */
	public Parser(final String rutaOlinea, boolean esArchivo) throws Exception {

		if (esArchivo) {
			this.bArchivo = true;
			this.ruta_archivo = rutaOlinea;
			this.archivo = new File(ruta_archivo);
			milogueador = Logger.getLogger(Parser.class);

			if (!archivo.exists()) {
				milogueador.error("no existe el archivo",
						new FileNotFoundException(
								"No existe el archivo pasado al Parser"));
			}
			try {
				FileInputStream fis = new FileInputStream(archivo);
				InputStreamReader isr = new InputStreamReader(fis,
						Constantes.DEFAULT_TEXT_INPUT_CHARSET);
				buffer = new BufferedReader(isr);
			} catch (Exception e) {
				logger.error("Error al crear el parser: " + e.getMessage());
				throw e;
			}

			// buffer = new BufferedReader(lector);

		} else {
			esArchivo = false;
			lineaSimple = rutaOlinea;
		}

	}

	 /**
	 * Constructor. Toma una cadena o archivo y realiza el procesado. Si es una
	 * linea, se utiliza un buffer para su tratamiento. de palabras
	 * 
	 * @param rutaOlinea
	 * @param esArchivo
	 * @throws Exception
	 */
	public Parser(final String rutaOlinea, boolean esArchivo, String charset) throws Exception {

		if (esArchivo) {
			esArchivo = true;
			this.ruta_archivo = rutaOlinea;
			File archivo = new File(ruta_archivo);
			milogueador = Logger.getLogger(Parser.class);

			if (!archivo.exists()) {
				milogueador.error("no existe el archivo",
						new FileNotFoundException(
								"No existe el archivo pasado al Parser"));
			}
			try {
				FileInputStream fis = new FileInputStream(archivo);
				InputStreamReader isr = new InputStreamReader(fis, charset);
				buffer = new BufferedReader(isr);
			} catch (Exception e) {
				logger.error("Error al crear el parser: " + e.getMessage());
				throw e;
			}
		
		//	buffer = new BufferedReader(lector);

		} else {
			esArchivo = false;
			lineaSimple = rutaOlinea;
		}

	}
	
	
	/**
	 * Metodo que devuelve la proxima linea a procesar.
	 * @throws IOException
	 */
	private String leerLinea() {

		String linea = "";

		while ((linea != null) && (linea.isEmpty())) {
			try {
				linea = (buffer.readLine());
			} catch (IOException e) {
				e.printStackTrace();
				this.moreLines = false;
				return null;
			}
		}
		if (linea != null) {
			this.moreLines = true;
			return linea;
		} else {
			moreLines = false;
			return null;
		}
	}
	
    /**
     * Permite saber si hay mas lineas que levantar desde el archivo
     * de texto.
     * @return true si hay mas lineas y false si no las hay. 
     */
	public final boolean hasMoreItems() {
		return moreLines;
	}

	/**
	 * Método que implementa la interfaz Bufferizable y permite recargar a
	 * pedido la colección recibida por parámetro. Se utiliza para paginar
	 * una colección y no tener cargados a memoria todos los elementos que
	 * la componen.
	 * @param coleccion colección a la que se prentende recargar el buffer
	 * @param maxLineas cantidad máxima de líneas que serán levantadas
	 * de disco para cargar a la colección, representa un limite del buffer. 
	 */
	public final void recargarBuffer(Collection<IunidadDeHabla> coleccion,
			int maxLineas) {

		String materiaPrima;
		int lineasProcesadas = 0;
		int sigPalabra = 0;
		String[] listaPalabras;

		materiaPrima = this.leerLinea();

		while ((lineasProcesadas <= maxLineas) && (materiaPrima != null)) {

			if (!PatternRecognizer.esLineaVacia(materiaPrima)) {
				listaPalabras = PatternRecognizer.procesarLinea(materiaPrima);
				while (sigPalabra < listaPalabras.length) {
					coleccion.add(PalabrasFactory
							.getPalabra(listaPalabras[sigPalabra]));
					sigPalabra++;
				}
			}
			sigPalabra = 0;
			lineasProcesadas++;
			materiaPrima = this.leerLinea();
		}
	}
	
	 /**
	 * Metodo que procesa una la linea, separandola por palabra y aplicando un
	 * filtrado a caracteres invalidos y agregando las palabras obtenidas a la
	 * coleccion pasada por parametro.
	 * 
	 * @param coleccion colección donde cargar las palabras obtenidas.
	 */
	private void procesarLineaSimple(Collection<IunidadDeHabla> coleccion) {

		String[] listaPalabras;
		int sigPalabra = 0;

		if (!PatternRecognizer.esLineaVacia(lineaSimple)) {
			listaPalabras = PatternRecognizer.procesarLinea(lineaSimple);
			while (sigPalabra < listaPalabras.length) {
				coleccion.add(PalabrasFactory
						.getPalabra(listaPalabras[sigPalabra]));
				sigPalabra++;
			}
		}
	}

	/**
	 * Metodo que devuelve una colección para ser usada en bufferización.
	 */
	public Collection<IunidadDeHabla> listar() {

		BufferedCollection<IunidadDeHabla> coleccionBuf = new BufferedCollection<IunidadDeHabla>(
				this);
		recargarBuffer(coleccionBuf, 1);
		return coleccionBuf;
	}

	/**
	 * Método que genera una colection para el tratamiento de palabras.
	 * 
	 * @return Collection
	 */
	public Collection<IunidadDeHabla> simpleString() {

		ArrayList<IunidadDeHabla> coleccion = new ArrayList<IunidadDeHabla>();
		procesarLineaSimple(coleccion);
		return coleccion;

	}
}
