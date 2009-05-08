/**
 * 
 */
package ar.com.datos.grupo5;

import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

/**
 * @author Led Zeppelin
 *
 */
public class ParPesoGlobalTermino implements Comparable {

	private double pesoGlobal;
	private RegistroTerminoDocumentos regTermDocs;
	/**
	 * @param pesoGlobal the pesoGlobal to set
	 */
	public void setPesoGlobal(double pesoGlobal) {
		this.pesoGlobal = pesoGlobal;
	}
	/**
	 * @return the pesoGlobal
	 */
	public double getPesoGlobal() {
		return pesoGlobal;
	}
	/**
	 * @param regTermDocs the regTermDocs to set
	 */
	public void setRegTermDocs(RegistroTerminoDocumentos regTermDocs) {
		this.regTermDocs = regTermDocs;
	}
	/**
	 * @return the regTermDocs
	 */
	public RegistroTerminoDocumentos getRegTermDocs() {
		return regTermDocs;
	}
	
	public int compareTo(Object o) {
		ParPesoGlobalTermino reg = (ParPesoGlobalTermino) o;
		if (this.pesoGlobal > reg.getPesoGlobal()) {
			return 1;
		}
		if (this.pesoGlobal == reg.getPesoGlobal()) {
			return 0;
		}
		if (this.pesoGlobal < reg.pesoGlobal) {
			return -1;
		}
		return 0;
	}
	
	
}
