/**
 * 
 */
package ar.com.datos.grupo5.compresion.ppmc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ar.com.datos.grupo5.compresion.aritmetico.ParCharProb;

/**
 * @author Led Zeppelin
 *
 */
public class Contexto {

	/**
	 * Contiene todas la letras del contexto asociado con su ocurrencia.
	 */
	private HashMap<Character,ParCharProb> listaOcurrenciaPorCaracter;
	
	/**
	 * Contiene el nombre del contexto asociado a la lista de Ocurrencias.
	 */
	private String contexto;
	
	/**
	 * Mantiene la cantidad total de letra emitidas incluyendo a los ESC.
	 */
	private int cantidadLetras;
	
	/**
	 * Constructor del contexto.
	 */
	public Contexto() {
		this.listaOcurrenciaPorCaracter = new HashMap<Character,ParCharProb>();
		this.setCantidadLetras(0);
	}
	
	/**
	 * Constructor del contexto. Setea el nombre del contexto.
	 * @param nombreContexto Nombre del contexto.
	 */
	public Contexto(final String nombreContexto) {
		this.setContexto(nombreContexto);
		this.listaOcurrenciaPorCaracter = new HashMap<Character,ParCharProb>();
		this.setCantidadLetras(0);
	}
	
	/**
	 * Intenta actualizar la letra del contexto, incrementanto la frecuencia.
	 * @param letra Letra a la cual se incrementa la frecuencia.
	 * @return	True si la pudo modificar o false si no existe la letra.
	 */
	public final boolean actualizarContexto(Character letra){
		if (this.listaOcurrenciaPorCaracter.containsKey(letra)){
			ParCharProb par;
			
			//Obtengo el elemento vinculado a la letra y actualizo.
			par = this.listaOcurrenciaPorCaracter.get(letra);
			par.setFrecuencia(par.getFrecuencia()+1);
			this.cantidadLetras++;
			
			
			//Actualizo la letra con el elemento par.
			this.listaOcurrenciaPorCaracter.put(letra, par);
			return true;
		}
		return false;
	}
	
	/**
	 * Intenta crear la letra en el contexto con frecuencia en 1.
	 * @param letra Letra a agregar.
	 * @return True si pudo agregar la letra, false si ya existía.
	 */
	public final boolean crearCharEnContexto(Character letra){
		if (!this.listaOcurrenciaPorCaracter.containsKey(letra)){
			ParCharProb par = new ParCharProb(letra, 1);
			par.setFrecuencia(1);
			
			//Actualizo la letra con el elemento par.
			this.listaOcurrenciaPorCaracter.put(letra, par);
			
			this.cantidadLetras++;
			
			return true;
		}
		return false;
	}
	
	public final Collection<ParCharProb> getArrayCharProb(){
		return this.listaOcurrenciaPorCaracter.values();
	}

	/**
	 * @param contexto the contexto to set
	 */
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	/**
	 * @return the contexto
	 */
	public String getContexto() {
		return contexto;
	}

	/**
	 * Verifica si existe la letra en el contexto.
	 * @param letra Letra a verficar su existencia.
	 * @return True si la letra ya exite y false si la letra no existe.
	 */
	public final boolean existeChar(Character letra) {
		if (this.listaOcurrenciaPorCaracter.containsKey(letra)) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica si existe la letra en el contexto.
	 * @param letra Letra a verficar su existencia.
	 * @return True si la letra ya exite y false si la letra no existe.
	 */
	public final ParCharProb getChar(Character letra) {
		if (this.listaOcurrenciaPorCaracter.containsKey(letra)) {
			return this.listaOcurrenciaPorCaracter.get(letra);
		}
		return null;
	}
	
	/**
	 * @param cantidadLetras the cantidadLetras to set
	 */
	public void setCantidadLetras(int cantidadLetras) {
		this.cantidadLetras = cantidadLetras;
	}

	/**
	 * @return the cantidadLetras
	 */
	public int getCantidadLetras() {
		return cantidadLetras;
	}
	
	/**
	 * Actualiza las probabilidades de todos los caracteres según 
	 * la cantidad de letras en el contexto.
	 */
	public void actualizarProbabilidades(){
		//Recorrer todo el hashMap y actualizar la frecuencia.
		Iterator<Entry<Character,ParCharProb>> it = this.listaOcurrenciaPorCaracter.entrySet().iterator();
		Entry<Character,ParCharProb> elemento;		
		ParCharProb par;
		while (it.hasNext()) {
			elemento = it.next();
			par = elemento.getValue();
			par.setProbabilidad((float) par.getFrecuencia()/ (float) this.cantidadLetras);
		}
	}
}
