package ar.com.datos.grupo5.compresion.lzp;
import ar.com.datos.grupo5.compresion.aritmetico.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.excepciones.SessionException;
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
	 * Motor aritmetico para los cararteres emitodos.
	 */
	private LogicaAritmetica motorAritCaracteres;
	
	/**
	 * Motor aritmetico para las longitudes.
	 */
	private LogicaAritmetica motorAritLongitudes;
	
	/**
	 * Contextos y posiciones.
	 */
	private ListaContextos listaContextos;
	
	/**
	 * Flag para saber si la session fue inicializada. 
	 */
	private boolean sesionIniciada = true;
	
	/**
	 * Ultimo Contexto, para saber que emito. 
	 */
	private String ultCtx;
	
	/**
	 * @return the ultCtx
	 */
	public final String getUltCtx() {
		return ultCtx;
	}

	/**
	 * @param ultCtx the ultCtx to set
	 */
	public final void setUltCtx(String ultCtx) {
		this.ultCtx = ultCtx;
	}

	public Lzp(){
		listaContextos = new ListaContextos();
		motorAritCaracteres = new LogicaAritmetica();
		motorAritLongitudes = new LogicaAritmetica();
		letrasCtx = new Orden();
		longitudesCtx = new Contexto();
		ultCtx = new String();
	}

	/**
	 * 
	 * @param cadena
	 */
	public String comprimir(String cadena) throws SessionException{
		if (!sesionIniciada) {
			throw new SessionException();
		}
		// TODO Auto-generated method stub
		
		String resultado = "";
		
		//Trabajar con un StringBuffer es mas rapido.
		StringBuffer buffer = new StringBuffer(cadena);
		
		//Si no hay nada aca, entonces es la primera iteracion.
		if (longitudesCtx.getCantidadLetras() == 0) {
			char primero = buffer.charAt(0);
			char segundo = buffer.charAt(1);
			
			//Saco los 2 primeros.
			buffer.delete(0, 2);

			//TODO: Hay que emitir estos caracteres sin longitudes.
			
			// Genero el CTX.
			ultCtx = buffer.substring(0, 2);
			listaContextos.setPosicion(ultCtx, 2);
		}
		
		resultado += ComprimirInterno(buffer);

		return resultado;
	}

	@Override
	public String descomprimir(String datos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizarSession() {
		sesionIniciada = false;
	}

	@Override
	public void iniciarSesion() {
		listaContextos = new ListaContextos();
		motorAritCaracteres = new LogicaAritmetica();
		motorAritLongitudes = new LogicaAritmetica();
		letrasCtx = new Orden();
		longitudesCtx = new Contexto();
		sesionIniciada = true;
		ultCtx = new String();
	}

	private String ComprimirInterno(StringBuffer cadena) {
		String result = new String();
		while (cadena.length() > 0){
			// Leer de a uno e ir revisando y comprimiendo en la salida
			char caracter = cadena.charAt(0);
			// Buscar el contexto...
			if (listaContextos.getPosicion(ultCtx)== null){
				// Creo el contexto y emito con long de match 0
				listaContextos.setPosicion(ultCtx, 2);
			}
			// Lo saco porque ya lo procese
			cadena.delete(0, 1);
			// Actualizo el ultimo contexto
			
			
			// La pos del contexto que se modifico es:
			// posActual - (length(match) + 1)
		} 
		return result;
	}
}