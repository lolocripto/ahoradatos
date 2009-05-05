package ar.com.datos.sortExterno;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:57 p.m.
 */
public class NodoRS {

	/**
	 * 0 - disponible 1 - congelado.
	 */
	private int flag;
	/**
	 * Frecuencia del termino en el documento.
	 */
	private int ftd;
	
	/**
	 * Id del documento.
	 */
	private int idDocumento;
	
	/**
	 * id del termino.
	 */
	private int idTermino;

	/**
	 * Contructor de la clase.
	 */
	public NodoRS() {
	}

	/**
	 *	@throws Throwable d. 
	 */
	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param idT id del termino
	 * @param idD id del documento
	 * @param ftdExt frecuncia del termino en el documento
	 */
	public NodoRS(final int idT, final int idD, final int ftdExt) {
		this.idTermino = idT;
		this.idDocumento = idD;
		this.ftd = ftdExt;
		this.flag = 0;
	}

	/**
	 * Comparar es una funcion que devuelve: 0 si this = NRS 1 si this > NRS -1 si
	 * this < NRS Siempre y cuando tenga flag == 0 sino devuelve 2 (si esta mal).
	 * 
	 * @param nRS c
	 * @return 0
	 */
	public final int comparar(final NodoRS nRS) {
		if ((this.flag == 1)&&(nRS.getFlag() == 0)) return 1;
		else
		if ((nRS.getFlag()==1)&&(this.flag==0)) return -1;
		else
		if ((nRS.getFlag()==1)&&(this.flag==1)) return 0;
		else
		if ((nRS.getFlag()==0)&&(this.flag==0))
		{
			if ((this.idTermino==nRS.getIdTermino())&&(this.idDocumento==nRS.getIdDocumento()))
				return 0;
			else if (this.idTermino>nRS.getIdTermino())
					return 1;
			     else if (this.idTermino < nRS.getIdTermino())
							return -1;
					  else if ((this.idTermino == nRS.getIdTermino())&&(this.idDocumento > nRS.getIdDocumento()))
								return 1;
							else if ((this.idTermino==nRS.getIdTermino())&&(this.idDocumento<nRS.getIdDocumento()))
									return -1;
								else return 2;
		}	
		else return 2;		
	}

	/**
	 * @param flagExt the flag to set
	 */
	public final void setFlag(final int flagExt) {
		this.flag = flagExt;
	}

	/**
	 * @return the flag
	 */
	public final int getFlag() {
		return flag;
	}

	/**
	 * @param ftdExt the ftd to set
	 */
	public final void setFtd(final int ftdExt) {
		this.ftd = ftdExt;
	}

	/**
	 * @return the ftd
	 */
	public final int getFtd() {
		return ftd;
	}

	/**
	 * @param idDocumentoExt the idDocumento to set
	 */
	public final void setIdDocumento(final int idDocumentoExt) {
		this.idDocumento = idDocumentoExt;
	}

	/**
	 * @return the idDocumento
	 */
	public final int getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idTerminoExt the idTermino to set
	 */
	public final void setIdTermino(final int idTerminoExt) {
		this.idTermino = idTerminoExt;
	}

	/**
	 * @return the idTermino
	 */
	public final int getIdTermino() {
		return idTermino;
	}

}