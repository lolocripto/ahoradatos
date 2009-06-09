/**
 * 
 */
package ar.com.datos.grupo5.compresion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.UnsignedInt;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author Led Zeppelin
 *
 */
public class conversionBitToByte {

	private byte[] datosComprimidos;
	private String datosBinarios;
	
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(conversionBitToByte.class);
	
	/**
	 * Agrega mas datos binarios.
	 * @param bits datos binarios.
	 */
	public final void setBits(String bits){
		this.datosBinarios += bits; 
	}
		
	
	//TODO: Ver si puedo parametrizar la clase y parsearlo de alguna manera.
	/**
	 * Obtiene los bytes con el contenido binario, pueden sobrar bits, estos se convertiran al finalizar la conversion.
	 * @return el arreglo de bytes.
	 */
	public final byte[] getBytes() {
		//Objetivo: Transformar el string en un arreglo de bytes.
		int longitud = this.datosBinarios.length();
		long cantidad = 0;
		int beginIndex = 0, endIndex = 0;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		if (longitud < Short.SIZE) {
			//Entonces lo parseo con un byte
			
			//obtengo la cantidad de bytes que puedo leer
			cantidad = longitud / (Byte.SIZE);
			endIndex += Byte.SIZE;
			
			for (int i = 0; i < cantidad; i++) {
				
				logger.debug("Inicio: "+beginIndex+" final: "+endIndex+" Longitud Total: "+this.datosBinarios.substring(beginIndex, endIndex));
				//Convierto el string de binario a byte para luego escribirlo en binario puro
				Long elemento = Long.parseLong(this.datosBinarios.substring(beginIndex, endIndex), 2);
				
				try {
					byte[] numeroBytes = Conversiones.longToArrayByte(elemento);
					dos.write(numeroBytes,7,1);
				} catch (IOException e) {
					//TODO: ver que hago aca!
					e.printStackTrace();
					return null;
				}
				//Muevo los offset para avanzar al siguiente byte
				endIndex += Byte.SIZE;
				beginIndex += Byte.SIZE;
			}
			
			if (longitud % (Byte.SIZE) > 0) {
				this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Byte.SIZE));
			} else {
				this.datosBinarios = "";
			}
	
		} else {
			if (longitud < Integer.SIZE) {
				//Entonces lo parseo con un Short
				cantidad = longitud / Short.SIZE;
				endIndex += Short.SIZE;
				
				for (int i = 0; i < cantidad; i++) {
					
					//Convierto el string de binario a byte para luego escribirlo en binario puro
					Long elemento = Long.parseLong(this.datosBinarios.substring(beginIndex, endIndex), 2);
					
					try {
						byte[] numeroBytes = Conversiones.longToArrayByte(elemento);
						dos.write(numeroBytes,6,2);
					} catch (IOException e) {
						//TODO: ver que hago aca!
						e.printStackTrace();
						return null;
					}
					//Muevo los offset para avanzar al siguiente Short
					endIndex += Short.SIZE;
					beginIndex += Short.SIZE;
				}
				if (longitud % Short.SIZE > 0) {
					this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Short.SIZE));
				} else {
					this.datosBinarios = "";
				}
				
			} else {
					//Entonces lo parseo con un Integer
					cantidad = longitud / Integer.SIZE;
					endIndex += Integer.SIZE;
					
					for (int i = 0; i < cantidad; i++) {
						
						//Convierto el string de binario a byte para luego escribirlo en binario puro
						Long elemento = Long.parseLong(this.datosBinarios.substring(beginIndex, endIndex), 2);
						
						try {
							byte[] numeroBytes = Conversiones.longToArrayByte(elemento);
							dos.write(numeroBytes,4,4);
						} catch (IOException e) {
							//TODO: ver que hago aca!
							e.printStackTrace();
							return null;
						}
						//Muevo los offset para avanzar al siguiente Integer
						endIndex += Integer.SIZE;
						beginIndex += Integer.SIZE;
					}
					if ((longitud % Integer.SIZE) > 0) {
						this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Integer.SIZE));
					} else {
						this.datosBinarios = "";
					}
			}
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Completa a 8 el binario.
	 * @param sobrante la cantidad
	 */
	private void completarByte(int faltante) {
		
		//lleno de ceros lo faltante para llegar a 8 bit
		for (int i = 0; i < faltante; i++) {
			this.datosBinarios += '0';
		}
		
	}

	/**
	 * Agrega bytes para luego convertilos en bits.
	 * @param datos bytes a convertir.
	 */
	public final void setBytes(byte[] datos){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (this.datosComprimidos != null)
				dos.write(this.datosComprimidos);
			dos.write(datos);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.datosComprimidos = bos.toByteArray();
	}
	
	/**
	 * Obtiene los bits que representan el contenido
	 * de los bytes.
	 * @return los bits en un string.
	 */
	public final String getBits(){
		
		int cantidadIntegers = this.datosComprimidos.length / Constantes.SIZE_OF_INT;
		 if ((this.datosComprimidos.length % Constantes.SIZE_OF_INT) > 0) {
			 cantidadIntegers++;
		 }
		
		this.datosBinarios = "";
		ByteArrayInputStream bis = new ByteArrayInputStream(this.datosComprimidos);  
		DataInputStream dis = new DataInputStream(bis);
		
		try {
		for (int i = 0; i < cantidadIntegers; i++) {
			
			byte[] bytesLec = {0, 0, 0, 0, 0, 0, 0, 0};
			dis.read(bytesLec, 4, 4);
			Long longRec = Conversiones.arrayByteToLong(bytesLec);
			Integer integerRec = longRec.intValue();
			this.datosBinarios += Integer.toBinaryString(integerRec);	
		}

		} catch (IOException E) {
			return null;
		}
		return this.datosBinarios;
	}
	
	public final boolean hasMoreBytes() {
		return (this.datosBinarios.length() > 0);
	}
	
	/**
	 * Cuando termina la compresion verifica los datos que quedan
	 * por devolver en Bytes, los convierte y los devuelve padeando
	 * para completar el byte o sea a 8 bits.
	 * @return El array de bytes correspondiente a la conversion.
	 */
	public final byte[] finalizarConversion(){

		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		int beginIndex = 0, endIndex = 1;
		int cantidad = this.datosBinarios.length() / (Byte.SIZE);
		endIndex += Byte.SIZE;
		
		for (int i = 0; i < cantidad; i++) {
			
			this.logger.debug("Inicio: "+beginIndex+" final: "+endIndex+" Longitud Total: "+this.datosBinarios.substring(beginIndex, endIndex));
			//Convierto el string de binario a byte para luego escribirlo en binario puro
			Long elemento = Long.parseLong(this.datosBinarios.substring(beginIndex, endIndex), 2);
			
			try {
				byte[] numeroBytes = Conversiones.longToArrayByte(elemento);
				dos.write(numeroBytes,7,1);
			} catch (IOException e) {
				//TODO: ver que hago aca!
				e.printStackTrace();
				return null;
			}
			//Muevo los offset para avanzar al siguiente byte
			endIndex += Byte.SIZE;
			beginIndex += Byte.SIZE;
		}
		
		int faltante = this.datosBinarios.length() % (Byte.SIZE);
		if (faltante > 0) {
			this.completarByte(faltante);
			Long elemento = Long.parseLong(this.datosBinarios.substring((int) (cantidad*Byte.SIZE)), 2);
			
			try {
				byte[] numeroBytes = Conversiones.longToArrayByte(elemento);
				dos.write(numeroBytes,7,1);
			} catch (IOException e) {
				//TODO: ver que hago aca!
				e.printStackTrace();
				return null;
			}
		}
		//Una vez terminada la conversion inicializo el conversor.
		inicializarConversor();
		
		return bos.toByteArray();
	}
	
	public conversionBitToByte(){
		this.inicializarConversor();
	}
	
	public final void inicializarConversor(){
		this.datosBinarios = "";
		this.datosComprimidos = null;
	}
}
