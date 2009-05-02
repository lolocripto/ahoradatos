package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.parser.CodificadorFrontCoding;
import ar.com.datos.parser.ITextInput;
import ar.com.datos.parser.TextInterpreter;

public class TestFrontCoding {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String terminos = new String("codazo codearse codera codicia codiciar codiciosa codicioso codificar codigo caca dale dar dare digale");
		ITextInput tratadorDeTexto = new TextInterpreter();
		try {
			Collection<IunidadDeHabla> texto = tratadorDeTexto.modoCarga(terminos, false);
			Collection<ClaveFrontCoding>  claves = CodificadorFrontCoding.codificar(texto);
			Iterator<ClaveFrontCoding> iterador = claves.iterator();
			while (iterador.hasNext()) {
				System.out.println(((ClaveFrontCoding)iterador.next()).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		
	}
}
