package ar.com.datos.parser;

import ar.com.datos.UnidadesDeExpresion.Palabra;

public class PalabrasFactory {

	public static Palabra getPalabra(String palabraEscrita) {
		String fonetica;
		fonetica = PatternRecognizer.posProcesadorFonetico(palabraEscrita);
		Palabra nuevapalabra = new Palabra(palabraEscrita, fonetica);
		return nuevapalabra;
		
	}
}
