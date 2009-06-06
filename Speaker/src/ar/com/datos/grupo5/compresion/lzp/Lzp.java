package ar.com.datos.grupo5.compresion.lzp;
import ar.com.datos.grupo5.compresion.aritmetico.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.interfaces.Compresor;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:24 a.m.
 */
public class Lzp implements Compresor {

	private IndiceContexto Indice;
	
	/**
	 * Contexto para las letra? porque carajo se llama Orden?
	 */
	private Orden letrasCtx;
	
	/**
	 * Contextos para las longitudes, para pasar al compresor aritmetico.
	 */
	private Contexto longitudesCtx;
	
	/**
	 * Compresor aritmetico para las longitudes y cararteres emitodos.
	 */
	private LogicaAritmetica compresorAritemtico;
	
	/**
	 * Contextos y posiciones.
	 */
	private ListaContextos listaContextos;

	public Lzp(){
		listaContextos = new ListaContextos();
	}

	/**
	 * 
	 * @param cadena
	 */
	public String comprimir(String cadena){
		return null;
	}

	@Override
	public String descomprimir(String datos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizarSession() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iniciarSesion() {
		// TODO Auto-generated method stub
		
	}

}