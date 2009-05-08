package ar.com.datos.grupo5.utils;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.ParPesoGlobalTermino;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

/**
 * 
 * @author Led Zeppelin
 *
 */
public final class LogicaVectorial {
	
	/**
	 * Calcula el peso global de un termino.
	 * @param cantDocumentos cantidad de documentos. 
	 * @param ni cantidad de documentos en los que aparecene.
	 * @return peso global del documento.
	 */
	public static double calcularPesoglobal(final int cantDocumentos, 
			final int ni) {
		 return Math.log10(cantDocumentos / ni);
	}
	/**
	 * @param offsetDocumento1 offset del documeto al cual se le quiere
	 * calcular la similitud.
	 * @param terminos1 documento a calcular la similitud con terminos2.
	 * @param terminos2 consulta. //Ya estoy pasando los terminos de la consulta en terminos1 por lo tanto
	 * busco directamente el documento y calculo la similitud
	 * @param cantDocumentos total de documentos. Lo saque porque ya calculo el PesoGlobal afuera
	 * @return similitud.
	 */
	public static double calcularSimilitud(
			final long offsetDocumento1,
			final Collection<ParPesoGlobalTermino> terminos1
			//,final Collection<RegistroTerminoDocumentos> terminos2
			) {
		
		double similitud = 0;
		Iterator<ParPesoGlobalTermino> itera = terminos1.iterator();
		ParPesoGlobalTermino pesoGlobalTermino;
		while (itera.hasNext()) {
				pesoGlobalTermino = itera.next();
			//Obtengo el pesoGlobal del termino.
				double pesoGlobal = pesoGlobalTermino.getPesoGlobal();
			//Obtengo la lista del termino.
			    RegistroTerminoDocumentos registro = pesoGlobalTermino.getRegTermDocs();
			/*
			 * En realidad yo le voy a pasar terminos que correspondan. no necesito el contains
			 *
			    if (terminos2.contains(registro)) {
*/
			    	similitud += (registro.getDocumentoFrecuencia(offsetDocumento1) 
			    			* (pesoGlobal * pesoGlobal));
    //            }
		}
		return similitud;
		
	}
	
	/**
	 * Constructor de la clase.
	 */
	private LogicaVectorial() {
		super();
	}
}
