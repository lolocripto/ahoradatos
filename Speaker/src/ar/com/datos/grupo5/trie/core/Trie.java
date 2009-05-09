package ar.com.datos.grupo5.trie.core;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.trie.proxy.PersistentNodeFactory;

public class Trie {

	private INodo root;
	int profundidad;

	/**
	 * Constructor del Trie,recibe la raiz.
	 */

	public Trie(INodo raiz) {
		this.root = raiz;
		profundidad = Constantes.SIZE_OF_TRIE + 1;

	}

	public void insert(String s, PunteroSonido punt) {
		INodo current = root;

		if (s.length() == 0) //caso puntual de buscar un string vacio
			current.setPuntero(punt);

		for (int i = 0; i < s.length() && i < this.profundidad; i++) {

			// diferencio si el nodo ya fue creado ó no al momento de acceder
			if (i < profundidad - 1) {
				if (current.getHijo(s.charAt(i)) != null) // nodo ya creado
				{
					current = current.getHijo(s.charAt(i));
				}

				else // Primera vez que se accede
				{
					current.setNuevoHijo(PersistentNodeFactory.getNewNodo(s
							.charAt(i)));
					current = current.getHijo(s.charAt(i));
				}
			} else {
				if (current.getHijo(s.substring(i)) != null) // nodo ya creado
				{
					current = current.getHijo(s.substring(i));
				}

				else // Primera vez que se accede
				{
					current.setNuevoHijo(PersistentNodeFactory.getNewNodo(s
							.substring(i)));
					current = current.getHijo(s.substring(i));
				}

			}

			// en la ultima letra o substring pongo el marcador
			if ((i == s.length() - 1) || (i == this.profundidad - 1))
				current.setPuntero(punt);
		}
	}

	public PunteroSonido search(String s) {
		INodo current = root;

		while (current != null) {

			for (int i = 0; i < s.length() && i < this.profundidad; i++) {

				if (i < profundidad - 1) {

					if (current.getHijo(s.charAt(i)) == null) {
						return null;
					} else {
						current = current.getHijo(s.charAt(i));
					}
				} else {

					if (current.getHijo(s.substring(i)) == null) {
						return null;
					} else {
						current = current.getHijo(s.substring(i));
					}
				}
			}

			// en este lugar sabemos que el string existe, pero debemos ver que
			// tenga un puntero asociado
			
			if (current.getPuntero() == null) {
				return null;
			}else{
				if (current.getPuntero().getOffset() ==null ){
					return null;
				}else{
					return current.getPuntero();
				}
			}
		}

		return null;
	}
	
	public void recEspacio(){
		this.root.recEspacio();
	}
	
}
