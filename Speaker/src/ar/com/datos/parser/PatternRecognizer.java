package ar.com.datos.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.datos.grupo5.Constantes;

public class PatternRecognizer {
	
	/**
	 * Método que limpia cualquier cadena de caracteres pasada por parámetro,
	 * modificando todos aquellos caracteres considerados inválidos. Son
	 * considerados caracteres válidos, todos aquellos que aporten datos
	 * pronunciables a la cadena de texto tomado como base el lenguaje Español.
	 * Ejemplo de uso, se toma: â,ã,ä,å y que no tienen sentido real y se
	 * reemplazan por "a".
	 * 
	 * @param linea
	 *            String con la cadena a limpiar convertido a minúsculas
	 * @return String con la cadena purificada
	 */
	private static String analisisLexico(String linea) {

		Pattern patron;
		Matcher comparador;

		// busco si hay problemas que arreglar
		patron = Pattern.compile("[âãäåàèêëìîïòôõöùû~]");
		comparador = patron.matcher(linea);
		if (comparador.find()) {
			linea = correctorLexico(linea);
		}
		return linea;
	}
    /**
     * Metodo que pasado un string modifica cambiando tildes
     * y/o caracteres invalidos relacionados principalmente
     * con vocales o caracteres sin sentido en el iodoma español.
     * Devuelve la string corregida  
     * @param termino
     * @return String
     */
	private static String correctorLexico(String termino) {
		Pattern patron;
		Matcher comparador;

		// ---Reemplazo las a---------
		patron = Pattern.compile("[âãäå]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("a");

		patron = Pattern.compile("[à]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("á");

		// ---Reemplazo las e---------
		patron = Pattern.compile("[è]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("é");

		patron = Pattern.compile("[êë]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("e");

		// ---Reemplazo las i---------
		patron = Pattern.compile("[ì]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("í");

		patron = Pattern.compile("[îï]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("i");

		// ---Reemplazo las o---------
		patron = Pattern.compile("[ò]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("ó");

		patron = Pattern.compile("[ôõö]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("o");

		// ---Reemplazo las u---------
		patron = Pattern.compile("[ù]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("ú");

		patron = Pattern.compile("[û]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("u");

		// reemplazo caracteres sin sentido en español
		patron = Pattern.compile("[~]");
		comparador = patron.matcher(termino);
		termino = comparador.replaceAll("");
		return termino;
	}
    /**
     * Permite separar en la linea pasada por parametro las palabras y numeros
     * de los signos de puntuacion y caracteres de escape para que al momento
     * de partir la línea se puedan capturar tanto palabras, numeros, simbolos
     * y signos de puntuacion.
     * @param linea
     * @return
     */
	private static String correctorSintactico(String linea) {

		Pattern patron;
		Matcher comparador;

		/*
		 * Expresión regular para todos los caracteres que no sean una letra o
		 * un numero excluyendo a la ñ, las vocales acentuadas, la "u" con
		 * diéresis y, por supuesto el espacio mismo
		 */
		String regEx = "[[^a-zA-Z_]&&[^ñáé~íóúü\\s\\/]]";

		patron = Pattern.compile(regEx);
		comparador = patron.matcher(linea);

		StringBuffer sb = new StringBuffer();
		String caracterHallado;

		while (comparador.find()) {
			caracterHallado = comparador.group();
			comparador.appendReplacement(sb, " " + caracterHallado + " ");
		}
		comparador.appendTail(sb);
		return sb.toString();

	}
	
	
	 /**
     * Permite separar en la linea pasada por parametro las palabras y numeros
     * de los signos de puntuacion y caracteres de escape para que al momento
     * de partir la línea se puedan capturar tanto palabras, numeros, simbolos
     * y signos de puntuacion.
     * @param linea
     * @return
     */
	private static String analisisContextual(String linea) {

		Pattern patron;
		Matcher comparador;

		/*
		 * (?<=[a-z])\\.(?=[a-z])) machea un punto que este antecedido por una letra
		 * y precedido por otra letra
		 * ((?<=\\D)\\,(?=\\D)) machea una coma que este antecedida por cualquier
		 * cosa menos un numero y precedida por cualquier cosa menos un numero
		 * 
		 */
		String regEx = "((?<=[a-z])\\.(?=[a-z]))|((?<=\\d)\\,(?=\\d))";

		patron = Pattern.compile(regEx);
		comparador = patron.matcher(linea);

		StringBuffer sb = new StringBuffer();
		String caracterHallado;

		while (comparador.find()) {
			caracterHallado = comparador.group();
			if (caracterHallado.equalsIgnoreCase(",")){
			comparador.appendReplacement(sb, " " +"å"+" ");
			}else{
				comparador.appendReplacement(sb, " " +"~"+" ");
			}
		}
		comparador.appendTail(sb);
		return sb.toString();

	}
	
	
	
	
    /**Metodo que separa la linea pasada por parametro
     * y la separa en strings las palabras usando como separador
     * el espacio en blanco o el caracter \s
     * @param linea
     * @return
     */
	private static String[] splitter(String linea) {
		linea = linea.trim();
		return linea.split("(\\s)+");
	}

	/**
	 * Método que permite detectar palabras Homónimas, es decir que suenan igual
	 * pero se escriben diferente.
	 */
	public static String posProcesadorFonetico(String linea) {

		Pattern patron;
		Matcher comparador;

		String regEx = Constantes.ESCAPES_REGEX;

	//	String regEx = "(l{2})";
		
		patron = Pattern.compile(regEx);
		comparador = patron.matcher(linea);

		StringBuffer sb = new StringBuffer();
		String stringHallado;

		while (comparador.find()) {
			stringHallado = comparador.group();
			comparador.appendReplacement(sb, caracterEscape(stringHallado));
		}
		comparador.appendTail(sb);
		return sb.toString();

	}
    /**
     * Metodo que recibe una String por parametro y la modifica
     * segun su fonetica para optimizar el guardado de palabras evitando 
     * paralabras foneticamente reptidas. Devuelve el string modificado
     * @param hallado
     * @return
     */
	private static String caracterEscape(String hallado) {

		String caracterDeEscape = "";

		if ((!hallado.isEmpty()) && (hallado != null) ){
		switch (hallado.charAt(0)) {
		case 'l': { //si encuentra 'LL' lo cambia por 'y'
			caracterDeEscape = "y";
			break;
		}
		case 'b': { //si encuentra b, lo cambia por v
			caracterDeEscape = "v";
			break;
		}
		case 'h': { //si encuentra h, la quita
			caracterDeEscape = "";
			break;
		}
		case 'm': { //si encuentra mb lo cambia a nv
			caracterDeEscape = "nv";
			break;
		}
		case 'j': { 
			if (hallado.charAt(1) == 'e') {
				caracterDeEscape = "ge"; //je por ge
			} else {
				caracterDeEscape = "gi"; //ji por gi
			}
			break;
		}
		case 'c': {
			if (hallado.charAt(1) == 'e') {
				caracterDeEscape = "se"; //ce por se
			} else {
				caracterDeEscape = "si"; //ci po si
			}
			break;
		}
		case 'z': {
			if (hallado.charAt(1) == 'a') {
				caracterDeEscape = "sa"; //za por sa
			} else {
				caracterDeEscape = "su"; //zu por su
			}
			break;
		}
		case '.': {
			caracterDeEscape = "punto"; 
			break;
		}

		case ',': {
			caracterDeEscape = "coma";
			break;
		}
		case '-': {
			caracterDeEscape = "menos";
			break;
		}
		case '+': {
			caracterDeEscape = "mas"; 
			break;
		}
		
		case '=': {
			caracterDeEscape = "igual";
			break;
		}
		case '$': {
			caracterDeEscape = "pesos";
			break;
		}
		case '*': {
			caracterDeEscape = "asterisco"; 
			break;
		}
		case '%': {
			caracterDeEscape = "porciento";
			break;
		}
		
		case '#': {
			caracterDeEscape = "numeral";
			break;
		}

		case '@': {
			caracterDeEscape = "arroba"; 
			break;
		}
		case '1': {
			caracterDeEscape = "uno"; 
			break;
		}
		case '2': {
			caracterDeEscape = "dos"; 
			break;
		}

		case '3': {
			caracterDeEscape = "tres"; 
			break;
		}

		case '4': {
			caracterDeEscape = "cuatro"; 
			break;
		}
		case '5': {
			caracterDeEscape = "cinco"; 
			break;
		}
		case '6': {
			caracterDeEscape = "seis"; 
			break;
		}
		
		case '7': {
			caracterDeEscape = "siete"; 
			break;
		}

		case '8': {
			caracterDeEscape = "ocho"; 
			break;
		}
		case '9': {
			caracterDeEscape = "nueve"; 
			break;
		}
		case '0': {
			caracterDeEscape = "cero"; 
			break;
		}
		
		default:
			break;
		}
		}
		return caracterDeEscape;
	}
    /**
     * Metodo que recibe una string por parametro y la separa en strings mas pequeñas 
     * separando por las palabras por expresiones regulares 
     * @param lineaEntrada
     * @return
     */
 	public static String[] procesarLinea(String lineaEntrada) {

 		//paso a minusculas
		lineaEntrada = lineaEntrada.toLowerCase(); 
		
		 //saco caracteres inválidos
		lineaEntrada = analisisLexico(lineaEntrada);
		
		//Detecto puntos y comas que deben pronunciarse
		lineaEntrada = analisisContextual(lineaEntrada);
		
		//separo las unidades pronunciables con espacios
		lineaEntrada = correctorSintactico(lineaEntrada);
		
		//corto el string en los espacios vacios
		return splitter(lineaEntrada);

	}
    /** Metodo que recibe una String e indica si la string esta vacia
     * @param texto
     * @return
     */	
	public static boolean esLineaVacia (String texto){
		
		if ((texto == "")||(texto=="\n")){
			return true;
		}else return false;
		
	}
}
