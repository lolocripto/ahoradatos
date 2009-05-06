package ar.com.datos.trie.proxy;

import java.lang.reflect.Proxy;

import ar.com.datos.trie.core.INodo;
import ar.com.datos.trie.core.Nodo;
import ar.com.datos.trie.persistence.AdministrationRegistry;
import ar.com.datos.trie.persistence.TrieNodeRegistry;
import ar.com.datos.trie.persistence.TriePersistenceImpl;

public class PersistentNodeFactory {

	private INodo createNodo(Class claseAdministracion) {

		INodo nodoReal = null;

		try {
			// Creamos un objeto de la clase que recibimos.
			nodoReal = (INodo) claseAdministracion.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * Creamos el CallBack Handler y le pasamos el objeto real para ser
		 * invocado posteriormente en su método invoke.
		 */

		NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);

		// Creamos el proxy.
		Class[] interfacesQueEncapsulo = new Class[] { INodo.class };
		return (INodo) Proxy.newProxyInstance(claseAdministracion
				.getClassLoader(), interfacesQueEncapsulo, handler);
	}

	public static INodo getNewProxy(Nodo unNodo) {

		INodo nodoReal = unNodo;
		Class claseAdministracion = Nodo.class;

		/*
		 * Creamos el CallBack Handler y le pasamos el objeto real para ser
		 * invocado posteriormente en su método invoke.
		 */

		NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);

		// Creamos el proxy.
		Class[] interfacesQueEncapsulo = new Class[] { INodo.class };
		return (INodo) Proxy.newProxyInstance(claseAdministracion
				.getClassLoader(), interfacesQueEncapsulo, handler);

	}

	public static INodo getNewNodo(char i) {

		INodo nodoReal = TriePersistenceImpl.getPersistenceSession().nuevoNodo(i);
		Class claseAdministracion = Nodo.class;

		/*
		 * Creamos el CallBack Handler y le pasamos el objeto real para ser
		 * invocado posteriormente en su método invoke.
		 */

		NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);

		// Creamos el proxy.
		Class[] interfacesQueEncapsulo = new Class[] { INodo.class };
		return (INodo) Proxy.newProxyInstance(claseAdministracion
				.getClassLoader(), interfacesQueEncapsulo, handler);

	}

	public static INodo getNewNodo(String i) {

		INodo nodoReal = TriePersistenceImpl.getPersistenceSession().nuevoNodo(i);
		Class claseAdministracion = Nodo.class;

		/*
		 * Creamos el CallBack Handler y le pasamos el objeto real para ser
		 * invocado posteriormente en su método invoke.
		 */

		NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);

		// Creamos el proxy.
		Class[] interfacesQueEncapsulo = new Class[] { INodo.class };
		return (INodo) Proxy.newProxyInstance(claseAdministracion
				.getClassLoader(), interfacesQueEncapsulo, handler);

	}

}
