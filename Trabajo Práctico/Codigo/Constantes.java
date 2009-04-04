package ar.com.datos.grupo5;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Constantes de la aplicacion.
 * @author LedZeppeling
 *
 */
public class Constantes {
	
	/**
	 * 
	 */
	private static SAXBuilder builder;
	
	/**
	 * 
	 */
	private static Document doc;
	
	/**
	 * 
	 */
    private static Element raiz; 
	
	/**
	 * Tamaño del buffer de lectura.
	 */
	public static final int TAMANIO_BUFFER_LECTURA = Integer
			.parseInt(getXML("TAMANIO_BUFFER_LECTURA"));
	
	
	/**
	 * Tamaño del buffer de escritura.
	 */
	public static final int TAMANIO_BUFFER_ESCRITURA = Integer
			.parseInt(getXML("TAMANIO_BUFFER_ESCRITURA"));
	
	/**
	 * Abrir un archivo para lectura.
	 */
	public static final String ABRIR_PARA_LECTURA = getXML("ABRIR_PARA_LECTURA");
	
	/**
	 * Abrir un archivo para lectura y escritura.
	 */
	public static final String ABRIR_PARA_LECTURA_ESCRITURA = getXML("ABRIR_PARA_LECTURA_ESCRITURA");
		
    /**
     * Tamaño en bytes del long.
	 */
	public static final int SIZE_OF_LONG = Long.SIZE / 8;
	
	/**
	 * Tamaño en bytes del int.
	 */
	public static final int SIZE_OF_INT = Integer.SIZE / 8;
	
	/**
	 * Tamaño del buffer de lectura para la colección auto pagináble.
	 */
	public static final int BUFFER_LECTURA_TEXT_INPUT = Integer
			.parseInt(getXML("BUFFER_LECTURA_TEXT_INPUT"));
	
	/**
	 * Tamaño de la cache de registros.
	 */
	public static final int TAMANO_CACHE = Integer
			.parseInt(getXML("TAMANO_CACHE"));
	
	/**
	 * Esta variable permite activar o desactivar las optimizaciones realizadas
	 * para el idioma español.
	 */
	public static final boolean SPANISH_OPTIMIZATION_ACTIVATED = (getXML("SPANISH_OPTIMIZATION_ACTIVATED")
			.equalsIgnoreCase("true")) ? true : false;
	
	/**
	 * Codificación por defecto utilizada en el parser.
	 */
	public static final String DEFAULT_TEXT_INPUT_CHARSET = getXML("DEFAULT_TEXT_INPUT_CHARSET");
	
	/**
	 * Definición de juego de caracteres utf8.
	 */
	public static final String CHARSET_UTF8 = "UTF-8";
	
	/**
	 * Definición de juego de caracteres ISO.
	 */
	public static final String CHARSET_ISO = "ISO-8859-1";
	
	/**
	 * Definición de juego de caracteres UTF16.
	 */
	public static final String CHARSET_UTF16 = "UTF-16";
	
	/**
	 * Definición de juego de caracteres ASCII.
	 */
	public static final String CHARSET_ASCII = "US-ASCII";

	/**
	 * Archivo que usa el diccionario para guardar las palabras.
	 */
	public static final String ARCHIVO_DICCIONARIO = getXML("ARCHIVO_DICCIONARIO");

	/**
	 * Archivo que usa el diccionario para guardar las palabras.
	 */
	public static final String ARCHIVO_AUDIO = getXML("ARCHIVO_AUDIO");
	
	
	public static String getXML(String nombre) {

		if ((builder == null) || (doc == null)) {
			try {
				SAXBuilder builder = new SAXBuilder(false);

				doc = builder.build("SpeakerConfig.xml");
				raiz = doc.getRootElement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Element e = raiz.getChild(nombre);
		return e.getTextTrim();

	}

	public static final String ESCAPES_REGEX() {
		return getXML("ESCAPES_REGEX");
	}
	
	
	/**
	 * expresiones regulares que definen optimizaciones en el español.
	 */
/*	
	public static final String ESCAPES_REGEX =
		"((l{2}))|"     // encuentra la "LL"
		+ "([b])|"      // encuentra b
		+ "((?<!c)h)|"  // encuentra "h" pero no antecedida por "c"
		+ "((je))|"     // encuentra je 
		+ "((ji))|"     // encuentra ji
		+ "((mb))|"     // encuentra mb
		+ "((ce))|"     // encuentra ce
		+ "((ci))|"     // encuentra ci
		+ "((za))|"     // encuentra za
		+ "((zu))|"     // encuentra zu
        + "((\\.))|"    // el punto
        + "((\\,))|"    // la coma
	 	+ "((\\-))|"    // signo menos
	 	+ "((\\+))|"    // signo mas
	 	+ "((\\=))|"    // signo igual
	 	+ "((\\$))|"    // signo pesos
	 	+ "((\\*))|"    // signo asterisco
	 	+ "((\\%))|"    // signo porcentaje
	 	+ "((\\#))|"    // signo numeral
	 	+ "((\\@))|"    // signo arroba
	 	+ "(([1]))|"    // numero uno
	 	+ "(([2]))|"    // numero dos
	 	+ "(([3]))|"    // numero tres
	 	+ "(([4]))|"    // numero cuatro
	 	+ "(([5]))|"    // numero cinco
	 	+ "(([6]))|"    // numero seis
	 	+ "(([7]))|"    // numero siete
	 	+ "(([8]))|"    // numero ocho
	 	+ "(([9]))|"    // numero nueve
	 	+ "(([0]))"    // numero cero
;*/
}
