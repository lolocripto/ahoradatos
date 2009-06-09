package ar.com.datos.grupo5.compresion.lzw;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.utils.Conversiones;

public class TestLZWBis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        CompresorLZWBis compresorBIS = new CompresorLZWBis();
        try {
			//compresorBIS.comprimeDatos("/wed/we/wee/web/wet");
	        try {
				byte[] comprimidoBytes = compresorBIS.comprimeDatos("CatCatInTheHatAndTheRat");
				String out = new String("compresion.lzw");
				FileOutputStream fileOut = new FileOutputStream(out);
				fileOut.write(comprimidoBytes);
				fileOut.close();
				String cadena = compresorBIS.descomprimirDatos(comprimidoBytes);
				System.out.println("quedo?? "+cadena);
			} catch (IOException e) {
				// TODO Auto-generated catch block\
				System.out.println("|error");
				e.printStackTrace();
			}
        	String comprimido = compresorBIS.comprimir("CatCatInTheHatAndTheRat");
			System.out.println("quedodo "+comprimido);
			String descomprimido = compresorBIS.comprimir(comprimido);
			
			
		} 
	    catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
	}

}
