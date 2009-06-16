package ar.com.datos.grupo5.trie.persistence;


import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.archivos.ArchivoBloques;

public class TrieArchiveHandler {

	private ArchivoBloques miArchivo;
	
	private int tamanioBloque = Constantes.TRIE_BLOCK_SIZE; //en bytes, segun mis calculos entran unos 30 o 35 punteros por nodo (promedio)
	
	private String rutaArch = Constantes.ARCHIVO_TRIE;
	
	private AdministrationRegistry registroAdministrativo;
	
	private static final Logger LOG = Logger.getLogger(TrieArchiveHandler.class);
	
	
	public TrieArchiveHandler() {
		//instancio el archivo de bloques
		miArchivo = new ArchivoBloques(tamanioBloque);
		try {
			miArchivo.abrir(rutaArch, Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			//si esta vacio inicializo
			if (miArchivo.estaVacio()){
				this.inicializarArchivo();
			}else{
			//sino leo el registro administrativo
			Contenedor cont = Contenedor.rehidratar(miArchivo.leerBloque(0));
			this.registroAdministrativo = new AdministrationRegistry(cont);
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void inicializarArchivo(){
		//inicializa el reg administrativo con cero
		this.registroAdministrativo = new AdministrationRegistry(); 
		try {
			//lo escribo a disco
			miArchivo.escribirBloque(this.registroAdministrativo.getPaqueteSerializado().serializar(), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void closeArchivo(){
		try {
			this.guardarRegistroAdministrativo();
			miArchivo.cerrar();
			
		} catch (IOException e) {
			LOG.error("no se ha podido cerrar el archivo de trie",e);
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
			
			LOG.error("no se ha podido guardar el nodo en el trie por ser demasiado grande");

		}else{ //si no excede intento guardarlo
	
		try {
			miArchivo.escribirBloqueSalteado(aux, (int)registroNodo.getNroNodo());
		} catch (IOException e) {
			LOG.error("no se ha podido guardar el nodo en el trie",e);
			e.printStackTrace();
		}
		}
	}
	
	public int BloqueParaNuevoNodo(){
		int aDevolver =(int) this.registroAdministrativo.sumarUnNodo();
		this.guardarRegistroAdministrativo();
		return aDevolver;
	}
	
	public int cantNodosExistentes(){
		return(int)this.registroAdministrativo.getCantidadNodosExistentes();
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
			LOG.error("no se ha podido recuperar el nodo del archivo de trie",e);
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
			
			//verifico que lo que levante no tenga sentido porque sino palma todo
			if (aux.length < Constantes.SIZE_OF_INT){
			LOG.error("TRIE: se ha obtenido informacion incorrecta del registro Administrativo " +
					"administrativo, probable corrupcion del archivo");
			}else{
			//rehidrato un contenedor, él se encarga de leer los datos necesarios
			//y descartar aquellos bytes de mas
			Contenedor cont = Contenedor.rehidratar(aux);
			//con el contenedor inicializo el registro administrativo
			 this.registroAdministrativo = new AdministrationRegistry(cont);
			}
		} catch (IOException e) {
			LOG.error("no se ha podido recuperar el nodo del archivo de trie", e);
			e.printStackTrace();
		}
		
	}
	
	
	public void guardarRegistroAdministrativo(){
		
		//genero un array auxiliar
		byte[] aux;
		//lo cargo con el contenido del nodo serializado
		aux = this.registroAdministrativo.getPaqueteSerializado().serializar();
		try {
			miArchivo.escribirBloqueSalteado(aux, 0);
		} catch (IOException e) {
			LOG.fatal("no se ha podido guardar el registro administrativo del trie", e);
			e.printStackTrace();
		}
		}

		
	}
