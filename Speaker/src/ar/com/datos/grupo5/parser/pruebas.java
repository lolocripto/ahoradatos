package ar.com.datos.grupo5.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.Constantes;

public class pruebas {

	
	public static void main(String Args[]) throws Exception {


		
		String ruta = "/home/zeke/Escritorio/test_1.txt";
		Parser miparser = new Parser(ruta, true);

		Collection<IunidadDeHabla> micolec = miparser.listar();
		
		Iterator<IunidadDeHabla> it = micolec.iterator();

	
		Boolean a = Constantes.SPANISH_OPTIMIZATION_ACTIVATED;
		
	    while (it.hasNext()){
	    	IunidadDeHabla i = it.next();
	    	System.out.print(i.getEquivalenteFonetico()+ " | ");
	    	System.out.print(i.getTextoEscrito()+ " | ");
	    	System.out.println(i.esPronunciable());
	    }
	
	}


		
	
//	System.out.print(uso);
	
	
	
	
}
	
