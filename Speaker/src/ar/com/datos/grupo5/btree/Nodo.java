package ar.com.datos.grupo5.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
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
		espacioTotal = Constantes.SIZE_OF_INDEX_BLOCK;
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
		//FIXME: Esto es cualquiera!!!!!!!
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
		this.ocupar(registro.getBytes().length);
		if (this.minIndiceCarga == Constantes.MENOR) {
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
				this.registros.add(pos, registro);
			break;
		}
		if (overflow) {
			return false;
		}
		return true;
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
				+ nroBloquePadre + "]");
		for (RegistroNodo reg : registros) {
			System.out.println("==== " + reg.getClave().getClave()
					+ " Puntero derecho: [" + reg.getNroBloqueDerecho()
					+ "] Puntero Izquierdo: [" + reg.getNroBloqueIzquierdo()
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
			nodo.insertarRegistro(registros.remove(minIndiceCarga));
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
	 * @return the nodos
	 */
	public final Nodo split(final boolean siguiente) {
		Nodo nodo = new Nodo();
		// FIXME Hacer el metodo
		if (siguiente) {
			// -> El nuevo nodo es MENOR que el nodo actual.
			// Paso todos los regs hasta el que garantiza el 66% de ocupacion
			
			// Luego lleno el nodo con lo del hermano siguiente
		} else {
			// -> El nuevo nodo es MAYOR que el nodo actual.
			// Paso todos los regs hasta el que garantiza el 66% de ocupacion
			
			// Luego lleno el nodo con lo del hermano anterior.
			
		}
		//Luego ver si tengo que generar el padre!!!!
		// COMO CARAJO HAGO PARA SPLITEAR EL PADRE???????
		return nodo;
	}
	
	/**
	 * @return si pudo ocupar el nodo, o no le alcanzo el espacio libre
	 */
	public final boolean tieneCargaMinima() {
		if ((this.espacioOcupado / this.espacioTotal) >= Constantes
				.FACTOR_CARGA_NODOS) {
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
		if ((this.espacioOcupado + espacio) > this.espacioTotal) {
			this.overflow = true;
		}
		this.espacioOcupado += espacio;
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
	 * 
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
			ocupar(4 * Constantes.SIZE_OF_INT);
			while (aLeer > leido) {
				
				//Lea la cantidad de bytes que ocupa el registro.
				cantidad = dos.readInt();
				leido += Constantes.SIZE_OF_INT + Constantes.SIZE_OF_INT;
				
				datos = new byte[cantidad];
				leido += dos.read(datos, 0, cantidad);
				reg = new RegistroNodo();
				reg.setBytes(datos, bloqueAnt);
				bloqueAnt = reg.getNroBloqueDerecho();
				//Los agrego a la lista.
				registros.add(reg);
				ocupar(cantidad);
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
	 * @param nroBloquePadre the nroBloquePadre to set
	 */
	public final void setNroBloquePadre(Integer nroBloquePadre) {
		this.nroBloquePadre = nroBloquePadre;
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
	public final void setNroBloque(Integer nroBloque) {
		this.nroBloque = nroBloque;
	}

	/**
	 * @param overflow the overflow to set
	 */
	public void setOverflow(boolean overflow) {
		this.overflow = overflow;
	}

	/**
	 * @return the overflow
	 */
	public boolean isOverflow() {
		return overflow;
	}
}
