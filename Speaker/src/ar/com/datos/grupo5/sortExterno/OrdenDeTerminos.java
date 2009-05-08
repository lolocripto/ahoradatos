package ar.com.datos.grupo5.sortExterno;

import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.utils.LogicaVectorial;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:16:18 p.m.
 */
public class OrdenDeTerminos {

	/**
	 * Lista con los nodos ordenados.
	 */
	private List<NodoOT> listaOrden;


	/**
	 * f.
	 * @throws Throwable f. 
	 */
	public void finalize() throws Throwable {
	}
	
	/**
	 * Constructor de la clase.
	 */
	public OrdenDeTerminos() {
	}
	
	/**
	 * Retorna 0 si inserto y 1 si no inserto por error de frecuencia.
	 * 
	 * @param id
	 * @param off
	 * @param N
	 * @param ft
	 */
	public final int agregarTermino(final Long id,final int off,final int N,final int ft) {
		if (ft > (N / 2)) {
			double wt = LogicaVectorial.calcularPesoglobal(N, ft);
			NodoOT n = new NodoOT(id, off, (long) wt);
			this.listaOrden.add(n);
			return 0;
		}
		return 1;
	}

	/**
	 * @param listaOrdenExt the listaOrden to set
	 */
	public final void setListaOrden(final List<NodoOT> listaOrdenExt) {
		this.listaOrden = listaOrdenExt;
	}

	/**
	 * @return the listaOrden
	 */
	public final List<NodoOT> getListaOrden() {
		return listaOrden;
	}
	
	/**
	 * Busca el offset del termino.
	 * @param id Id del termino
	 * @return -1 si no lo encuentra, si 
	 * lo encuentra devuelve el offset.
	 */
	public final int getOffset(final int id) {
		Iterator<NodoOT> it;
		int off = -1;
		it = this.listaOrden.iterator();
		NodoOT nodo;
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getIdTermino() == id) {
				off = nodo.getOffset();
				return off;
			}
		}
		return off;
	}

	/**
	 * 
	 * @param id Id del termino.
	 * @return El peso global
	 */
	public final double getPesoAbsoluto(final int id) {
		Iterator<NodoOT> it;
		double wt = -1;
		
		it = this.listaOrden.iterator();
		NodoOT nodo;
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getIdTermino() == id) {
				wt = nodo.getWt();
				return wt;
			}
		}
		return wt;
	}
}