/**
 * 
 */
package ar.com.datos.grupo5.btree;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.archivos.ArchivoBloques;
import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Clase que implementa árboles B*.
 * @author Led Zeppelin
 */
public final class BStar implements BTree {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(BStar.class);
	
	/**
	 * archivo para el arbol.
	 */
	private ArchivoBloques archivo;
	
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
		archivo = new ArchivoBloques();
		nodoRaiz = null;
		
		try {
			archivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			byte[] nodoLeido = archivo.leerBloque(0);
			if (nodoLeido != null && nodoLeido.length > 0) {
				nodoRaiz = new Nodo();
				nodoRaiz.setBytes(nodoLeido);
			}
			
		} catch (FileNotFoundException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
		}
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
		
		//Abro el archivo.
		try {
			archivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			LOG.error("Error: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		//Obtengo el nodo en el que podria estar la clave.
		Nodo nodo = buscarNodo(clave);
		
		//Verifico si la clave está.
		int posReg = nodo.buscarRegistro(clave);
		
		//Cierro el archivo.
		try {
			archivo.cerrar();
		} catch (IOException e) {
			LOG.error("", e);
			e.printStackTrace();
		}
		
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
		
		Nodo nodoAux = null;
		nodoActual = nodoRaiz;
		int posReg = 0;
		int nroBloque = 0;
		
		while (nodoActual != null) {
			
			//Busco la clave en el nodo.
			posReg = nodoActual.buscarRegistro(clave);
			
			switch (posReg) {
			case MENOR: //La clave es menor al primero, voy por la izquierda.
				if (!nodoActual.isEsHoja()) {

					nroBloque = nodoActual.getRegistros().get(0)
							.getNroBloqueIzquierdo();
					nodoAux = new Nodo();
					try {
						nodoAux.setBytes(archivo.leerBloque(nroBloque));
					} catch (IOException e) {
						e.printStackTrace();
					}
					nodoActual = nodoAux;
				} else {
					return nodoActual;
				}
				break;
				
			case MAYOR: //La clave es mayor al ultimo, voy por la derecha.
				if (!nodoActual.isEsHoja()) {
					nroBloque = nodoActual.getRegistros().get(
							nodoActual.getRegistros().size() - 1)
							.getNroBloqueIzquierdo();
					nodoAux = new Nodo();
					try {
						nodoAux.setBytes(archivo.leerBloque(nroBloque));
					} catch (IOException e) {
						e.printStackTrace();
					}
					nodoActual = nodoAux;
				} else {
					return nodoActual;
				}
				break;
				
			default: //Encontré la clave que buscaba o una mayor.
				//Veo si lo que recupere el igual o mayor.
				if (nodoActual.getRegistros().get(posReg).getClave().equals(
						clave)) {
					
					return nodoActual;
				} else { // Es mayor.
					if (!nodoActual.isEsHoja()) {
						nroBloque = nodoActual.getRegistros().get(posReg)
								.getNroBloqueIzquierdo();
						nodoAux = new Nodo();
						try {
							nodoAux.setBytes(archivo
									.leerBloque(nroBloque));
						} catch (IOException e) {
							e.printStackTrace();
						}
						nodoActual = nodoAux;
					} else {
						return nodoActual;
					}
				}
			}
		}
		return nodoActual;
	}
	
	/**
	 * Inserta un registro en el Arbol.
	 * 
	 * @param registro
	 *            El registro para insertar.
	 * @return true si lo inserta.
	 */
	public boolean insertar(final RegistroNodo registro) {
		
		//FIXME: Arreglar esto!!!!!
		registro.setNroBloqueDerecho(-1);
		registro.setNroBloqueIzquierdo(-1);
		if (this.nodoRaiz == null) {
			
			//Creo la raiz e inserto el registro.
			this.nodoRaiz = new Nodo();
			//El primero es hoja al pricipio.
			nodoRaiz.setEsHoja(true);
			nodoRaiz.setNroBloque(0);
			nodoRaiz.setNroBloquePadre(-1);
			registro.setNroBloqueDerecho(-1);
			registro.setNroBloqueIzquierdo(-1);
			this.nodoRaiz.insertarRegistro(registro);
			try {
				archivo.escribirBloque(nodoRaiz.getBytes(), 0);
			} catch (IOException e) {
				LOG.error("Error: " + e.getMessage());
				e.printStackTrace();
			}
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
