package ar.com.datos.trie.core;

import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.trie.persistence.TrieArchiveIndex;
import ar.com.datos.trie.proxy.NodeRetrieveHandler;
import ar.com.datos.trie.proxy.PersistentNodeFactory;

import com.sun.corba.se.impl.orbutil.graph.Node;


public class TestTrie {

	public static void main(String[] args) {

		
	 System.out.println(System.currentTimeMillis());
		
	 PersistentNodeFactory factory = new PersistentNodeFactory();
	 
	 
	 INodo uno = factory.createNodo(Nodo.class);
	 
	 Trie T = new Trie(uno);
		
		
		Long L = new Long(4123);
		PunteroSonido p = new PunteroSonido(L);

		T.insert("caca", p);
		T.insert("cacarusa", p);
		T.insert("cacaroto", p);
		T.insert("pis", p);
		T.insert("piso", p);
		T.insert("almeja", p);
		T.insert("gota", p);
		T.insert("go",p);
		T.insert("amigdala", p);
		T.insert("p", p);
		T.insert("w", p);

		T.search("cacarusa");
		T.search("go");
		T.search("alme");
		T.search("almeja");
		T.search("w");
		T.search("p");
		T.search("blah");
		T.search("");

		System.out.println(System.currentTimeMillis());
	}

}