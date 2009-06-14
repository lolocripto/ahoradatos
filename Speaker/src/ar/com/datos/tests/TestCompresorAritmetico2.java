package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.excepciones.SessionException;

public class TestCompresorAritmetico2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		CompresorAritmetico comp = new CompresorAritmetico(1,true);
		
		comp.iniciarSesion();
		
		String cadena ="Hola como estas\n";
		String cadena2 ="Espero no del todo mal";
		String bits;
		
		String cadenaRestaur;
		
		
		try {
			 
			bits = comp.comprimir(cadena);
			bits += comp.comprimir(cadena2);
			bits += comp.finalizarSession();
			
		
			//descomprimir
			
			comp.iniciarSesion();
			
			StringBuffer sb = new StringBuffer(bits.substring(0, 15));
			
			cadenaRestaur = comp.StringCompleto(sb);
			
			sb = new StringBuffer(bits.substring(15));
			
			cadenaRestaur += comp.StringCompleto(sb);
			
			cadenaRestaur += comp.finalizarSession();
			
			
			System.out.println(cadenaRestaur);
			
			
			
			 
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
