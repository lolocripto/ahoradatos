package ar.com.datos.tests;

import java.util.Iterator;

import ar.com.datos.trie.core.Nodo;
import ar.com.datos.trie.core.PunteroSonido;
import ar.com.datos.trie.core.TrieAdministrator;


public class PruebasTrie {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		TrieAdministrator ta = new TrieAdministrator();
		
		String palabra1 = "te";
		String palabra2 = "bien";
		String palabra3 = "teta";
		String palabra4 = "bienaventurado";
		
		
		Long punt1 = new Long (12);
		Long punt2 = new Long (23);
		
//		ta.agregarPalabra(palabra1, punt1);
//		ta.agregarPalabra(palabra2, punt2);
		
		ta.agregarPalabra(palabra3, punt1);
		ta.agregarPalabra(palabra4, punt2);
		
		ta.buscarPalabra(palabra1);
		ta.buscarPalabra(palabra2);
		ta.buscarPalabra(palabra3);
		ta.buscarPalabra(palabra4);
		
		
		ta.terminarSesion();

		
		
		
		
	}

}
