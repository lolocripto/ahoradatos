package ar.com.datos.grupo5.interfaces;

/**
 * Interface para poder comunicarse con el usuario.
 * 
 * @author LedZeppeling
 * 
 */
public interface InterfazUsuario {

	/**
	 * Despliega un mensaje para el usuario.
	 * 
	 * @param mensaje Texto a mostrar.
	 *            .
	 */
	void mensaje(final String mensaje);

	/**
	 * Pide al usuario que ingrese datos, con la posibilidad de hacer alguna
	 * pregunta.
	 * 
	 * @param mensaje Pregunta u sugerencia que se le hace al usuario.
	 *            .
	 * @return mensaje con el resultado.
	 */
	String obtenerDatos(final String mensaje);

	/**
	 * Mensaje para el usuario sin un fin de linea.
	 * 
	 * @param mensaje Texto a mostrar.
	 *            .
	 */
	void mensajeSinSalto(final String mensaje);
	
}
