package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.CompresorConsola;
import ar.com.datos.grupo5.parser.Parser;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestCompresorConsola {

	
	public static void main (String[] args){
		
		
		
		String archivoPlano = "./poemasreturns.txt";
		String archivoComp = "./poemasreturns.comp";
		String destinoDescomp = "./poemasreturnsDesc.txt";
		

	//	CompresorConsola.comprimir(MetodoCompresion.PPMC, archivoPlano, archivoComp);

		CompresorConsola.descomprimir(MetodoCompresion.PPMC, archivoComp, destinoDescomp);
		
		
	}
	
}
