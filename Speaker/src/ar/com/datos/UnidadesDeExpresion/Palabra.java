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
	public final int hashCode(){
		
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

}
