package ar.com.datos.grupo5.parser;

import java.util.Collection;

import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;

/**
 * Interface implementada por las clases del paquete Parser. Permite obtener una
 * colección de palabras para su verificación de almacenamiento en disco o para
 * su administrar su reproducción. A tales efectos se disponen 2 métodos
 * diferentes, de modo que las clases que lo implementen agregen fácilmente
 * optimizaciones sobre los diferentes procesos.
 * 
 * @author LedZeppeling
 * 
 */
public interface ITextInput  {

	/**
	 * Lee de un string o archivo con un charset modificado, por defecto se toma UTF8
	 * @param rutaOlinea
	 * @param esArchivo
	 * @param Charset
	 * @return
	 * @throws Exception
	 */
	public Collection<IunidadDeHabla> modoLecturaCambioCharset(String rutaOlinea,
			boolean esArchivo, String Charset) throws Exception;
	
	/**
	 * Lee de un archivo y almacena su contenido para futuros accesos
	 * @param rutaArchivo
	 * @return
	 */
	public Collection<IunidadDeHabla> modolecturaYalmacenamiento (String rutaArchivo);
	
	/**
	 * lee de un archivo o string pero no almacena su contenido.
	 * @param rutaOlinea
	 * @param esArchivo
	 * @return
	 * @throws Exception
	 */
	public Collection<IunidadDeHabla> modoLecturaSinAlmacenamiento(String rutaOlinea,
			boolean esArchivo) throws Exception;
	
	/**
	 * lee desde un documento almacenado
	 * @param offset offset del documento almacenado
	 * @return coleccion con palabras
	 */
	public Collection<IunidadDeHabla> modoLecturaDocAlmacenado(long offset);
	
}
