/**
 * 
 */
package ar.com.datos.grupo5.utils;

import java.util.Comparator;

import ar.com.datos.grupo5.ParFrecuenciaDocumento;

/**
 * @author Led Zeppelin
 *
 */
public class comparadorFrecuencias implements Comparator<ParFrecuenciaDocumento>{

	/*
	 *negativo si o1 < o2
	 *cero     si o1 = o2
	 *positivo si o1 > o2
	 */
	public int compare(ParFrecuenciaDocumento o1, ParFrecuenciaDocumento o2) {
		ParFrecuenciaDocumento parFrecDoc1
			= (ParFrecuenciaDocumento) o1;
		
		ParFrecuenciaDocumento parFrecDoc2
		= (ParFrecuenciaDocumento) o2;
		
		return (-parFrecDoc1.compareTo(parFrecDoc2));
	}

	public boolean equals(Object o) {
		  return this == o;
	}

}
