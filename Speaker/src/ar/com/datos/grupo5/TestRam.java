package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
		ArrayList<NodoListaEspacioLibre> lista = new ArrayList<NodoListaEspacioLibre>();
		
		NodoListaEspacioLibre nodo;
		
		for (int i = 0; i < 408; i++) {
		nodo = new NodoListaEspacioLibre();
		nodo.setEspacio((short) (25 + i));
		nodo.setNroBloque(2 + i);
		lista.add(nodo);
		}
		
		//listasI.setEspacioLibrePorBloque(lista);
		
		/*
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		try {
			listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			logger.debug("No se pudo abrir el diccionario.");
		}

		try {
			listasI.cerrar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Error al intentar cerrar el archivo.");
		}
		
		logger.debug("Abrio el archivo de listas invertidas");
	}
}
