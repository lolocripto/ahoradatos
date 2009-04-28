/**
 * 
 */
package ar.com.datos.UnidadesDeExpresion;

/**
 * @author LedZeppelin
 *
 */
public class Palabra implements IunidadDeHabla {

	/**
	 * 
	 */
	private boolean pronunciable;
	
	/**
	 * 
	 */
	private Object sonido;
	
	/**
	 * 
	 */
	private String palabraEscrita;
	
	/**
	 * 
	 */
	private String equivalenteFonetico;
	
	/**
	 * Constructor para los objetos palabra, por defecto al instanciarse todas
	 * las palabras son pronunciables.
	 * @param palabraEscrita palabra tal cual como fue escrita por el usuario
	 * @param equivalenteFonetico palabra procesada para almacenar en disco
	 */
	public Palabra(String palabraEscrita, String equivalenteFonetico) {
		
		this.pronunciable = true;
		this.palabraEscrita = palabraEscrita;
		this.equivalenteFonetico = equivalenteFonetico;
	}
	
	public Palabra(String soloPalabra){
		this.palabraEscrita=soloPalabra;
		this.pronunciable=true;
	}
 
	/**
	 * @return El string con la palabra. 
	 */
	public final String toString() {
		return palabraEscrita;
	}
	
	/**
	 * @return El string con la palabra.
	 */
	public final String getTextoEscrito(){
		return palabraEscrita;
	}
	
	/**
	 * Dos palabras son iguales si y solo si tienen igual equivalente fonético.
	 * 
	 * @return siempre 0;
	 */
	public final int hashCode() {

		return 0;

	}
	
	/**
	 * 
	 */
	public final boolean esPronunciable() {
		return this.pronunciable;
	}

	/**
	 * @param pronunciable .
	 */
	public final void setPronunciable(final boolean pronunciable) {
		this.pronunciable = pronunciable;
	}
	
	/**
	 * 
	 */
	public final Object getAudio() {
		return this.sonido;

	}

	/**
	 * 
	 */
	public final void setAudio(final Object o) {
		this.sonido = o;

	}
	
    /**
     * Devuelve su equivalente fonético.
     * @return El string con el equivalente fonetico.
     */
	public final String getEquivalenteFonetico() {
		return equivalenteFonetico;
	}
   
	/**
	 * 
	 */
	public final void setEquivalenteFonetico(String equivalenteFonetico) {
		this.equivalenteFonetico = equivalenteFonetico;
	}

	/**
	 * 
	 */
	public void say() {
		
    }
	/**
	 * @override
	 * @param o.
	 * @return true si tienen el mismo equivalente fonetico.
	 */
	public final boolean equals(final Object o) {
		String s1 = this.getEquivalenteFonetico();
		String s2 =(((Palabra) (o)).getEquivalenteFonetico()).toString();
		if (s1.compareTo(s2) == 0 )
		  return true;
	return false;   
	}
    /**
     * Compara el equivalente fonetico de la palabra.
     * @param o Objeto a comparar
     * @return int >0 si el equivalente Fonetico de o es menor 
     *  <0 si es mayor y  0 si son iguales.
     */
	public final int compareTo(final Object o) {
		String eqiu = ((Palabra) o).getEquivalenteFonetico();
		return this.getEquivalenteFonetico().compareTo(eqiu);
	}

}
