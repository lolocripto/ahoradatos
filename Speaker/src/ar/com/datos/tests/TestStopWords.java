package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.ITextInput;
import ar.com.datos.grupo5.parser.ParserStopWords;
import ar.com.datos.grupo5.parser.TextInterpreter;

public class TestStopWords {

	/**
	 * Clase de prueba para la generacion de Front Coding sobre una cadena de
	 * texto. Recien se hizo lo de las stop words nada mas
	 * 
	 * @param args
	 */

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
	//	String cadenaTexto = new String("Organización de Datos, entrega numero dos, viene jodida la mano che, si no nos ponemos las pilas esto explota");
		
		String cadenaTexto = new String("hola como estas");
		
		ITextInput tratadorDeTexto = new TextInterpreter();
		Collection<IunidadDeHabla> palabras = new ArrayList();
		
		Iterator<IunidadDeHabla> it = Constantes.LISTA_STOP_WORDS.iterator();
		
		try {
			Collection<IunidadDeHabla> texto = tratadorDeTexto.modoLecturaSinAlmacenamiento(cadenaTexto, false);
	
			Iterator<IunidadDeHabla> iterador = texto.iterator();
			
			while (iterador.hasNext()) {
				IunidadDeHabla unidad = iterador.next();
				String lapalabra = unidad.getTextoEscrito();
				System.out.println(lapalabra + "  es stop word?: " + unidad.isStopWord());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}