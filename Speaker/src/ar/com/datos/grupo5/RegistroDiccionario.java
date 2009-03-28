
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
	 * Cuantos bytes puedo pasar.
	 */
	private Long moreBytes;
	
	/**
	 * Offset.
	 */
	private Long offset;
	
	/**
	 * El dato que se guarda.
	 */
	private String dato;
	
	/**
	 * Tamaño del dato almacenado (SIZE_OF).
	 */
	private int longDato;
	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {		
		if (moreBytes>0)
			return true;
		return false;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			
			byte[] datosByte = dato.getBytes();
			
			if(moreBytes == (dato.length() * 8 + 12)){	
				byte[] longDatoBytes = Conversiones.intToArrayByte(longDato);
				byte[] offsetBytes = Conversiones.longToArrayByte(offset);
				
				dos.write(offsetBytes, 0, offsetBytes.length);
				dos.write(longDatoBytes, 0, longDatoBytes.length);
				moreBytes -= offsetBytes.length;
				moreBytes -= longDatoBytes.length;
			}	
			dos.write(datosByte, 0, datosByte.length);
			moreBytes -= datosByte.length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena el registro con la información del buffer.
	 */
	public void llenar() {
		// TODO Llenar este método
	}
	
	/**
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
		this.longDato = dato.length();
		// Acá considero el tamaño (int) y el offset (long).
		this.moreBytes = (long) this.longDato * 8 + 12; 
	}

}
