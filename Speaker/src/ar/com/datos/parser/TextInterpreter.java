package ar.com.datos.parser;

import java.util.Collection;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;

public class TextInterpreter implements ITextInput {

	public Collection<IunidadDeHabla> modoCarga(String rutaOlinea,
			boolean esArchivo) throws Exception {

		Parser miparser = new Parser(rutaOlinea, esArchivo);
		if (esArchivo) {
			return miparser.listar();
		} else {
			return miparser.simpleString();
		}
	}

	public Collection<IunidadDeHabla> modoLectura(String rutaOlinea,
			boolean esArchivo) throws Exception {

		Parser miparser = new Parser(rutaOlinea, esArchivo);
		if (esArchivo) {
			return miparser.listar();
		} else {
			return miparser.simpleString();
		}
	}

}
