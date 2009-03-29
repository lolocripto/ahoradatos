package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;

/**
 * @author zeke
 *
 */
public class Parser implements BufferRecharger{

	private String ruta_archivo;
	private File archivo;
	private static Logger milogueador;
	private FileReader lector;
	private BufferedReader buffer;
	private boolean moreLines;
	

	public Parser(final String ruta_archivo) throws FileNotFoundException {
		
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
	private String leerLinea() {
		
		String linea=null;
		try {
			linea = (buffer.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (linea != null){
		linea.toLowerCase();
		return linea;
		}else{
			moreLines = false;
			return null;
		}
	}
	

	@Override
	public final boolean hasMoreItems() {
		return moreLines;
	}

	

	public final void recargarBuffer(final Collection<Object> coleccion, 
			final int maxLineas) {
		
		String materiaPrima;
		int lineasProcesadas =0;
		int sigPalabra =0;
		String[] listaPalabras;
		materiaPrima = this.leerLinea();
				
		while ( (lineasProcesadas<maxLineas)&& (materiaPrima !=null))	{
			materiaPrima = this.leerLinea();
			listaPalabras = PatternRecognizer.procesarLinea(materiaPrima);
			
			while ( sigPalabra < listaPalabras.length){
				coleccion.add(PalabrasFactory.getPalabra(listaPalabras[sigPalabra]));
				sigPalabra++;
			}
			lineasProcesadas++;
			
			}
			
		}



	@Override
	public Collection< ? > listar() {
		Collection<IunidadDeHabla> coleccion = new ArrayList<IunidadDeHabla>();
		return coleccion;
	}
	
	}
