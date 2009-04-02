package ar.com.datos.parser;

import ar.com.datos.UnidadesDeExpresion.Palabra;
import ar.com.datos.grupo5.Constantes;

public class PalabrasFactory {

	public static Palabra getPalabra(String palabraEscrita) {
		String fonetica;
		if (Constantes.SPANISH_OPTIMIZATION_ACTIVATED){
		fonetica = PatternRecognizer.posProcesadorFonetico(palabraEscrita);
		}else{
			fonetica = palabraEscrita;
		}
		Palabra nuevapalabra = new Palabra(palabraEscrita, fonetica);
		return nuevapalabra;
	}
}
