package ar.com.datos.grupo5.utils;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

public class LogicaVectorial {
	
	/**
	 * Calcula el peso global de un termino.
	 * @param cantDocumentos cantidad de documentos. 
	 * @param ni cantidad de documentos en los que aparecene.
	 * @return peso global del documento.
	 */
	public static final double calcularPesoglobal(final int cantDocumentos, final int ni) {
		 return Math.log10(cantDocumentos + ni);
	}
	/**
	 * 
	 * @param terminos1 documento a calcular la similitud con terminos2.
	 * @param terminos2
	 * @param cantDocumentos total de documentos.
	 * @return similitud.
	 */
	public static final double cacularSimilitud(Collection<RegistroTerminoDocumentos> terminos1,Collection<RegistroTerminoDocumentos> terminos2,int cantDocumentos) {
		double similitud = 0;
		Iterator<RegistroTerminoDocumentos> itera = terminos1.iterator();
		while (itera.hasNext()) {
			    RegistroTerminoDocumentos registro = itera.next();
			    if (terminos2.contains(registro)) {
			    	double pesoGlobal=calcularPesoglobal(cantDocumentos, registro.getCantidadDocumentos());
			    	similitud+=pesoGlobal*pesoGlobal;
                }
		}
		return similitud;
	}
}
