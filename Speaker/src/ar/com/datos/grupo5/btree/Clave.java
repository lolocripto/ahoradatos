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
	private String claveStr;
	
	/**
	 * Constructor vacio..
	 */
	public Clave() {
		super();
	}
	
	/**
	 * Constructor con la clave.
	 * @param clave La clave.
	 */
	public Clave(final String clave) {
		claveStr = clave;
	}
	/**
	 * Para comparar las claves.
	 * @param clave la clave a comparar.
	 * @return 0, 1, -1.
	 */
	public final int compareTo(final Clave clave) {
		
		return this.claveStr.compareTo(clave.getClave());
	}

	/**
	 * @return the clave
	 */
	public final String getClave() {
		return claveStr;
	}

	/**
	 * @param clave the clave to set
	 */
	public final void setClave(final String clave) {
		this.claveStr = clave;
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
			return c.equals(this.claveStr);
		} else if (clave instanceof Clave) {
			Clave otraClave = (Clave) clave;
			return otraClave.getClave().equals(claveStr);
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
