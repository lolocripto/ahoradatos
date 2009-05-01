package ar.com.datos.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.ListasInvertidas;
import ar.com.datos.grupo5.ParFrecuenciaDocumento;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

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
		Collection<ParFrecuenciaDocumento> lista = new ArrayList<ParFrecuenciaDocumento>();
		
		/*
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		
		try {
			listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			logger.debug("No se pudo abrir el diccionario.");
		}
		
		logger.debug("Abrio el archivo de listas invertidas");
		
		/*
		 * Agrego una lista invertida.
		 */
		
		ParFrecuenciaDocumento nodo;
		
		for (int i = 0; i < 300; i++) {
			nodo = new ParFrecuenciaDocumento();
			nodo.setFrecuencia((long) (20+i));
			nodo.setOffsetDocumento((long) (2+i));
			lista.add(nodo);
		}
		Long numero = new Long(1);
		listasI.agregar(numero, lista);
		
		long nroBloque = listasI.getBloqueInsertado(); 

		logger.debug("Inserte una lista invertida");
		
		try {
			listasI.cerrar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Error al intentar cerrar el archivo.");
		}
		
		logger.debug("Cerre el archivo");
		
		
		/*
		 * Abro el archivo para la carga y consulta del diccionario
		 */
		
		System.out.println(Constantes.SIZE_OF_LONG);
		System.out.println(Constantes.SIZE_OF_TRIE);
		System.out.println(Constantes.SIZE_OF_SHORT);
		System.out.println(Constantes.SIZE_OF_CHAR);
		System.out.println(Constantes.SIZE_OF_INT);
		System.exit(0);
		
		try {
			listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			logger.debug("No se pudo abrir el diccionario.");
		}
		
		logger.debug("Abrio el archivo de listas invertidas");
		//int nroBloque = 2; 
		RegistroTerminoDocumentos rtd = listasI.leerLista(1, (int) nroBloque);
		
		System.out.println("Cantidad documentos: " + rtd.getCantidadDocumentos());
		System.out.println("Id del termino: " + rtd.getIdTermino());
		
		logger.debug("Lei la lista invertida");
		
		try {
			listasI.cerrar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Error al intentar cerrar el archivo.");
		}
		logger.debug("Cerre el archivo");
		
	}
}
