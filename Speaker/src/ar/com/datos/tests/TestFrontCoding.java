package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroFTRS;

public class TestFrontCoding {
	
	public static void main(String[] args) {
		
		Collection<CFFTRS> listaPalabras = new ArrayList<CFFTRS>();
		Collection<RegistroFTRS> registros = new ArrayList<RegistroFTRS>();
		
		CodificadorFrontCoding codi = new CodificadorFrontCoding();
		
		
		CFFTRS reg = new CFFTRS();
		reg.setPalabraDecodificada("codazo");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("codearse");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("codera");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("codicia");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("codiciar");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("codiciosa");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("dale");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("dar");
		listaPalabras.add(reg);
		
		reg = new CFFTRS();
		reg.setPalabraDecodificada("digale");
		listaPalabras.add(reg);
		
		registros = codi.codificar(listaPalabras);
		
		Iterator<RegistroFTRS> it = registros.iterator();
		
		RegistroFTRS reg1;
		while (it.hasNext()) {
			 reg1 = it.next();
			 System.out.println(reg1.getClaveFrontCoding().getCaracteresCoincidentes());
			 System.out.println(reg1.getClaveFrontCoding().getLongitudTermino());
			 System.out.println(reg1.getClaveFrontCoding().getTermino());
		}
		
		listaPalabras = codi.decodificar(registros);

		Iterator<CFFTRS> it1 = listaPalabras.iterator();
		
		CFFTRS reg2;
		while (it1.hasNext()) {
			 reg2 = it1.next();
			 System.out.println(reg2.getPalabraDecodificada());
		}
}
}

