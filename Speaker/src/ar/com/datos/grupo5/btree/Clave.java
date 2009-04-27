package ar.com.datos.grupo5.btree;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;


/**
 * Clave de un registro.
 * @author cristian
 *
 */
public class Clave implements Comparable<Clave> {

	/**
	 * La clave.
	 */
	private String claveStr;
		
	/**
	 * Para comparar las claves.
	 * @param clave la clave a comparar.
	 * @return 0, 1, -1.
	 */
	public final int compareTo(final Clave clave) {
		
		return this.claveStr.compareTo(clave.getClave());
	}

	/**
	 * @return the clave
	 */
	public final String getClave() {
		return claveStr;
	}

	/**
	 * @param clave the clave to set
	 */
	public final void setClave(final String clave) {
		this.claveStr = clave;
	}
	
	/**
	 * Determina cuando una clave es igual a otra.
	 * @param clave .
	 * @return true si son iguales.
	 */
	@Override
	public final boolean equals(final Object clave) {
		
		if (clave instanceof String) {
			String c = (String) clave;
			return c.equals(this.claveStr);
		}
		
		return false;
	}
	
	/**
	 * Para el equals.
	 * @return al hash code.
	 */
	public final int hashCode() {
		return 0;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan a la clave.
	 */
	public byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			byte[] datosByte = claveStr.getBytes();
			byte[] longDatoBytes = Conversiones.intToArrayByte(datosByte.length);

			dos.write(longDatoBytes, 0, longDatoBytes.length);
			dos.write(datosByte, 0, datosByte.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques.
	 */
	public void setBytes(final byte[] buffer) {
	// TODO verificar este método!!!!
		this.setClave(new String(buffer));
	}

}
