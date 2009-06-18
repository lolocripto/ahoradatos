package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.compresion.lz78.Lz78W;
import ar.com.datos.grupo5.excepciones.SessionException;

public class TestLZ78 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Lz78W lz78 = new Lz78W();

		//compresorBIS.comprimeDatos("/wed/we/wee/web/wet");
		try {

			//conversionBitToByte conver = new conversionBitToByte();
			lz78.iniciarSesion();
			String archivo = new String("poemas.txt");
		    BufferedReader br      = new BufferedReader (new FileReader(new File (archivo)));
		    String linea = new String();
		    StringBuffer buffer = new StringBuffer();
		    linea = br.readLine();
		    //while ((linea = br.readLine()) != null) {
		    	String bits = lz78.comprimir(linea);
		    	bits += lz78.finalizarSession();
		    	System.out.println(bits);
		    	buffer.append(bits);
		    	lz78.iniciarSesion();
		    	String y = lz78.descomprimir(buffer);
				System.out.println("comprimio? " + y.equals(linea));
		    //}
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
