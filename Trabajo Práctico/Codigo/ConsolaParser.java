/**
 * 
 */
package ar.com.datos.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cristian
 *
 */
public class ConsolaParser {

	/**
	 * Parsea una linea parametros de la consola.
	 * @param linea parametros.
	 * @return los parametros en un array.
	 */
	public final String[] parseLine(final String linea) {
		
		String starts = " \"";
		String ends = "\" ";
		
		String lineaAux = linea;
		
		lineaAux = lineaAux.replaceAll(starts, " \"'");
		lineaAux = lineaAux.replaceAll(ends, "'\" ");
		
		List < String > param = new ArrayList < String >();
		
		String[] parametros = lineaAux.split("\"");
		
		for (int i = 0; i < parametros.length; i++) {
			parametros[i] = parametros[i].trim();
			if (parametros[i].length() != 0) {
				if (parametros[i].indexOf("'") == -1) {
					parametros[i] = parametros[i].replace("'", "").trim();
					if (parametros.length > 0) {
						param.add(parametros[i]);
					}
				} else {
					String[] subParam = parametros[i].split(" ");
					for (int j = 0; j < subParam.length; j++) {
						subParam[j] = subParam[j].trim();
						if (subParam[j].length() > 0) {
							param.add(subParam[j]);
						}
					}
				}
			}
		}
		
		String[] result = new String[param.size()];
		for (int i = 0; i < param.size(); i++) {
			result[i] = param.get(i);
		}
		
		return result;
	}
}
