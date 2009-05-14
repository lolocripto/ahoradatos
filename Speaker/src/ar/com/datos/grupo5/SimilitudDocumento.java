/**
 * 
 */
package ar.com.datos.grupo5;

/**
 * @author Led Zeppelin
 *
 */
public class SimilitudDocumento implements Comparable<SimilitudDocumento> {

	private double similitud;
	private Long documento;
	
	/**
	 * @param similitud the similitud to set
	 */
	public void setSimilitud(double similitud) {
		this.similitud = similitud;
	}
	
	/**
	 * @return the similitud
	 */
	public double getSimilitud() {
		return similitud;
	}
	
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(Long documento) {
		this.documento = documento;
	}
	
	/**
	 * @return the documento
	 */
	public Long getDocumento() {
		return documento;
	}
	
	public int compareTo(SimilitudDocumento reg) {
		
		if (this.similitud > reg.getSimilitud()) {
			return 1;
		}
		if (this.similitud == reg.getSimilitud()) {
			return 0;
		}
		if (this.similitud < reg.getSimilitud()) {
			return -1;
		}
		return 0;
	}
	
}
