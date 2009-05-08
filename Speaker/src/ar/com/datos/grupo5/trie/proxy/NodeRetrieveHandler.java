package ar.com.datos.grupo5.trie.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import ar.com.datos.grupo5.trie.core.INodo;
import ar.com.datos.grupo5.trie.core.Nodo;
import ar.com.datos.grupo5.trie.core.PunteroSonido;
import ar.com.datos.grupo5.trie.persistence.ParStringPuntero;
import ar.com.datos.grupo5.trie.persistence.TrieNodeRegistry;
import ar.com.datos.grupo5.trie.persistence.TriePersistenceImpl;

public class NodeRetrieveHandler implements InvocationHandler {

	TrieNodeRegistry registro = null;
	INodo nodoReal = null;

	public NodeRetrieveHandler(INodo miNodo) {
		this.nodoReal = miNodo;
	}

	/*
	 * Este es el método callback que será invocado previamente a cada método de
	 * los managers.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (method.getName() == "getHijo") {
			this.accederNodo(this.nodoReal, args[0]);
		}

		if (method.getName() == "setNuevoHijo") {
			this.agregarNuevoHijo(this.nodoReal, (INodo) args[0]);
		}

		if (method.getName() == "setPuntero") {
			this.setPuntero(this.nodoReal, (PunteroSonido) args[0]);
		}

		// Continuamos invocando al Manager real.
		return method.invoke(nodoReal, args);
	}

	/**
	 * Cuando se captura una llamada al metodo setNuevoHijo se ejecuta esta
	 * función que permite actualizar la lista de punteros del padre.
	 * 
	 * @param objetoRealActual
	 * @param nuevoHijo
	 */
	private void agregarNuevoHijo(INodo objetoRealActual, INodo nuevoHijo) {

		TrieNodeRegistry registroDelObjetoActual = objetoRealActual
				.getRegistroAsociado();

		ParStringPuntero psp = new ParStringPuntero(nuevoHijo.getNumeroNodo(),
				nuevoHijo.getContenido());

		registroDelObjetoActual.listaDepunteros.add(psp);

		registroDelObjetoActual.setDirty(true);

	}

	private void setPuntero(INodo unNodo, PunteroSonido punt) {

		unNodo.getRegistroAsociado().setPuntero(punt.getOffset());
		unNodo.getRegistroAsociado().setDirty(true);

		TriePersistenceImpl.getPersistenceSession().agregarNodoSucio(
				(Nodo) unNodo);

	}

	private void accederNodo(INodo objetoRealActual, Object palabra) {

		// si se quizo acceder a un hijo mediante una letra
		if (palabra instanceof Character) {
			char i = (Character) palabra;

			// verifico primero si dicho hijo ya fue cargado, en cuyo caso no
			// hago nada
			if (objetoRealActual.getHijo(i) == null) {

				// si fue cargado genero un par auxiliar para buscar en la lista
				// de punteros si tiene ese hijo pero no fue levantado
				Long l = new Long(1);
				ParStringPuntero aux = new ParStringPuntero(l,
						((Character) palabra).toString());

				// obtengo la posicion del objeto buscado
				int result = objetoRealActual.getRegistroAsociado().listaDepunteros
						.indexOf(aux);

				// si el objeto fue encontrado (resultado distinto de -1) traigo
				// del disco el nodo en cuestion
				if (result != -1) {
					Nodo miNodo;
					miNodo = this.obtenerNodoDeDisco(objetoRealActual
							.getRegistroAsociado().listaDepunteros.get(result));
					objetoRealActual.setNuevoHijo(PersistentNodeFactory
							.getNewProxy(miNodo));

				}
			}

		} else {

			String i = (String) palabra;

			// verifico primero si dicho hijo ya fue cargado, en cuyo caso no
			// hago nada
			if (objetoRealActual.getHijo(i) == null) {

				// si fue cargado genero un par auxiliar para buscar en la lista
				// de punteros si tiene ese hijo pero no fue levantado
				Long l = new Long(1);
				ParStringPuntero aux = new ParStringPuntero(l, i);

				// obtengo la posicion del objeto buscado
				int result = objetoRealActual.getRegistroAsociado().listaDepunteros
						.indexOf(aux);

				// si el objeto fue encontrado (resultado distinto de -1) traigo
				// del disco el nodo en cuestion
				if (result != -1) {
					Nodo miNodo;
					miNodo = this.obtenerNodoDeDisco(objetoRealActual
							.getRegistroAsociado().listaDepunteros.get(result));
					objetoRealActual.setNuevoHijo(PersistentNodeFactory
							.getNewProxy(miNodo));

				}

			}
		}
	}

	private Nodo obtenerNodoDeDisco(ParStringPuntero psp) {
		return TriePersistenceImpl.getPersistenceSession().getNodo(
				psp.getNumeroNodo());
	}

}
