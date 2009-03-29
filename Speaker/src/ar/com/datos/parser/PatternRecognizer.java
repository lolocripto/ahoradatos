package ar.com.datos.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static String correctorSintactico(String linea) {

		Pattern patron;
		Matcher comparador;

		/*
		 * Expresión regular para todos los caracteres que no sean una letra o
		 * un numero excluyendo a la ñ, las vocales acentuadas, la "u" con
		 * diéresis y, por supuesto el espacio mismo
		 */
		String regEx = "[\\W&&[^ñáéíóúü\\s]]";

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

	private static String[] splitter(String linea) {

		return linea.split("\\s");
	}

	/*
	 * Método que permite detectar palabras Homónimas, es decir que suenan igual
	 * pero se escriben diferente.
	 */
	public static String posProcesadorFonetico(String linea) {

		Pattern patron;
		Matcher comparador;

		String regEx = "((l{2}))|"
				+ // encuentra la "ll" ó la "y"
				"([b])|"
				+ // encuentra b ó v
				"((?<!c)h)|"
				+ // encuentra "h" pero no antecedida por "c"
				"((je))|"
				+ // encuentra la combinacion ge ó je
				"((ji))|" + "((mb))|" + "((ce))|" + "((ci))|" + "((za))|"
				+ "((zu))";

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

	private static String caracterEscape(String hallado) {

		String caracterDeEscape = "x";

		switch (hallado.charAt(0)) {
		case 'l': {
			caracterDeEscape = "y";
			break;
		}
		case 'b': {
			caracterDeEscape = "v";
			break;
		}
		case 'h': {
			caracterDeEscape = "";
			break;
		}
		case 'm': {
			caracterDeEscape = "nv";
			break;
		}
		case 'j': {
			if (hallado.charAt(1) == 'e') {
				caracterDeEscape = "ge";
			} else {
				caracterDeEscape = "gi";
			}
			break;
		}
		case 'c': {
			if (hallado.charAt(1) == 'e') {
				caracterDeEscape = "se";
			} else {
				caracterDeEscape = "su";
			}
			break;
		}
		case 'z': {
			if (hallado.charAt(1) == 'a') {
				caracterDeEscape = "sa";
			} else {
				caracterDeEscape = "su";
			}
			break;
		}

		default:
			break;
		}
		return caracterDeEscape;
	}

	public static String[] procesarLinea(String lineaEntrada) {

		return splitter(correctorSintactico(analisisLexico(lineaEntrada)));

	}
}
