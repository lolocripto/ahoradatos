package ar.com.datos.grupo5.sortExterno;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;


/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:16:43 p.m.
 */
public class ReplacementSelection {

	/**
	 * Nombre del archivo con los registros a ordenar.
	 */
	private String arch;
	
	/**
	 * Lista con los pares idTermino - Documento.
	 */
	private List<NodoRS> listaNodo;
	
	/**
	 * Lista de particiones generadas.
	 */
	private ArrayList<String> listaParticiones;
	
	/**
	 * Memoria que voy a usar como ventana de datos 
	 * para las particiones.
	 */
	private int memoria;
	
	/**
	 * Cantidad de nodos que entran en la memoria asignada.
	 */
	private int cantidadNodos;
	
	/**
	 * Contructor de copia por defecto.
	 */
	public ReplacementSelection() {
		this.listaNodo = new ArrayList<NodoRS>();
		this.listaParticiones = new ArrayList<String>();
	}
	
	public void finalize() throws Throwable {
	}

	/**
	 * Define el nombre del archivo y lee la cantidad de memoria 
	 * en bytes a usar.
	 * @param archExt Nombre del archivo que tiene 
	 * los elementos a ordena.
	 */
	public ReplacementSelection(final String archExt) {
		this.arch = archExt;
		this.memoria = Constantes.TAMANIO_BUFFER_REPLACEMENT_SELECTION;
		this.listaNodo = new ArrayList<NodoRS>();
		this.listaParticiones = new ArrayList<String>();
	}
	
	/**
	 * Devuelve la cantidad de elementos no congelados.
	 * @return Cantidad de elementos no congelados.
	 */
	private int cuantosDisponibles() {
		
		Iterator<NodoRS> it;
		NodoRS nodo;
		
		it = this.listaNodo.iterator();
		int cont = 0;
		
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getFlag() == 0) {
				cont++;
			}
		}
		return cont;
	}

	/**
	 * Convierte los nodos congelados en nodos diponibles.
	 * @return 
	 */
	private void hacerDisponible() {
		
		Iterator<NodoRS> it;
		NodoRS nodo;
		
		it = this.listaNodo.iterator();
		
		while (it.hasNext()) {
			nodo = it.next();
			nodo.setFlag(0);
		}
	}
	
	/**
	 * 
	 * @return
	 * 		devuelve true si todos los nodos están congelados.
	 * False si no lo están.
	 */
	private boolean nodoRSCongelado() {
		
		int cantCongelada = 0;
		Iterator<NodoRS> it = this.listaNodo.iterator();
		NodoRS nodo;
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getFlag() == 1) {
				cantCongelada++;	
			}
		}
		return (cantCongelada == this.cantidadNodos);
	}

	/**
	 * Verifica si la lista tiene más elementos.
	 * @return True si no tiene más elementos. False si aún tiene.
	 */
	private boolean nodoRSVacio() {
		return this.listaNodo.isEmpty();
	}

	/**
	 * Busca el menor de los nodos.
	 * @return el menor de los nodos.
	 */
	private NodoRS obtenerMenorNodo() {
		
		int comp;
		Iterator<NodoRS> i, j;
		NodoRS nodoI, nodoJ;
		j = this.listaNodo.iterator();
		i = this.listaNodo.iterator();
		nodoJ = j.next();
		while (i.hasNext()) {
			nodoI = i.next();
			comp = nodoI.comparar(nodoJ);
			if (comp == -1) {
				nodoJ = nodoI;
			}
		}
		return nodoJ;
	}

	/**
	 * Metodo que se encarga de ordenar lo elementos leidos y
	 * generar las particiones.
	 * @return cantidad de particiones.
	 */
	public final int ordenar() {
		int flag, desc, comp;
		long nRegistros, cont = 0;
		NodoRS nodo = new NodoRS();
		NodoRS nodo1 = new NodoRS();
		NodoRS menor;
		byte[] dataNodo = new byte[nodo.getTamanio()];
		Integer particionNumero = new Integer(1);
		RandomAccessFile p;
		
		//1-calculo tamaño de lista de objetos a ordenar
		this.cantidadNodos = this.memoria / nodo.getTamanio();
		
		//2-abro el archivo
		try {
			RandomAccessFile archivoTrabajo = new RandomAccessFile(arch,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			if (archivoTrabajo != null) {
				nRegistros = archivoTrabajo.length() / nodo.getTamanio();
				archivoTrabajo.seek(0);
				//3-cargo por primera vez el buffer(Lista de NodoRS)
				for (int i = 0; i < this.cantidadNodos && i < nRegistros; i++) {
					archivoTrabajo.read(dataNodo, 0, dataNodo.length);
					nodo.setBytes(dataNodo);
					this.listaNodo.add(nodo);
					nodo = new NodoRS();
					cont++;
				}
				
				while (!nodoRSCongelado() && !nodoRSVacio()) {
					//4-arranca el bucle (mientras el buffer no este vacio ni congelado)
					//4.1-creo Pi
					String nombreParticion = new String(this.arch + "part" + particionNumero.toString()); 
					p = new RandomAccessFile(nombreParticion,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
					flag = 0;
					while (!nodoRSCongelado() && !nodoRSVacio() && flag == 0) {
						
						//4.2-tomo el menor del buffer con flag en disponible
						menor = obtenerMenorNodo();
						//4.3-lo grabo en Pi y lo elimino de listaNodo
						dataNodo = menor.getBytes();
						p.write(dataNodo, 0, dataNodo.length);
						this.listaNodo.remove(menor);
						//4.4-leo el siguiente del archivo f
						if (cont < nRegistros) {
							nodo1 = new NodoRS();
							archivoTrabajo.read(dataNodo, 0, dataNodo.length);
							nodo1.setBytes(dataNodo); //idT,idD,fdt
							cont++;
							//4.4.1-si reg(f) <= grabado -> congelado
							comp = nodo1.comparar(menor);
							if (comp == -1 || comp == 0) {
								nodo1.setFlag(1);
								this.listaNodo.add(nodo1);
							}
							//4.4.2-si reg(f) > grabado -> disponible
							if (comp == 1) {
								nodo1.setFlag(0);
								this.listaNodo.add(nodo1);
							}
						} else {
							flag = 1;
							desc = cuantosDisponibles();
							//guardo los disponibles que quedaron en la particion 
							for (int j = 0; j < desc; j++) {
								menor = obtenerMenorNodo();
								//4.3-lo grabo en Pi y lo elimino de listaNodo
								dataNodo = menor.getBytes();
								p.write(dataNodo, 0, dataNodo.length);
								this.listaNodo.remove(menor);
							}
						}
					}
					//5-cierro Pi y registro que la guarde
					p.close();
					listaParticiones.add(nombreParticion);
					//6-pongo los flag en disponible y vuelvo a empezar
					hacerDisponible();
					particionNumero++;
				}
				archivoTrabajo.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return this.listaParticiones.size();
	}

	/**
	 * @return La lista
	 */
	public final ArrayList<String> getListaParticiones() {
		return this.listaParticiones;
	}
	
	/**
	 * @return La lista
	 */
	public final String getArch() {
		return this.arch;
	}
}