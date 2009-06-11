package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

import java.io.IOException;
import java.util.ArrayList;

public class pruebaAritmetico {

	 public static void main(String args[]) throws IOException {
		 
		//A comprimir BAABCA 
		LogicaAritmetica la = new LogicaAritmetica();
		
		ArrayList<ParCharProb>lista = new ArrayList<ParCharProb>();
		
		double n1 = 3;
		double n2 = 6;
		ParCharProb p1 = new ParCharProb('a',n1/n2);
		
		n1 = 2;
		ParCharProb p2 = new ParCharProb('b',n1/n2);
		
		n1 = 1;
		ParCharProb p3 = new ParCharProb('c',n1/n2);
		
		lista.add(p1);
		lista.add(p2);
		lista.add(p3);

		String salida="";
		salida = la.comprimir(lista, 'b');
		System.out.println(salida);
		
		salida += la.comprimir(lista, 'a');
		System.out.print(salida);
	
		salida += la.comprimir(lista, 'a');
		System.out.println(salida);
		
		salida += la.comprimir(lista, 'b');
		System.out.println(salida);

		salida += la.comprimir(lista, 'c');
		System.out.println(salida);
		
		salida += la.comprimir(lista, 'a');
		System.out.println(salida);

		salida += la.finalizarCompresion();
		System.out.println(salida);
		
		
	 }
	
	
	
}
