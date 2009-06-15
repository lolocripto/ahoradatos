package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.compresion.lz78.CompresorLZ78;
import ar.com.datos.grupo5.excepciones.SessionException;

public class TestLZ78 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompresorLZ78 compresorBIS = new CompresorLZ78();
		compresorBIS.iniciarSesion();

		//compresorBIS.comprimeDatos("/wed/we/wee/web/wet");
		try {

			conversionBitToByte conver = new conversionBitToByte();
			compresorBIS.iniciarSesion();
			String archivo = new String("poemas.txt");
		    BufferedReader br      = new BufferedReader (new FileReader(new File (archivo)));
		    String linea = new String();
		    compresorBIS.iniciarSesion();
		    while ((linea = br.readLine()) != null) {
		    	String enbites = compresorBIS.comprimir(linea);
			    compresorBIS.finalizarSession();
			    compresorBIS.iniciarSesion();
			    String y = compresorBIS.descomprimir(new StringBuffer(enbites));
				System.out.println("comprimio? " + y.equals(linea));
				compresorBIS.finalizarSession();
				compresorBIS.iniciarSesion();
		    	
		    }
		    compresorBIS.finalizarSession();
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
