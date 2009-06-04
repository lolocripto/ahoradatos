/**
 * 
 */
package ar.com.datos.grupo5.compresion;

import ar.com.datos.grupo5.compresion.ppmc.Ppmc;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.MetodoCompresion;

/**
 * @author Led Zeppelin
 *
 */
public class CompresorFactory {
	
	/**
	 * Devuelve el tipo de compresor pedido.
	 * @param metodo El metodo por el cual el compresor va a 
	 * comprimir
	 * @return el compresor.
	 */
	public static Compresor getCompresor(MetodoCompresion metodo){
		Compresor moduloCompresor;
		switch(0) {
			case 0:{
				//PPMC
				moduloCompresor = new Ppmc();
			}
			break;
			case 1:{
				//LZP
				moduloCompresor = new Ppmc();
			}
			break;
			case 2:{
				//LZ78
				moduloCompresor = new Ppmc();
			}
			break;
			default:
				moduloCompresor = null;
		}
		return moduloCompresor;
	}
}
