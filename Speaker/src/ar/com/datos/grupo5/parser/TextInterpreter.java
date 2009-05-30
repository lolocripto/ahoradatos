package ar.com.datos.grupo5.parser;

import java.util.Collection;

import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;

/**
 * Es la clase que se utiliza para parsear y cargar las palabras.
 * 
 * @author LedZeppeling
 */
public class TextInterpreter implements ITextInput {


	/**
	 * En este modo se lee de un archivo o string sin almacenarlo en un charset diferente
	 * a UTF8
	 */
	public Collection<IunidadDeHabla> modoLecturaCambioCharset(String rutaOlinea,
			boolean esArchivo, String Charset) throws Exception {

		Parser miparser = new Parser(rutaOlinea, esArchivo, Charset);
		if (esArchivo) {
			return miparser.listar();
		} else {
			return miparser.simpleString();
		}
	}
	

	/**
	 * en este modo se lee un archivo cuya ruta es pasada por parametro y el mismo
	 * se almacena para posterior reproduccion
	 * @param rutaArchivo
	 * @return
	 * @throws Exception 
	 */
	public Collection<IunidadDeHabla> modolecturaYalmacenamiento (String rutaArchivo) throws Exception{
		
		Parser miparser = null;
		miparser = new Parser(rutaArchivo);
		return miparser.listar();
		
	}


	/**
	 * en este modo, se lee sobre un archivo o string pero el mismo no se almacena
	 */
	public Collection<IunidadDeHabla> modoLecturaSinAlmacenamiento(String rutaOlinea,
			boolean esArchivo) throws Exception {

		Parser miparser = new Parser(rutaOlinea, esArchivo);
		if (esArchivo) {
			return miparser.listar();
		} else {
			return miparser.simpleString();
		}
	}
	
	/**
	 * en este modo se lee directamente desde un documento almacenado, pasando
	 * por parametro el offset del mismo
	 * @param offset
	 * @return
	 */
	public Collection<IunidadDeHabla> modoLecturaDocAlmacenado(long offset){
		
		Parser miParser = new Parser(offset);
		return miParser.listar();
	}
	
}
