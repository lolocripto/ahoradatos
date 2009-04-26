/**
 * 
 */
package ar.com.datos.grupo5.btree;

import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Clase que implementa árboles B*.
 * @author Led Zeppelin
 */
public final class BStar implements BTree {
	
	/**
	 * Nodo Actual.
	 */
	private Nodo nodoActual;
	
	/**
	 * Nodo Raiz.
	 */
	private Nodo nodoRaiz;
	
	/**
	 * Constructor.
	 */
	public BStar() {
		nodoActual = null;
		nodoRaiz = null;
	}
	
	/**
	 * Para indicar que la clave es mayor que la ultima del nodo.
	 */
	private static final int MAYOR = -2;
	
	/**
	 * Para indicar que la clave es menor que la primera del nodo.
	 */
	private static final int MENOR = -1;
	
	/**
	 * Busca un registro.
	 * 
	 * @param clave
	 *            la clave del registro.
	 * @return El registro buscado o el siguiente inmediatamente mayor.
	 */
	public RegistroNodo buscar(final Clave clave) {
		//FIXME: Revisar.
		
		//Obtengo el nodo en el que podria estar la clave.
		Nodo nodo = buscarNodo(clave);
		
		//Verifico si la clave está.
		int posReg = nodo.buscarRegistro(clave);
		switch (posReg) {
		case MENOR:
		case MAYOR:
			return null;
		default:
			if (nodo.getRegistros().get(posReg).getClave().equals(clave)) {
				return nodo.getRegistros().get(posReg);
			} else {
				return null;
			}
		}
	}

	/**
	 * Devuelve el nodo en el cual podria insertarse o encontrarse un registro.
	 * 
	 * @param clave .
	 * @return El nodo buscado.
	 */
	private Nodo buscarNodo(final Clave clave) {
		
		if (nodoRaiz == null) {
			return null;
		}
		
		Nodo nodoAux = nodoRaiz;
		int posReg = 0;
		
		while (nodoAux != null) {
			
			//Busco la clave en el nodo.
			posReg = nodoAux.buscarRegistro(clave);
			
			switch (posReg) {
			case MENOR: //La clave es menor al primero, voy por la izquierda.
				if (!nodoAux.isEsHoja()) {
					nodoAux = nodoAux.getNodos().get(0);
				} else {
					return nodoAux;
				}
				break;
				
			case MAYOR: //La clave es mayor al ultimo, voy por la derecha.
				if (!nodoAux.isEsHoja()) {
					nodoAux = nodoAux.getNodos().get(nodoAux.getNodos().size());
				} else {
					return nodoAux;
				}
				break;
				
			default: //Encontré la clave que buscaba o una mayor.
				//Veo si lo que recupere el igual o mayor.
				if (nodoAux.getRegistros().get(posReg).getClave().equals(
						clave)) {
					
					return nodoAux;
				} else { // Es mayor.
					if (!nodoAux.isEsHoja()) {
						nodoAux = nodoAux.getNodos().get(posReg);
					} else {
						return nodoAux;
					}
				}
			}
		}
		return nodoAux;
	}
	
	/**
	 * Inserta un registro en el Arbol.
	 * 
	 * @param registro
	 *            El registro para insertar.
	 * @return true si lo inserta.
	 */
	public boolean insertar(final RegistroNodo registro) {
		
		if (this.nodoRaiz == null) {
			
			//Creo la raiz e inserto el registro.
			this.nodoRaiz = new Nodo();
			//El primero es hoja al pricipio.
			nodoRaiz.setEsHoja(true);
			this.nodoRaiz.insertarRegistro(registro);
			return true;
		}
		// TODO Terminar de implementar.
		//Busco en donde insertar.
		Nodo nodo = this.buscarNodo(registro.getClave());
		nodo.insertarRegistro(registro);
		this.nodoActual = nodo;
		return true;
	}

	/**
	 * true si lo modifica.
	 * @param registro El registro que se quiere modificar.
	 * @return true si lo modifica.
	 */
	public boolean modificar(final RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return El registro siguiente. Null si no existe siguiente.
	 */
	public RegistroNodo siguiente() {
		//En b* no hay siguiente.
		return null;
	}

	/**
	 * Para testear.
	 */
	public void listar() {
		
		nodoActual.listar();
	}

}
