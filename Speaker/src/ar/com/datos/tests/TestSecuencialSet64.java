package ar.com.datos.tests;

import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.registros.RegistroFTRS;

public class TestSecuencialSet64 {

public static void main (String[] args){
		
		ArchivoSecuencialSet miArchivo = new ArchivoSecuencialSet();
		
		System.out.println(miArchivo.getCantBloquesUsados());
		
		Clave clave = new Clave();
		clave.setClave("mandarina");

		BloqueFTRS miBloque = miArchivo.leerBloque(1);
		
		RegistroFTRS miRegistro = miBloque.buscarRegistro(clave);
		
		System.out.println(miRegistro.getBloqueListaInvertida());
		
	//	miRegistro.setBloqueListaInvertida(new Long(9090));
		
	//	miArchivo.escribirBloque(miBloque, 1);
		
		miArchivo.cerrar();
}
}
