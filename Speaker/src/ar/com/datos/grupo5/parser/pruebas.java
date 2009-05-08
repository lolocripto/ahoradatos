package ar.com.datos.grupo5.parser;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;


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
	
