package ar.com.datos.grupo5.compresion.lz78;

/** 
 * Lista de elementos codificados en LZ78
 * @author gabriel
 *
 */
public class EncodedListLZ78 {
	EncodedElementLZ78 first = null; //primer elemento de la lista.

	EncodedElementLZ78 last = null;

	/**
	 * Si la lista esta vacía
	 * @return boolean: true si la lista esta vacía.
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Agrega un elemento al final de la lista
	 * @param pIndex
	 * @param pPhrase
	 * @return EncodedElementLZ78, elemento insertado a la lista.
	 * @throws ExceptionEncodedListLZ78
	 */
	public EncodedElementLZ78 addElement(int pIndex, String pPhrase)
			throws ExceptionEncodedListLZ78 {
		if (pPhrase == null)
			throw new ExceptionEncodedListLZ78("no se paso una string");
		if (pPhrase.length() == 0)
			throw new ExceptionEncodedListLZ78("string vacia");
		EncodedElementLZ78 hElement = new EncodedElementLZ78(pIndex, pPhrase
				.charAt(pPhrase.length() - 1));
		if (isEmpty())
			first = last = hElement;
		else {
			last.setNext(hElement);
			last = hElement;
		}
		return hElement;
	}

	/**
	 * Devuelve la lista completa.
	 * @return String
	 */
	public String asString() {
		// Returns the whole list converted to string
		String hStr = new String();
		if (isEmpty())
			return hStr;
		for (EncodedElementLZ78 actual = first; actual != null; actual = actual
				.getNext()) {
			hStr = hStr + actual.asString();
			if (actual.getNext() != null)
				hStr = hStr + ";";
		}
		return hStr;
	}

}
