package ar.com.datos.grupo5.compresion.lz78;


/**
 * NodeLZ78
 * Clase que representa el nodo en el LZ78 Tree
 **/
class NodeLZ78 {
	private int index;				//Índice del nodo, orden de creación
   private int level;				// Level en el arbol
   private char character;			// caracter (ultimo caracter en el String)
   private String phrase;			// Every node is proxy of some phrase. ver!
   private NodeLZ78 next = null;    // Siguiente nodo en el mismo nivel.
   private NodeLZ78 sons = null;    // Hijos.
   
   /**
    * Constructor.
    * @param pI
    * @param pLevel
    * @param pStr
    * @throws ExceptionNodeLZ78
    */
   public NodeLZ78( int pI, int pLevel, String pStr ) throws ExceptionNodeLZ78 {
   		/** Si la longitud de pStr es 0 y no es raiz
   		 * se lanza la exception "StringPassedToNodeIsEmpty"
   		 * Si la string es null, se lanza la excepción "NoStringPassedToNode".
   		 */ 
   	  index = pI; //si es 0 es raíz
      level = pLevel;
      if ( pStr == null )
      	throw new ExceptionNodeLZ78( "No se paso un string al nodo" );
      if ( pStr.length( ) == 0 && pI != 0 )
      	throw new ExceptionNodeLZ78( "La cadena esta vacía" );
      phrase = new String( pStr );
      if ( pStr.length( ) != 0 )
	      character = pStr.charAt( pStr.length( ) - 1 );
      else
      	character = ' ';
   }

   /**
    * Devuelve el índice del nodo
    * @return int
    */
   public int getIndex( ) {
	   return index;
   }
   
   /**
    * Devuelve el caracter del nodo.
    * Returns the character of the node.
    * @return char
    */
   
   public char getCharacter( ) {
   
   	return character;
   }
   /**
    * Devuelve la phrase del nodo.
    * @return String
    */
   public String getPhrase( ) {
     	return phrase;
   }
   /**
    * Devuelve el sigueinte nodo
    * @return NodeLZ78
    */
   public NodeLZ78 getNext( ) {
   	return next;
   }
   
   /**
    * Setea el siguiente nodo.
    * @param pNode el nodo siguiente.
    */
   private void setNext( NodeLZ78 pNode ) {
      	next = pNode;
   }
   /**
    * Inserta un nuevo hijo al nodo.
    * @param pNode.
    */
   public void insertSon( NodeLZ78 pNode ) {
      pNode.setNext( sons );
      sons = pNode;
   }
   /**
    * Devuelve todos los hijos del nodo.
    * @return NodeLZ78
    */
   public NodeLZ78 getSons( ) {
   	return sons;
   }
  /**
   * Devuelve el nivel del nodo en el arbol.
   * @return int
   */
   public int getLevel( ) {
   	return level;
   }
   /**
    * Devuelve la información del nodo.
    * @return String
    */
   public String getAsString( ) {
      return new StringBuffer( ).append( "(" ).append( index ).append( ":").append( phrase ).append( ")" ).toString( );
   }
   
   /**
    * Devuelve la información del nodo con nivel. 
    * Para poder representar su nivel real en el arbol.
    * @return String.
    */
   public String getAsStringWithLevel( ) {
   	String hMezer = "";
      for ( int i = 0; i < level; i++ ) {
      	hMezer = hMezer + "  ";
      }
      return new StringBuffer( ).append( hMezer ).append( "(" ).append( index ).append( ":").append( phrase ).append( ")" ).toString( );
   }
   /**
    * Devuelve la cantidad de hijos que posee el nodo
    * @return int
    */
   public int countAllLevelSons( ) {
   	int hCount = 0;
      NodeLZ78 hNode = this.getSons( );
      while ( hNode != null ) {
      	hCount += hNode.countAllLevelSons( );
         hCount++;
         hNode = hNode.getNext( );
      }
      return hCount;
   }
}
