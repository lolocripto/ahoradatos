package ar.com.datos.sortExterno;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:16:31 p.m.
 */
public class NodoOT {

	/**
	 * El id global del termino.
	 */
	private Long idTermino;
	
	/**
	 * Offset del termino.
	 */
	private int offset;
	
	/**
	 * Posicion de la Palabra dentro del documento.
	 */
	private double wt;

	/**
	 * Constructor de la clase.
	 */
	public NodoOT() {

	}

	/**
	 * @throws Throwable d.
	 */
	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param id Id del termino.
	 * @param off Offset del termino.
	 * @param wtExt Lugar del termino dentro del documento.
	 */
	public NodoOT(final Long id, final int off, final long wtExt) {
		this.setIdTermino(id);
		this.setOffset(off);
		this.setWt(wtExt);
	}

	/**
	 * @param wtExt the wt to set
	 */
	public final void setWt(final double wtExt) {
		this.wt = wtExt;
	}

	/**
	 * @return the wt
	 */
	public final double getWt() {
		return wt;
	}

	/**
	 * @param offsetExt the offset to set
	 */
	public final void setOffset(final int offsetExt) {
		this.offset = offsetExt;
	}

	/**
	 * @return the offset
	 */
	public final int getOffset() {
		return offset;
	}

	/**
	 * @param idTerminoExt the idTermino to set
	 */
	public final void setIdTermino(final Long idTerminoExt) {
		this.idTermino = idTerminoExt;
	}

	/**
	 * @return the idTermino
	 */
	public final Long getIdTermino() {
		return idTermino;
	}


}