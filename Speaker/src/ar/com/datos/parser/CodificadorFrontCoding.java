package ar.com.datos.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.ClaveFrontCoding;

public class CodificadorFrontCoding {
	
	
	/**
	 * Metodo que dado una serie de palabras las codifica 
	 * @param palabras
	 * @return Collection de claves en FrontCoding.
	 */
	public static Collection<ClaveFrontCoding> codificar(final Collection listaPalabras) {
		
		/*Ordeno alfabeticamente, ver
		Collections.sort(listaPalabras);
		*/
		Iterator iterador = listaPalabras.iterator();
		Collection<ClaveFrontCoding> claves = new ArrayList<ClaveFrontCoding>();
		String anterior = new String();
		while (iterador.hasNext()) {
			String actual = iterador.next().toString();
		    int caracterescoincidentes = coincidentes(actual, anterior);
		    String termino = actual.substring(caracterescoincidentes, actual.length());
		    claves.add(new ClaveFrontCoding(caracterescoincidentes, termino.length(), termino));
		    anterior = actual;	
		}
	   return claves;
	}	
	
	/**
	 * Compara la cantidad de caracteres coincidentes en orden de s1, desde el comienzo, de s2.
	 * @param s1,s2
	 * @return cantidad de caracteres coincidentes
	 */
    private static int coincidentes(String s1, String s2){
    	if (s2.isEmpty())
    		return 0;
    	char[] caracteresS1 = s1.toCharArray();
    	char[] caracteresS2 = s2.toCharArray();
    	boolean coincidentes = true;
    	int cantidad = 0;
    	int i = 0;
    	while (coincidentes && (i < caracteresS1.length - 1 || i < caracteresS2.length - 1)) {
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
	 * @param linea
	 * @return
	 */
	public static String[] decodificar(String linea) {
		char[] caracteresLinea = linea.toCharArray();
		int i = 0;
		String[] listaPalabras = null ;
		while (i< caracteresLinea.length) {
 		 int caracteres = caracteresLinea[i];
 		 String terminoAnterior = new String();
 		 i++;
		 int longitud = caracteresLinea[i];
		 String terminoActual = new String(caracteresLinea,i,i+longitud);
		 String terminoCompleto = new String();
		 if (caracteres != 0) {
			 terminoCompleto = terminoAnterior.substring(0, caracteres)+terminoActual;
		 }
		 else {
			 terminoCompleto = terminoActual;
		 }
		 i += longitud;
		  listaPalabras[listaPalabras.length] = terminoCompleto;
		}
		
		return listaPalabras;
	}
	    
}
