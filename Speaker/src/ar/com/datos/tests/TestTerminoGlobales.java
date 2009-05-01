/**
 * 
 */
package ar.com.datos.tests;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.TerminosGlobales;
import ar.com.datos.grupo5.registros.RegistroTerminoGlobal;
import ar.com.datos.grupo5.archivos.Directo;
/**
 * @author xxvkue
 *
 */
public class TestTerminoGlobales {

	private static Logger logger = Logger.getLogger(TestTerminoGlobales.class);
	/**
	 * Test y ejemplo de uso de archivos globales.
	 * @param args
	 */
	public static void main(String[] args) {
		TerminosGlobales archivo = new TerminosGlobales();
		
		/*
		 * Abro el archivo para la carga y consulta de los terminosGlobales
		 */
		try {
			archivo.abrir(Constantes.ARCHIVO_TERMINOS_GLOBALES,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			logger.debug("No abrio el archivo TerminoGlobales");
		}
		logger.debug("Abrio el archivo TerminoGlobales");
		
		//Creo el registro de termino global
		Long offset1 = archivo.agregar("Caso");
		Long offset2 = archivo.agregar("Casito");
		Long offset3 = archivo.agregar("Final");
		Long offset4 = archivo.agregar("Fantasy");
		Long offset5 = archivo.agregar("XIII");
		
		String regTG;
		
		//leo Caso
		regTG = archivo.leerTermino(offset1);
		System.out.println(regTG);
		//leo Caso
		regTG = archivo.leerTermino(offset2);
		System.out.println(regTG);
		//leo Caso
		regTG = archivo.leerTermino(offset3);
		System.out.println(regTG);
		//leo Caso
		regTG = archivo.leerTermino(offset4);
		System.out.println(regTG);
		//leo Caso
		regTG = archivo.leerTermino(offset5);
		System.out.println(regTG);
	}

}
