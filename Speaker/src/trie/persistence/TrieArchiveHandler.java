package trie.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.archivos.ArchivoBloques;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TrieArchiveHandler {

	private ArchivoBloques miArchivo;
	
	private int tamanioBloque=512; //en bytes, segun mis calculos entran unos 30 o 35 punteros por nodo (promedio)
	
	private String rutaArch = "/home/zeke/Escritorio/trie.dat";
	
	
	public TrieArchiveHandler() {
		super();
		miArchivo = new ArchivoBloques(tamanioBloque);
		try {
			miArchivo.abrir(rutaArch, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			System.out.println("no se puede abrir el archivo de trie");
			e.printStackTrace();
		}
	}
	
	
	public void closeArchivo(){
		try {
			miArchivo.cerrar();
		} catch (IOException e) {
			System.out.println("no se ha podido cerrar el archivo de trie");
			e.printStackTrace();
		}
		
	}
	
	
	public void guardarNodo(TrieNodeRegistry registroNodo){
		
		//genero un array auxiliar
		byte[] aux;
		//lo cargo con el contenido del nodo serializado
		aux = registroNodo.getPaqueteSerializado().serializar();
		
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

	
	
	
	
	
	
}
