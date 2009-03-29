/**
 * 
 */
package ar.com.datos.UnidadesDeExpresion;

/**
 * @author zeke
 *
 */
public class Palabra implements IunidadDeHabla {

	private boolean pronunciable;
	private Object sonido;
	private String palabraEscrita;
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
 
	@Override
	public String toString() {
		return palabraEscrita;
	}
	
	/**
	 * Dos palabras son iguales si y solo si tienen igual equivalente fonético
	 */
	public int hashCode(){
		
		return 0;
		
	}
	
	public boolean esPronunciable() {
		return this.pronunciable;
	}

	public void setPronunciable(boolean pronunciable) {
		this.pronunciable = pronunciable;
	}
	
	public Object getAudio() {
		return this.sonido;

	}

	public void setAudio(Object o) {
		this.sonido = o;

	}

	public String getEquivalenteFonetico() {
		return equivalenteFonetico;
	}

	public void setEquivalenteFonetico(String equivalenteFonetico) {
		this.equivalenteFonetico = equivalenteFonetico;
	}

	public void say() {
		// TODO Auto-generated method stub
		
	}

}
