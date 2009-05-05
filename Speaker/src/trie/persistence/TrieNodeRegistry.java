package trie.persistence;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import trie.core.INodo;
import trie.core.Nodo;
import trie.core.PunteroSonido;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.utils.Conversiones;

public class TrieNodeRegistry {


		private Long nroNodo; //el numero del nodo que se esta almacenando
		
		private String contenido; //letra del nodo actual
		
		/**
		 * puntero a sonido, si la letra actual es la ultima de una palabra,
		 * debiera haber un puntero hacia la ubicación de su sonido asociado.
		 */
		private Long puntero;
		
		/**
		 * lista con los hijos y punteros a ellos. si fuese un nodo hoja esta lista no se usa					
		 */
		private ArrayList<ParStringPuntero> listaDepunteros;
		

		public TrieNodeRegistry(INodo unNodo) {
			this.nroNodo = unNodo.getNumeroNodo();
			this.contenido = unNodo.getContenido();
			
		}
		
		public TrieNodeRegistry (byte[] nodoSerializado){
			
			
			
			
		}
		
		
		public byte[] serializar(){
		    //genero un contenedor para este registro
			Contenedor container = new Contenedor();
			
			//genero mis buffers para cargar bytes
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			
			
			try {
				dos.writeLong(this.getNroNodo());
				dos.write(this.serializarLista());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			container.setDato(bos.toByteArray());
			return container.serializar();
		}
		

		
		private byte[] serializarLista(){
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			
			/*
			 * cargo el contenido del nodo actual en un parstringpuntero, de esa forma
			 * se pueden tratar por igual, las referencias a los hijos y  punteros a esos nodos
			 * como el string del nodo actual y su puntero a sonido
			 */
			ParStringPuntero psp = new ParStringPuntero(this.puntero,this.contenido);
			psp.getPaqueteSerializado();
			
			Iterator<ParStringPuntero> it = listaDepunteros.iterator();
			try {
				dos.write(psp.getPaqueteSerializado().serializar());
				
				while (it.hasNext()) { //itero sobre toda la coleccion de punteros
					dos.write(it.next().getPaqueteSerializado().serializar()); // y los serializo en un contenedor
				}
				
			} catch (IOException e) {
	
				e.printStackTrace();
			}
			
			return bos.toByteArray();
			
		}
		
	
		public void setNroNodo(long nroBloque) {
			this.nroNodo = nroBloque;
		}

		public long getNroNodo() {
			return nroNodo;
		}


}
