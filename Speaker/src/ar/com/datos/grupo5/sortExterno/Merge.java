package ar.com.datos.grupo5.sortExterno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:45 p.m.
 */
public class Merge {
	/**
	 * Nombre del archivo final. 
	 */
	private String arch;
	
	/**
	 * Lista que contiene los nombres de las particiones y 
	 * la cantidad de registros de cada una.
	 */
	private ArrayList<NodoParticion> listaNodoParticion;
	
	/**
	 * Lista de particiones disponibles.
	 */
	private ArrayList<String> listaParticiones;
	
	/**
	 * Nombre de la particion a unir.
	 */
	private String nombreParticion1;
	
	/**
	 * Nombre de la particion a unir.
	 */
	private String nombreParticion2;

	/**
	 * Logger para la clase.
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Merge.class);
	
	/**
	 * Contructor default de la clase.
	 */
	public Merge(){
		listaNodoParticion = new ArrayList<NodoParticion>();
		listaParticiones = new ArrayList<String>();
	}

	public void finalize() throws Throwable {

	}

	/**
	 * Constructor de la clase.
	 * @param lP Lista de particiones a unirse.
	 * @param archExt Nombre de archivo final.
	 */
	public Merge(final ArrayList<String> lP, final String archExt) {
		this.listaParticiones = lP;
		this.arch = archExt;
		listaNodoParticion = new ArrayList<NodoParticion>();
	}
	
	/**
	 * Genera y controla todo el procedimiento de merge 
	 * de las particiones disponibles.
	 */
	public final void ejecutarMerge() {
		generarCantidadesRegistrosPorArchivo();
		int cant = obtenerCantidadParticiones();
		while (cant > 1) {
			obtenerDosPaticionesMenores();
			unirDosPaticiones();
			cant = obtenerCantidadParticiones();
		}
		//renombro archivo original
		if (cant == 1) {

			File fileViejo = new File(arch);
			File fileNuevo = new File(this.listaParticiones.get(0));
			fileViejo.delete();
			fileNuevo.renameTo(fileViejo);
			
		}
	}

	/**
	 * Genera una lista con los pares Particion - Cantidad de registros.
	 */
	private void generarCantidadesRegistrosPorArchivo() {
		Iterator<String> it;
		long nRegistros = 0;
		RandomAccessFile file;
		NodoParticion nodoParticion;
		NodoRS nodoRS = new NodoRS();
		it = this.listaParticiones.iterator();
		String nodo;
		while (it.hasNext()) {
			nodo = it.next();
			//para cada particion calculo la cantidad de registros que tiene
			try {
				file = new RandomAccessFile(nodo,
						Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				
				nRegistros = file.length() / nodoRS.getTamanio();
				file.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nodoParticion = new NodoParticion(nodo, nRegistros);
			this.listaNodoParticion.add(nodoParticion);
		}
	}
	
	/**
	 * Permite obtener la cantidad de particiones 
	 * que quedaron si merge.
	 * @return Cantidad de particiones.
	 */
	private int obtenerCantidadParticiones() {
		return this.listaParticiones.size();
	}

	/**
	 * Obtiene las dos particiones mas chicas.
	 */
	private void obtenerDosPaticionesMenores() {
		
		Iterator<NodoParticion> it;
		Long  cant; 
		int salida;
		NodoParticion nodoParticion;
		
		for (int i = 0; i < 2; i++) {
			//busco el menor con flag == 0
			it = listaNodoParticion.iterator();
			cant = 100032767L;
			while (it.hasNext()) {
				nodoParticion = it.next();
				if (nodoParticion.getFlag() == 0 && nodoParticion.getNRegistros() <= cant) {
					cant = nodoParticion.getNRegistros();
				}
			}

			it = listaNodoParticion.iterator();
			salida = 0;
			while (it.hasNext() && salida == 0) {
				
				nodoParticion = it.next();
				if (nodoParticion.getNRegistros() == cant && nodoParticion.getFlag() == 0) {
					if (i == 0) {
						nombreParticion1 = new String(nodoParticion.getParticion());
					} else {
						nombreParticion2 = new String(nodoParticion.getParticion());
					}
					nodoParticion.setFlag(1);
					salida = 1;
				}
			}
		}
	}

	/**
	 * Une dos porticiones.
	 */
	private void unirDosPaticiones() {
		
		int salida = 0, i1 = 0, i2 = 0, comp;
		int finArchivo1 = 0;
		int finArchivo2 = 0;
		long cantidadRegistrosfP1, cantidadRegistrosfP2;
		NodoRS nodo1 = new NodoRS();
		NodoRS nodo2 = new NodoRS();
		String nombreNuevaParticion;
		byte[] dataNodo = new byte[nodo1.getTamanio()];
		
		
		try {
			RandomAccessFile fParticion1 = new RandomAccessFile(nombreParticion1,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			RandomAccessFile fParticion2 = new RandomAccessFile(nombreParticion2,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		
		
			nombreNuevaParticion = new String(nombreParticion1 + "P");
		
			RandomAccessFile fParticion3 = new RandomAccessFile(nombreNuevaParticion,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		
			if (fParticion1 != null && fParticion2 != null && fParticion3 != null) {
			
			  cantidadRegistrosfP1 = fParticion1.length() / nodo1.getTamanio();
			  cantidadRegistrosfP2 = fParticion2.length() / nodo1.getTamanio();
				
				fParticion1.read(dataNodo, 0, dataNodo.length);
				nodo1.setBytes(dataNodo);
				
				fParticion2.read(dataNodo, 0, dataNodo.length);
				nodo2.setBytes(dataNodo);
		
				while (salida == 0) {
					comp = nodo1.comparar(nodo2);
					switch(comp) {
					case 0:
							if( nodo1.getIdDocumento() == 10000000L && nodo2.getIdDocumento() == 10000000L) {
								salida = 1;
							} else {
								if (i1 < cantidadRegistrosfP1) {
									dataNodo = nodo1.getBytes();
									fParticion3.write(dataNodo, 0, dataNodo.length);
									finArchivo1 = fParticion1.read(dataNodo, 0, dataNodo.length);
									nodo1 = new NodoRS();
									nodo1.setBytes(dataNodo);
									i1++;
								} else {
									nodo1.setIdDocumento(10000000L);
								}
							}
						break;
					case 1:
							if (i1 < cantidadRegistrosfP1) {
								dataNodo = nodo1.getBytes();
								fParticion3.write(dataNodo, 0, dataNodo.length);
								finArchivo1 = fParticion1.read(dataNodo, 0, dataNodo.length);
								nodo1 = new NodoRS();
								nodo1.setBytes(dataNodo);
								i1++;
							} else {
								nodo1.setIdDocumento(10000000L);
							}
						break;
					case -1:
							if (i2 < cantidadRegistrosfP2) {
								dataNodo = nodo2.getBytes();
								fParticion3.write(dataNodo, 0, dataNodo.length);
								finArchivo2 = fParticion2.read(dataNodo, 0, dataNodo.length);
								nodo2 = new NodoRS();
								nodo2.setBytes(dataNodo);
								i2++;
							} else {
								nodo2.setIdDocumento(10000000L);
							}
						break;
						default:
							break;
					}
					
					//Simulo los end of file
					if (finArchivo1 == -1 && finArchivo2 == -1) {
						salida = 1;
					}
				}
			fParticion1.close();
			fParticion2.close();
			fParticion3.close();
			}
			
		//actualizo la lista de nodos con las particiones
		fParticion1 = new RandomAccessFile(nombreNuevaParticion,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		
		long nRegistros = fParticion1.length() / nodo1.getTamanio();
		
		fParticion1.close();
		
		Iterator<NodoParticion> it;
		it = this.listaNodoParticion.iterator();
		
		NodoParticion nodoParticion;
		salida = 0;
		
		while (it.hasNext() && salida == 0) {
			nodoParticion = it.next();
			if (nodoParticion.getParticion().compareTo(nombreParticion1) == 0) {
				nodoParticion.setNRegistros(nRegistros);
				nodoParticion.setFlag(0);
				salida = 1;
			}
		}
		
		it = listaNodoParticion.iterator();
		salida = 0;
		
		while (it.hasNext() && salida == 0) {
			nodoParticion = it.next();
			if (nodoParticion.getParticion().compareTo(nombreParticion2) == 0) {
				this.listaNodoParticion.remove(nodoParticion);
				salida = 1;
			}
		}
		
		//Borro los archivos de las particiones viejas
		File p1 = new File(nombreParticion1);
		p1.delete();

		File p2 = new File(nombreParticion2);
		p2.delete();
		
		//Renombro el archivo con la nueva particion 
		//como si fuere la particion 1
		File p3 = new File(nombreNuevaParticion);
		p3.renameTo(p1);

		Iterator<String> itString;
		itString = listaParticiones.iterator();
		salida = 0;
		String nodo;
		while (itString.hasNext() && salida == 0) {
			nodo = itString.next();
			if (nodo.compareTo(nombreParticion2) == 0) {
				this.listaParticiones.remove(nodo);
				salida = 1;
			}
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}