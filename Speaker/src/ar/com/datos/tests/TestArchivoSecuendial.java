/**
 * 
 */
package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroFTRS;

/**
 * @author Led Zeppelin
 *
 */

public class TestArchivoSecuendial {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
	Collection<CFFTRS> listaPalabras = new ArrayList<CFFTRS>();
	Collection<RegistroFTRS> registros = new ArrayList<RegistroFTRS>();
	
	CodificadorFrontCoding codi = new CodificadorFrontCoding();
	
	ArchivoSecuencialSet archivo = new ArchivoSecuencialSet();
	
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
	
	BloqueFTRS bloque = new BloqueFTRS((ArrayList<CFFTRS>) listaPalabras);
	
	/* Grabo el registro en el archivo del Secuencial set */
	archivo.escribirBloque(bloque, 1);
	
	/* Leo el registro en el secuencial set */
	bloque = archivo.leerBloque(1);
	
	//bloque = new BloqueFTRS();
	
	listaPalabras = bloque.getListaTerminosSinFrontCodear();

	Iterator<CFFTRS> it1 = listaPalabras.iterator();
	
	CFFTRS reg2;
	while (it1.hasNext()) {
		 reg2 = it1.next();
		 System.out.println(reg2.getPalabraDecodificada());
	}
}
}
