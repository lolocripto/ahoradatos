/**
 * 
 */
package ar.com.datos.compresion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.Core;

/**
 * @author Ramiro
 *
 */
public class mtf {
	
	private ArrayList<Character> indicesCadena;
	
	private String lexico;
	

	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Core.class);
	private char[] lexicoLista;

	public mtf() {
		this.lexico = new String(" !\"$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPKRSTUVWXYZ[\\]^_`abcdefghijklmnopkrstuvwxyz{|}~");
    };   
		          
	private String traerDetras(int numero)
	{
	     return this.lexico;
	}

	public String MTFfromEncoding(byte[] cadena)
	{
	    /* Recibo la Cadena separada por comas */
		String		resultado = "";
		Character[] 	letra = new Character[2];
		char		ultimaLetraUsada;
		int 		i;
		short		posicionLetra;

		/* reestablezco el lexico */
		this.lexico = new String(" !\"$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPKRSTUVWXYZ[\\]^_`abcdefghijklmnopkrstuvwxyz{|}~");

		ByteArrayInputStream bis = new ByteArrayInputStream(cadena);  
		DataInputStream dis = new DataInputStream(bis);
		     
		/* recorro las posiciones del lexico */
		for (i = 0; i < ( cadena.length / Constantes.SIZE_OF_SHORT); i++) {
			/* levanto dos caracteres */
			try {
				posicionLetra = dis.readShort();
				ultimaLetraUsada = lexico.charAt(posicionLetra);
				resultado += ultimaLetraUsada;	
				traerAdelante(posicionLetra);
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return resultado;
	}
	
	
	private void traerAdelante(int numero) throws Exception {
	     
	     ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	     DataOutputStream dos = new DataOutputStream(bos);
	      	     
	     try {
	    	 
	    	//Paso a la primera posicion la ultima letra utilizada
		    dos.write(this.lexico.charAt(numero));
		    
		    // Copio la primera parte de la cadena
		    dos.write(this.lexico.getBytes(), 0, numero);

		    numero++;
		    
		    // Copio la segunda parte de la cadena
			dos.write(this.lexico.getBytes(), numero, this.lexico.getBytes().length - numero);
			
			this.lexico = bos.toString();
		} catch (IOException e) {
			throw e;
		}
	     
	 }


	public byte[] mTFtoEncoding(String cadena) {
	     short	lugar;
	     int 	i;
	     
	     ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	     DataOutputStream dos = new DataOutputStream(bos);
	     
	     /* Recorro la cadena a reordenar */

	     for (i = 0; i < cadena.length(); i++) {
	    	 //strchr(this->Lexico,Cadena[i]);
	    	 //busco el lugar del caracter en el string
	     	lugar = (short) this.lexico.indexOf(cadena.charAt(i)); 
	     		
	     	if (lugar >= 0 || lugar <= this.lexico.length()) {
		     		//Obtengo la distancia al primer elemento.
		     	    //sprintf(numero,"%i",Lugar-this->Lexico);
	     		try {
	     			dos.writeShort(lugar);
					traerAdelante(lugar);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	     	}
	     }
	     return bos.toByteArray();
	}

	private short valor(Character letra) {
		if (Character.isDigit(letra)) {
			return (short) Character.getNumericValue(letra);
		} 
		
	    switch (Character.toUpperCase(letra)) {
	    	case 'A': return 10;
	    	case 'B': return 11;
	    	case 'C': return 12;
	    	case 'D': return 13;
	    	case 'E': return 14;
	    	case 'F': return 15;
	    }
	return 0;
	}

	private int convertirToHexa(String numero)
	{
		int i, numeroDecimal=0;

		numeroDecimal = valor(numero.charAt(0))*16;
		numeroDecimal += valor(numero.charAt(1));
		return numeroDecimal;
	}
	
}
