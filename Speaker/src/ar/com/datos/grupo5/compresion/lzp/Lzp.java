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
	private Orden letrasLeidas;
	private Contexto longitudes;
	private LogicaAritmetica compresorAritemtico;

	public Lzp(){

	}

	public void finalize() throws Throwable {

	}

	public void algoritmoDeMatch(){

	}

	public void algoritmoDePersistenciaABinario(){

	}

	/**
	 * 
	 * @param cadena
	 */
	public byte[] comprimir(String cadena){
		return null;
	}

	public String descomprimir(){
		return "";
	}

	public byte[] finalizarCompresion(){
		return null;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(IndiceContexto indice) {
		Indice = indice;
	}

	/**
	 * @return the indice
	 */
	public IndiceContexto getIndice() {
		return Indice;
	}

	/**
	 * @param letrasLeidas the letrasLeidas to set
	 */
	public void setLetrasLeidas(Orden letrasLeidas) {
		this.letrasLeidas = letrasLeidas;
	}

	/**
	 * @return the letrasLeidas
	 */
	public Orden getLetrasLeidas() {
		return letrasLeidas;
	}

	/**
	 * @param longitudes the longitudes to set
	 */
	public void setLongitudes(Contexto longitudes) {
		this.longitudes = longitudes;
	}

	/**
	 * @return the longitudes
	 */
	public Contexto getLongitudes() {
		return longitudes;
	}

	/**
	 * @param compresorAritemtico the compresorAritemtico to set
	 */
	public void setCompresorAritemtico(LogicaAritmetica compresorAritemtico) {
		this.compresorAritemtico = compresorAritemtico;
	}

	/**
	 * @return the compresorAritemtico
	 */
	public LogicaAritmetica getCompresorAritemtico() {
		return compresorAritemtico;
	}

}