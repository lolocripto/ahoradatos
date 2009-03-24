package ar.com.datos.grupo5.interfaces;

/**
 * Interface para poder comunicarse con el usuario.
 * @author cristian
 *
 */
public interface InterfazUsuario {

	/**
	 * Despliega un mensaje para el usuario.
	 * @param mensaje .
	 */
	void mensaje(final String mensaje);
	
	/**
	 * Pide al usuario que ingrese datos, 
	 * con la posibilidad de hacer alguna pregunta.
	 * @param mensaje .
	 * @return mensaje con el resultado.
	 */
	String obtenerDatos(final String mensaje);
	
}
