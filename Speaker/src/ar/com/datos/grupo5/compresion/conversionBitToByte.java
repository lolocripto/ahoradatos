/**
 * 
 */
package ar.com.datos.grupo5.compresion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.UnsignedInt;
import ar.com.datos.grupo5.compresion.ppmc.Ppmc;

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
		
		UnsignedInt numero = new UnsignedInt(1);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);

		
		
		//TODO: Hacer la validacion nuevamente porque no es asi!!!!
		
		if (longitud < Short.SIZE) {
			//Entonces lo parseo con un byte
			
			//obtengo la cantidad de bytes que puedo leer
			cantidad = longitud / (Byte.SIZE-1);
			endIndex += Byte.SIZE-1;
			
			for (int i = 0; i < cantidad; i++) {
				
				this.logger.debug("Inicio: "+beginIndex+" final: "+endIndex+" Longitud Total: "+this.datosBinarios.substring(beginIndex, endIndex));
				//Convierto el string de binario a byte para luego escribirlo en binario puro
				Byte elemento = Byte.parseByte(this.datosBinarios.substring(beginIndex, endIndex),2);
				
				try {
					dos.writeByte(elemento.byteValue());
				} catch (IOException e) {
					//TODO: ver que hago aca!
					e.printStackTrace();
					return null;
				}
				//Muevo los offset para avanzar al siguiente byte
				endIndex += Byte.SIZE-1;
				beginIndex += Byte.SIZE-1;
			}
			
			if (longitud % (Byte.SIZE-1) > 0) {
				this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Byte.SIZE-1));
			}
	
		} else {
			if (longitud <= Integer.SIZE) {
				//Entonces lo parseo con un Short
				cantidad = longitud / Short.SIZE;
				endIndex += Short.SIZE;
				
				for (int i = 0; i < cantidad; i++) {
					
					//Convierto el string de binario a byte para luego escribirlo en binario puro
					Short elemento = Short.parseShort(this.datosBinarios.substring(beginIndex, endIndex),2);
					
					try {
						dos.writeShort(elemento);
					} catch (IOException E) {
						//TODO: ver que hago aca!
						return null;
					}
					//Muevo los offset para avanzar al siguiente byte
					endIndex += Short.SIZE;
					beginIndex += Short.SIZE;
				}
				if (longitud % Short.SIZE > 0) {
					this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Short.SIZE));
				}
				
			} else {
				if (longitud <= Long.SIZE) {
					//Entonces lo parseo con un Integer
					cantidad = longitud / Integer.SIZE;
					endIndex += Integer.SIZE;
					
					for (int i = 0; i < cantidad; i++) {
						
						//Convierto el string de binario a byte para luego escribirlo en binario puro
						Integer elemento = Integer.parseInt(this.datosBinarios.substring(beginIndex, endIndex),2);
						
						try {
							dos.writeInt(elemento);
						} catch (IOException E) {
							//TODO: ver que hago aca!
							return null;
						}
						//Muevo los offset para avanzar al siguiente byte
						endIndex += Integer.SIZE;
						beginIndex += Integer.SIZE;
					}
					if (longitud % Integer.SIZE > 0) {
						this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Integer.SIZE));
					}
				} else {
					//Entoces lo parseo con un Long
					cantidad = longitud / Long.SIZE;
					endIndex += Long.SIZE;
					
					for (int i = 0; i < cantidad; i++) {
						
						//Convierto el string de binario a byte para luego escribirlo en binario puro
						Long elemento = Long.parseLong(this.datosBinarios.substring(beginIndex, endIndex),2);
						
						try {
							dos.writeLong(elemento);
						} catch (IOException E) {
							//TODO: ver que hago aca!
							return null;
						}
						//Muevo los offset para avanzar al siguiente byte
						endIndex += Long.SIZE;
						beginIndex += Long.SIZE;
					}
					if (longitud % Long.SIZE > 0) {
						this.datosBinarios = this.datosBinarios.substring((int) (cantidad*Long.SIZE));
					}
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
		this.datosComprimidos = datos;
	}
	
	/**
	 * Obtiene los bits que representan el contenido
	 * de los bytes.
	 * @return los bits en un string.
	 */
	public final String getBits(){
		
		int cantidadIntegers = this.datosComprimidos.length / Integer.SIZE;

		ByteArrayInputStream bis = new ByteArrayInputStream(this.datosComprimidos);  
		DataInputStream dis = new DataInputStream(bis);
		try {
		for (int i = 0; i < cantidadIntegers; i++) {
			this.datosBinarios += Integer.toBinaryString(dis.readInt());
		}
		} catch (IOException E) {
			return null;
		}
		return this.datosBinarios;
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
