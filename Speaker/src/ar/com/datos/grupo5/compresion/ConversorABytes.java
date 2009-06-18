package ar.com.datos.grupo5.compresion;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;

public class ConversorABytes {
	
     public static int convertirBytesAInt(byte[] datoBytes){
		return 0;
    	 
     }
     
     public static byte[] convertirIntABytes(int dato) {
		return null;
    	 
     }
     
     public static byte[] convertirUnsignedShortABytes(int dato) {
    	 int segundoByte = (0x0000FF & (dato));  
		 int primerByte = ((dato) & 0x00FF00 ) >> 8;  
		 byte[] shortBytes= {(byte)primerByte,(byte)segundoByte};
    	 return shortBytes;
     }
     
     public static int convertirBytesAUnsignedShort(byte[] datosBytes) {
    	 byte firstByte = (byte) (0x000000FF & (datosBytes[0]));
    	 byte secondByte = (byte) (0x000000FF & (datosBytes[1]));
    	 int shortNumber = (firstByte << 8 | secondByte);
    	 return shortNumber;
    	 
     }
     
     public static char convertirBytesAChar(byte[] datosBytes) {
    	 byte firstByte = (byte) (0x000000FF & (datosBytes[0]));
    	 byte secondByte = (byte) (0x000000FF & (datosBytes[1]));
    	 char caracter = (char) (firstByte << 8 | secondByte);
    	 return caracter;
    	 
     }
     
     public static byte[] convertirCharABytes(char dato) {
    	 return Character.toString(dato).getBytes();
    	 
     }
     
     public static byte[] convertirStringBinariaABytes(String bits) {
    	 return null;
     }

     public static String convertirStringBinariaABytes(byte[] bytes) {
    	 return null;
     }
     
 	public static String charToBinaryString(final Character letra) {
		String binaryInt = Integer.toBinaryString(letra.charValue());
		int faltante = (16 - binaryInt.length());
		for(int i = 0; i < faltante; i++) {
			binaryInt = "0" + binaryInt;
		}
		
		return binaryInt;
	}
	public static String shortToBinaryString(final Integer numero) {

		String binaryInt = Integer.toBinaryString(numero.intValue());
		
		int faltante = (16 - binaryInt.length());
		for(int i = 0; i < faltante; i++) {
			binaryInt = "0" + binaryInt;
		}
		
		return binaryInt;

	}
	public static int binaryStringtoInt(String binaria) {
		int resultado = 0;
		for (int i = 0; i<binaria.length(); i++) {
			resultado += (Math.pow(2,binaria.length()-1-i))*(Integer.parseInt(Character.toString(binaria.charAt(i))));
		}
		
		return resultado;
		
		
	}


     
     
}
