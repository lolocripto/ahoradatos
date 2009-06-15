package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.excepciones.SessionException;

public class TestCompresorAritmetico2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		CompresorAritmetico comp = new CompresorAritmetico(1,true);
		
		try {
			 
		    //descomprimir
			
			comp.iniciarSesion();
			
			String bits = "0100110000011100111001000001100100110001001111001000000010100100111010111110001110011011000000101101100111101111000001011101011101011011010110100101100100110000100100010000101110010111100010001001011011101001111100110100100001110011110010010101010011001101111101101001011110011100111010111100101101100010000010011010010000100110001100001111001111000111111110110010001001101101111001101100011100010011111010010001000000110001";
			
			StringBuffer sb = new StringBuffer(bits);
			
			String cadenaRestaur = comp.StringCompleto(sb);
		
			System.out.println(cadenaRestaur);
						 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
