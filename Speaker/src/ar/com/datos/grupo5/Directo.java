/**
 * 
 */
package ar.com.datos.grupo5;

import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * @author cristian
 *
 */
public class Directo extends Archivo {

	/**
	 * Offset del ultimo registro insertado.
	 */
	private long offset = 0L;
	
	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(Directo.class);
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#borrar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public final boolean borrar(final Registro registro)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#buscar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public final boolean buscar(final Registro registro)
			throws UnImplementedMethodException {

		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see ar.com.datos.grupo5.interfaces.Archivo#insertar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public void insertar(final Registro registro) throws IOException {
		
		offset = file.length();
		super.insertar(registro);
	}

	/**
	 * Posiciona en puntero del archivo en la posicion deseada.
	 * 
	 * @param pos
	 *            La posicion a la cual sedesa apuntar.
	 * @throws IOException .
	 */
	public final void posicionar(final long pos) throws IOException {
		
		file.seek(pos);
	}

	/**
	 * @return El offset del ultimo registro insertado.
	 */
	public final long getOffset() {
		return offset;
	}

	/**
	 * @see Archivo#primero()
	 */
	@Override
	public Registro primero() throws UnImplementedMethodException {
		
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see Archivo#siguiente()
	 */
	@Override
	public Registro siguiente() throws UnImplementedMethodException {
		
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

}
