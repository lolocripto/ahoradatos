/**
 * 
 */
package ar.com.datos.tests;

import java.io.IOException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.compresion.ppmc.Ppmc;

/**
 * @author Led Zeppelin
 *
 */
public class TestPpmc {

	public static void main(String[] args) {
		Ppmc compresorPpmc = new Ppmc();
		conversionBitToByte conversor = new conversionBitToByte();
		System.out.println("TATATAAAAALO: 12Bytes, 96bits");
		compresorPpmc.iniciarSesion();
		String cadena = compresorPpmc.comprimir("TATATAAAAALO");
		cadena += compresorPpmc.finalizarCompresion();
		conversor.setBits(cadena);
		
		System.out.println("Compresion TATATAAAAALO: 12Bytes, "+cadena.length()+" bits");
		
		try {
		RandomAccessFile file = new RandomAccessFile("pruebaConvertir.data", Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		file.write(conversor.getBytes());
		if (conversor.hasMoreBytes()) {
			file.write(conversor.finalizarConversion());
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		compresorPpmc.finalizarSession();
	}
}
