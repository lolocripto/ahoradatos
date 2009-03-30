package ar.com.datos.grupo5.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Funciones de conversion de datos.
 * @author cristian
 *
 */
public class Conversiones {
	
	/**
	 * Constructor.
	 */
	public Conversiones() {
		super();
	}

	/**
	 * Long to array byte.
	 * 
	 * @param l
	 *            el long a convertir.
	 * @return el long en bytes.
	 */
	public static final byte[] longToArrayByte(final long l) {
		
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
	public static final long arrayByteToLong(final byte[] array) {
		
		
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
	public static final byte[] intToArrayByte(final int numero) {
		
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
	public static final int arrayByteToInt(final byte[] array) {
		
		
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
}
