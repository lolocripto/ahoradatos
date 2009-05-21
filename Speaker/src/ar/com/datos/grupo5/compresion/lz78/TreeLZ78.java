package ar.com.datos.grupo5.compresion.lz78;

/**TreeLZ78
 * Clase que representa la lista de frases en LZ78 como nodos de un arbol
 * @author gabriel
 *
 */
public class TreeLZ78 {
	private NodeLZ78 root;

	private NodeLZ78 actual; 

	private int actualindex = 0;
    /**
     * Constructor, crea la raíz
     *
     */
	public TreeLZ78() {
		try {
			actual = root = new NodeLZ78(actualindex++, 0, "");
		} 
		catch (ExceptionNodeLZ78 e) {
		}
	}
    /**
     * Va a la raíz
     *
     */
	public void goToRoot() {
		actual = root;
	}
    
	/** 
     * Da el nodo actual.
     * @return NodeLZ78
     */
	private NodeLZ78 getActual() {
		return actual;
	}
    /**
     * Devuelve el string del nodo actual
     * @return String
     */
	public String getActualString() {
		return actual.getPhrase();
	}
    /**
     * Devuelve el indice del actual.
     * @return int
     */
	public int getActualIndex() {
		// Returns actual index
		return actual.getIndex();
	}
    /**
     * Busca el hijo de menjor nivel que coincide con pchar.l 
     * @param pChar
     * @return NodeLZ78
     */
	private NodeLZ78 getLowerLevelSonByCharacter(char pChar) {
		boolean hFound = false;
		NodeLZ78 hNode1 = null, hNode2 = actual.getSons();
		while (hNode2 != null && !hFound) {
			hFound = (hNode2.getCharacter() == pChar);
			hNode1 = hNode2;
			hNode2 = hNode2.getNext();
		}
		if (!hFound)
			return null;
		return hNode1;
	}
   /**
    * Va al hijo de menor nivel lamado pChar
    * @param pChar
    * @return int
    * @throws ExceptionTreeLZ78 si no encuentra un nodo coincidente con pchar
    */
	public int goToLowerLevelSon(char pChar) throws ExceptionTreeLZ78 {
		NodeLZ78 hNode;
		if ((hNode = getLowerLevelSonByCharacter(pChar)) == null)
			throw new ExceptionTreeLZ78("El nodo no esta presente");
		NodeLZ78 hNode1 = actual.getSons();
		int i, hCount = 1;
		for (i = 1; hNode1 != hNode && i < actualindex; i++) {
			hCount += hNode1.countAllLevelSons();
			hCount++;
			hNode1 = hNode1.getNext();
		}
		actual = hNode;
		return hCount;
	}
   /**
    * Inserta un nuevo nodo a la lista de hijos del nodo actual
    * @param pStr
    * @return NodeLZ78 el nodo insertado
    * @throws ExceptionTreeLZ78 si ya se encuentra el nodo insertado
    */
	public NodeLZ78 insertNode(String pStr) throws ExceptionTreeLZ78 {
		NodeLZ78 hNode;
		if (pStr.length() == 0 || pStr == null)
			throw new ExceptionTreeLZ78("String vacia");
		if (pStr.compareTo(actual.getPhrase() + pStr.charAt(pStr.length() - 1)) != 0)
			throw new ExceptionTreeLZ78(
					"String del nuevo nodo no coincide con la string del nodo actual ");
		if ((getLowerLevelSonByCharacter(pStr.charAt(pStr.length() - 1))) != null)
			throw new ExceptionTreeLZ78("Nodo ya esta presente");
		try {
			hNode = new NodeLZ78(actualindex++, actual.getLevel() + 1, pStr);
			actual.insertSon(hNode);
			return hNode;
		} catch (ExceptionNodeLZ78 e) {
			throw new ExceptionTreeLZ78("Node: " + e.getMessage());
		}
	}
    //TODO: ver sentido de su funcionamiento
	private int recurseSpann(int pI, NodeLZ78 pNode, NodeLZ78[] pPole) {
		NodeLZ78 hSons, hNexts;
		pPole[pI++] = pNode;
		if ((hSons = pNode.getSons()) != null) {
			pI = recurseSpann(pI, hSons, pPole);
		}
		if ((hNexts = pNode.getNext()) != null) {
			pI = recurseSpann(pI, hNexts, pPole);
		}
		return pI;
	}
    /**
     * Devuelve todo los nodos en un array de String
     * @return String[]
     */
	public String[] getAllNodesAsArrayOfString() {
		NodeLZ78[] hPole = getAllNodesAsArray();
		String[] hStringPole = new String[hPole.length];
		for (int i = 0; i < hPole.length; i++) {
			hStringPole[i] = hPole[i].getAsStringWithLevel();
		}
		return hStringPole;
	}
	/**
     * Devuelve todo los nodos en un array
     * @return NodeLZ78[]
     */
	
	private NodeLZ78[] getAllNodesAsArray() {
		// Returns all nodes (except the root) in an array sorted by their index.
		NodeLZ78[] hPole = new NodeLZ78[actualindex - 1];
		NodeLZ78 hActual = root.getSons();
		int hI = recurseSpann(0, hActual, hPole);
		return hPole;
	}
    /**
     * Devuelve todos los nodos en un formato mostrable
     * @return String
     */
	public String getAllNodesAsString() {
		String hString = new String();
		NodeLZ78[] hPole = getAllNodesAsArray();
		for (int i = 0; i < hPole.length; i++) {
			hString = hString + hPole[i].getAsString();
			if (i < (hPole.length - 1))
				hString = hString + ", ";
		}
		return hString;
	}
}
