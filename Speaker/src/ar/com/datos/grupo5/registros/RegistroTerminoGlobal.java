
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

//TODO: Ver comentarios, JAVADOC
/**
 * Esta Clase implementa la interfaz del registro para audio.
 * 
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class RegistroTerminoGlobal implements Registro {

	/**
	 * Cuantos bytes puedo pasar.
	 */
	private long moreBytes;
	
	/**
	 * 
	 */
	private short longDato;
	
	/**
	 * 
	 */
	private String dato;

	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * 
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {
		
		if (moreBytes > 0) {
			return true;
		}
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
			
			int longDatosAdic = Constantes.SIZE_OF_SHORT;
			
			byte[] datosByte = dato.getBytes();
			
			if (moreBytes == (dato.length() + longDatosAdic)) {
				byte[] longDatoBytes = Conversiones.shortToArrayByte(longDato);

				dos.write(longDatoBytes, 0, longDatoBytes.length);
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
	 * @param datos the dato to set
	 */
	public final void setDato(final String termino) {
	 	this.dato = termino;
		this.longDato = (short) dato.length();
		this.moreBytes = (long) (this.longDato + Constantes.SIZE_OF_INT); 
	}
	/**
	 * 
	 * @return dato.
	 */
	public final String getDato(){
		return this.dato;
	}
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#getLongDatos()
	 * @return Devuelve la longitud del dato almacenado.
	 */
	public final short getLongDatos() {
		return longDato;
	}
		
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
	public final void setBytes(final byte[] buffer, final Long offset) {

		this.longDato = (short) buffer.length;
		dato = new String(buffer);
	}
	

}
