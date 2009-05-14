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
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.sortExterno.NodoParticion;
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
	 * Lista con los nodos hojas que se modificaron
	 */
	private ArrayList<Nodo> nodosModificados;

	/**
	 * 
	 */
	private ArchivoSecuencialSet secuencialSet = null;
	
	/**
	 * Constructor.
	 * @throws Exception .
	 */
	public BStar() throws Exception {
		nodoActual = null;
		archivo = new ArchivoBloques(Constantes.SIZE_OF_INDEX_BLOCK);
		nodoRaiz = null;
		nodosModificados = new ArrayList<Nodo>();
		
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
	 * @throws IOException .
	 */
	public RegistroNodo buscar(final Clave clave) throws IOException {
		
		//Abro el archivo.
		abrirArchivos();
		
		//Obtengo el nodo en el que podria estar la clave.
		Nodo nodo = buscarNodo(clave);
		
		if (nodo == null || nodoRaiz == null) {
			return null;
		}
		
		//Verifico si la clave está.
		int posReg = nodo.buscarRegistro(clave);
		
		cerrarArchivos();
		switch (posReg) {
		case Constantes.MENOR:
		case Constantes.MAYOR:
			return null;
		default:
			if (nodo.getRegistros().get(posReg).getClave().equals(clave)) {
				RegistroNodo reg = nodo.getRegistros().get(posReg);
				reg.setPunteroBloque(nodo.getPunteroBloque());
				return reg;
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
	 * @throws IOException .
	 */
	private Nodo buscarNodo(final Clave clave) throws IOException {
		
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
						throw e;
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
						throw e;
					}
					nodoActual = nodoAux;
				} else {
					return nodoActual;
				}
				break;
			//Encontré la clave que buscaba o una mayor.
			default:
				//Si es hoja, lo devuelvo.
				if (nodoActual.isEsHoja()) {
					return nodoActual;
				} else {
					Clave claveAux = nodoActual.getRegistros().get(posReg).getClave();
					if (clave.compareTo(claveAux) >= 0) {
						nroBloque = nodoActual.getRegistros().get(posReg)
								.getNroBloqueDerecho();
					} else {
						nroBloque = nodoActual.getRegistros().get(posReg)
						.getNroBloqueIzquierdo();
					}
					nodoAux = new Nodo();
					try {
						nodoAux.setBytes(archivo
								.leerBloque(nroBloque));
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
					}
					nodoActual = nodoAux;
				}
				//Veo si lo que recupere el igual o mayor.
//				if (nodoActual.getRegistros().size() == 0) {
//					return nodoActual;
//				}
//				if (nodoActual.getRegistros().get(posReg).getClave().equals(
//						clave)) {
//					
//					return nodoActual;
//				} else { // Es mayor.
//					if (!nodoActual.isEsHoja()) {
//						nroBloque = nodoActual.getRegistros().get(posReg)
//								.getNroBloqueIzquierdo();
//						nodoAux = new Nodo();
//						try {
//							nodoAux.setBytes(archivo
//									.leerBloque(nroBloque));
//						} catch (IOException e) {
//							e.printStackTrace();
//							throw e;
//						}
//						nodoActual = nodoAux;
//					} else {
//						return nodoActual;
//					}
//				}
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
			Integer puntero = secuencialSet.reservarBloqueLibre();
			nodoRaiz.setPunteroBloque(puntero);
			//El primero es hoja al pricipio.
			nodoRaiz.setEsHoja(true);
			nodoRaiz.setNroBloque(Constantes.NRO_BLOQUE_RAIZ);
			nodoRaiz.setNroBloquePadre(-1);
			nodoRaiz.insertarRegistro(registro);
			ultimoBloque = Constantes.NRO_BLOQUE_RAIZ;
			
			guardarDatosAdministrativos();
			
			archivo.escribirBloque(nodoRaiz.getBytes(), nodoRaiz
						.getNroBloque());
			
			nodosModificados.clear();
			nodosModificados.add(nodoRaiz);
			
			cerrarArchivos();

			return true;
		}
		//Busco en donde insertar.
		Nodo nodo = buscarNodo(registro.getClave());
		//Si ya existe, no inserto.
		if (nodo.existeClave(registro.getClave())) {
			cerrarArchivos();
			LOG.debug("No se inserto en el elemento ["
					+ registro.getClave().getClave()
					+ "] debido a que ya existia la clave en el arbol.");
			return true;
		}
		
		if (nodo.insertarRegistro(registro)) {
			
			this.nodoActual = nodo;
			nodosModificados.clear();
			nodosModificados.add(nodoActual);
			
		} else if (nodo.getNroBloquePadre() < 0) {
			// Es la raiz.
			// Splitear nodo
			System.out.println("=====SplitRaiz=======Nodo: "
					+ nodo.getNroBloque() + "========");
			return splitRaiz(nodo);
				
		} else {
			//Intento pasar el registro.
			//Si no puedo, veo con cual lo puedo Splitear
			if (!pasarRegistro(nodo)) {
				
				System.out.println("=====Split=======Nodo: "
						+ nodo.getNroBloque() + "========");
				Nodo padre = split(nodo);

				while (padre.isOverflow()) {
					
					if (padre.getNroBloquePadre() < 0) {
						System.out
								.println("=====Overflow - SplitRaiz=======Nodo: "
										+ padre.getNroBloque() + "========");
						return splitRaiz(padre);
					} else {
						if (!pasarRegistro(padre)) {
							System.out
									.println("=====Overflow Split=======Nodo: "
											+ padre.getNroBloque() + "========");
							padre = split(padre);
						} else {
							padre.setOverflow(false);
						}
					}
				}
				
				archivo.escribirBloque(padre.getBytes(), padre
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
	 * Split interno.
	 * @param nodo .
	 * @throws IOException .
	 * @return Si pudo Splitear, devuelve true, sino false.
	 */
	private boolean splitRaiz(final Nodo nodo) throws IOException {
		
		ArrayList<Nodo> nodos = nodo.splitRaiz(ultimoBloque);
		Integer punteroBloque = nodoRaiz.getPunteroBloque();
		nodoRaiz = nodos.get(0);
		nodoActual = nodos.get(1);
		ultimoBloque += 2;
		Nodo nodoAuxiliar = new Nodo();
		
		if (!nodoActual.isEsHoja()) {
			//reasignar los NroBloquePadre de los hijos de nodo y de nodoActual
			for (RegistroNodo reg : nodo.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodo.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodo
					.getUltimoRegistro().getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodo.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
			for (RegistroNodo reg : nodoActual.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodoActual.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodoActual
					.getUltimoRegistro().getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodoActual.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
		} else {
			nodoActual.setPunteroBloque(punteroBloque);
			punteroBloque = secuencialSet.reservarBloqueLibre();
			nodo.setPunteroBloque(punteroBloque);
			nodosModificados.clear();
			nodosModificados.add(nodoActual);
			nodosModificados.add(nodo);
		}
		archivo.escribirBloque(nodoRaiz.getBytes(), nodoRaiz
				.getNroBloque());
		archivo.escribirBloque(nodo.getBytes(), nodo
				.getNroBloque());
		archivo.escribirBloque(nodoActual.getBytes(),
				nodoActual.getNroBloque());

		
		guardarDatosAdministrativos();
		cerrarArchivos();
		
		return true;
	}
	
	
	/**
	 * Split interno.
	 * @param nodo .
	 * @return nodo.
	 * @throws IOException .
	 */
	private Nodo split(final Nodo nodo) throws IOException {
		
		Nodo nodoHno = new Nodo();
		Nodo nodoPadre = new Nodo();
		Nodo nodoAuxiliar = new Nodo();
		nodoPadre
				.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
		int pos = nodoPadre.buscarRegistro(nodo.getPrimerRegistro()
				.getClave());
		if ((pos < nodoPadre.getRegistros().size() - 1)
				&& (pos != Constantes.MAYOR)) {
			nodoHno.setBytes(archivo.leerBloque(nodoPadre.getRegistros().get(
					pos + 1).getNroBloqueDerecho()));
			Nodo nuevoHno = nodo.split(nodoHno, nodoPadre, true, ultimoBloque);
			nodoActual = nuevoHno;
		} else {
			if (pos == Constantes.MAYOR) {
				pos = nodoPadre.getRegistros().size() - 1;
			}
			nodoHno.setBytes(archivo.leerBloque(nodoPadre.getRegistros().get(
					pos).getNroBloqueIzquierdo()));
			Nodo nuevoHno = nodo.split(nodoHno, nodoPadre, false, ultimoBloque);
			nodoActual = nuevoHno;
		}
		
		nodoActual.setPunteroBloque(secuencialSet.reservarBloqueLibre());
		
		ultimoBloque++;
		
		if (nodoPadre.getNroBloquePadre() < 0) {
			nodoRaiz = nodoPadre;
		}
		archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
		archivo.escribirBloque(nodoActual.getBytes(), nodoActual
				.getNroBloque());
		if (!nodoPadre.isOverflow()) {
			archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
					.getNroBloque());
		}
		archivo.escribirBloque(nodoHno.getBytes(), nodoHno
				.getNroBloque());
		if (!nodo.isEsHoja()) {
			for (RegistroNodo reg : nodo.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodo.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodo.getUltimoRegistro()
					.getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodo.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
			for (RegistroNodo reg : nodoPadre.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodoPadre.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodoPadre.getUltimoRegistro()
					.getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodoPadre.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
			for (RegistroNodo reg : nodoHno.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodoHno.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodoHno.getUltimoRegistro()
					.getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodoHno.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
			for (RegistroNodo reg : nodoActual.getRegistros()) {
				nodoAuxiliar.setBytes(archivo.leerBloque(reg
						.getNroBloqueIzquierdo()));
				nodoAuxiliar.setNroBloquePadre(nodoActual.getNroBloque());
				archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
						.getNroBloque());
			}
			nodoAuxiliar.setBytes(archivo.leerBloque(nodoActual.getUltimoRegistro()
					.getNroBloqueDerecho()));
			nodoAuxiliar.setNroBloquePadre(nodoActual.getNroBloque());
			archivo.escribirBloque(nodoAuxiliar.getBytes(), nodoAuxiliar
					.getNroBloque());
		}
		
		// Guardo los nodos hojas modificados para pasarselos al secuencial set
		if (nodo.isEsHoja()) {
			nodosModificados.clear();
			nodosModificados.add(nodo);
			nodosModificados.add(nodoHno);
			nodosModificados.add(nodoActual);
		}
		
		return nodoPadre;
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
			System.out.println("Nodos modificados:");
			for (Nodo nodo : nodosModificados) {
				System.out.println(nodo.getNroBloque());
			}
			System.out.println("######################################");
			
			archivo.cerrar();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param nodo El nodo en el que no entra
	 * @param reg el registro a pasar a un hermano de nodo.
	 * @return si exito true.
	 * @throws IOException no pudo obtener el nodo
	 */
	private boolean pasarRegistro2(final Nodo nodo)
			throws IOException {
		
			Nodo nodoPadre = new Nodo();
			RegistroNodo reg = new RegistroNodo();
			// Lo seteo aca SOLO para ver a cual le puede pasar, luego se cambia
			reg  = nodo.getUltimoRegistro();
			LOG.debug("Leo nro de bloque: " + nodo.getNroBloquePadre());
			nodoPadre.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
			//ahora nodo es el nodo padre del nodo que busque
			int pos = nodoPadre.buscarRegistro(reg.getClave());
			boolean izq = true;
			boolean der = true;
			if (pos == Constantes.MENOR ){
				izq = false;
			}
			if (pos == Constantes.MAYOR ){
				der = false;
			}
			if ((izq)||(pos < nodoPadre.getRegistros().size())) {
					int pos_izq = pos;
					if (!der){
						// Estoy en el ultimo
						pos_izq = nodoPadre.getRegistros().size() -1;
					}
					// Primero evaluo el IZQ
					Nodo nodoIZQ = new Nodo();
					nodoPadre.setBytes(archivo.leerBloque(nodoPadre.getRegistros().get(pos_izq).getNroBloqueIzquierdo()));
				}
			
			if (izq && der) {
				
			} else {
				
			}
			return false;
	}
	
	/**
	 * 
	 * @param padre
	 * @param izquierdo
	 * @return
	 * @throws IOException 
	 */
	boolean pasarDerechaIzquierda(Nodo padre,
								  Nodo izquierdo,
								  Nodo derecho,
								  int posPadre) throws IOException {
		
		//El registro que apunta a los dos nodos.
		RegistroNodo regParaBajar = padre.getRegistros().get(posPadre);
		RegistroNodo regParaSubir = null;
		
		//Veo si tengo espacio para bajar la clave.
		//TODO: Ver lo del tamaño del registro en el nodo.
		if (!izquierdo.hayEspacio(regParaBajar.getBytes().length)) {
			return false;
		}
		// Si no es hoja, subo la clave al padre y bajo una del padre al
		// hermano.
		if (derecho.isEsHoja()) {
			regParaSubir = padre.getRegistros().get(1);
		} else {
			regParaSubir = padre.getPrimerRegistro();
		}
		
		if (!padre.hayEspacio(regParaSubir.getBytes().length)) {
			return false;
		}
		
		// Si llegue hasta aca, tengo lugar, entonces paso los regsitros.
		// Bajo el registro del padre al izquierdo.
		RegistroNodo regNuevo = new RegistroNodo();
		regNuevo.setClave(regParaBajar.getClave());
		regNuevo.setPunteroBloque(regParaBajar.getPunteroBloque());
		izquierdo.insertarRegistro(regNuevo);
		//Reemplazo la clave en el padre.
		regParaBajar.setClave(regParaSubir.getClave());
		//Remuevo el primer registro.
		derecho.removerRegistro(0);

		return true;
	}
	
	/**
	 * @param nodo El nodo en el que no entra
	 * @param reg el registro a pasar a un hermano de nodo.
	 * @return si exito true.
	 * @throws IOException no pudo obtener el nodo
	 */
	private boolean pasarRegistro(final Nodo nodo)
			throws IOException {
		
			Nodo nodoPadre = new Nodo();
			RegistroNodo reg = new RegistroNodo();
			// Lo seteo aca SOLO para ver a cual le puede pasar, luego se cambia
			reg  = nodo.getUltimoRegistro();
			LOG.debug("Leo nro de bloque: " + nodo.getNroBloquePadre());
			nodoPadre.setBytes(archivo.leerBloque(nodo.getNroBloquePadre()));
			//ahora nodo es el nodo padre del nodo que busque
			int pos = nodoPadre.buscarRegistro(reg.getClave());
			int nroHno = 0;
			byte[] datos = null;
			Nodo nodoHnoIzquierdo = null;
			Nodo nodoHnoDerecho = null;
			
			switch (pos) { 
			case Constantes.MENOR:
				//Tiene que pasarlo al de la izquierda
				nodoHnoDerecho = new Nodo();
				nroHno = nodoPadre.getPrimerRegistro().getNroBloqueDerecho();
				datos = archivo.leerBloque(nroHno);
				nodoHnoDerecho.setBytes(datos);
				if (!nodoHnoDerecho.hayEspacio(reg.getBytes().length)) {
					return false;
				}
				if (!nodo.isEsHoja()) {
					// pasar la clave y el resto reasignar las referencias
					RegistroNodo aux = new RegistroNodo();
					aux.setClave(nodoPadre.getPrimerRegistro().getClave());
					aux.setNroBloqueIzquierdo(nodo.getUltimoRegistro()
							.getNroBloqueDerecho());
					aux.setNroBloqueDerecho(nodoHnoDerecho.getPrimerRegistro()
							.getNroBloqueIzquierdo());
					if (!nodoHnoDerecho.hayEspacio(aux.getBytes().length)) {
						return false;
					}
					nodoHnoDerecho.insertarRegistro(aux);
					int espacio = nodoPadre.getEspacioOcupado();
					espacio += diferenciaClaves(nodoPadre.getPrimerRegistro(), nodo
						.getUltimoRegistro());
					nodoPadre.setEspacioOcupado(espacio);
					if (nodoPadre.isOverflow()) {
						LOG.error("Error, nodo en overflow.");
					}
					nodoPadre.getPrimerRegistro().setClave(
							nodo.getUltimoRegistro().getClave());
					
					nodo.removerRegistro(nodo.getRegistros().size() - 1);
					nodoHnoIzquierdo = new Nodo();
					nodoHnoIzquierdo.setBytes(archivo.leerBloque(nodoHnoDerecho
							.getPrimerRegistro().getNroBloqueIzquierdo()));
					nodoHnoIzquierdo.setNroBloquePadre(nodoHnoDerecho
							.getNroBloque());
					archivo.escribirBloque(nodoHnoIzquierdo.getBytes(), 
							nodoHnoIzquierdo.getNroBloque());
					
				} else {
					if (!nodoHnoDerecho.hayEspacio(nodo.getUltimoRegistro().getBytes().length)) {
						return false;
					}
					nodoHnoDerecho.insertarRegistro(nodo.getUltimoRegistro());
					int espacio = nodoPadre.getEspacioOcupado();
					espacio += diferenciaClaves(nodoPadre.getPrimerRegistro(), nodo
						.getUltimoRegistro());
					nodoPadre.setEspacioOcupado(espacio);
					if (nodoPadre.isOverflow()) {
						LOG.error("Error, nodo en overflow.");
					}
					nodoPadre.getPrimerRegistro().setClave(
						nodo.getUltimoRegistro().getClave());
					nodo.removerRegistro(nodo.getRegistros().size() - 1);
					nodosModificados.clear();
					nodosModificados.add(nodo);
					nodosModificados.add(nodoHnoDerecho);
				}
				archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
					.getNroBloque());
				archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
				archivo.escribirBloque(nodoHnoDerecho.getBytes(),
						nodoHnoDerecho.getNroBloque());
				if (nodoPadre.getNroBloquePadre() < 0) {
					nodoRaiz = nodoPadre;
				}
				System.out.println("=====PasarRegistroMENOR=======Nodo: "
						+ nodo.getNroBloque() + "========");
				return true;
			case Constantes.MAYOR:
				//Tiene que pasarlo al de la izquierda
				nodoHnoIzquierdo = new Nodo();
				nroHno = nodoPadre.getUltimoRegistro().getNroBloqueIzquierdo();
				datos = archivo.leerBloque(nroHno);
				nodoHnoIzquierdo.setBytes(datos);	
				reg  = nodo.getPrimerRegistro();
				if (!nodoHnoIzquierdo.hayEspacio(reg.getBytes().length)) {
					return false;
				}
				if (!nodo.isEsHoja()) {
					// pasar la clave y el resto reasignar las referencias
					RegistroNodo aux = new RegistroNodo();
					aux.setClave(nodoPadre.getUltimoRegistro().getClave());
					aux.setNroBloqueIzquierdo(nodoHnoIzquierdo
							.getUltimoRegistro().getNroBloqueDerecho());
					aux.setNroBloqueDerecho(nodo.getPrimerRegistro()
							.getNroBloqueIzquierdo());
					if (!nodoHnoIzquierdo.hayEspacio(aux.getBytes().length)) {
						return false;
					}
					nodoHnoIzquierdo.insertarRegistro(aux);
					int espacio = nodoPadre.getEspacioOcupado();
					espacio += diferenciaClaves(nodoPadre.getUltimoRegistro(), nodo
						.getPrimerRegistro());
					nodoPadre.setEspacioOcupado(espacio);
					if (nodoPadre.isOverflow()) {
						LOG.error("Error, nodo en overflow.");
					}
					nodoPadre.getUltimoRegistro().setClave(
							nodo.getPrimerRegistro().getClave());
					nodo.removerRegistro(0);
					nodoHnoDerecho = new Nodo();
					nodoHnoDerecho.setBytes(archivo.leerBloque(nodoHnoIzquierdo
							.getUltimoRegistro().getNroBloqueDerecho()));
					nodoHnoDerecho.setNroBloquePadre(nodoHnoIzquierdo
							.getNroBloque());
					archivo.escribirBloque(nodoHnoDerecho.getBytes(), 
							nodoHnoDerecho.getNroBloque());
					
				} else {
					if (!nodoHnoIzquierdo.hayEspacio(nodo.getPrimerRegistro().getBytes().length)) {
						return false;
					}
					nodoHnoIzquierdo.insertarRegistro(nodo.getPrimerRegistro());
					nodo.removerRegistro(0);
					int espacio = nodoPadre.getEspacioOcupado();
					espacio += diferenciaClaves(nodoPadre.getUltimoRegistro(), nodo
						.getPrimerRegistro());
					nodoPadre.setEspacioOcupado(espacio);
					if (nodoPadre.isOverflow()) {
						LOG.error("Error, nodo en overflow.");
					}
					nodoPadre.getUltimoRegistro().setClave(
							nodo.getPrimerRegistro().getClave());
					nodosModificados.clear();
					nodosModificados.add(nodo);
					nodosModificados.add(nodoHnoIzquierdo);
				}
				archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
					.getNroBloque());
				archivo.escribirBloque(nodo.getBytes(), nodo.getNroBloque());
				archivo.escribirBloque(nodoHnoIzquierdo.getBytes(),
						nodoHnoIzquierdo.getNroBloque());
				if (nodoPadre.getNroBloquePadre() < 0) {
					nodoRaiz = nodoPadre;
				}
				System.out.println("=====PasarRegistroMAYOR=======Nodo: "
						+ nodo.getNroBloque() + "========");
				return true;
			default:
				//Puede pasarlo a cualquiera, empiezo probando por el IZQUIERDO
				int pos_izq = 0;
				int pos_der = nodoPadre.getRegistros().size() - 1;
				if (pos == 0) {
					pos_der = pos + 1;	
				} else if (pos == nodoPadre.getRegistros().size() - 1) {
					pos_izq = pos - 1;
				}
				int nroHnoDerecho = nodoPadre.getRegistros().get(pos_der)
						.getNroBloqueDerecho();
				int nroHnoIzquierdo = nodoPadre.getRegistros().get(pos_izq)
						.getNroBloqueIzquierdo();
				nodoHnoDerecho = new Nodo();
				nodoHnoDerecho.setBytes(archivo.leerBloque(nroHnoDerecho));
				if (nodoHnoDerecho.hayEspacio(reg.getBytes().length)) {
					
					if (!nodo.isEsHoja()) {
						// pasar la clave y el resto reasignar las referencias
						RegistroNodo aux = new RegistroNodo();
						aux.setClave(nodoPadre.getRegistros().get(pos_der)
								.getClave());
						aux.setNroBloqueIzquierdo(nodo.getUltimoRegistro()
								.getNroBloqueDerecho());
						aux.setNroBloqueDerecho(nodoHnoDerecho
								.getPrimerRegistro().getNroBloqueIzquierdo());
						if (!nodoHnoDerecho.hayEspacio(aux.getBytes().length)) {
							return false;
						}
						nodoHnoDerecho.insertarRegistro(aux);
						int espacio = nodoPadre.getEspacioOcupado();
						espacio += diferenciaClaves(nodoPadre.getRegistros().get(
							pos), nodo.getUltimoRegistro());
						nodoPadre.setEspacioOcupado(espacio);
						if (nodoPadre.isOverflow()) {
							LOG.error("Error, nodo en overflow.");
						}
						nodoPadre.getRegistros().get(pos).setClave(
								nodo.getUltimoRegistro().getClave());
						nodo.removerRegistro(nodo.getRegistros().size() - 1);
						nodoHnoIzquierdo = new Nodo();
						nodoHnoIzquierdo.setBytes(archivo.leerBloque(nodoHnoDerecho
								.getPrimerRegistro().getNroBloqueIzquierdo()));
						nodoHnoIzquierdo.setNroBloquePadre(nodoHnoDerecho
								.getNroBloque());
						archivo.escribirBloque(nodoHnoIzquierdo.getBytes(), 
								nodoHnoIzquierdo.getNroBloque());
					} else {
						RegistroNodo regAux = nodo.removerRegistro(
								nodo.getRegistros().size() - 1);
						if (!nodoHnoDerecho.hayEspacio(regAux.getBytes().length)) {
							return false;
						}
						nodoHnoDerecho.insertarRegistro(regAux);
						int espacio = nodoPadre.getEspacioOcupado();
						espacio += diferenciaClaves(nodoPadre.getRegistros().get(
							pos_der), regAux);
						nodoPadre.setEspacioOcupado(espacio);
						if (nodoPadre.isOverflow()) {
							LOG.error("Error, nodo en overflow.");
						}
						nodoPadre.getRegistros().get(pos).setClave(
								regAux.getClave());
						nodosModificados.clear();
						nodosModificados.add(nodo);
						nodosModificados.add(nodoHnoDerecho);
					}
					archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
							.getNroBloque());
					archivo.escribirBloque(nodo.getBytes(), 
							nodo.getNroBloque());
					archivo.escribirBloque(nodoHnoDerecho.getBytes(),
							nodoHnoDerecho.getNroBloque());
					if (nodoPadre.getNroBloquePadre() < 0) {
						nodoRaiz = nodoPadre;
					}
					System.out.println("=====PasarRegistroDefaultMayor=======Nodo: "
							+ nodo.getNroBloque() + "========");
					return true;
				} else {
					nodoHnoIzquierdo = new Nodo();
					reg = nodo.getPrimerRegistro();
					nodoHnoIzquierdo.setBytes(
							archivo.leerBloque(nroHnoIzquierdo));
					reg = nodo.getUltimoRegistro();
					if (nodoHnoIzquierdo.hayEspacio(reg.getBytes().length)) {
						if (!nodo.isEsHoja()) {
							RegistroNodo aux = new RegistroNodo();
							aux.setClave(nodoPadre.getRegistros().get(pos_izq)
									.getClave());
							aux.setNroBloqueIzquierdo(nodoHnoIzquierdo
									.getUltimoRegistro().getNroBloqueDerecho());
							aux.setNroBloqueDerecho(nodo.getPrimerRegistro()
									.getNroBloqueIzquierdo());
							if (!nodoHnoIzquierdo.hayEspacio(aux.getBytes().length)) {
								return false;
							}
							nodoHnoIzquierdo.insertarRegistro(aux);
							int espacio = nodoPadre.getEspacioOcupado();
							espacio += diferenciaClaves(nodoPadre.getRegistros().get(
								pos_izq), nodoPadre.getPrimerRegistro());
							nodoPadre.setEspacioOcupado(espacio);
							if (nodoPadre.isOverflow()) {
								LOG.error("Error, nodo en overflow.");
							}
							nodoPadre.getRegistros().get(pos).setClave(
									nodo.getPrimerRegistro().getClave());
							nodo.removerRegistro(0);
							nodoHnoDerecho = new Nodo();
							nodoHnoDerecho.setBytes(archivo.leerBloque(nodoHnoIzquierdo
									.getUltimoRegistro().getNroBloqueDerecho()));
							nodoHnoDerecho.setNroBloquePadre(nodoHnoIzquierdo
									.getNroBloque());
							archivo.escribirBloque(nodoHnoDerecho.getBytes(), 
									nodoHnoDerecho.getNroBloque());
						} else {
							RegistroNodo regAux = nodo.removerRegistro(0);
							if (!nodoHnoIzquierdo.hayEspacio(regAux.getBytes().length)) {
								return false;
							}
							nodoHnoIzquierdo.insertarRegistro(regAux);
							int espacio = nodoPadre.getEspacioOcupado();
							//FIXME: Aca decia pos, puse pos - 1
							espacio += diferenciaClaves(nodoPadre.getRegistros().get(
								pos_izq), regAux);
							nodoPadre.setEspacioOcupado(espacio);
							if (nodoPadre.isOverflow()) {
								LOG.error("Error, nodo en overflow.");
							}
							nodoPadre.getRegistros().get(pos_izq).setClave(
								regAux.getClave());
							nodosModificados.clear();
							nodosModificados.add(nodo);
							nodosModificados.add(nodoHnoIzquierdo);
						}
						archivo.escribirBloque(nodoPadre.getBytes(), nodoPadre
							.getNroBloque());
						archivo.escribirBloque(nodo.getBytes(),
								nodo.getNroBloque());
						archivo.escribirBloque(nodoHnoIzquierdo.getBytes(),
								nodoHnoIzquierdo.getNroBloque());
						if (nodoPadre.getNroBloquePadre() < 0) {
							nodoRaiz = nodoPadre;
						}
						System.out.println("=====PasarRegistroDefaultMenor=======Nodo: "
								+ nodo.getNroBloque() + "========");
						return true;
					} else {
						return false;
					}
				}
			}
	}
	
	/**
	 * 
	 * @param regOld
	 * @param regNew
	 * @return
	 */
	public int diferenciaClaves(RegistroNodo regOld, RegistroNodo regNew) {
		return regNew.getClave().getClave().getBytes().length
				- regOld.getClave().getClave().getBytes().length;
	}
	/**
	 * Abre los dos archivos.
	 * @param todos .
	 * @return .
	 */
	private boolean abrirArchivos() {
		
		try {
			archivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			return true;
			
		} catch (FileNotFoundException e) {
			LOG.error("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @param todos .
	 * @return si exito true.
	 */
	private boolean cerrarArchivos() {
		
		try {
			archivo.cerrar();
			return true;
		} catch (IOException e) {
			LOG.error("Error al cerrar archivo.", e);
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Nodo> getNodosModificados() {
		return nodosModificados;
	}

	public void setSecuencialSet(ArchivoSecuencialSet secuencialSet) {
		this.secuencialSet = secuencialSet;
	}
}
