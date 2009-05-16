/**
 * 
 */
package ar.com.datos.tests;

import ar.com.datos.compresion.mtf;
import ar.com.datos.grupo5.Constantes;

/**
 * @author Ramiro
 *
 */
public class TestMtf {

	/**
	 * @param args k.
	 */
	public static void main(final String[] args) {
		mtf moveToFront = new mtf();
		byte[] encoding = moveToFront.mTFtoEncoding("hola mundo");
		System.out.println(encoding);
		String dencoding = moveToFront.MTFfromEncoding(encoding);
		System.out.println(dencoding);
	}

}
