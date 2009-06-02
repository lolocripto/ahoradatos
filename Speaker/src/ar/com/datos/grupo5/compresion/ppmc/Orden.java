package ar.com.datos.grupo5.compresion.ppmc;
/**
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;

import ar.com.datos.grupo5.compresion.aritmetico.ParCharProb;
/**
 * @author Led Zeppiln
 *
 */
public class Orden {

	private int Numero;
	//FIXME: Encapsular el contexto.
	private HashMap<String, HashMap<Character,ParCharProb> > listaContexto;
	
	public Orden(){
		
	}
	
	/**
	 * Obtengo la lista de ocurrencias en el contexto recibido.
	 * @param contexto Contexto del cual voy a buscar la lista de ocurrencias. 
	 * @return Lista de ocurrencias de las letras.
	 */
	public final HashMap<Character,ParCharProb> obtenerListaContextos(String contexto){
		return this.listaContexto.get(contexto);
	}
	
	/**
	 * Incrementa la frecuencia del char que se emitio, sino existia el contexto se 
	 * crea.
	 * @param contexto Contexto en el cual actulizo la frecuencia.
	 * @param letra Letra de la cual actualizo la frecuencia.
	 */
	public final void incrementarFrecuenciaChar(String contexto, Character letra){
		ParCharProb par;
		if (this.listaContexto.containsKey(contexto)) {
			//El contexto Existe
			if (this.listaContexto.get(contexto).containsKey(letra)){
				par = this.listaContexto.get(contexto).get(letra);
				par.setProbabilidad(par.getProbabilidad() + 1);
			} else {
				par = new ParCharProb(letra, 1);
				this.listaContexto.get(contexto).put(letra, par);
			}
		} else {
			//Creo el contexto
			this.crearContexto(contexto, letra);
		}
		
	}
	
	/**
	 * Creo el contexto, lo inicializo con el caracter buscado y el escape en 1  
	 * @param contexto Contexto a crear.
	 * @param letra Letra a agregar en ese contexto.
	 */
	private final void crearContexto(String contexto, Character letra) {
		
		ParCharProb par;
		//El contexto no existe por lo tanto lo creo
		this.listaContexto.put(contexto, new HashMap<Character,ParCharProb>());
		
		//FIXME: Para el EOF QUE PONGO!!!, me conviene cambiarlo a String
		/* NO VA
		 * par = new ParCharProb(letra, 1);
		 * this.listaContexto.get(contexto).put(letra, par);
		 */
		
		
		//Creo el ESC, ver que pasa aca. El ultimo UNICODE
		par = new ParCharProb(new Character(' '), 1);
	
	}
	
	/**
	 * Obtiene todos los valores del HashMap
	 * @return Un vector de Objects.
	 */
	public final Collention<ParCharProb> obtenerArrayList(String contexto){
		return this.listaContexto.get(contexto).values();
	}
	
	private final void inicializarContexto(){
		//Cuando esta vacio lo inicializo
	}
}
