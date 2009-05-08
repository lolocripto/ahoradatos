package ar.com.datos.grupo5.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;

/** 
 * Clase que utilizada para el filtrado de stop words.
 * @author LedZeppelin
 *
 */


public class ParserStopWords {

	Collection<IunidadDeHabla> StopWords ;
	/**
	 * Metodo que falta hacer el javadoc je.
	 * @param palabras
	 * @return Collection
	 */
    public ParserStopWords() {
    	StopWords = Constantes.LISTA_STOP_WORDS;
    }
	public final Collection<IunidadDeHabla> filtroStopWords(final Collection<IunidadDeHabla> palabras) {
	    Collection<IunidadDeHabla> auxpalabras = palabras;
	    auxpalabras.removeAll(StopWords);
	    return auxpalabras;
	    
	}
	
	
	/**
	 * Indica si la palabra pertenece a la lista de stop words.
	 * @param palabra
	 * @return
	 */
	public boolean IsStopWord(IunidadDeHabla palabra){
	    return StopWords.contains(palabra);
	}

}