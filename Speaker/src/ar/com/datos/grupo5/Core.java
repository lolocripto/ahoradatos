package ar.com.datos.grupo5;

import java.io.File;
/**
 * @author xxvkue
 *
 */
public class Core {

	/** Atributos de la Clase.
	 *  
	 */
	private String palabraActual;
	
	/**
	 * documento.
	 */
	private File documento;

	/* Hilo para la parte de grabacion */

	/**
	 * 
	 * @param pathDocumento Agrergar comentario.
	 * @return lo mismo.
	 */
	public final String documentoAParsear(final String pathDocumento) {
		// 0 => Ok, 1 => No se encontro
		int estadoArchivo = 0;

		/**
		 * creamos el archivo a leer
		 */
		documento = new File(pathDocumento);

		return "";
	}

	/**
	 * 
	 * @return coemntar
	 */
	public final int comenazarGrabacion() {
		return 0;
	}

	/**
	 * 
	 * @return comentar.
	 */
	public final int finalizarGrabacion() {
		return 0;
	}

	/**
	 * 
	 * @param pathDocumento comentar
	 * @return comentar.
	 */
	public final int reproducirDocumento(final String pathDocumento) {
		return 0;
	}

	/**
	 * 
	 * @param textoAReproducir comentar
	 * @return comentar
	 */
	public final int reproducirTexto(final String textoAReproducir) {
		return 0;
	}

	/**
	 * Recibe la respuesta del usuario si esta bien o no la grabación 
	 * de la palabra. 
	 * En caso de ser correcta la grabacion de la palabra se procede a 
	 * grabar la palabra
	 * y el correspondiente audio, luego devuelve la siquiente palabra 
	 * a ser grabada.
	 * En caso de ser incorrecta la grabacion de la palabra se devuelve 
	 * la proxima palabra
	 * a ser grabada que en este caso es la misma dado que se grabo mal.
	 * @param respuesta
	 * Confirmación introducida por el usuario. 
	 * @return
	 * Proxima palabra a grabar.
	 */
	public final String confirmarGrabacion(final String respuesta) {
		if (respuesta.compareToIgnoreCase("S") == 0) {
			/* 
			 Grabo al archivo 
			 Archivo.WriteAudio(Palabra,Audio);
			 this.PalabraActual = BuscarSiguientePalabra();
			 */
			return this.palabraActual;
		}
		return this.palabraActual;
	}
}
