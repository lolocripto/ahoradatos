package ar.com.datos.grupo5.trie.persistence;


import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.archivos.ArchivoBloques;

public class TrieArchiveHandler {

	private ArchivoBloques miArchivo;
	
	private int tamanioBloque = Constantes.TRIE_BLOCK_SIZE; //en bytes, segun mis calculos entran unos 30 o 35 punteros por nodo (promedio)
	
	private String rutaArch = Constantes.ARCHIVO_TRIE;
	
	
	public TrieArchiveHandler() {
		super();
		miArchivo = new ArchivoBloques(tamanioBloque);
		try {
			miArchivo.abrir(rutaArch, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			if (miArchivo.estaVacio()){
				this.inicializarArchivo();
			}
			
			Contenedor cont = Contenedor.rehidratar(miArchivo.leerBloque(0));
			AdministrationRegistry.hidratar(cont);
			
		} catch (Exception e) {
			System.out.println("no se puede abrir el archivo de trie");
			e.printStackTrace();
		}
	}
	
	
	public void closeArchivo(){
		try {
			
			miArchivo.escribirBloque(AdministrationRegistry.getPaqueteSerializado().serializar(), 0);
			miArchivo.cerrar();
			
		} catch (IOException e) {
			System.out.println("no se ha podido cerrar el archivo de trie");
			e.printStackTrace();
		}
		
	}
	
	
	private void inicializarArchivo(){
		
		AdministrationRegistry.setCantNodos(0);
		
		try {
			miArchivo.escribirBloque(AdministrationRegistry.getPaqueteSerializado().serializar(), 0);
		} catch (IOException e) {
			System.out.println("no se ha podido inicializar el archivo de trie");
			e.printStackTrace();
		}
		
	}
	
	public void guardarNodo(TrieNodeRegistry registroNodo){
		
		System.out.println("se guarda el nodo numero: "+registroNodo.getNroNodo());
		
		//genero un array auxiliar
		byte[] aux;
		//lo cargo con el contenido del nodo serializado
		aux = registroNodo.getPaqueteSerializado().serializar();
		
		System.out.println("de tamaño: "+aux.length);
		
		//verifico que no exceda el limite del bloque
		if (this.tamanioBloque < aux.length){
			
			System.out.println("no se ha podido guardar el nodo en el trie por ser demasiado grande");

		}else{ //si no excede intento guardarlo
	
		try {
			miArchivo.escribirBloqueSalteado(aux, (int)registroNodo.getNroNodo());
		} catch (IOException e) {
			System.out.println("no se ha podido guardar el nodo en el trie");
			e.printStackTrace();
		}
		}
	}
	
	
	
	public TrieNodeRegistry recuperarNodo (long numero){
		
		try {
			//genero un array auxiliar
			byte [] aux;
			
			//lo cargo con el contenido del bloque
			aux = miArchivo.leerBloque((int)numero);
			
			//rehidrato un contenedor, él se encarga de leer los datos necesarios
			//y descartar aquellos bytes de mas
			Contenedor cont = Contenedor.rehidratar(aux);
			
			//con la seccion de datos del contenedor, genero un nuevo registro de nodo
			TrieNodeRegistry tnr = new TrieNodeRegistry(cont.getDato());
			return tnr;
			
		} catch (IOException e) {
			System.out.println("no se ha podido recuperar el nodo del archivo de trie");
			e.printStackTrace();
		}
		return null;
	}
	
	public void iniciarRegistroAdministrativo(){
		
		try {
			//genero un array auxiliar
			byte [] aux;
			
			//lo cargo con el contenido del bloque
			aux = miArchivo.leerBloque(0);
			
			//rehidrato un contenedor, él se encarga de leer los datos necesarios
			//y descartar aquellos bytes de mas
			Contenedor cont = Contenedor.rehidratar(aux);
			
			//con el contenedor inicializo el registro administrativo
			AdministrationRegistry.hidratar(cont);
			
		} catch (IOException e) {
			System.out.println("no se ha podido recuperar el nodo del archivo de trie");
			e.printStackTrace();
		}
		
	}
	
	
	public void guardarRegistroAdministrativo(){
		
		
		//genero un array auxiliar
		byte[] aux;
		//lo cargo con el contenido del nodo serializado
		aux = AdministrationRegistry.getPaqueteSerializado().serializar();
		
		//verifico que no exceda el limite del bloque
		if (this.tamanioBloque < aux.length){
			
			System.out.println("no se ha podido guardar el nodo en el trie por ser demasiado grande");

		}else{ //si no excede intento guardarlo
	
		try {
			miArchivo.escribirBloqueSalteado(aux, 0);
		} catch (IOException e) {
			System.out.println("no se ha podido guardar el nodo en el trie");
			e.printStackTrace();
		}
		}
		
	}

	
	
	
	
	
	
}
