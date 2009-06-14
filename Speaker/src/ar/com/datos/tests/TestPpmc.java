/**
 * 
 */
package ar.com.datos.tests;

import java.io.IOException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.compresion.ppmc.Ppmc;
import ar.com.datos.grupo5.excepciones.SessionException;

/**
 * @author Led Zeppelin
 *
 */
public class TestPpmc {

	public static void main(String[] args){
		try {
			Ppmc compresorPpmc = new Ppmc();
			conversionBitToByte conversor = new conversionBitToByte();
			System.out.println("TATATAAAAALO: 12Bytes, 96bits");
			compresorPpmc.iniciarSesion();
			String cadena;
			cadena = compresorPpmc.comprimir("TATATAAAAALO");
			cadena += compresorPpmc.finalizarSession();
			conversor.setBits(cadena);
			
			System.out.println("Compresion TATATAAAAALO: 12Bytes, "+cadena.length()+" bits \n"+cadena);
			
			try {
			RandomAccessFile file = new RandomAccessFile("pruebaConvertir.data", Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			file.write(conversor.getBytes());
			if (conversor.hasMoreBytes()) {
				file.write(conversor.finalizarConversion());
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			compresorPpmc.iniciarSesion();
			StringBuffer buffer = new StringBuffer(cadena);
			String cadenaDescomprimida = "";
			while (Constantes.EOF.compareTo(cadena.charAt(0)) != 0) {//Contantes.EOF) {
				cadena = compresorPpmc.descomprimir(buffer);
				cadenaDescomprimida += cadena;
			}
			System.out.println(cadenaDescomprimida);
			
		} catch (SessionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
