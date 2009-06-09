package ar.com.datos.grupo5.compresion.lzw;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
				conversionBitToByte conver = new conversionBitToByte();
				conver.setBytes(comprimidoBytes);
				String cadenaBits = conver.getBits();
				System.out.println("||con metodo en byte[]||"+cadenaBits);
				String out = new String("compresion.lzw");
				FileOutputStream fileOut = new FileOutputStream(out);
				fileOut.write(comprimidoBytes);
				fileOut.close();
				
				try {
					compresorBIS.iniciarSesion();
					String enbites = compresorBIS.comprimir("CatCatInTheHatAndTheRat");
				    conver.setBits(enbites);
				    byte[] cachirla = conver.getBytes();
					String out2 = new String("compresion0001.lzw");
					FileOutputStream fileOut2 = new FileOutputStream(out2);
					fileOut2.write(cachirla);
					fileOut2.close();
				    
				    leeCadena(enbites);	
				} catch (SessionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block\
				System.out.println("|error");
				e.printStackTrace();
			}
	
	}

	private static void leeCadena(String comprimido) {
		conversionBitToByte conversor = new conversionBitToByte();
		int k=0;
		while (k < comprimido.length()) {
		conversor.setBits(comprimido.substring(k,k+16));
		System.out.println("sub)"+comprimido.substring(k,k+16));
		int codigo = Conversiones.arrayByteToShort(conversor.getBytes());
		if (codigo<255)
			System.out.println("     >>>>"+(char)codigo);
		System.out.println(codigo);
		k+=16;
		}		
		
		
	}
}
