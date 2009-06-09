package ar.com.datos.grupo5.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Funciones de conversion de datos.
 * @author LedZeppeling
 *
 */
public final class Conversiones {
	
	/**
	 * Constructor.
	 */
	private Conversiones() {
		super();
	}

	/**
	 * Long to array byte.
	 * 
	 * @param l
	 *            el long a convertir.
	 * @return el long en bytes.
	 */
	public static byte[] longToArrayByte(final long l) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeLong(l);
			dos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();  
	}
	
	/**
	 * array byte to long.
	 * 
	 * @param array
	 *            el array a convertir.
	 * @return el long obtenido.
	 */
	public static long arrayByteToLong(final byte[] array) {
		
		
		ByteArrayInputStream bis = new ByteArrayInputStream(array);  
		DataInputStream dos = new DataInputStream(bis);
		long result = 0;
			
		try {
			result = dos.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Int to array byte.
	 * 
	 * @param numero
	 *            el entero a convertir.
	 * @return el entero en bytes.
	 */
	public static byte[] intToArrayByte(final int numero) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeInt(numero);
			dos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();  
	}
	
	/**
	 * array byte to int.
	 * 
	 * @param array
	 *            el array de bytes a convertir.
	 * @return el entero obtenido.
	 */
	public static int arrayByteToInt(final byte[] array) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(array);  
		DataInputStream dos = new DataInputStream(bis);

		int result = 0;
			
		try {
			result = dos.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * array byte to short.
	 * 
	 * @param array
	 *            el array de bytes a convertir.
	 * @return el entero obtenido.
	 */
	public static short arrayByteToShort(final byte[] array) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(array);  
		DataInputStream dos = new DataInputStream(bis);

		short result = 0;
			
		try {
			result = dos.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Int to array byte.
	 * 
	 * @param numero
	 *            el entero a convertir.
	 * @return el entero en bytes.
	 */
	public static byte[] shortToArrayByte(final int numero) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			
			dos.writeShort(numero);
			dos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();  
	}
	
	/**
	 * Int to array byte.
	 * 
	 * @param numero
	 *            el entero a convertir.
	 * @return el short en binario.
	 */
	public static String shortToBinaryString(final Integer numero) {
		return Integer.toBinaryString(numero.intValue()).substring(16, 32);
	}
	
	public static String charToBinaryString(final Character letra) {
		String binaryInt = Integer.toBinaryString(Character.getNumericValue(letra));
		int faltante = (16 - binaryInt.length());
		for(int i = 0; i < faltante; i++) {
			binaryInt = "0" + binaryInt;
		}
		return binaryInt;
	}
	
	/**
	 * Recibe un BinaryString y lo convierte en una tira de bytes
	 * @param letra
	 * @return
	 */
	public static byte[] BinaryStringToBytes(final String cadena) {
		
		int cantidadBytes = cadena.length() / Byte.SIZE;
		int faltante = cadena.length() % Byte.SIZE;
		int start = 0, end = 1;
		StringBuffer bufferString = new StringBuffer(cadena);
		 
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		end += Byte.SIZE;
		String byteString = "";
		//Los bytes completos los escribo en el dos
		for (int i = 0; i < cantidadBytes; i++) {
			byteString = bufferString.substring(start,end);
			try {
				dos.writeByte(Conversiones.BinaryByteStringToByte(byteString));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bufferString.delete(start,end);
		}
		if (faltante > 0){
			for(int i = 0; i < faltante; i++) {
				bufferString.append("0");
			}
			byteString = bufferString.substring(start,end);
			try {
				dos.writeByte(Conversiones.BinaryByteStringToByte(byteString));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bufferString.delete(start,end);
		}
		return bos.toByteArray();
	}
	
	public static byte BinaryByteStringToByte(final String byteString) {	
		//Convierto el string de binario a byte para luego escribirlo en binario puro
		Short elemento = Short.parseShort(byteString, 2);
		byte[] numeroBytes = Conversiones.shortToArrayByte(elemento);
		return numeroBytes[1];
	}
}
