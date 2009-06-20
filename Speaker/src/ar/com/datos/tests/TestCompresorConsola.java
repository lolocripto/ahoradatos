package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.CompresorConsola;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestCompresorConsola {

	public static void main (String[] args){
			
		//String archivoPlano = "./poemasreturns.txt";
		String archivoPlano = "poemasLzp2.txt";
		String archivoComp = "./poemaslzp2testPpmc.comp";
		String destinoDescomp = "./poemaslzp2testPpmc.txt";
		
		//CompresorConsola.comprimir(MetodoCompresion.PPMC, archivoPlano, archivoComp);	
		CompresorConsola.descomprimir(MetodoCompresion.PPMC, archivoComp, destinoDescomp);
	}
}
