/**
 * 
 */
package ar.com.datos.grupo5.btree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
		archivo = new ArchivoBloques(Constantes.SIZE_OF_INDEX_BLOCK);
		nodoRaiz = null;
		
		try {
			if (abrirArchivos()) {
				
				byte[] nodoLeido = archivo.leerBloque(0);
				if (nodoLeido != null && nodoLeido.length > 0) {
					nodoRaiz = new Nodo();
					nodoRaiz.setBytes(nodoLeido);
				}
			}
		} catch (IOException e) {
			LOG.error("", e);
			e.printStackTrace();
		}
	}
	
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
		abrirArchivos();
		
		//Obtengo el nodo en el que podria estar la clave.
		Nodo nodo = buscarNodo(clave);
		
		//Verifico si la clave está.
		int posReg = nodo.buscarRegistro(clave);
		
		cerrarArchivos();
		
		switch (posReg) {
		case Constantes.MENOR:
		case Constantes.MAYOR:
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
			case Constantes.MENOR: //La clave es menor al primero, voy por la izquierda.
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
				
			case Constantes.MAYOR: //La clave es mayor al ultimo, voy por la derecha.
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
				if (nodoActual.getRegistros().size() == 0) {
					return nodoActual;
				}
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
		if (nodoRaiz == null) {
			
			//Creo la raiz e inserto el registro.
			nodoRaiz = new Nodo();
			//El primero es hoja al pricipio.
			nodoRaiz.setEsHoja(true);
			nodoRaiz.setNroBloque(0);
			nodoRaiz.setNroBloquePadre(-1);
			registro.setNroBloqueDerecho(-1);
			registro.setNroBloqueIzquierdo(-1);
			nodoRaiz.insertarRegistro(registro);
			try {
				archivo.escribirBloque(nodoRaiz.getBytes(), nodoRaiz
						.getNroBloque());
			} catch (IOException e) {
				LOG.error("Error: " + e.getMessage());
				e.printStackTrace();
			}
			return true;
		}
		// TODO Terminar de implementar.
		//Busco en donde insertar.
		Nodo nodo = buscarNodo(registro.getClave());
		if (nodo.insertarRegistro(registro)) {
			this.nodoActual = nodo;	
		} else {
			//no puedo insertar!!!!
			//TODO traer el padre!!!!!!! 
			try {
				if (nodo.equals(nodoRaiz)) {
					// Splitear nodo
					ArrayList<Nodo> nodos = nodo.splitRaiz();
					nodoRaiz = nodos.get(nodos.size() - 1);
					// TODO Que escriba los 3 nodos a disco!!!
					
				} else {
					//Intento pasar el registro.
					//Si no puedo, veo con cual lo puedo Splitear
					if (!pasarRegistro(nodo, registro)) {
						// Buscar a los SIBLINGS!!!
						// Primero al izquierdo
						Nodo nodoAux = nodo.split(false);
						// Persistir los cambios!!!!!
						nodoActual = nodoAux;
					}
					
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Escribo el bloque completo.
		try {
			archivo.escribirBloque(nodoActual.getBytes(), nodoActual
					.getNroBloque());
		} catch (IOException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
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
		
		try {
			archivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR,
					Constantes.ABRIR_PARA_LECTURA);
			if (nodoActual == null) {
				nodoActual = new Nodo();
				nodoActual.setBytes(archivo.leerBloque(0));
				nodoRaiz = nodoActual;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nodoActual.listar();
	}

	/**
	 * @param nodo El nodo en el que no entra
	 * @param reg el registro a pasar a un hermano de nodo.
	 * @return si exito true.
	 * @throws IOException no pudo obtener el nodo
	 */
	private boolean pasarRegistro(final Nodo nodo, final RegistroNodo reg) 
	throws IOException {
			Nodo nodoAux = new Nodo();
			nodoAux.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
			//ahora nodo es el nodo padre del nodo que busque
			int pos = nodoAux.buscarRegistro(reg.getClave());
			Nodo nodoHno = new Nodo();
			int nroHno;
			switch (pos) {
			// Obtengo el hermano... Por DEFAULT USO EL MENOR 
			case Constantes.MENOR:
				nroHno = nodoAux.getPrimerRegistro().getNroBloqueIzquierdo();
				break;
			case Constantes.MAYOR:
				nroHno = nodoAux.getUltimoRegistro().getNroBloqueDerecho();
				break;
			default:
				nroHno = nodoAux.getRegistros().get(pos)
				.getNroBloqueIzquierdo();
				break;
			}
			nodoHno.setBytes(archivo.leerBloque(nroHno));
			
		return false;
	}
	
	/**
	 * 
	 * @return si exito true.
	 */
	private boolean abrirArchivos() {
		
		try {
			archivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			//archivo.crear(Constantes.ARCHIVO_ARBOL_BSTAR);
			
			return true;
			
		} catch (FileNotFoundException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return si exito true.
	 */
	private boolean cerrarArchivos() {
		
		try {
			archivo.cerrar();
			return true;
		} catch (IOException e) {
			LOG.error("", e);
			e.printStackTrace();
			return false;
		}
	}
}
