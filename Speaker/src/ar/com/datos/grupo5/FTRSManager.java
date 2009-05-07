package ar.com.datos.grupo5;

import java.io.IOException;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class FTRSManager {

	/**
	 * Arbol.
	 */
	private BTree arbolFTRS;

	/**
	 * Constructor.
	 * 
	 */
	public FTRSManager() {
		arbolFTRS = new BStar();
	}

	/**
	 * Busca la palabra dentro del Sistema FTRS.
	 * 
	 * @param palabra
	 *            buscada
	 * @return id del termino correspondiente a la palabra
	 */
	public final long buscarPalabra(final IunidadDeHabla palabra) {
		long id = 0;
		RegistroNodo nodo = new RegistroNodo();
		if (!palabra.isStopWord()) {
			nodo = arbolFTRS.buscar(new Clave(palabra.getTextoEscrito()));
			/* que hago con esto ? */
		}
		return id;
	}

	/**
	 * Verifica la existencia de la palabra.
	 * 
	 * @param palabra
	 * @return true si se encuentra la palabra
	 */
	public final boolean existePalabra(final IunidadDeHabla palabra) {
		boolean existe = false;
		if (!palabra.isStopWord()) {
			RegistroNodo nodo = arbolFTRS.buscar(new Clave(palabra
					.getTextoEscrito()));
			existe = (nodo != null);
		}
		return existe;
	}

	/**
	 * Agrega un termino.
	 * 
	 * @param idTermino
	 * @param termino
	 */
	public final void agregarTermino(final long idTermino, final String termino) {
		
		RegistroFTRS registroFtrs = new RegistroFTRS();
		registroFtrs.setClave(new Clave(termino));
		registroFtrs.setBloqueListaInvertida(-1L);
		registroFtrs.setIdTermino(idTermino);
		try {
			this.arbolFTRS.insertar(registroFtrs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void actualizarTermino(final Long offsetLista, final String termino) {
		//Armar el registro y llamar a actualizar del bTree
		
		RegistroFTRS registroFtrs = new RegistroFTRS();
		registroFtrs.setClave(new Clave(termino));
		registroFtrs.setBloqueListaInvertida(offsetLista);
		this.arbolFTRS.modificar(registroFtrs);
	}
}
