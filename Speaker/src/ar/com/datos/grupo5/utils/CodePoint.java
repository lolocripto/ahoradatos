/**
 * 
 */
package ar.com.datos.grupo5.utils;

import ar.com.datos.grupo5.excepciones.CodePointException;

/**
 * @author Led Zeppelin
 *
 */
public class CodePoint {

	public final Character getChar(final int codepoint) throws CodePointException{
		if (codepoint <= Character.MAX_VALUE && codepoint >= Character.MIN_VALUE) {
			return new Character(Character.toChars(codepoint)[0]);	
		} else {
				throw new CodePointException();
		}
	}
	
	public final int getCodePoint(final Character letra){
		char[] i = new char[1];
	    i[0] = letra;
	    return Character.codePointAt(i, 0);	
	}
}
