package ar.com.datos.grupo5.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Representa el nodo de arbol b.
 * @author LedZeppeling
 *
 */
public class Nodo {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Nodo.class);
	
	/**
	 * Flag de desborde.
	 */
	private boolean overflow;	
	/**
	 * 
	 * Espacio en el nodo.
	 */
	private int minIndiceCarga;
	
	/**
	 * Espacio en el nodo.
	 */
	private int espacioTotal;
	
	/**
	 * Espacio ocupado.
	 */
	private int espacioOcupado;
	
	/**
	 * Nro de bloque.
	 */
	private Integer nroBloque;
		
	/**
	 * Si el nodo es hoja o no.
	 */
	private boolean esHoja = false;
	
	/**
	 * Lista de registros.
	 */
	private ArrayList< RegistroNodo > registros;
	
	/**
	 * El nodo padre.
	 */
	private Integer nroBloquePadre;

	/**
	 * Constructor.
	 */
	public Nodo() {
		
		nroBloquePadre = null;
		registros = new ArrayList<RegistroNodo>();
		this.overflow = false;
		minIndiceCarga = -1;
		espacioTotal = Constantes.SIZE_OF_INDEX_BLOCK
				- Constantes.SIZE_OF_ADMIN_NODE_DATA;
		espacioOcupado = 0;
	}
	
	/**
	 * Constructor.
	 * @param nodo Nodo padre.
	 */
	/*public Nodo(final Nodo nodo) {
		
		nroBloqueSiguiente = null;
		nroBloqueAnterior = null;
		nroBloquePadre = nodo;
		registros = new ArrayList<RegistroNodo>();
		espacioTotal = Constantes.SIZE_OF_INDEX_BLOCK;
	}	*/
	
	/**********************
	 * Metodos
	 **********************/

	/**
	 * Verifica la existencia de una clave.
	 * @param clave .
	 * @return true si la clave existe.
	 */
	public final boolean existeClave(final Clave clave) {

		return this.registros.contains(clave);
	}
	
	/**
	 * Busca una clave en el nodo.
	 * 
	 * @param clave
	 *            .
	 * @return El indice en el array en donde esta el registro que contiene la
	 *         clave o del primer elemento que es mayor a la clave. 
	 */
	public final int buscarRegistro(final Clave clave) {

		int pos = -1;
		int resultado = 0;
		
		if (registros.size() == 0) {
			return 0;
		}
		
		// Si la clave es menor a la primera, no está.
		if (clave.compareTo(registros.get(0).getClave()) < 0) {
			return Constantes.MENOR;
		}

		// Si la clave es mayor a la ultima, no está.
		if (clave.compareTo(registros.get(registros.size() - 1)
				.getClave()) > 0) {
			return Constantes.MAYOR;
		}
		
		//Recorro los nodos en busca de la clave.
		for (RegistroNodo reg : this.registros) {
			pos++;
			
			resultado = reg.getClave().compareTo(clave);
			switch (resultado) {
				//Si es igual o mayor, devuelvo el indice.
				case 0:
				case 1:
					return pos;
				//Sigo buscando;
				default:
					break;
			}
		}
		
		return pos;
	}
	
	/**
	 * Agrega un nodo.
	 * @param registro El reistro para insertar.
	 * @return El resultado de la insercion.
	 */
	public final boolean insertarRegistro(final RegistroNodo registro) {
		
		//Obtengo la posicion en donde debo insertarlo.
		ocupar(registro.getBytes().length);
		if (minIndiceCarga < 0) {
			tieneCargaMinima();
		}
		int pos = this.buscarRegistro(registro.getClave());
		switch (pos) {
		case Constantes.MENOR:
				this.registros.add(0, registro);
			break;
		case Constantes.MAYOR:
				this.registros.add(registros.size(), registro);
			break;
		default:
				//	Me fijo si ya lo contiene.
				//if (registros.get(pos).getClave().equals(registro.getClave())) {
				//	return true;
				//}
				this.registros.add(pos, registro);
			break;
		}
		if (overflow) {
			return false;
		}
		return true;
	}
	
	/**
	 * Remueve el registro en la posicion pos.
	 * 
	 * @param pos
	 *            .
	 */
	public final void removerRegistro(final int pos) {

		RegistroNodo reg = registros.remove(pos);
		ocupar(-reg.getBytes().length);
	}
	
	/**
	 * @param registro the registro to set
	 * @return .
	 */
	public final boolean setRegistro(final RegistroNodo registro) {
		int pos = this.buscarRegistro(registro.getClave());
		if (pos >= 0) {
			this.registros.set(pos, registro);
			return true;
		}
		return false;
	}

	
	/**
	 * @param registro the registro to set
	 * @return .
	 */
	public final boolean pasarRegistro(final RegistroNodo registro) {
		// FIXME
		//TODO Solo aplica para los nodos hoja
		if (this.nroBloquePadre == null) {
				// Es el unico nodo -> Raiz SOLA!!!!!
				return false;
			} 
		return false;
	}
	
	/**
	 * Para testear.
	 */
	public final void listar() {
		
		System.out.println();
		System.out.println("Contenido del nodo: [" + nroBloque + "] Padre: ["
				+ nroBloquePadre + "] FactMinCarga: " + minIndiceCarga);
		for (RegistroNodo reg : registros) {
			System.out.println("==== " + reg.getClave().getClave()
					+ " Puntero Izquierdo: [" + reg.getNroBloqueIzquierdo()
					+ "] Puntero derecho: [" + reg.getNroBloqueDerecho()
					+ "]");
		}
	}
	
	/**
	 * La raiz cuando hace split, genera 2 nodos nuevos. Uno de estos nodos va a
	 * pasar a ser la nueva raiz, y los otros dos sus hijos. El nodo actual, que
	 * es la raiz, por comodidad va a pasar a ser un hijo ya que la raiz solo va
	 * a contener un registro y es mas facil crear un nodo nuevo.
	 * 
	 * @param ultimoNroBloque el numero del ultimo bloque asignado.
	 * @return the nodos
	 */
	public final ArrayList<Nodo> splitRaiz(final int ultimoNroBloque) {
		
		ArrayList<Nodo> nodos = new ArrayList<Nodo>();
		Nodo nodo = new Nodo();
		Nodo nuevaRaiz = new Nodo();
		
		this.setNroBloquePadre(Constantes.NRO_BLOQUE_RAIZ);
		this.setNroBloque(ultimoNroBloque + 2);
		nodo.setNroBloquePadre(Constantes.NRO_BLOQUE_RAIZ);
		nodo.setNroBloque(ultimoNroBloque + 1);
		nodo.setEsHoja(this.isEsHoja());
		nuevaRaiz.setNroBloquePadre(-1);
		nuevaRaiz.setNroBloque(Constantes.NRO_BLOQUE_RAIZ);
		nuevaRaiz.setEsHoja(false);
		
		// TODO Terminar Metodo
		// Llenar nodo hno (nodo)
		while (minIndiceCarga <= registros.size() - 1) {
			RegistroNodo reg = registros.remove(minIndiceCarga);
			nodo.insertarRegistro(reg);
			this.ocupar(-reg.getBytes().length);
		}
		// Cargar Nueva raiz (nodoAux)
		RegistroNodo reg = new RegistroNodo();
		reg.setClave(nodo.getPrimerRegistro().getClave());
		reg.setNroBloqueIzquierdo(this.getNroBloque());
		reg.setNroBloqueDerecho(ultimoNroBloque + 1);
		nuevaRaiz.insertarRegistro(reg);
		nodos.add(nodo);
		nodos.add(nuevaRaiz);
		return nodos;
	}
	
	/**
	 * @param siguiente es el nodo con el cual lo tengo que tratar para dividir.
	 * @param nodoHermano es el hermano del nodo que tengo que splitear
	 * @param nodoPadre El padre del nodo y su hermano
	 * @param ultimoNroBloque .
	 * @return the nodos
	 */
	public final Nodo split(final Nodo nodoHermano, final Nodo nodoPadre, 
			final boolean siguiente, final int ultimoNroBloque) {
		
		Nodo nuevoHermano = new Nodo();
		nuevoHermano.setNroBloque(ultimoNroBloque + 1);
		nuevoHermano.setNroBloquePadre(getNroBloquePadre());
		nuevoHermano.setEsHoja(this.isEsHoja());
		ArrayList<RegistroNodo> regs = new ArrayList<RegistroNodo>();
	
		if (siguiente) {
			// -> El nuevo nodo es MENOR que el nodo actual.
			// Paso todos los regs hasta el que garantiza el 66% de ocupacion
			// Primero paso los del nodo actual
			int pos = nodoPadre.buscarRegistro(nodoHermano
					.getPrimerRegistro().getClave());
			while (minIndiceCarga <= registros.size() - 1) {
				RegistroNodo reg = registros.remove(minIndiceCarga);
				regs.add(reg);
				this.ocupar(-reg.getBytes().length);
			}
			// ahora, tengo que vaciar el hno y llenarlo otra vez
			for (int index = 0; index < nodoHermano.registros.size() - 1; index++) {
				RegistroNodo reg = nodoHermano.registros.remove(index);
				regs.add(reg);
				nodoHermano.ocupar(-reg.getBytes().length);
			}
			nodoHermano.minIndiceCarga = Constantes.MENOR;
			//ahora lo cargo hasta la minima ocupacion
			while (nodoHermano.minIndiceCarga < 0) {
				RegistroNodo reg = regs.remove(0);
				nodoHermano.insertarRegistro(reg);
			}
			// Luego lleno el nodo con lo del hermano siguiente
			while (regs.size() > 0) {
				RegistroNodo reg = regs.remove(0);
				nuevoHermano.insertarRegistro(reg);
			}
			// Ahora tengo que cargar las claves en el padre, y listo!
			RegistroNodo reg = nodoPadre.registros.remove(pos);
			nodoPadre.ocupar(-reg.getBytes().length);
			reg.setClave(nodoHermano.getPrimerRegistro().getClave());
			nodoPadre.insertarRegistro(reg);
			
			RegistroNodo reg2 = new RegistroNodo();
			reg2.setClave(nuevoHermano.getPrimerRegistro().getClave());
			reg2.setNroBloqueIzquierdo(nodoHermano.getNroBloque());
			reg2.setNroBloqueDerecho(nuevoHermano.getNroBloque());
			nodoPadre.insertarRegistro(reg);
			
		} else {
			// -> El nuevo nodo es MAYOR que el nodo actual.
			// Paso todos los regs hasta el que garantiza el 66% de ocupacion
			int pos = nodoPadre.buscarRegistro(getPrimerRegistro().getClave());
			// Luego lleno el nodo con lo del hermano anterior.
			
		}
		//Luego ver si tengo que generar el padre!!!!
		// COMO CARAJO HAGO PARA SPLITEAR EL PADRE???????
		return nuevoHermano;
	}
	
	/**
	 * @return si pudo ocupar el nodo, o no le alcanzo el espacio libre
	 */
	public final boolean tieneCargaMinima() {
		Float ocup = new Float(espacioOcupado);
		ocup = ocup / espacioTotal;
		if (ocup >= Constantes.FACTOR_CARGA_NODOS) {
			minIndiceCarga = registros.size();			
			return true;
		}
		return false;
	}
	
	/**
	 * @param espacio the espacioOcupado to set
	 * @return si pudo ocupar el nodo, o no le alcanzo el espacio libre
	 */
	public final void ocupar(final int espacio) {
		if ((espacioOcupado + espacio) > this.espacioTotal) {
			overflow = true;
		}
		espacioOcupado += espacio;
	}
	
	/**
	 * Para serializar.
	 * @return bytes[]
	 */
	public final byte[] getBytes() {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {			
			
			byte[] longDatos = Conversiones.intToArrayByte(nroBloque);
			//Nro de bloque
			dos.write(longDatos, 0, longDatos.length);
			longDatos = Conversiones.intToArrayByte(nroBloquePadre);
			//Nro de bloque del padre
			dos.write(longDatos, 0, longDatos.length);
			longDatos = Conversiones.intToArrayByte(espacioOcupado);
			
			//LOG.debug("Espacio ocupado: " + espacioOcupado);
			
			//Espacio ocupado del nodo.
			dos.write(longDatos, 0, longDatos.length);
			
			byte[] regBytes = null;
			int offset = 0;
			//Serializo todos los registros.
			for (RegistroNodo registro : registros) {
				regBytes = registro.getBytes();
				// El primer registro lo grabo completo, despues no grabo el
				// puntero al bloque de la izquierda para no repetir.
				dos.write(regBytes, offset, regBytes.length - offset);
				if (offset == 0) {
					offset += Constantes.SIZE_OF_INT;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}

	/**
	 * @return .
	 */
	public final boolean hasMoreBytes() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param buffer el nodo, pero expresado como cadena de bytes.
	 */
	public final void setBytes(final byte[] buffer) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dos = new DataInputStream(bis);
		int bloqueAnt = 0, leido = 0, aLeer = 0;
		int cantidad = 0;
		byte[] datos = new byte[Constantes.SIZE_OF_INT];
		RegistroNodo reg = null;
		registros = new ArrayList<RegistroNodo>();
		
		try {
			//Leo el numero de bloque.
			nroBloque = dos.readInt();
			nroBloquePadre = dos.readInt();
			espacioOcupado = dos.readInt();
			aLeer = espacioOcupado;
			
			// Leo el primer dato del primer registro,que es el numero de bloque
			// izquierdo al que apunta.
			bloqueAnt = dos.readInt();
			while (aLeer > leido) {
				
				//Lea la cantidad de bytes que ocupa el registro.
				cantidad = dos.readInt();
				leido += Constantes.SIZE_OF_INT + Constantes.SIZE_OF_INT;
				
				datos = new byte[cantidad];
				leido += dos.read(datos, 0, cantidad);
				reg = new RegistroNodo();
				reg.setBytes(datos, bloqueAnt);
				bloqueAnt = reg.getNroBloqueDerecho();
				if (bloqueAnt < 0) {
					esHoja = true;
				}
				//Los agrego a la lista.
				registros.add(reg);
				if (this.tieneCargaMinima() && (minIndiceCarga < 0)) {
					minIndiceCarga = this.registros.size() - 1;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**************************
	 * Getters and Setters
	 **************************/
	
	/**
	 * @return the espacioTotal
	 */
	public final int getEspacioTotal() {
		return espacioTotal;
	}

	/**
	 * @return the espacioOcupado
	 */
	public final int getEspacioOcupado() {
		return espacioOcupado;
	}
	
	/**
	 * @return the esHoja
	 */
	public final boolean isEsHoja() {
		return esHoja;
	}

	/**
	 * @return the registros
	 */
	public final ArrayList<RegistroNodo> getRegistros() {
		return registros;
	}

	/**
	 * @param espacio the espacioTotal to set
	 */
	public final void setEspacioTotal(final int espacio) {
		this.espacioTotal = espacio;
	}

	/**
	 * @param espacio the espacioOcupado to set
	 */
	public final void setEspacioOcupado(final int espacio) {
		this.espacioOcupado = espacio;
	}
	
	/**
	 * @param hoja the esHoja to set
	 */
	public final void setEsHoja(final boolean hoja) {
		this.esHoja = hoja;
	}
	
	/**
	 * @return El primer registro del nodo.
	 */
	public final RegistroNodo getPrimerRegistro() {
		return this.registros.get(0);
	}
	
	/**
	 * @return El ultimo registro del nodo.
	 */
	public final RegistroNodo getUltimoRegistro() {
		return this.registros.get(registros.size() - 1);
	}
	
	/**
	 * @return the nroBloquePadre
	 */
	public final Integer getNroBloquePadre() {
		return nroBloquePadre;
	}

	/**
	 * @param nroBloque the nroBloquePadre to set
	 */
	public final void setNroBloquePadre(final Integer nroBloque) {
		this.nroBloquePadre = nroBloque;
	}

	/**
	 * @return the nroBloque
	 */
	public final Integer getNroBloque() {
		return nroBloque;
	}

	/**
	 * @param nroBloque the nroBloque to set
	 */
	public final void setNroBloque(final Integer nroBloque) {
		this.nroBloque = nroBloque;
	}

	/**
	 * @param overflow the overflow to set
	 */
	public final void setOverflow(final boolean overflow) {
		this.overflow = overflow;
	}

	/**
	 * @return the overflow
	 */
	public final boolean isOverflow() {
		return overflow;
	}
}
