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


		String ruta = "/home/zeke/Escritorio/problemafax";
		Parser miparser = new Parser(ruta, false);
		Collection<IunidadDeHabla> micolec = miparser.listar();
		
		Iterator<IunidadDeHabla> it = micolec.iterator();

	
	    while (it.hasNext()){
	    	
	    	System.out.println(it.next().toString());
	    }
	    	
	}
	
	
	
	
}
	
