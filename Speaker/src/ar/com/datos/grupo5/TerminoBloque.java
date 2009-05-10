/**
 * 
 */
package ar.com.datos.grupo5;

/**
 * @author Led Zeppelin
 *
 */
public class TerminoBloque {
	private Long idTermino;
	private Long bloque;
	
	public TerminoBloque(final Long idtermino,final Long bloque) {
		this.setIdTermino(idtermino);
		this.setBloque(bloque);
	}

	/**
	 * @param bloque the bloque to set
	 */
	public void setBloque(Long bloque) {
		this.bloque = bloque;
	}

	/**
	 * @return the bloque
	 */
	public Long getBloque() {
		return bloque;
	}

	/**
	 * @param idTermino the idTermino to set
	 */
	public void setIdTermino(Long idTermino) {
		this.idTermino = idTermino;
	}

	/**
	 * @return the idTermino
	 */
	public Long getIdTermino() {
		return idTermino;
	}
	
}
