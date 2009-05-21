package ar.com.datos.grupo5.compresion.lz78;

/**
 * Clase que contiene el elemento codificado en LZ78 dupla (índice, phrase)
 * 
 * @author gabriel
 * 
 */
public class EncodedElementLZ78 {
	private int indexOfPhrase; // índice que representa el nivel en el arbol

	//	 caracter añadido para preceder a la frase

	private char characterAddedToPhrase;

	private EncodedElementLZ78 next = null; // siguiente elemento (hermano).

	/**
	 * Constructor
	 */
	public EncodedElementLZ78(int pIndex, char pLetter) {
		indexOfPhrase = pIndex;
		characterAddedToPhrase = pLetter;
	}

	/**
	 * Setea el siguiente elemento.
	 * 
	 * @param pElement
	 */
	public void setNext(EncodedElementLZ78 pElement) {
		// 
		next = pElement;
	}

	/**
	 * Devuelve el siguiente elemento
	 * 
	 * @return EncodedElementLZ78
	 */
	public EncodedElementLZ78 getNext() {
		return next;
	}

	/**
	 * Devuelve el elemento convertido en String.
	 * 
	 * @return String
	 */
	public String asString() {
		//String("(" + indexOfPhrase + "," + characterAddedToPhrase
		//+ ")");
		return new String( new Integer(indexOfPhrase).toString() + characterAddedToPhrase);
	}

}
