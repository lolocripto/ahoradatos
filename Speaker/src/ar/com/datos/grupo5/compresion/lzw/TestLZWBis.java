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
			System.out.println("quedo "+comprimido);
			String fout = new String("compresion222.lzw");
			FileOutputStream ffileOut = new FileOutputStream(fout);
			conversionBitToByte conversor = new conversionBitToByte();
			conversor.setBits(comprimido);
			byte[] codigoEnBytes = conversor.getBytes();
			byte[] loQueFalto =conversor.finalizarConversion();
			
			for (int j=0;j<loQueFalto.length;j++) {
			   codigoEnBytes[codigoEnBytes.length+j] = loQueFalto[j];	
			}
			
			ffileOut.close();
			
		} 
	    catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       byte[] datosComprimidos = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(datosComprimidos);  
		DataInputStream dis = new DataInputStream(bis);
		
		byte[] bytesLec = {0, 0, 0, 0};
		try {
			dis.read(bytesLec, 2, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer integerRec = Conversiones.arrayByteToInt(bytesLec);
		String datosBinarios = (Integer.toBinaryString(integerRec)).substring(8, 17);	
	}

}
