package ar.com.datos.grupo5.compresion.lzw;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.utils.Conversiones;

public class TestLZWBis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        CompresorLZWBis compresorBIS = new CompresorLZWBis();
        compresorBIS.iniciarSesion();
        
			//compresorBIS.comprimeDatos("/wed/we/wee/web/wet");
	        try {
				byte[] comprimidoBytes = compresorBIS.comprimeDatos("CatCatInTheHatAndTheRat");
				String out = new String("compresion.lzw");
				FileOutputStream fileOut = new FileOutputStream(out);
				fileOut.write(comprimidoBytes);
				fileOut.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block\
				System.out.println("|error");
				e.printStackTrace();
			}
		
        
        try {
        	
        	String comprimido = compresorBIS.comprimir("CatCatInTheHatAndTheRat");
			System.out.println("quedodo "+comprimido);
			String fout = new String("compresion222.lzw");
			FileOutputStream ffileOut = new FileOutputStream(fout);
			conversionBitToByte conversor = new conversionBitToByte();
			conversor.setBits(comprimido);
			ffileOut.write(conversor.getBytes());
			ffileOut.close();
			
		} 
	    catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
	}

}
