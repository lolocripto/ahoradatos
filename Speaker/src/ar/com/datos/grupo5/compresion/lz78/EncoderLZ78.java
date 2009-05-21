package ar.com.datos.grupo5.compresion.lz78;

public class EncoderLZ78 {
	private int start;
	
	private int end; 

	private int poradi;

	private String text; // Texto a codificar

	private TreeLZ78 phraseTree; 

	private EncodedListLZ78 encodedText;  // Lista de textos codificados

	private String actualPhrase; //referencia a la ultima frase

	private int actualIndexOfPhrase; //referencia a la ultima frase

	/**
	 * Inicializa el codificador
	 * @param pStr
	 * @throws ExceptionEncoderLZ78
	 */
	public EncoderLZ78(String pStr) throws ExceptionEncoderLZ78 {
		if (pStr == null)
			throw new ExceptionEncoderLZ78("No text passed");
		if (pStr.length() == 0)
			throw new ExceptionEncoderLZ78("Text is empty");
		text = new String(pStr);
		initialize();
	}
   /**
    * Inicializa atriburos
    *
    */
	private void initialize() {
		start = end = 0;
		phraseTree = new TreeLZ78();
		encodedText = new EncodedListLZ78();
		actualPhrase = null;
	}

	private boolean isEndOfText() {
		return (text.length() <= end);
	}
    /**
     * Devuelve la ista de texto codificado
     * @return EncodedListLZ78
     */
	public EncodedListLZ78 getEncodedText() {
		return encodedText;
	}
    /**
     * Devuelve el comienzo
     * @return int
     */
	public int getStart() {
		return start;
	}
	/**
	 * Devuelve el final
	 * @return int
	 */
	public int getEnd() {
	    return end;
	}
    /**
     * Devuelve el arbol utilizado.
     * @return TreeLZ78
     */
	public TreeLZ78 getPhraseTree() {
		return phraseTree;
	}
   /**
    * Hace la codificacion de a pasos, ejecutandose un paso cada vez
    * que se llama a este metodo
    * @return CodeComponentLZ78 
    * @throws ExceptionEncoderLZ78
    */ 
	public CodeComponentLZ78 makeCodingStep() throws ExceptionEncoderLZ78 {
		try {
			CodeComponentLZ78 hComponent = null;
			if (!(isEndOfText())) {
				hComponent = findPhrase();
				hComponent.setElement(encodedText.addElement(
						actualIndexOfPhrase, actualPhrase));
				moveToNextIntoText();
			}
			return hComponent;
		} catch (ExceptionEncodedListLZ78 e) {
			throw new ExceptionEncoderLZ78("EncodedList: " + e.getMessage());
		}
	}

	public int getPoradiPhrase() {
		return poradi;
	}
    /**
     * Codifica el texto a LZ78 y lo guarda en la lista
     * @throws ExceptionEncoderLZ78
     */
	public void runCoding() throws ExceptionEncoderLZ78 {
		try {
			initialize();
			while (!(isEndOfText())) {
				findPhrase();
				encodedText.addElement(actualIndexOfPhrase, actualPhrase);
				moveToNextIntoText();
			}
		} catch (ExceptionEncodedListLZ78 e) {
			throw new ExceptionEncoderLZ78("EncodedList: " + e.getMessage());
		}
	}
	
    /**
     * Busca una frase en el principio del texto desde start y al final con el indice end
     * y despues de encontrar el nodo que representa la frase buscada, un nuevo nodo con 
     * esa frase, concatenada con el caracter text[end+1] es creado y agregado a la listas 
     * de hijos del nodo 
     * @return
     * @throws ExceptionEncoderLZ78
     */
	private CodeComponentLZ78 findPhrase() throws ExceptionEncoderLZ78 {
		boolean hFound = false;
		int hIndex = 0;
		CodeComponentLZ78 hComponent = new CodeComponentLZ78();
		end = start;
		phraseTree.goToRoot();
		while (!hFound && !isEndOfText()) {
			try {
				hIndex = phraseTree.getActualIndex();
				hComponent.sumaPoradi(phraseTree.goToLowerLevelSon(text
						.charAt(end)));
				end++;
			} catch (ExceptionTreeLZ78 e) {
				try {
					actualPhrase = new String(text.substring(start, end + 1));
					hComponent.setNode(phraseTree.insertNode(actualPhrase));
					hFound = true;
				} catch (ExceptionTreeLZ78 exc) {
					throw new ExceptionEncoderLZ78("Tree: " + exc.getMessage());
				}
			}
		}
		actualIndexOfPhrase = phraseTree.getActualIndex();
		if (!hFound) {
			actualIndexOfPhrase = hIndex;
			actualPhrase = new String(text.substring(start, text.length()));
			end--;
			hComponent.decrementaPoradi();
		}
		return hComponent;
	}
	
	/**
	 * Mueve el índice al principio de nueva frase.
	 *
	 */
	private void moveToNextIntoText() {
		start = end + 1;
		end = start;
	}

}
