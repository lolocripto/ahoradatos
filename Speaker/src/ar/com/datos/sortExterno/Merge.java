package ar.com.datos.sortExterno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:45 p.m.
 */
public class Merge {

	private String arch;
	private List<NodoParticion> listaNodoParticion;
	private List<String> listaParticiones;
	private int cantidadNodos;

	public Merge(){
		listaNodoParticion = new ArrayList<NodoParticion>();
		listaParticiones = new ArrayList<String>();
	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param LP
	 * @param tam
	 * @param archExt
	 */
	public Merge(final List<String> LP, final int tam, final String archExt){
		this.listaParticiones = LP;
		this.cantidadNodos = tam;
		this.arch = archExt;
	}

	public int ejecutarMerge(){
		generarCantidadesRegistrosPorArchivo();
		int cant = obtenerCantidadParticiones();
		String P1 = new String();
		String P2 = new String();
		while (cant > 1)
		{
			obtenerDosPaticionesMenores(P1,P2);
			unirDosPaticiones(P1,P2);
			cant = obtenerCantidadParticiones();
		}
		//renombro archivo original
		if (cant==1)
		{
			//TODO: Danger
			File file1 = new File(arch,Constantes.ABRIR_PARA_LECTURA);
			File file = new File(P1,Constantes.ABRIR_PARA_LECTURA);
			file.renameTo(file1);
			file1.delete();
		}
		return 0;
	}

	private void generarCantidadesRegistrosPorArchivo() {
		Iterator<String> it;
		long nRegistros = 0;
		RandomAccessFile file;
		NodoParticion nodoParticion;
		NodoRS nodoRS = new NodoRS();
		it = this.listaParticiones.iterator();
		String nodo;
		while (it.hasNext()){
			nodo = it.next();
			//para cada particion calculo la cantidad de registros que tiene
			try {
				file = new RandomAccessFile(nodo,
						Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				
				nRegistros = file.length() / nodoRS.getTamanio();
				file.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			nodoParticion = new NodoParticion(nodo, nRegistros);
			this.listaNodoParticion.add(nodoParticion);
		}
	}

	public int listar() {
		/*
		list<char*>::iterator it;
		int idt,idd,fdt,i;
		long nRegistros;
		printf("Merge: \n");
		for(it=listaParticiones.begin();it!=listaParticiones.end();it++)
		{
			FILE* p = fopen((char*)(*it),"rb");
			fseek(p,0,SEEK_END);
			nRegistros = ftell(p)/(3*sizeof(int));
			fseek(p,0,SEEK_SET);
			i=0;
			while(i<nRegistros)
			{	
				fread(&idt,1,sizeof(int),p);
			 	fread(&idd,1,sizeof(int),p);
			 	fread(&fdt,1,sizeof(int),p);
				printf("%d ",idt);
				printf("%d ",idd);
				printf("%d | ",fdt);
				i++;
			}
			printf("\n");
			fclose(p);
		}*/
		return 0;
	}

	private int obtenerCantidadParticiones() {
		return this.listaParticiones.size();
	}

	/**
	 * 
	 * @param P1
	 * @param P2
	 */
	private void obtenerDosPaticionesMenores(String P1, String P2) {
		
		Iterator<NodoParticion> it;
		Long  cant; 
		int salida;
		NodoParticion nodoParticion;
		
		for(int i=0;i<2;i++) {
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
			salida=0;
			while(it.hasNext() && salida == 0) {
				
				nodoParticion = it.next();
				if (nodoParticion.getNRegistros() == cant && nodoParticion.getFlag() == 0) {
					if ( i == 0 ) {
						P1 = nodoParticion.getParticion();
					} else {
						P2 = nodoParticion.getParticion();
					}
					nodoParticion.setFlag(1);
					salida = 1;
				}
			}
		}
	}

	/**
	 * 
	 * @param P1
	 * @param P2
	 */
	private int unirDosPaticiones(String P1, String P2) {
		
		int salida=0, i1=0, i2=0, comp;
		int finArchivo1 = 0;
		int finArchivo2 = 0;
		long cantidadRegistrosfP1,cantidadRegistrosfP2;
		NodoRS nodo1 = new NodoRS();
		NodoRS nodo2 = new NodoRS();
		String nombreNuevaParticion;
		byte[] dataNodo = new byte[nodo1.getTamanio()];
		
		
		try {
			RandomAccessFile fParticion1 = new RandomAccessFile(P1,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			RandomAccessFile fParticion2 = new RandomAccessFile(P2,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		
		
			nombreNuevaParticion = new String(P1 + "P");
		
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
					if (comp == -1 || comp == 0) {
						if (i1 < cantidadRegistrosfP1){
							dataNodo = nodo1.getBytes();
							fParticion3.write(dataNodo, 0 ,dataNodo.length);
							finArchivo1 = fParticion1.read(dataNodo, 0, dataNodo.length);
							nodo1 = new NodoRS();
							nodo1.setBytes(dataNodo);
							i1++;
						} else {
							//TODO: Algo raro en el codigo anterior.
						}
					} else {
						if (i2 < cantidadRegistrosfP2) {
							dataNodo = nodo2.getBytes();
							fParticion3.write(dataNodo, 0 ,dataNodo.length);
							finArchivo2 = fParticion2.read(dataNodo, 0, dataNodo.length);
							nodo2 = new NodoRS();
							nodo2.setBytes(dataNodo);
							i2++;
						} else {
							//TODO: algo raro en el codigo anterior
						}
					}
					//Simulo los end of file
					if(finArchivo1 == -1 && finArchivo2 == -1) {
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
		
		while(it.hasNext() && salida == 0)
		{
			nodoParticion = it.next();
			if (nodoParticion.getParticion().compareTo(P1) == 0) {
				nodoParticion.setNRegistros(nRegistros);
				nodoParticion.setFlag(0);
				salida = 1;
			}
		}
		
		it = listaNodoParticion.iterator();
		salida = 0;
		
		while(it.hasNext() && salida == 0)
		{
			nodoParticion = it.next();
			if (nodoParticion.getParticion().compareTo(P2) == 0) {
				this.listaNodoParticion.remove(nodoParticion);
				salida = 1;
			}
		}
		
		//Borro los archivos de las particiones viejas
		File p1 = new File(P1,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		p1.delete();
		File p2 = new File(P2,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		p2.delete();
		//Renombro el archivo con la nueva particion como si fuere la particion 1
		File p3 = new File(nombreNuevaParticion,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		p3.renameTo(p1);
		
		Iterator<String> itString;
		itString = listaParticiones.iterator();
		salida = 0;
		String nodo;
		while(itString.hasNext() && salida == 0)
		{
			nodo = itString.next();
			if (nodo.compareTo(P2) == 0) {
				this.listaParticiones.remove(nodo);
				salida = 1;
			}
		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}