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
	reg.setPalabraDecodificada("mandarota");
	listaPalabras1.add(reg);
	
	reg = new CFFTRS();
	reg.setPalabraDecodificada("cortina");
	listaPalabras2.add(reg);
	
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
	reg.setPalabraDecodificada("lamina");
	listaPalabras3.add(reg);
	
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
	reg.setPalabraDecodificada("fortuna");
	listaPalabras4.add(reg);
	
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
	
	BloqueFTRS bloque2 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras2);
	
	BloqueFTRS bloque3 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras3);
	
	BloqueFTRS bloque4 = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras4);
	
/*	
	ArrayList<BloqueFTRS> lista = new ArrayList<BloqueFTRS>();
	
	lista.add(bloque1);
	lista.add(bloque2);
	lista.add(bloque3);
	lista.add(bloque4);
*/	
	
	miArchivo.escribirBloque(bloque1, 1);
	miArchivo.escribirBloque(bloque2, 2);
	miArchivo.escribirBloque(bloque3, 3);
	miArchivo.escribirBloque(bloque4, 4);

	
	BloqueFTRS bloque = miArchivo.leerBloque(1);
	
	Collection<RegistroFTRS> listaPalabras = bloque.getListaTerminosFrontCodeados();

	Iterator<RegistroFTRS> it1 = listaPalabras.iterator();
	
	RegistroFTRS reg2;
	while (it1.hasNext()) {
		 reg2 = it1.next();
		 System.out.println(reg2.getClaveFrontCoding().getTermino());
	}
	
	
	
	
	}
	
	
	
}
