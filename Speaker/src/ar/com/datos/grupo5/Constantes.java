package ar.com.datos.grupo5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.PalabrasFactory;


/**
 * Constantes de la aplicacion.
 * @author LedZeppeling
 *
 */
@SuppressWarnings("unchecked")
public final class Constantes {
	
	/**
	 * Constructor privado.
	 */
	private Constantes() {
		super();
	}

	
	/**
	 * Maxima longitud de Match.
	 */
	public static final int MAX_LONGITD_MATCH = 2^16 - 1;
	/**
	 * Para indicar que la clave es mayor que la ultima del nodo.
	 */
	public static final Float FACTOR_CARGA_NODOS = new Float(0.66);
	
	/**
	 * Para indicar que la clave no existe.
	 */
	public static final int NOEXISTE = -3;
	
	/**
	 * Para indicar que la clave es mayor que la ultima del nodo.
	 */
	public static final int MAYOR = -2;
	
	/**
	 * Para indicar que la clave es menor que la primera del nodo.
	 */
	public static final int MENOR = -1;
	
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
         * Tamaño en bytes del char.
	 */
	public static final int SIZE_OF_CHAR = Character.SIZE / SIZE_OF_BYTE;
	
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
	 * Tamaño del buffer de escritura.
	 */
	public static final int TAMANIO_BUFFER_ESCRITURA = Integer
			.parseInt(getXML("TAMANIO_BUFFER_ESCRITURA"));
	
	/**
	 * Tamaño del buffer para generar las particiones por replacement selection.
	 */
	public static final int TAMANIO_BUFFER_REPLACEMENT_SELECTION = Integer
			.parseInt(getXML("TAMANIO_BUFFER_REPLACEMENT_SELECTION"));
	
	/**
	 * Abrir un archivo para lectura.
	 */
	public static final String ABRIR_PARA_LECTURA = "r";
	
	/**
	 * Abrir un archivo para lectura y escritura.
	 */
	public static final String ABRIR_PARA_LECTURA_ESCRITURA = "rw";

	/**
	 * Tamaño en bytes de la informacion administrativa del nodo.
	 */
	public static final int SIZE_OF_ADMIN_NODE_DATA = 4 * SIZE_OF_INT;
	
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
	public static final String CHARSET_UTF16 = "UTF-16BE";
	
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

	
	public static final int AUTO_FLUSH_AT = Integer.
		parseInt(getXML("AUTO_FLUSH_AT"));
	
	/**
	 * Archivo que usa ListasInvertidas para guardar las listas invertidas.
	 */
	public static final String ARCHIVO_LISTAS_INVERTIDAS = 
		getXML("ARCHIVO_LISTAS_INVERTIDAS");	
	
	/**
	 * Archivo que usa ListasInvertidas para guardar las listas invertidas.
	 */
	public static final String ARCHIVO_TERMINOS_GLOBALES = 
		getXML("ARCHIVO_TERMINOS_GLOBALES");
	
	
	public static final String ARCHIVO_TRIE =
		getXML("ARCHIVO_TRIE");
	
	
	public static final int TRIE_BLOCK_SIZE = Integer.
	parseInt(getXML("TRIE_BLOCK_SIZE"));
	
	/**
	 * Tamaño para los bloques del archivo de listas invertidas.
	 */
	public static final int SIZE_OF_LIST_BLOCK = Integer.parseInt(
			getXML("SIZE_OF_LIST_BLOCK"));

	/**
	 * Define el tamaño del Bloque del Secuencial Set, en función del
	 * tamaño del de bloque del árbol
	 * @return El valor del tamaño del bloque
	 */
	private static final int loadSecuencialSetBlockSize() {
		int tamanio = Integer.parseInt(
				getXML("SIZE_OF_SECUENCIAL_SET_BLOCK"));
		if (tamanio ==3 * SIZE_OF_INDEX_BLOCK) {
			return 3 * SIZE_OF_INDEX_BLOCK;
		} else {
			return 3 * SIZE_OF_INDEX_BLOCK;
		}
	}
	
	/**
	 * Tamaño para los bloques del archivo de listas invertidas.
	 */
	public static final int SIZE_OF_SECUENCIAL_SET_BLOCK = 
		loadSecuencialSetBlockSize();
	
	/**
	 * Archivo para el arbol BStar.
	 */
	public static final String ARCHIVO_ARBOL_BSTAR = 
		getXML("ARCHIVO_ARBOL_BSTAR");
	
	/**
	 * Archivo para el arbol BStar.
	 */
	public static final String ARCHIVO_ARBOL_BSTAR_SECUENCIAL_SET = 
		getXML("ARCHIVO_ARBOL_BSTAR_SECUENCIAL_SET");
	
	/**
	 * Archivo para el arbol BStar.
	 */
	public static final Float FACTOR_CARGA_BLOQUES = Float.parseFloat(
		getXML("FACTOR_CARGA_BLOQUES"));
	
	/**
	 * Cantidad de Resultados en Consulta rankeada.
	 */
	public static final int TOP_RANKING = Integer.parseInt(
			getXML("TOP_RANKING"));

	/**
	 * Nuero de bloque de los datos arministrativos del archivo del arbol.
	 */
	public static final int NRO_BLOQUE_ADMIN = 0;
	
	/**
	 * Orden máximo con el que va a trabajar el PPMC.
	 */
	public static final int ORDER_MAX_PPMC = Integer.parseInt(
			getXML("ORDER_MAX_PPMC"));
	
	/**
	 * Valor para el caracter ESC
	 */
	public static final Character ESC = new Character('\uFFFE');  
	
	/**
	 * Valor para el caracter EOF
	 */
	public static final Character EOF = new Character('\uFFFF');  
	
	/**
	 * Nro de bloque de la raiz del arbol.
	 */
	public static final int NRO_BLOQUE_RAIZ = 1;
	
	/**
	 * Archivo para los documentos.
	 */
	public static final String ARCHIVO_DOCUMENTOS = 
		getXML("ARCHIVO_DOCUMENTOS");
	
	/**
	 * Archivo que usa el diccionario para guardar las palabras.
	 */
	public static final String ARCHIVO_TRABAJO = 
		getXML("ARCHIVO_TRABAJO");
	
	/**
	 * Metodo de compresión por defecto para los documentos.
	 */
	public static final String METODO = getXML("METODO");
	
	
	
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
	
	/**
	 * Carga un listado de palabras a filtrar.
	 * @return listado de palabras.
	 */
	private static List<IunidadDeHabla> stopWords() {

			try {
				builder = new SAXBuilder(false);
				doc = builder.build("StopWords.xml");
				raiz = doc.getRootElement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		List<Element> lista = raiz.getChildren("palabra");
		Iterator<Element> it = lista.iterator();
		ArrayList<IunidadDeHabla> listaFinal = new ArrayList<IunidadDeHabla>();

		while (it.hasNext()) {
			listaFinal.add(PalabrasFactory.getPalabraSinChequeoStop(it.next().getText()));
		}
			return listaFinal;
	}
	
	/**
	 * Lista de palabras a filtrar.
	 */
	public static final List<IunidadDeHabla> LISTA_STOP_WORDS = stopWords();

	
	/**
	 * Carga un listado de palabras a filtrar.
	 * @return listado de palabras.
	 */
	private static List<Character> alfabetoDisponible() {
		List<Character> lista = new ArrayList<Character>();
		Character letra;
		
		for (int i = 0; i < 300; i++) {
			letra = new Character(Character.toChars(i)[0]);
			if (letra != null){
				if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(letra)) {
					lista.add(letra);
				} else {
					if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(letra)) {
						lista.add(letra);
					}
				}
			}
		}
		return lista;
	}
	
	/**
	 * Lista de palabras a filtrar.
	 */
	public static final List<Character> LISTA_CHARSET_LATIN = alfabetoDisponible();
}
