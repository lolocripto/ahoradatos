package ar.com.datos.grupo5;

/**
 * Clase que encapsula la relacion de la frecuencia del termino
 * con el documento en el que aparece.
 * @author Led Zeppelin
 *
 */
public class ParFrecuenciaDocumento {

	/**
	 * Frecuencia con la que aparece el termino en el documento asociado.
	 */
	private Long frecuencia;
	
	/**
	 * Documento asociado donde aparece el termino.
	 */
	private Long offsetDocumento;
	
	/**
	 * Permite obtener la frecuencia del termino en el documento.
	 * @return
	 * 			Devuelve la frecuencia del termino en el 
	 * 			documento asociado.
	 */
	public final Long getFrecuencia() {
		return this.frecuencia;
	}
	
	/**
	 * Permite obtener el offset al documento del par.
	 * @return
	 * 			Devuelve el offset al documento.
	 */
	public final Long getOffsetDocumento() {
		return this.offsetDocumento;
	}
	
	/**
	 * Permite modificar el atriburo Frecuencia.
	 * @param frecuenciaExt 
	 * 				Frecuencia del termino en el documento asociado.
	 */
	public final void setFrecuencia(final Long frecuenciaExt) {
		this.frecuencia = frecuenciaExt;
	}

	/**
	 * Permite setear el offset del documento asociado a la frecuencia.
	 * @param offsetDocumentoExt
	 * 			El offset que apunta al documento
	 */
	public final void setOffsetDocumento(final Long offsetDocumentoExt) {
		this.offsetDocumento = offsetDocumentoExt;
	}
	
	/**
	 * Constructor de la clase.
	 *
	 */
	public ParFrecuenciaDocumento() {
		this.frecuencia = 0L;
		this.offsetDocumento = 0L;
	}
}
