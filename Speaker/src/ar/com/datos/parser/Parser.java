package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.BufferedCollection;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;

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
	

	public Parser(final String ruta_archivo) {
		
		this.ruta_archivo = ruta_archivo;
		File archivo = new File(ruta_archivo);
		milogueador = Logger.getLogger(Parser.class);

		if (!archivo.exists()) {
			milogueador.error("no existe el archivo",
					new FileNotFoundException(
							"No existe el archivo pasado al Parser"));
		}
		
		try {
			lector = new FileReader(archivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(lector);

	}

	
	/**
	 * @param caca
	 * @throws IOException
	 */
	private String leerLinea() {
		
		String linea="";
		
		
		while ((linea != null)&&(linea.isEmpty())){
		try {
			linea = (buffer.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			this.moreLines=false;
			return null;
		}
		}
		if (linea != null){
		this.moreLines=true;
		return linea;
		}else{
			moreLines = false;
			return null;
		}
		}
	

	public final boolean hasMoreItems() {
		return moreLines;
	}

	public final void recargarBuffer( Collection<IunidadDeHabla> coleccion, int maxLineas) {

		String materiaPrima;
		int lineasProcesadas = 0;
		int sigPalabra = 0;
		String[] listaPalabras;
		materiaPrima = this.leerLinea();

		while ((lineasProcesadas < maxLineas) && (materiaPrima != null)) {
			
			if (!PatternRecognizer.esLineaVacia(materiaPrima)){
			listaPalabras = PatternRecognizer.procesarLinea(materiaPrima);
			while (sigPalabra < listaPalabras.length) {
				coleccion.add(PalabrasFactory
						.getPalabra(listaPalabras[sigPalabra]));
				sigPalabra++;
			}
			}
			sigPalabra=0;
			lineasProcesadas++ ;
			materiaPrima = this.leerLinea();
		}
	}
	

	public Collection< IunidadDeHabla > listar() {
		BufferedCollection<IunidadDeHabla> coleccion = new BufferedCollection<IunidadDeHabla>(this);
		recargarBuffer(coleccion, 1);
		return coleccion;
	}
	
	}
