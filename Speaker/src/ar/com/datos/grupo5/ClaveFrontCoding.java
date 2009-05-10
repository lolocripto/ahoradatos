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

	private byte caracteresCoincidentes;

	private byte longitudTermino;

	/**
	 * Ver si seria una clave en vez de un String.
	 */
	private String termino;
	
	public ClaveFrontCoding(byte[] buffer) {
		try {
			this.setBytes(buffer);
		} catch (IOException e) {
			System.out.println("Incosistencias en los datos de la Clave creada.");
		}
	}
	/**
	 * Constructor.
	 * @param caracteres
	 * @param nroBloque
	 * @param longitud
	 * @param termino
	 */
	public ClaveFrontCoding(final byte caracteres, final byte longitud,
			final String termino) {
		this.caracteresCoincidentes = caracteres;
		this.longitudTermino = longitud;
		this.termino = termino;
	}
	
	public ClaveFrontCoding (){
		
	}
	

	public void setCaracteresCoincidentes(byte caracteresCoincidentes) {
		this.caracteresCoincidentes = caracteresCoincidentes;
	}

	public int getCaracteresCoincidentes() {
		return caracteresCoincidentes;
	}

	public void setLongitudTermino(byte longitud) {
		this.longitudTermino = longitud;
	}

	public int getLongitudTermino() {
		return longitudTermino;
	}

	public void setTermino(String termino) {
		this.termino = termino;
		this.longitudTermino = (byte)termino.length();
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
			byte[] claveBytes = this.termino.getBytes();
			
			dos.write(caracteresCoincidentes);
			dos.write(longitudTermino);
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
	public void setBytes(final byte[] buffer)
			throws IOException {
		//TODO TESTEARLO YA!!!!!!!!!!
	
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dos = new DataInputStream(bis);
		byte[] datos = null;
		
		try {
			//Leo la longitud de la clave
			this.caracteresCoincidentes = dos.readByte();
			this.longitudTermino = dos.readByte();
			datos = new byte[this.longitudTermino];
			this.termino = new String(datos);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
