/**
 * 
 */
package ar.com.datos.tests;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
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
		try {
		CompresorAritmetico compressOrder0 = new CompresorAritmetico(0, true);
		
		//CompresorAritmetico compressOrder1 = new CompresorAritmetico(1, true);
		String cadena = "TATATAAAAALO dllfdfasdf faskdf dsgñkjqpioq adsfkjadsfñoikj \tadsfñkjadsf \rñkdf wjgnñpaskd\n";
		//String cadena = "BAABCA";
		String bitOrden0 = "";
		//String bitOrden1 = "";
		Character letra;
		compressOrder0.iniciarSesion();
		//compressOrder1.iniciarSesion();
		
		for (int i = 0; i < cadena.length(); i++) {
			letra = cadena.charAt(i);
			if (letra == 'O') {
				System.out.println("Letra Dañina");
			}
			bitOrden0 += compressOrder0.comprimir(letra.toString());
			//bitOrden1 += compressOrder1.comprimir(letra.toString());
		}
		bitOrden0 += compressOrder0.finalizarSession();
		//bitOrden1 += compressOrder1.finalizarCompresion();
		System.out.println("Salida comprimida orden0: " + bitOrden0);
		//System.out.println("Salida comprimida orden1: " + bitOrden1);
		
		
		
		compressOrder0.iniciarSesion();
		StringBuffer cadenaBuffer = new StringBuffer(bitOrden0);
		//StringBuffer cadenaBuffer = new StringBuffer("011111111100000001000000001111101000011100001100001101100100101111010110000000000");
		String salida = " ";
		while (salida != null && !Constantes.EOF.equals(salida.charAt(salida.length()-1))){
			if (Constantes.EOF.equals(salida.charAt(salida.length()-1))) {
				System.out.println("Son iguales");
			} else {
				System.out.println("No son iguales");
			}
			if (salida.charAt(salida.length()-1) == 'L') {
				System.out.println("letra L");
			}
			System.out.println("Salida: " + salida);
			salida += compressOrder0.descomprimir(cadenaBuffer);
			System.out.println("buffer: " + cadenaBuffer);
		}
		
		} catch (SessionException e) {
			e.printStackTrace();
		}
	}

}
