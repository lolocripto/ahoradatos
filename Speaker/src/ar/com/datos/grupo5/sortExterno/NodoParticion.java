package ar.com.datos.grupo5.sortExterno;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:26 p.m.
 */
public class NodoParticion implements Comparable<NodoParticion>{

	/**
	 * flag.
	 */
	private int flag;
	
	/**
	 * Cantidad de Registros en la particion.
	 */
	private long nRegistros;
 
	/**
	 * Particion.
	 */
	private String particion;

	/**
	 * Constructor de la clase. 
	 */
	public NodoParticion() {

	}
	/**
	 * f.
	 * @throws Throwable f. 
	 */
	public void finalize() throws Throwable {

	}

	/**
	 * Constructor de la clase con parametros.
	 * @param part Particion
	 * @param n registros en la particion.
	 */
	public NodoParticion(final String part, final long n) {
		this.setNRegistros(n);
		int l = part.length();
		if (l > 0) {
		this.setParticion(part);
		} else {
			this.setParticion(null);
		}
		this.setFlag(0);
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
	 * @param nRegistrosExt the nRegistros to set
	 */
	public final void setNRegistros(final long nRegistrosExt) {
		this.nRegistros = nRegistrosExt;
	}
	/**
	 * @return the nRegistros
	 */
	public final long getNRegistros() {
		return nRegistros;
	}
	/**
	 * @param particionExt the partición to set
	 */
	public final void setParticion(final String particionExt) {
		this.particion = particionExt;
	}
	/**
	 * @return the particion
	 */
	public final String getParticion() {
		return particion;
	}
	
	public int compareTo(NodoParticion o) {
		
		long result;
		result = this.nRegistros - o.nRegistros;

		if (result>0){
			return 1;
		}else{
			if (result <0){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}