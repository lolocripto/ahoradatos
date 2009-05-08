package ar.com.datos.grupo5.parser;

//import gnu.java.beans.editors.StringEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;

public class CodificadorFrontCoding {

	/**
	 * Metodo que dado una serie de palabras las codifica.
	 * 
	 * @param listaPalabras jj.
	 * @return Collection de claves en FrontCoding.
	 */
	public static Collection<ClaveFrontCoding> codificar(
			final Collection<IunidadDeHabla> listaPalabras) {

		/*
		 * Ordeno alfabeticamente, ver Collections.sort(listaPalabras);
		 */
		Iterator iterador = listaPalabras.iterator();
		Collection<ClaveFrontCoding> claves = new ArrayList<ClaveFrontCoding>();
		String anterior = new String();
		while (iterador.hasNext()) {
			String actual = iterador.next().toString();
			int caracterescoincidentes = coincidentes(actual, anterior);
			String termino = actual.substring(caracterescoincidentes, actual
					.length());
			int nroBloque = 0; /*ver de donde obtener el nro de bloque*/
			claves.add(new ClaveFrontCoding(caracterescoincidentes,
					termino.length(), termino));
			anterior = actual;
		}
		return claves;
	}

	/**
	 * Compara la cantidad de caracteres coincidentes en orden de s1, desde el
	 * comienzo, de s2.
	 * 
	 * @param s1,s2
	 * @return cantidad de caracteres coincidentes
	 */
	private static int coincidentes(String s1, String s2) {
		if (s2.isEmpty())
			return 0;
		char[] caracteresS1 = s1.toCharArray();
		char[] caracteresS2 = s2.toCharArray();
		boolean coincidentes = true;
		int cantidad = 0;
		int i = 0;
		while (coincidentes
				&& (i < caracteresS1.length - 1 || i < caracteresS2.length - 1)) {
			coincidentes = (caracteresS1[i] == caracteresS2[i]);
			if (coincidentes) {
				cantidad++;
			}
			i++;
		}
		return cantidad;
	}

	/**
	 * Dada una linea en front coding decodifica.
	 * 
	 * @param linea
	 * @return
	 */
	public static Collection<String> decodificar(String linea) {
		char[] caracteresLinea = linea.toCharArray();
		int i = 0;
		Collection<String> listaPalabras = new ArrayList<String>();
		String terminoAnterior = new String();
		while (i < caracteresLinea.length) {
			Character caracter = new Character(linea.charAt(i));
			int caracteresCompartidos = Integer.parseInt(caracter.toString());
			i++;
			Character caracter2 = new Character(linea.charAt(i));
			int longitud = Integer.parseInt(caracter2.toString());
			String terminoActual = new String(caracteresLinea, i + 1, longitud);
			String terminoCompleto = new String();
			if (caracteresCompartidos != 0) {
				terminoCompleto = terminoAnterior.substring(0,
						caracteresCompartidos)
						+ terminoActual;
			} else {
				terminoCompleto = terminoActual;
			}
			i += longitud + 1;
			listaPalabras.add(terminoCompleto);
			terminoAnterior = terminoCompleto;
		}

		return listaPalabras;
	}

}
