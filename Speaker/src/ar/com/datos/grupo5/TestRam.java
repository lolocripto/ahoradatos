package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import org.apache.log4j.Logger;

/**
 * 
 * @author xxvkue
 *
 */
public final class TestRam {

	/**
	 * .
	 */
	private static Logger logger = Logger.getLogger(TestRam.class);
	/**
	 * .
	 */
	private TestRam() {
		
	}
	/**
	 * @param args
	 * 	Argumento
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		ListasInvertidas listasI = new ListasInvertidas();
		
		System.out.println((Integer.SIZE + Short.SIZE)/8);
		/*
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		try {
			listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			logger.debug("No se pudo abrir el diccionario.");
		}

		logger.debug("Abrio el archivo Diccionario");
	}
}
