/**
 * 
 */
package ar.com.datos.grupo5.compresion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author xxvkue
 *
 */
public class testConvertBitToByte {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		char hola = new Character(Character.toChars(65000)[0]);
		if (new Character(Character.toChars(65000)[0]).compareTo(hola) == 0) {
			System.out.println("igual");
		}
		System.out.println(new Character(Character.toChars(64)[0]));
		if (new Character(Character.toChars(64)[0]).compareTo(hola) != 0) {
			System.out.println("distinto");
		}
		char[] i = new char[1];
		i[0] = hola;

		System.out.println(Character.codePointAt(i, 0));
		
		/*
		try {
			RandomAccessFile file = new RandomAccessFile("pruebaConvertir.data", Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			conversionBitToByte convertir = new conversionBitToByte();
			//String algo = "11111110001"; 			//1 bytes + 3 bits = byte! FE2
			//String algo = "111111100011100011"; 		//2 bytes + 2 bits=> Short! FE38C
			//String algo = "1111111000111000110100"; 	//2,5 bytes => Short! FE38D0
			String algo = "1111000011110000111100001111000010"; 		//4 o + bytes => Integer! F0F0F0F08
			convertir.setBits(algo);
			byte[] algo1 = convertir.getBytes();
			file.write(algo1);
			algo1 = convertir.finalizarConversion();
			file.write(algo1);
			file.close();
			file = new RandomAccessFile("pruebaConvertir.data", Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			long cantidadIntegers = file.length() / Constantes.SIZE_OF_INT;
			if ((file.length() % Constantes.SIZE_OF_INT) > 0) cantidadIntegers++;
			//Leo de a por integers
			for (long i = 0; i < cantidadIntegers; i++) {
				algo1 = new byte[4];
				file.read(algo1, 0, 4);
				convertir.setBytes(algo1);
			}
			System.out.println(convertir.getBits());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
