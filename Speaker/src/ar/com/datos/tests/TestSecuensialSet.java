package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroFTRS;

public class TestSecuensialSet {

	
	public static void main (String[] args){
		
		
		
	ArchivoSecuencialSet miArchivo = new ArchivoSecuencialSet();
		
		
	//instancio una parva de objetos para guardar en el bloque
	
	
	Collection<CFFTRS> listaPalabras1 = new ArrayList<CFFTRS>();
	
	Collection<CFFTRS> listaPalabras2 = new ArrayList<CFFTRS>();
	
	Collection<CFFTRS> listaPalabras3 = new ArrayList<CFFTRS>();
	
	Collection<CFFTRS> listaPalabras4 = new ArrayList<CFFTRS>();
	
	
	
	
	
	CodificadorFrontCoding codi = new CodificadorFrontCoding();
	

	
	CFFTRS reg = new CFFTRS();
	reg.setPalabraDecodificada("mandarina");
	listaPalabras1.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("manzana");
	listaPalabras1.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("mamadera");
	listaPalabras1.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("mamacora");
	listaPalabras1.add(reg);
	
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("corbata");
	listaPalabras2.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("corbina");
	listaPalabras2.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("colorada");
	listaPalabras2.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("camaron");
	listaPalabras2.add(reg);
	
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("lazarillo");
	listaPalabras3.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("lamer");
	listaPalabras3.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("lamela");
	listaPalabras3.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("lampara");
	listaPalabras3.add(reg);
	
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("formalidad");
	listaPalabras4.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("fortachon");
	listaPalabras4.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("foz");
	listaPalabras4.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("fulbo");
	listaPalabras4.add(reg);
	
	
	
	BloqueFTRS bloque1 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras1);
	bloque1.setPunteroAlSiguiente(2);
	
	
	BloqueFTRS bloque2 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras2);
	bloque2.setPunteroAlSiguiente(3);
	
	BloqueFTRS bloque3 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras3);
	bloque3.setPunteroAlSiguiente(4);
	
	BloqueFTRS bloque4 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras4);
	bloque4.setPunteroAlSiguiente(0);
	
/*	
	ArrayList<BloqueFTRS> lista = new ArrayList<BloqueFTRS>();
	
	lista.add(bloque1);
	lista.add(bloque2);
	lista.add(bloque3);
	lista.add(bloque4);
*/	
	
	miArchivo.reservarBloqueLibre();
	miArchivo.escribirBloque(bloque1, 1);
	miArchivo.reservarBloqueLibre();
	miArchivo.escribirBloque(bloque2, 2);
	miArchivo.reservarBloqueLibre();
	miArchivo.escribirBloque(bloque3, 3);
	miArchivo.reservarBloqueLibre();
	miArchivo.escribirBloque(bloque4, 4);

	

	
	for (int i = 1; i < 5; i++) {
		
		BloqueFTRS bloque = miArchivo.leerBloque(i);
		
		System.out.println(bloque.getPunteroAlSiguiente());
		
		Collection<RegistroFTRS> lista = bloque.getListaTerminosFrontCodeados();
		
		Collection<CFFTRS> listaPalabras = bloque.getListaTerminosSinFrontCodear();

		Iterator<CFFTRS> it1 = listaPalabras.iterator();
		Iterator<RegistroFTRS> it2 = lista.iterator();
		
		
		System.out.println(" ");
		System.out.println("Bloque: "+ i);
		System.out.println(" ");
		
		CFFTRS reg2;
		RegistroFTRS reg3;
		while (it1.hasNext()) {
			 reg2 = it1.next();
			 reg3 = it2.next();
			 
			 System.out.println(reg2.getPalabraDecodificada());
			 System.out.println(reg3.getBloqueListaInvertida());
			 System.out.println(reg2.getPalabraCodificada());
			 System.out.println(reg3.getBloqueListaInvertida());
			 System.out.println(reg3.getIdTermino());
			 System.out.println(reg3.getClaveFrontCoding().getTermino());
			 
		}
		System.out.println(" ");
	}
	
	
	
	miArchivo.cerrar();
	
	}
	
	
	
}
