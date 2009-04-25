/**
 * 
 */
package ar.com.datos.grupo5.btree;

import java.util.ArrayList;

import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Clase que implementa el nodo de los árboles B*.
 * @author Led Zeppelin
 */
public final class BAsterisk implements BTree {
	
	/**
	 * Nodo Actual.
	 */
	private Nodo nodoActual;
	
	/**
	 * Nodo Raiz.
	 */
	private Nodo nodoRaiz;

	/**
	 * Busca un registro.
	 * 
	 * @param clave
	 *            la clave del registro.
	 * @return El registro buscado o el siguiente inmediatamente mayor.
	 */
	public RegistroNodo buscar(final Clave clave) {
		
		//TODO CORREGIR ESTE METODO
		if (nodoRaiz == null) {
			return null;
		}
		
		Nodo nodoAux = nodoRaiz;
		int posReg = 0;
		
		while (nodoAux != null) {
			//resultado = nodoAux.get
			
			//Busco la clave en el nodo.
			posReg = nodoAux.buscarRegistro(clave);
			
			switch (posReg) {
			case -1: //La clave es menor al primero, voy por la izquierda.
				nodoAux = nodoAux.getNodos().get(0);
				break;
				
			case -2: //La clave es mayor al ultimo, voy por la derecha.
				nodoAux = nodoAux.getNodos().get(nodoAux.getNodos().size());
				break;

			default:
				//Veo si lo que recupere el igual o mayor.
				if (nodoAux.getRegistros().get(posReg).getClaveNodo().equals(
						clave)) {
					
					return nodoAux.getRegistros().get(posReg);
				} else { // Es mayor.
					nodoAux = nodoAux.getNodos().get(posReg);
				}
			}			
		}
		
		return null;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#insertar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public boolean insertar(final RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#modificar(ar.com.datos.grupo5.interfaces.Registro)
	 */
	public boolean modificar(final RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see ar.com.datos.grupo5.btree.BTree#siguiente()
	 */
	public RegistroNodo siguiente() {
		// TODO Auto-generated method stub
		return null;
	}

}
