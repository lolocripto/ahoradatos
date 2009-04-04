package ar.com.datos.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;

public class pruebas {

	
	public static void main(String Args[]) throws Exception {


		String ruta = "/home/zeke/Escritorio/prueba";
		Parser miparser = new Parser(ruta, true);

		Collection<IunidadDeHabla> micolec = miparser.listar();
		
		Iterator<IunidadDeHabla> it = micolec.iterator();

	
	    while (it.hasNext()){
	    	IunidadDeHabla i = it.next();
	    	System.out.print(i.getEquivalenteFonetico()+ " | ");
	    	System.out.print(i.getTextoEscrito()+ " | ");
	    	System.out.println(i.esPronunciable());
	    }
	
	}
	
	
	
	
}
	
