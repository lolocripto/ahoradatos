
package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Esta clase implementa el registro para el diccionario.
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author Diego
 */
public class RegistroDiccionario implements Registro {
	
	/**
	 * Offset.
	 */
	private Long offset;
	
	/**
	 * El dato que se guarda.
	 */
	private String dato;
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 */
	public byte[] toBytes() {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		byte[] datosByte = dato.getBytes();
		byte[] longDatoBytes = Conversiones.intToArrayByte(dato.length());
		byte[] offsetBytes = Conversiones.longToArrayByte(offset);
		
		try {
			dos.write(datosByte, 0, datosByte.length);
			dos.write(datosByte, 0, longDatoBytes.length);
			dos.write(datosByte, 0, offsetBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
		
//		String cadena = "";
//		
//		/**
//		 * 1° Agrego el offset.
//		 */
//		cadena = this.offset.toString();
//		
//		/**
//		 * Luego agrego la longitud.
//		 */
//		Integer len = this.dato.length();
//		cadena += len.toString();
//		
//		/**
//		 * Por ultimo, el dato.
//		 */
//		cadena += this.dato;
//		
//		return cadena.getBytes();
		
	}
	
	/**
	 * Método que llena el registro con la información del buffer.
	 */
	public void llenar() {
		// TODO Llenar este método
	}
	
	/*
	 * Método que devuelve el offset.
	 * @return El offset en el archivo de audio.
	 */
	public final Long getOffset() {
		return offset;
	}

	/**
	 * Método para cargar el offset.
	 * @param offset El offset a cargar.
	 */
	public final void setOffset(final Long offset) {
		this.offset = offset;
	}

	/**
	 * Método para devolver el dato.
	 * @return El dato.
	 */
	public final String getDato() {
		return dato;
	}

	/**
	 * Método para cargar el dato.
	 * @param dato El dato a setear.
	 */
	public final void setDato(final String dato) {
		this.dato = dato;
	}

}
