package ar.com.datos.tests;

import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.trie.core.TrieAdministrator;


public class PruebasTrie {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		Long l0 = System.currentTimeMillis();
		
		TrieAdministrator ta = new TrieAdministrator();
		
/*		
		List<IunidadDeHabla> lista = Constantes.LISTA_STOP_WORDS;
		
		Iterator<IunidadDeHabla> it = lista.iterator();
		
		Long punt1 = new Long (97465);
		
		int i =0;
		
		while (it.hasNext()){
			ta.agregarPalabra(it.next().getTextoEscrito(), punt1);
			i++;
		}
		
*/
		
		
		
		String palabra1 = "te";
		String palabra2 = "bien";
		String palabra3 = "teta";
		String palabra4 = "bienaventurado";
		
		
		Long punt1 = new Long (12);
		Long punt2 = new Long (23);
		
	//	ta.agregarPalabra(palabra1, punt1);
	//	ta.agregarPalabra(palabra2, punt2);
		
	//	ta.agregarPalabra(palabra3, punt1);
	//	ta.agregarPalabra(palabra4, punt2);
		
		Long c;
		c= ta.buscarPalabra(palabra1);
		
		System.out.println(c);
		
		c=ta.buscarPalabra("cachanga");
		System.out.println(c);
		
		c= ta.buscarPalabra("tet");
		
		System.out.println(c);
		ta.buscarPalabra(palabra2);
		c= ta.buscarPalabra(palabra3);
		System.out.println(c);
		
		ta.buscarPalabra(palabra3);
		ta.buscarPalabra("dfsadfsdfsdgfsgdfgdgdfgsdf");
		ta.buscarPalabra(palabra4);
		
		
		ta.terminarSesion();

//		System.out.println(i);
		
		System.out.println(l0);
		
		System.out.println(System.currentTimeMillis());
		
		
		
	}

}
