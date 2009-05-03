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
import ar.com.datos.grupo5.utils.Conversiones;

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
	 * Ultimo Bloque - Para saber donde escribir en el archivo.
	 */
	private int ultimoBloque;
	
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
				
				cargarDatosAdministrativos();
				byte[] nodoLeido = archivo
						.leerBloque(Constantes.NRO_BLOQUE_RAIZ);
				
				if (nodoLeido != null && nodoLeido.length > 0) {
					nodoRaiz = new Nodo();
					nodoRaiz.setBytes(nodoLeido);
				}
			}
			cerrarArchivos();
		} catch (IOException e) {
			LOG.error("", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga los datos administrativos del arbol que se guardan en el primer
	 * bloque del archivo.
	 */
	private void cargarDatosAdministrativos() {
		
		try {
			
			byte[] datos = archivo.leerBloque(Constantes.NRO_BLOQUE_ADMIN);
			if (datos != null && datos.length >= Constantes.SIZE_OF_INT) {
				ultimoBloque = Conversiones.arrayByteToInt(datos);
			} else {
				ultimoBloque = Constantes.NRO_BLOQUE_ADMIN;
				archivo.escribirBloque(Conversiones
						.intToArrayByte(ultimoBloque),
						Constantes.NRO_BLOQUE_ADMIN);
			}
			
		} catch (IOException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Guarda los datos administrativos del arbol en el archivo.
	 */
	private void guardarDatosAdministrativos() {
		
		byte[] datos = new byte[Constantes.SIZE_OF_INT];
		
		//Guardo el nro del ultimo bloque escrito.
		datos = Conversiones.intToArrayByte(ultimoBloque);
		
		try {
			archivo.escribirBloque(datos, Constantes.NRO_BLOQUE_ADMIN);
		} catch (IOException e) {
			LOG.error("Error: " + e.getMessage());
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
			//La clave es menor al primero, voy por la izquierda.
			case Constantes.MENOR:
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
			//La clave es mayor al ultimo, voy por la derecha.
			case Constantes.MAYOR:
				if (!nodoActual.isEsHoja()) {
					nroBloque = nodoActual.getRegistros().get(
							nodoActual.getRegistros().size() - 1)
								.getNroBloqueDerecho();
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
			//Encontré la clave que buscaba o una mayor.
			default:
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
	 * @throws IOException .
	 */
	public boolean insertar(final RegistroNodo registro) throws IOException {
		
		abrirArchivos();
		if (nodoRaiz == null) {
			
			//Creo la raiz e inserto el registro.
			nodoRaiz = new Nodo();
			//El primero es hoja al pricipio.
			nodoRaiz.setEsHoja(true);
			nodoRaiz.setNroBloque(Constantes.NRO_BLOQUE_RAIZ);
			nodoRaiz.setNroBloquePadre(-1);
			nodoRaiz.insertarRegistro(registro);
			ultimoBloque = Constantes.NRO_BLOQUE_RAIZ;
			
			//FIXME: guardo en disco los datos administrativos
			// ver si se puede sacarlo de aca, habria que hacerlo cada tanto.
			guardarDatosAdministrativos();
			
			archivo.escribirBloque(nodoRaiz.getBytes(), nodoRaiz
						.getNroBloque());
			
			cerrarArchivos();

			return true;
		}
		//Busco en donde insertar.
		Nodo nodo = buscarNodo(registro.getClave());
		//Si ya existe, no inserto.
		if (nodo.existeClave(registro.getClave())) {
			//FIXME creo que esta bien, seria para no insertar duplicados.
			cerrarArchivos();
			LOG.debug("No se inserto en el elemento ["
					+ registro.getClave().getClave()
					+ "] debido a que ya existia la clave en el arbol.");
			return true;
		}
		
		if (nodo.insertarRegistro(registro)) {
			
			this.nodoActual = nodo;
			
		} else if (nodo.getNroBloquePadre() < 0) {
			// Es la raiz.
			// Splitear nodo
			ArrayList<Nodo> nodos = nodo.splitRaiz(ultimoBloque);
			nodoRaiz = nodos.get(1);
			nodoActual = nodos.get(0);
			ultimoBloque += 2;
			
			archivo.escribirBloque(nodoRaiz.getBytes(), nodoRaiz
					.getNroBloque());
			archivo.escribirBloque(nodoActual.getBytes(),
					nodoActual.getNroBloque());
			archivo.escribirBloque(nodo.getBytes(), nodo
					.getNroBloque());
			
			guardarDatosAdministrativos();
			cerrarArchivos();
			
			return true;
				
		} else {
			//Intento pasar el registro.
			//Si no puedo, veo con cual lo puedo Splitear
			if (!pasarRegistro(nodo, registro)) {
				
				Nodo nodoHno = new Nodo();
				Nodo nodoPadre = new Nodo();
				nodoPadre
						.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
				int pos = nodoPadre.buscarRegistro(nodo.getPrimerRegistro()
						.getClave());
			
				nodoHno.setBytes(archivo.leerBloque(nodoPadre.getRegistros()
						.get(pos + 1).getNroBloqueDerecho()));
				Nodo nuevoHno = nodo.split(nodoHno, nodoPadre, true,
						ultimoBloque);
				nodoActual = nuevoHno;
				
				ultimoBloque++;
				
				archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
				//archivo.escribirBloque(nodoActual.getBytes(), nodoActual
				//.getNroBloque());
				archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
						.getNroBloque());
				archivo.escribirBloque(nodoHno.getBytes(), nodoHno
						.getNroBloque());
				
				guardarDatosAdministrativos();
				cerrarArchivos();
				
				return true;
			}
		}
		
		archivo.escribirBloque(nodoActual.getBytes(), nodoActual
					.getNroBloque());
		
		guardarDatosAdministrativos();
		cerrarArchivos();
		
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
			
			System.out.println("######################################");
			for (int i = Constantes.NRO_BLOQUE_RAIZ; i <= ultimoBloque; i++) {
				
				nodoActual = new Nodo();
				nodoActual.setBytes(archivo.leerBloque(i));
				nodoActual.listar();
				
			}
			System.out.println("######################################");
			
			archivo.cerrar();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param nodo El nodo en el que no entra
	 * @param reg el registro a pasar a un hermano de nodo.
	 * @return si exito true.
	 * @throws IOException no pudo obtener el nodo
	 */
	private boolean pasarRegistro(final Nodo nodo, final RegistroNodo reg)
			throws IOException {
		
			Nodo nodoPadre = new Nodo();
			nodoPadre.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
			//ahora nodo es el nodo padre del nodo que busque
			int pos = nodoPadre.buscarRegistro(reg.getClave());
			Nodo nodoHno = new Nodo();
			int nroHno = 0;
			byte[] datos = null;
			switch (pos) {
			// Obtengo el hermano... Por DEFAULT USO EL MENOR 
			case Constantes.MENOR:
				nroHno = nodoPadre.getPrimerRegistro().getNroBloqueDerecho();
				datos = archivo.leerBloque(nroHno);
				nodoHno.setBytes(datos);
				if (!nodoHno.hayEspacio(reg.getBytes().length)) {
					return false;
				}
				nodoHno.insertarRegistro(nodo.getUltimoRegistro());
				nodoPadre.getPrimerRegistro().setClave(
					nodo.getUltimoRegistro().getClave());
				nodo.removerRegistro(nodo.getRegistros().size() - 1);
				archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
					.getNroBloque());
				archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
				archivo.escribirBloque(nodoHno.getBytes(),
						nodoHno.getNroBloque());
				if (nodoPadre.getNroBloquePadre() < 0) {
					nodoRaiz = nodoPadre;
				}
				return true;
			case Constantes.MAYOR:
				nroHno = nodoPadre.getUltimoRegistro().getNroBloqueIzquierdo();
				datos = archivo.leerBloque(nroHno);
				nodoHno.setBytes(datos);
				if (!nodoHno.hayEspacio(reg.getBytes().length)) {
					return false;
				}
				nodoHno.insertarRegistro(nodo.getPrimerRegistro());
				nodo.removerRegistro(0);
				nodoPadre.getUltimoRegistro().setClave(
						nodo.getPrimerRegistro().getClave());
				archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
					.getNroBloque());
				archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
				archivo.escribirBloque(nodoHno.getBytes(),
						nodoHno.getNroBloque());
				if (nodoPadre.getNroBloquePadre() < 0) {
					nodoRaiz = nodoPadre;
				}
				return true;
			default:
				//TODO Hacer esto que es lo mas jodido.
				nroHno = nodoPadre.getRegistros().get(pos)
					.getNroBloqueIzquierdo();
				nodoHno.setBytes(archivo.leerBloque(nroHno));
				break;
			}
			
			
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
