package ar.com.datos.grupo5.compresion.lz78;

/**
 * Componente de una dupla en LZ78.
 * @author LedZeppelin
 *
 */
public class CodeComponentLZ78 {
	private NodeLZ78 node = null;
    //TODO: ver significado.
	private int poradi = 0;

	private EncodedElementLZ78 element = null;
    
	/**
     * Devuelve el elemento codificado en LZ78.
     * @return EncodedElementLZ78
     */
	public EncodedElementLZ78 getElement() {
		return element;
	}
    /**
     * Setea el elemento al pasado por parametro. 
     * @param element
     */
	public void setElement(EncodedElementLZ78 element) {
		this.element = element;
	}
	/**
     * Devuelve el nodo. 
     * @param element
     */
	public NodeLZ78 getNode() {
		return node;
	}
	/**
     * Setea el nodo al pasado por parametro. 
     * @param element
     */
	public void setNode(NodeLZ78 node) {
		this.node = node;
	}

	public int getPoradi() {
		return poradi;
	}

	public void setPoradi(int poradi) {
		this.poradi = poradi;
	}

	public void sumaPoradi(int poradi) {
		this.poradi += poradi;
	}

	public void decrementaPoradi() {
		this.poradi--;
	}

}
