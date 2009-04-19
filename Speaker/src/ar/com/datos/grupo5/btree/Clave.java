package ar.com.datos.grupo5.btree;


/**
 * Clave de un registro.
 * @author cristian
 *
 */
public class Clave implements Comparable<Clave> {

	/**
	 * La clave.
	 */
	private String clave;
		
	/**
	 * Para comparar las claves.
	 * @param clave la clave a comparar.
	 * @return 0, 1, -1.
	 */
	public final int compareTo(final Clave clave) {
		
		return this.clave.compareTo(clave.getClave());
	}

	/**
	 * @return the clave
	 */
	public final String getClave() {
		return clave;
	}

	/**
	 * @param clave the clave to set
	 */
	public final void setClave(final String clave) {
		this.clave = clave;
	}
	
	/**
	 * Determina cuando una clave es igual a otra.
	 * @param clave .
	 * @return true si son iguales.
	 */
	@Override
	public final boolean equals(final Object clave) {
		
		if (clave instanceof String) {
			String c = (String) clave;
			return c.equals(this.clave);
		}
		
		return false;
	}
	
	/**
	 * Para el equals.
	 * @return al hash code.
	 */
	public final int hashCode() {
		return 0;
	}

}
