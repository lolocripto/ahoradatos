package ar.com.datos.grupo5.parser;

import java.util.Collection;

import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;

/**
 * Es la clase que se utiliza para parsear y cargar las palabras.
 * 
 * @author LedZeppeling
 */
public class TextInterpreter implements ITextInput {


	public Collection<IunidadDeHabla> modoCarga(String rutaOlinea,
			boolean esArchivo, DocumentsManager docMan) throws Exception {

		Parser miparser = new Parser(rutaOlinea, esArchivo, docMan);
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
	
	public Collection<IunidadDeHabla> modoLecturaDocAlmacenado(DocumentsManager doc){
		
		Parser miParser = new Parser(doc);
		return miParser.listar();
	}
	
}
