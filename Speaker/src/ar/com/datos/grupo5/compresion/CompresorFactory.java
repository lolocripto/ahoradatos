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
	public static Compresor getCompresor(String metodo){
		Compresor moduloCompresor = null;
		if (MetodoCompresion.valueOf(metodo) == MetodoCompresion.PPMC) {
			moduloCompresor = new Ppmc();
			return moduloCompresor;
		}
		if (MetodoCompresion.valueOf(metodo) == MetodoCompresion.LZP) {
			moduloCompresor = null;
			return moduloCompresor;
		} 
		if (MetodoCompresion.valueOf(metodo) == MetodoCompresion.LZ78) {
			moduloCompresor = null;
			return moduloCompresor;
		} 
		if (MetodoCompresion.valueOf(metodo) == MetodoCompresion.ARIT) {
			moduloCompresor = null;
			return moduloCompresor;
		} 
		
		return moduloCompresor; 
	}
}
