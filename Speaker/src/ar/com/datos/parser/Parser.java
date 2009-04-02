package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.BufferedCollection;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.Constantes;

/**
 * @author zeke
 * 
 */
public class Parser implements BufferRecharger<IunidadDeHabla> {

	private String ruta_archivo;
	private File archivo;
	private static Logger milogueador;
	private FileReader lector;
	private BufferedReader buffer;
	private boolean moreLines;
	private boolean esArchivo;
	private String lineaSimple;

	  /**
	    * Constructor.
	    * Toma una cadena o archivo y realiza el procesado. Si es una linea,
	    * se utiliza un buffer para su tratamiento. 
	    * de palabras 
	    * @param rutaOlinea
	    * @param esArchivo
	    */
	public Parser(final String rutaOlinea, boolean esArchivo) {

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
			try{
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis, Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			buffer = new BufferedReader(isr);
			}
			catch(Exception e){}
		
		//	buffer = new BufferedReader(lector);

		} else {
			esArchivo = false;
			lineaSimple = rutaOlinea;
		}

	}

	  /**
	    * Constructor.
	    * Toma una cadena o archivo y realiza el procesado. Si es una linea,
	    * se utiliza un buffer para su tratamiento. 
	    * de palabras 
	    * @param rutaOlinea
	    * @param esArchivo
	    */
	public Parser(final String rutaOlinea, boolean esArchivo, String charset) {

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
			try{
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis, charset);
			buffer = new BufferedReader(isr);
			}
			catch(Exception e){}
		
		//	buffer = new BufferedReader(lector);

		} else {
			esArchivo = false;
			lineaSimple = rutaOlinea;
		}

	}
	
	
	/**
	 * Metodo que devuelve la proxima linea a procesar
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

	public final boolean hasMoreItems() {
		return moreLines;
	}

	public final void recargarBuffer(Collection<IunidadDeHabla> coleccion,
			int maxLineas) {

		String materiaPrima;
		int lineasProcesadas = 0;
		int sigPalabra = 0;
		String[] listaPalabras;

		materiaPrima = this.leerLinea();

		while ((lineasProcesadas < maxLineas) && (materiaPrima != null)) {

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
     * Metodo que procesa una la linea, separandola por palabra y aplicando un filtrado
     * a caracteres invalidos y agregando las palabras obtenidas a la coleccion pasada 
     * por parametro
     * @param coleccion
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
     *  Metodo que devuelve una coleccion para ser usada
     *  en bufferización
     */
	public Collection<IunidadDeHabla> listar() {

		BufferedCollection<IunidadDeHabla> coleccionBuf = new BufferedCollection<IunidadDeHabla>(
				this);
		recargarBuffer(coleccionBuf, 1);
		return coleccionBuf;
	}

	/**
     * Método que genera una colection para el tratamiento de palabras
     * @return Collection 
     */
	public Collection<IunidadDeHabla> simpleString() {

		ArrayList<IunidadDeHabla> coleccion = new ArrayList<IunidadDeHabla>();
		procesarLineaSimple(coleccion);
		return coleccion;

	}
}
