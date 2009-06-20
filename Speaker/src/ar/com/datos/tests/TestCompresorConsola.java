package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.CompresorConsola;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestCompresorConsola {

	public static void main (String[] args){
			
		//String archivoPlano = "./poemasreturns.txt";
		String archivoPlano = "poemasLzp2.txt";
		String archivoComp = "poemasLzp2.lz78";
		String destinoDescomp = "poemasLzp2.lz78.desc";
		
		//CompresorConsola.comprimir(MetodoCompresion.LZ78, archivoPlano, archivoComp);	
		CompresorConsola.descomprimir(MetodoCompresion.LZ78, archivoComp, destinoDescomp);
	}
}
