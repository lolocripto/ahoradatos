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
		RegistroNodo nodo = new RegistroNodo();
		
		if (nodoRaiz == null) {
			return null;
		}
		 nodo = this.nodoRaiz.buscarRegistro(clave);
		Nodo nodoAux = nodoRaiz;
		int resultado = 0;
		int posReg = 0;
		RegistroNodo registro = null;
		
		while (nodoAux != null) {
			//resultado = nodoAux.get
			
			//Busco la clave en el nodo.
			posReg = nodoAux.buscarRegistro(clave);
			
			if (posReg != -1) { //Lo encontré.
				return nodoAux.getRegistros().get(posReg);
			} else { //No lo encontré.
				registro = nodoAux.getRegistros().get(posReg);
				switch (registro.getClaveNodo().compareTo(clave)) {
				case 0:
					
					break;
				case 1:
					
					break;
				case -1:
					
					break;
				default:
					
					break;
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
