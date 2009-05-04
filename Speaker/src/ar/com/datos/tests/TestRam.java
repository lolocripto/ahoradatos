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
		int nroBloque = 5;
		long idtermino = 4;
		int accion = 1; // 0-Escribir, 1-Leer, 2-Modificar
		
		switch (accion) {
			case 0:{
				//Insertar
				try {
					listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
							Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				} catch (FileNotFoundException e) {
					logger.debug("No se pudo abrir el diccionario.");
				}
				
				logger.debug("Abrio el archivo de listas invertidas");
				
				ParFrecuenciaDocumento nodo;
				
				//for (int i = 0; i < 300; i++) {
				//for (int i = 0; i < 100; i++) {
				//for (int i = 0; i < 50; i++) {
				for (int i = 0; i < 15; i++) {
					nodo = new ParFrecuenciaDocumento();
					nodo.setFrecuencia((long) (20+i));
					nodo.setOffsetDocumento((long) (2+i));
					lista.add(nodo);
				}

				listasI.agregar(idtermino, lista);
				
				nroBloque = (int) listasI.getBloqueInsertado(); 

				logger.debug("Inserte una lista invertida, en el bloque: "+nroBloque);
				
				try {
					listasI.cerrar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.debug("Error al intentar cerrar el archivo.");
				}
				
				logger.debug("Cerre el archivo");
			
			}
			break;
			case 1:
			{
				//Leer
				try {
					listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
							Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				} catch (FileNotFoundException e) {
					logger.debug("No se pudo abrir el diccionario.");
				}
				
				logger.debug("Abrio el archivo de listas invertidas");
				RegistroTerminoDocumentos rtd = listasI.leerLista(idtermino, (int) nroBloque);
				
				if (rtd == null) {
					logger.debug("Lista no encontrada");
				} else {
				System.out.println("Cantidad documentos: " + rtd.getCantidadDocumentos());
				System.out.println("Id del termino: " + rtd.getIdTermino());
				
				logger.debug("Lei la lista invertida");
				}
				
				try {
					listasI.cerrar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.debug("Error al intentar cerrar el archivo.");
				}
				logger.debug("Cerre el archivo");

			}
			break;
			case 2:
			{
				//Modificar
				try {
					listasI.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
							Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				} catch (FileNotFoundException e) {
					logger.debug("No se pudo abrir el diccionario.");
				}
				
				logger.debug("Abrio el archivo de listas invertidas");
				
				ParFrecuenciaDocumento nodo;
				
				for (int i = 0; i < 370; i++) {
				//for (int i = 0; i < 102; i++) {
				//for (int i = 0; i < 52; i++) {
				//for (int i = 0; i < 20; i++) {
					nodo = new ParFrecuenciaDocumento();
					nodo.setFrecuencia((long) (20+i));
					nodo.setOffsetDocumento((long) (2+i));
					lista.add(nodo);
				}
				
				if (listasI.modificarLista(nroBloque, idtermino, lista)) {
					logger.debug("modifique la lista invertida");
				} else {
					logger.debug("no pude modificar la lista invertida");
				}

				
				
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
				
	}
}
