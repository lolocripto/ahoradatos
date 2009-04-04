package ar.com.datos.parser;

import java.util.Collection;
import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;

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
	 * Método que permite obtener una colección de palabras para verificar su
	 * presencia en disco.
	 * 
	 * @param rutaOlinea .
	 *            ruta absoluta del archivo de carga
	 * @param esArchivo .
	 * @return Colección de objetos "palabra"
	 * @throws Exception .
	 */
	Collection<IunidadDeHabla> modoCarga(String rutaOlinea, boolean esArchivo)
			throws Exception;
	
	/**
	 * Método que permite obtener una colección ordenada de palabras para su
	 * reproducción.
	 * 
	 * @param rutaOlinea 
	 *            String que contiene una ruta absoluta de archivo a reproducir,
	 *            ó bien directamente un string con texto a ser reproducido.
	 * @param esArchivo
	 *            boolean que permite decidir si el String pasado por parámetro
	 *            es un archivo o un texto.
	 * @return Se devuelve una colección ordenada con los elementos a
	 *         pronunciarse
	 * @throws Exception .
	 */
	Collection<IunidadDeHabla> modoLectura(String rutaOlinea, boolean esArchivo)
			throws Exception;
	
}
