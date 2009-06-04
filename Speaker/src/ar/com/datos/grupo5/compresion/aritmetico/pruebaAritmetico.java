package ar.com.datos.grupo5.compresion.aritmetico;

import java.io.IOException;
import java.util.ArrayList;

public class pruebaAritmetico {

	 public static void main(String args[]) throws IOException {
//		    int n = 170; // 10101010

//		    System.out.println("Value in binary: 10101010");
//
//		    System.out.println("Number of one bits: " + Integer.bitCount(n));
//
//		    System.out.println("Highest one bit: " + Integer.highestOneBit(n));
//
//		    System.out.println("Lowest one bit: " + Integer.lowestOneBit(n));
//
//		    System.out.println("Number of leading zeros : " + Integer.numberOfLeadingZeros(n));
//
//		    System.out.println("Number of trailing zeros : " + Integer.numberOfTrailingZeros(n));
//
//		    System.out.println("\nBeginning with the value 1, " + "rotate left 16 times.");
//		    n = 1;
//		    for (int i = 0; i < 16; i++) {
//		      n = Integer.rotateLeft(n, 1);
//		      System.out.println(n);
//		    }
		    
//		    
//		    String s = "1010";
//		    
//		    int a = Integer.parseInt(s, 2);
//		    
//		    System.out.println(a);
//
//		  }
		 
		 
		LogicaAritmetica la = new LogicaAritmetica();
		
		ArrayList<ParCharProb>lista = new ArrayList<ParCharProb>();
		
		ParCharProb p1 = new ParCharProb('a',0.2F);
		
		ParCharProb p2 = new ParCharProb('f',0.25F);
		
		ParCharProb p3 = new ParCharProb(';',0.13F);
		
		ParCharProb p4 = new ParCharProb('u',0.18F);
		
		ParCharProb p5 = new ParCharProb('8',0.21F);
		
		ParCharProb p6 = new ParCharProb('w',0.03F);
		
		lista.add(p1);
		lista.add(p2);
		lista.add(p3);
		lista.add(p4);
		lista.add(p5);
		lista.add(p6);

		System.out.print(la.comprimir(lista, 'a'));
	
		System.out.print(la.comprimir(lista, 'f'));
		
		System.out.print(la.comprimir(lista, 'u'));
		
		System.out.print(la.finalizarCompresion());
		 
	 }
	
	
	
}
