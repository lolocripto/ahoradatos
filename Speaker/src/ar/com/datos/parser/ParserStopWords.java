package ar.com.datos.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.Constantes;

/** 
 * Clase que utilizada para el filtrado de stop words.
 * @author LedZeppelin
 *
 */


public class ParserStopWords {

	/**
	 * Metodo que falta hacer el javadoc je.
	 * @param palabras
	 * @return Collection
	 */
	public final Collection<IunidadDeHabla> filtroStopWords(final Collection<IunidadDeHabla> palabras) {
	    Collection<IunidadDeHabla> StopWords = this.CargaStopWords();
	    Collection<IunidadDeHabla> auxpalabras = palabras;
	    Iterator<IunidadDeHabla> iterador = StopWords.iterator();
	    while (iterador.hasNext()) {
	    	IunidadDeHabla unidad = iterador.next();
		    System.out.println(unidad.getTextoEscrito());
		    System.out.println(auxpalabras.contains(unidad));	
	    }
	    return auxpalabras;
	  
	}
	
	/**
	 * Indica si la palabra pertenece a la lista de stop words.
	 * @param palabra
	 * @return
	 */
	private boolean IsStopWord(String palabra){
		return false;
	}
	/** 
	 * Metodo que levanta el archivo de stop words en una colleccion.
	 * @return
	 */
	private Collection<IunidadDeHabla> CargaStopWords(){
		 Collection<IunidadDeHabla> ListaStopWord = new ArrayList<IunidadDeHabla>();
		File archivo = new File("stop_words");
		FileInputStream fis;
		try {
			fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			BufferedReader buffer = new BufferedReader(isr);
			 String stopwords =  buffer.readLine();
			 String listastop[] = stopwords.split("\\s");
			 for (int i = 0; i < listastop.length; i++){
				 ListaStopWord.add(PalabrasFactory.getPalabra(listastop[i]));
			 }
			 				 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return ListaStopWord;
		
	}

}
