
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
	 * Si hay mas bytes para devolver.
	 */
	private boolean hasMore = true;
	
	/**
	 * Offset.
	 */
	private Long offset;
	
	/**
	 * El dato que se guarda.
	 */
	private String dato;
	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {
		
		if (hasMore) {
			hasMore = !hasMore;
			return true;
		} else {
			hasMore = !hasMore;
			return false;
		}
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		byte[] datosByte = dato.getBytes();
		byte[] longDatoBytes = Conversiones.intToArrayByte(dato.length());
		byte[] offsetBytes = Conversiones.longToArrayByte(offset);
		
		try {
			dos.write(offsetBytes, 0, offsetBytes.length);
			dos.write(longDatoBytes, 0, longDatoBytes.length);
			dos.write(datosByte, 0, datosByte.length);
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
	}

}
