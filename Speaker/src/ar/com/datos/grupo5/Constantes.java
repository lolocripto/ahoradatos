package ar.com.datos.grupo5;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Constantes de la aplicacion.
 * @author LedZeppeling
 *
 */
public final class Constantes {
	
	/**
	 * Constructor privado.
	 */
	private Constantes() {
		super();
	}

	/**
	 * Tamaño en bits de un byte.
	 */
	private static final int SIZE_OF_BYTE = 8;

	/**
	 * Tamaño en bytes del bloque del archivo de indice.
	 */
	public static final int SIZE_OF_INDEX_BLOCK = Integer
	.parseInt(getXML("SIZE_OF_INDEX_BLOCK"));

	/**
	 * Tamaño del orden del  Trie.
	 */
	public static final int SIZE_OF_TRIE = Integer
	.parseInt(getXML("SIZE_OF_TRIE"));	
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
	public static final String ABRIR_PARA_LECTURA = "r";
	
	/**
	 * Abrir un archivo para lectura y escritura.
	 */
	public static final String ABRIR_PARA_LECTURA_ESCRITURA = "rw";
		
    /**
     * Tamaño en bytes del long.
	 */
	public static final int SIZE_OF_LONG = Long.SIZE / SIZE_OF_BYTE;
	
	/**
	 * Tamaño en bytes del int.
	 */
	public static final int SIZE_OF_INT = Integer.SIZE / SIZE_OF_BYTE;
	
	/**
     * Tamaño en bytes del short.
	 */
	public static final int SIZE_OF_SHORT = Short.SIZE / SIZE_OF_BYTE;
	
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
	public static final boolean SPANISH_OPTIMIZATION_ACTIVATED = 
		(getXML("SPANISH_OPTIMIZATION_ACTIVATED")
			.equalsIgnoreCase("true")) ? true : false;
	
	/**
	 * Codificación por defecto utilizada en el parser.
	 */
	public static final String DEFAULT_TEXT_INPUT_CHARSET =
		getXML("DEFAULT_TEXT_INPUT_CHARSET");
	
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
	public static final String ARCHIVO_DICCIONARIO = 
		getXML("ARCHIVO_DICCIONARIO");
	
	/**
	 * .
	 */
	public static final String ESCAPES_REGEX = getXML("ESCAPES_REGEX");

	/**
	 * Archivo que usa el diccionario para guardar las palabras.
	 */
	public static final String ARCHIVO_AUDIO = getXML("ARCHIVO_AUDIO");
	
	/**
	 * Lee delarchivo de configuracion.
	 * 
	 * @param nombre clave para recuperar el valor.
	 * @return El valor leido.
	 */
	public static String getXML(final String nombre) {

		if ((builder == null) || (doc == null)) {
			try {
				builder = new SAXBuilder(false);

				doc = builder.build("SpeakerConfig.xml");
				raiz = doc.getRootElement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Element e = raiz.getChild(nombre);
		return e.getTextTrim();

	}
}
