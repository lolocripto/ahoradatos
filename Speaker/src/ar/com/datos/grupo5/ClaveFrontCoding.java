package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.utils.Conversiones;

public class ClaveFrontCoding extends Clave {

	private int caracteresCoincidentes;

	private int longitudTermino;

	/**
	 * Ver si seria una clave en vez de un String.
	 */
	private String termino;
	/**
	 * Constructor.
	 * @param caracteres
	 * @param nroBloque
	 * @param longitud
	 * @param termino
	 */
	public ClaveFrontCoding(final int caracteres, final int longitud,
			final String termino) {
		this.caracteresCoincidentes = caracteres;
		this.longitudTermino = longitud;
		this.termino = termino;
	}

	public void setCaracteresCoincidentes(int caracteresCoincidentes) {
		this.caracteresCoincidentes = caracteresCoincidentes;
	}

	public int getCaracteresCoincidentes() {
		return caracteresCoincidentes;
	}

	public void setLongitudTermino(int longitud) {
		this.longitudTermino = longitud;
	}

	public int getLongitudTermino() {
		return longitudTermino;
	}

	public void setTermino(String termino) {
		this.termino = termino;
	}

	public String getTermino() {
		return termino;
	}

	public String toString() {
		String termino = new String();
		termino += (new Integer(caracteresCoincidentes).toString());
		termino += (new Integer(longitudTermino).toString());
		termino += (this.termino);

		return termino;
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 * @throws IOException ,
	 */
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			// TODO TESTEARME!!!!!!!!!
			byte[] caracateresCoincidentesBytes =
				Conversiones.intToArrayByte(caracteresCoincidentes);

			byte[] longitudTerminoByte = 
				Conversiones.intToArrayByte(this.longitudTermino);

			byte[] claveBytes = this.termino.getBytes();
			
			dos.write(caracateresCoincidentesBytes);
			dos.write(longitudTerminoByte);
			dos.write(claveBytes);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param bloqueAnt nro de bloque anterior.
	 * @throws IOException .
	 */
	public void setBytes(final byte[] buffer, final int bloqueAnt)
			throws IOException {
		//TODO TESTEARLO YA!!!!!!!!!!
	
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dos = new DataInputStream(bis);
		byte[] datos = null;
		
		try {
			//Leo la longitud de la clave
			this.caracteresCoincidentes = dos.readInt();
			this.longitudTermino = dos.readInt();
			datos = new byte[this.longitudTermino];
			this.termino = new String(datos);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
