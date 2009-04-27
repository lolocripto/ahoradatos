package ar.com.datos.grupo5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.UnidadesDeExpresion.Palabra;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.parser.ParserStopWords;
import ar.com.datos.parser.TextInterpreter;

public class TestStopWords {

	/**
	 * Clase de prueba para la generacion de Front Coding sobre una cadena de
	 * texto. Recien se hizo lo de las stop words nada mas
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		String cadenaTexto = new String("Organización de Datos, entrega numero dos, viene jodida la mano che, si no nos ponemos las pilas esto explota");
		ITextInput tratadorDeTexto = new TextInterpreter();
		Collection<IunidadDeHabla> palabras = new ArrayList();
		try {
			palabras = tratadorDeTexto.modoCarga(cadenaTexto, false);
			ParserStopWords parser = new ParserStopWords();
			Collection<IunidadDeHabla> filtradas = parser
					.filtroStopWords(palabras);
			Iterator<IunidadDeHabla> iterador = filtradas.iterator();
			while (iterador.hasNext()) {
				IunidadDeHabla unidad = iterador.next();
				String lapalabra = unidad.getEquivalenteFonetico();
				System.out.print("sw= " + lapalabra + "\n");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
