/**
 * 
 */
package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.aritmeticoRamiro.CompresorAritmetico;
import ar.com.datos.grupo5.excepciones.SessionException;

/**
 * @author Led Zeppelin
 *
 */
public class TestCompresorAritemetico {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompresorAritmetico compressOrder0 = new CompresorAritmetico(0, false);
		CompresorAritmetico compressOrder1 = new CompresorAritmetico(1, true);
		String cadena = "TATATAAAAALO";
		String bitOrden0 = "";
		String bitOrden1 = "";
		Character letra;
		compressOrder0.iniciarSesion();
		compressOrder1.iniciarSesion();
		try {
		for (int i = 0; i < cadena.length(); i++) {
			letra = cadena.charAt(i);
			bitOrden0 += compressOrder0.comprimir(letra.toString());
			bitOrden1 += compressOrder1.comprimir(letra.toString());
		}
		System.out.println("Salida comprimida orden0: " + bitOrden0);
		System.out.println("Salida comprimida orden1: " + bitOrden1);
		} catch (SessionException e) {
			e.printStackTrace();
		}
	}

}
