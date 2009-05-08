package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.UnidadesDeExpresion.Palabra;
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
		String cadenaTexto = new String("Organización de Datos, entrega numero dos, viene jodida la mano che, si no nos ponemos las pilas esto explota");
		ITextInput tratadorDeTexto = new TextInterpreter();
		Collection<IunidadDeHabla> palabras = new ArrayList();
		
		Iterator<IunidadDeHabla> it = Constantes.LISTA_STOP_WORDS.iterator();
		
		System.out.println("yuhuuu:  ");
		
		 while (it.hasNext()) {
			String e = it.next().getTextoEscrito();
			System.out.println(e);
		}
			
		try {
			//FIXME: LE agregue el ultimo parametro en null porque no compilaba.
			Collection<IunidadDeHabla> texto = tratadorDeTexto.modoCarga(cadenaTexto, false,null);
			ParserStopWords parser = new ParserStopWords();
			Collection<IunidadDeHabla> filtradas = parser
					.filtroStopWords(texto);
			Collections.sort((List<IunidadDeHabla>) filtradas);
			Iterator<IunidadDeHabla> iterador = filtradas.iterator();
			while (iterador.hasNext()) {
				IunidadDeHabla unidad = iterador.next();
				String lapalabra = unidad.getEquivalenteFonetico();
				System.out.println(lapalabra);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}