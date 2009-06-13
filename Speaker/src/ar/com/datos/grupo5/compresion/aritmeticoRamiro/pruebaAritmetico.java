package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

import java.io.IOException;
import java.util.ArrayList;

import ar.com.datos.grupo5.Constantes;

public class pruebaAritmetico {

	 public static void main(String args[]) throws IOException {
		 
		//A comprimir BAABCA 
		LogicaAritmetica la = new LogicaAritmetica();
		
		ArrayList<ParCharProb>lista = new ArrayList<ParCharProb>();
		
		double n1 = 3;
		double n2 = 7;
		ParCharProb p1 = new ParCharProb('a',n1/n2);
		
		n1 = 2;
		ParCharProb p2 = new ParCharProb('b',n1/n2);
		
		n1 = 1;
		ParCharProb p3 = new ParCharProb('c',n1/n2);
		
		n1 = 1;
		ParCharProb p4 = new ParCharProb(Constantes.EOF,n1/n2);
		lista.add(p1);
		lista.add(p2);
		lista.add(p3);
		lista.add(p4);
		
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

		salida += la.comprimir(lista, Constantes.EOF);
		System.out.println(salida);

		salida += la.finalizarCompresion();
		System.out.println(salida);

		la = new LogicaAritmetica(); 
		String bits = new String("01110110011010100011111110100100100000000000");
		StringBuffer bitsBuffer = new StringBuffer(bits);
		salida = "0";
		while (salida != null && !Constantes.EOF.equals(salida.charAt(salida.length()-1))){
			if (Constantes.EOF.equals(salida.charAt(salida.length()-1))) {
				System.out.println("Son iguales");
			} else {
				System.out.println("No son iguales");
			}
			System.out.println("Salida: " + salida);
			salida += la.descomprimir(lista, bitsBuffer).toString();
			System.out.println("buffer: " + bitsBuffer);
		}
		System.out.println("descompresion terminada: " + salida);
	 }
	
	
	
}
