package ar.com.datos.grupo5.archivos;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.registros.RegistroAdmSecSet;

public class ArchivoSecuencialSet {

	private ArchivoBloques miArchivo;
	
	private RegistroAdmSecSet regAdm;
	


	public ArchivoSecuencialSet(){
		
		miArchivo = new ArchivoBloques(Constantes.SIZE_OF_SECUENCIAL_SET_BLOCK);
		try {
			miArchivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR_SECUENCIAL_SET, 
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			System.out.println("no se ha podido abrir el archivo de secuencialsetBstar");
			e.printStackTrace();
		}
		
		//instancio el bloque administrativo
		try {
			
			if (miArchivo.file.length() ==0){
				this.regAdm = new RegistroAdmSecSet();
			}else{
			
			byte[] aux = miArchivo.leerBloque(0);
			this.regAdm = new RegistroAdmSecSet(aux);
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	public long getProxBloqueLibre(){
		
		return this.regAdm.reservarNuevoBloque();
	}
	
	
	
	public void cerrar(){
		
		//guardo el registro administrativo
		try {
			this.miArchivo.escribirBloque(this.regAdm.getBytes(), 0);
			//cierro el archivo
			this.miArchivo.cerrar();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void bloquesActualizados(Collection<Nodo>listaNodosActualizados){
		
		Iterator<Nodo> it = listaNodosActualizados.iterator();
		
		
		while (it.hasNext()){
			
			
			Nodo actual = it.next();
			
			actual.getRegistros();
			
			
		}
		
		
		
	}
	
	
	
	
}
