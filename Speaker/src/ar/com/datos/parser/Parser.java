package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * @author zeke
 *
 */
public class Parser {

	private String ruta_archivo;
	private File archivo;
	private static Logger milogueador;
	private FileReader lector;
	private BufferedReader buffer;

	public Parser(String ruta_archivo) throws FileNotFoundException {
		this.ruta_archivo = ruta_archivo;
		File archivo = new File(ruta_archivo);
		milogueador = Logger.getLogger(Parser.class);

		if (!archivo.exists()) {
			milogueador.error("no existe el archivo",
					new FileNotFoundException(
							"No existe el archivo pasado al Parser"));
		}
		lector = new FileReader(archivo);
		buffer = new BufferedReader(lector);

	}

	/**
	 * @param caca
	 * @throws IOException
	 */
	
	
	private void leerLinea(String caca) throws IOException {

		
		String linea = (buffer.readLine()).toLowerCase();
		String regEx = "([;\\s\\t:?!]+)|[,.\\s]{2} |(^(\\d\\D\\d)) ";
		String[] vector = caca.split(regEx);

	}

	
	}
