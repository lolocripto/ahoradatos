package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.CompresorConsola;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestCompresorConsola {

	public static void main (String[] args){
			
		//String archivoPlano = "./poemasreturns.txt";
		String archivoPlano = "poemasLzp2.txt";
		String archivoComp = "poemasLzp2.txt.lz78";
		String destinoDescomp = "poemasLzp2.txt.desc";
		
		CompresorConsola.comprimir(MetodoCompresion.LZP, archivoPlano, archivoComp);	
		CompresorConsola.descomprimir(MetodoCompresion.LZP, archivoComp, destinoDescomp);
	}
}
