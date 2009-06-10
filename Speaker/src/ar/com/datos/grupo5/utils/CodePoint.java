/**
 * 
 */
package ar.com.datos.grupo5.utils;

/**
 * @author Led Zeppelin
 *
 */
public class CodePoint {

	public final Character getChar(final int CodePoint){
		return new Character(Character.toChars(CodePoint)[0]);
	}
	
	public final int getCodePoint(final Character letra){
		char[] i = new char[1];
	    i[0] = letra;
	    return Character.codePointAt(i, 0);	
	}
}
