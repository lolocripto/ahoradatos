package ar.com.datos.grupo5.compresion.ppmc;
/**
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ar.com.datos.grupo5.compresion.aritmetico.ParCharProb;
/**
 * @author Led Zeppiln
 *
 */
public class Orden {

	private int Numero;
	private HashMap< String, Contexto > listaContexto;
	
	public Orden(){
	}
	
	/**
	 * Obtengo el contexto con el nombre recibido.
	 * @param contexto Contexto el cual voy a devolver. 
	 * @return El contexto.
	 */
	public final Contexto getContexto(String contexto){
		return this.listaContexto.get(contexto);
	}
	
	/**
	 * Incrementa la frecuencia del char que se emitio, sino existia el contexto se 
	 * crea.
	 * @param contexto Contexto en el cual actulizo la frecuencia.
	 * @param letra Letra de la cual actualizo la frecuencia.
	 */
	public final boolean actualizarContexto(String contexto, Character letra){
		Contexto ctx = this.listaContexto.get(contexto); 
		if (ctx.actualizarContexto(letra)) {
			//Actualizacion correcta
			return true;
		} else {
			//La letra a actualizar con existía dentro del contexto
			if (ctx.crearCharEnContexto(letra)){
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Obtiene todos los valores del HashMap
	 * @return Un vector de Objects.
	 */
	public final Collection<ParCharProb> obtenerArrayList(String contexto){
		return this.listaContexto.get(contexto).getArrayCharProb();
	}
	
	/**
	 * Crea un contexto en el orden actual.  
	 * @param nombreContexto Nombre del contexto. 
	 * @return El contexto creado.
	 */
	public final Contexto crearContexto(String nombreContexto){
		//Cuando no existe se crea
		Contexto ctx = new Contexto(nombreContexto);
		this.listaContexto.put(nombreContexto, ctx);
		return ctx;
	}
}
