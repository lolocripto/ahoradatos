package ar.com.datos.grupo5.parser;

//import gnu.java.beans.editors.StringEditor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.registros.RegistroFTRS;

public class CodificadorFrontCoding {

	/**
	 * Metodo que dado una serie de palabras las codifica.
	 * 
	 * @param listaPalabras jj.
	 * @return Collection de claves en FrontCoding.
	 */
	public static Collection<RegistroFTRS> codificar(
			final Collection<CFFTRS> listaPalabras) {

		/*
		 * Ordeno alfabeticamente, ver Collections.sort(listaPalabras);
		 */
/*		Iterator iterador = listaPalabras.iterator();
		Collection<ClaveFrontCoding> claves = new ArrayList<ClaveFrontCoding>();
		String anterior = new String();
		while (iterador.hasNext()) {
			String actual = iterador.next().toString();
			int caracterescoincidentes = coincidentes(actual, anterior);
			String termino = actual.substring(caracterescoincidentes, actual
					.length());
			int nroBloque = 0; /*ver de donde obtener el nro de bloque*/
		/*
			claves.add(new ClaveFrontCoding(caracterescoincidentes,
					termino.length(), termino));
			anterior = actual;
		}
		*/
		return null;
	}
	

	public static ClaveFrontCoding codificar(String palabraActual, String palabraAnteriorDecodificada){
		/*
		boolean seguir =true;
		int pointer =0;
		
		while (seguir){
		
			 if( seguir= (palabraActual.charAt(pointer) == 
				 palabraAnteriorDecodificada.charAt(pointer))){
				 pointer++;
			 }
		}
		
		palabraActual.startsWith();
		ClaveFrontCoding clave = new ClaveFrontCoding();
		
		*/
		return null;
	}
	
	
	
	//public static Collection<String> decodificar(Collecion registros FTRS)
	

	
	
	/**
	 * Compara la cantidad de caracteres coincidentes en orden de s1, desde el
	 * comienzo, de s2.
	 * 
	 * @param s1,s2
	 * @return cantidad de caracteres coincidentes
	 */
	private static int coincidentes(String s1, String s2) {
		if (s2.isEmpty())
			return 0;
		char[] caracteresS1 = s1.toCharArray();
		char[] caracteresS2 = s2.toCharArray();
		boolean coincidentes = true;
		int cantidad = 0;
		int i = 0;
		while (coincidentes
				&& (i < caracteresS1.length - 1 || i < caracteresS2.length - 1)) {
			coincidentes = (caracteresS1[i] == caracteresS2[i]);
			if (coincidentes) {
				cantidad++;
			}
			i++;
		}
		return cantidad;
	}

	/**
	 * Dada una linea en front coding decodifica.
	 * 
	 * @param linea
	 * @return
	 */
	public static Collection<CFFTRS> decodificar(Collection<RegistroFTRS> registros) {
		//char[] caracteresLinea = linea.toCharArray();
		
		Collection<CFFTRS> listaConvertida = new ArrayList<CFFTRS>();
		Iterator<RegistroFTRS> it = registros.iterator();
		RegistroFTRS reg, regAnt;
		ClaveFrontCoding cl;
		
		String ultimaPalabra = "";
		
		CFFTRS 	regResultante;
		while (it.hasNext()) {
			reg = it.next();
			regResultante = new CFFTRS();
			cl = reg.getClaveFrontCoding();
			//Si no tiene caracteres igual entoces es la palabra completa
			if (cl.getCaracteresCoincidentes() == 0) {
				regResultante.setPalabraDecodificada(cl.getTermino());
				ultimaPalabra = cl.getTermino();
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();  
				DataOutputStream dos = new DataOutputStream(bos);
				
				try {
					dos.write(ultimaPalabra.getBytes(), 0, cl.getCaracteresCoincidentes());
					dos.write(cl.getTermino().getBytes(), 0, cl.getLongitudTermino());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				ultimaPalabra = new String(bos.toByteArray());
				regResultante.setPalabraDecodificada(bos.toString());
				
			}
			//Asocio el termino descodificado al registro
			regResultante.setRegistroAsociado(reg);
			listaConvertida.add(regResultante);
			
			//El registro actual es el anterior al siguiente
			regAnt = reg;
		}
		
		return listaConvertida;
	}

}
