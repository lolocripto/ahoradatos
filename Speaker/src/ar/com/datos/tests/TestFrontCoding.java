package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.parser.ITextInput;
import ar.com.datos.grupo5.parser.TextInterpreter;

public class TestFrontCoding {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String terminos = new String("codazo codearse codera codicia codiciar codiciosa codicioso codificar codigo caca dale dar dare digale");
		ITextInput tratadorDeTexto = new TextInterpreter();
		String lineaFrontCoding = new String();
		String oo = "5";
		int aa = Integer.parseInt(oo); 
		System.out.println("aaa"+aa);
		try {
			//FIXME: le agregueel ultimo parametro en null porque no compilaba.
			Collection<IunidadDeHabla> texto = tratadorDeTexto.modoCarga (terminos, false,null);
			Collection<ClaveFrontCoding>  claves = CodificadorFrontCoding.codificar(texto);
			Iterator<ClaveFrontCoding> iterador = claves.iterator();
			while (iterador.hasNext()) {
				ClaveFrontCoding clave = ((ClaveFrontCoding)iterador.next());
				System.out.println(clave.toString());
				lineaFrontCoding+=clave.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<String> lista = CodificadorFrontCoding.decodificar(lineaFrontCoding);
		Iterator<String> iterador = lista.iterator();
		while (iterador.hasNext()) {
			System.out.println(iterador.next());
		}		
	}
}
