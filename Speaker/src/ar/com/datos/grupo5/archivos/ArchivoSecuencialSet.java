package ar.com.datos.grupo5.archivos;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.registros.RegistroAdmSecSet;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.trie.persistence.Contenedor;

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
	

	public void bloquesActualizados(Collection<Nodo>listaNodosActualizados, String nuevaPalabra, long IdTermino){

		
		//genero un registro con la nueva palabra a agregar
		RegistroFTRS registroNuevaPalabra = new RegistroFTRS();
		registroNuevaPalabra.setIdTermino(IdTermino);
		
		ClaveFrontCoding cf = new ClaveFrontCoding();
		cf.setTermino(nuevaPalabra);
		
		registroNuevaPalabra.setClave(cf);
		
		//---------------------
		
		
		//genero una lista para almacenar todos los elementos a reacomodar
		ArrayList<RegistroNodo> listaElementosEnNodos = new ArrayList<RegistroNodo>();
		
		ArrayList<RegistroFTRS> listaRegistrosEnBloques = new ArrayList<RegistroFTRS>();
		
		//agrego el registro de la palabra recien ingresada
		listaRegistrosEnBloques.add(registroNuevaPalabra);
		
		//obtengo un iterador sobre la lista de nodos actualizados
		Iterator<Nodo> it = listaNodosActualizados.iterator();
		
		while (it.hasNext()){
			
			//obtengo el nodo actual
			Nodo actual = it.next();
			
			//obtengo la lista del nodo actual
			ArrayList<RegistroNodo> listaDelNodo =actual.getRegistros();
			
			//debo buscar en cada nodo de los modificados, cual es el que contiene el registroAgregado
			listaDelNodo.indexOf(registroNuevaPalabra);
			
			
			//agrego todos los elementos que se han modificado en los nodos
			listaElementosEnNodos.addAll(listaDelNodo);
			
			//agrego a la lista todos los elementos levantados de los bloques en disco
			listaRegistrosEnBloques.addAll(this.obtenerListaElementosBloque(actual.getPunteroBloque()));
			
		}
		
		listaElementosEnNodos.indexOf(registroNuevaPalabra);
		
		
	}
	
	
	public void escribirBloque (BloqueFTRS bloque, int numBloque){
		
		try {
			this.miArchivo.escribirBloqueSalteado(bloque.getBytes(), numBloque);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	public BloqueFTRS leerBloque (int numeroBloque){
		
		try {
			
			Contenedor cont = Contenedor.rehidratar(this.miArchivo.leerBloque(numeroBloque));
			return new BloqueFTRS (cont.getDato());
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	private ArrayList<RegistroFTRS> obtenerListaElementosBloque(int numBloque){
		
		BloqueFTRS miBloque = this.leerBloque(numBloque);
		
		return (ArrayList<RegistroFTRS>) miBloque.getListaTerminosFrontCodeados();
		
		
	}
	
	
	
	
}
