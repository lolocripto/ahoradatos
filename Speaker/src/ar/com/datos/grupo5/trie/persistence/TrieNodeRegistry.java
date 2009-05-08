package ar.com.datos.grupo5.trie.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.trie.core.INodo;
import ar.com.datos.grupo5.trie.core.Nodo;
import ar.com.datos.grupo5.trie.core.PunteroSonido;
import ar.com.datos.grupo5.utils.Conversiones;

public class TrieNodeRegistry {


		private Long nroNodo; //el numero del nodo que se esta almacenando
		
		private String contenido; //letra del nodo actual
		
		private boolean isDirty;
		
		private Nodo nodoReferenciado;

		/**
		 * puntero a sonido, si la letra actual es la ultima de una palabra,
		 * debiera haber un puntero hacia la ubicación de su sonido asociado.
		 */
		private Long puntero;
		
		public Long getPuntero() {
			return puntero;
		}


		public void setPuntero(Long puntero) {
			this.puntero = puntero;
		}

		/**
		 * lista con los hijos y punteros a ellos. si fuese un nodo hoja esta lista no se usa					
		 */
		public ArrayList<ParStringPuntero> listaDepunteros;
		

		public TrieNodeRegistry(INodo unNodo) {
			this.nroNodo = unNodo.getNumeroNodo();
			this.contenido = unNodo.getContenido();
			if (unNodo.getPuntero()!=null){
			this.puntero = unNodo.getPuntero().getOffset();
			}else{
				this.puntero =null;
			}
			this.nodoReferenciado = (Nodo)unNodo;
			unNodo.setRegistroAsociado(this);
			
			this.isDirty = true;
			
			listaDepunteros = new ArrayList<ParStringPuntero>();
			
		}
		
		
		public boolean isDirty() {
			return isDirty;
		}

		public void setDirty(boolean isDirty) {
			this.isDirty = isDirty;
		}

		public String getContenido() {
			return contenido;
		}

		public void setContenido(String contenido) {
			this.contenido = contenido;
		}
		
		
		
		
		public TrieNodeRegistry (byte[] registroSerializado){
			
			this.isDirty =false;
			//inicalizo la lista
			listaDepunteros = new ArrayList<ParStringPuntero>();
			
			//genero mis streams para trabajar con el array de bytes
			ByteArrayInputStream bis = new ByteArrayInputStream(registroSerializado);  
			DataInputStream dis = new DataInputStream(bis);
			
			try {
				
				//genero la longitud del dato que debo leer
				int longitud = registroSerializado.length - Constantes.SIZE_OF_LONG;
				
				//obtengo el primer valor que es el long que hace referencia al numero de nodo
				this.nroNodo = dis.readLong();
				
				//creo un array auxiliar
				byte[] aux = new byte[longitud];
				//coloco el resto de los bytes en el array
				dis.read(aux);
				
				//genero un contenedor nuevo, este tiene dentro otros varios contenedores
				Contenedor contenedorDeLista;
				contenedorDeLista = Contenedor.rehidratar(aux); //lo rehidrato
				
				//genero la lista de contenedores de la que hablaba antes
				ArrayList<Contenedor> lista;
				
				//dentro del area de datos del contenedorDeLista, se encuentra la lista serializada, se la paso
				// al metodo idoneo para recuperarla
				lista = Contenedor.rehidratarLista(contenedorDeLista.getDato());
				
				Iterator<Contenedor> it = lista.iterator();
				
				//el primer elemento de esa lista, es el string del nodo actual y en caso de existir, el puntero
				//al que hace referencia. recordar que esto permite (a costa de ocupar mas espacio tratar a los 
				// nodos hoja y los internos de la
				//misma forma. asi como tambien no diferenciar, en disco, un puntero a otro nodo de un puntero
				//a un offset del archivo de sonidos.
				ParStringPuntero primerPar = new ParStringPuntero(it.next().getDato());
				
				this.contenido = primerPar.getLetraOpalabra();
				this.puntero = primerPar.getNumeroNodo();
				
				while (it.hasNext()){
					ParStringPuntero psp = new ParStringPuntero(it.next().getDato());
					this.listaDepunteros.add(psp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		private byte[] serializar(){
		    //genero un contenedor para este registro
		//	Contenedor container = new Contenedor();
			
			//genero mis buffers para cargar bytes
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			
			
			try {
				dos.writeLong(this.getNroNodo());
				dos.write(this.serializarLista());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		//	container.setDato(bos.toByteArray());
		//	return container.serializar();
			
			return bos.toByteArray();
		}
		

		/**
		 * 
		 * Devuelve un array de bytes que es un gran contenedor, dentro de él, en el area de datos, hay serializados a su vez
		 *  varios otros contenedores, conteniendo los pares StringPuntero.
		 * 
		 * |---------------------------------------------Contenedor 1----------------------------------------------------|
		 * |         |-------------------------------Area datos contenedor 1-------------------------------------------| |
		 * |		 |			|--------Area datos cont2------------|			|--------------area datos con3-------| | |
		 * |Longitud |Longitud  | puntero (long) | String (variable) | Longitud | puntero (long) | String (variable) | | |
		 * |         |			|____________________________________|			|____________________________________| | |
		 * |         |_________________________________________________________________________________________________| |
		 * |_____________________________________________________________________________________________________________|
		 * 
		 * @return
		 */
		private byte[] serializarLista(){
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			
			/*
			 * cargo el contenido del nodo actual en un parstringpuntero, de esa forma
			 * se pueden tratar por igual, las referencias a los hijos y  punteros a esos nodos
			 * como el string del nodo actual y su puntero a sonido
			 */
			ParStringPuntero psp = new ParStringPuntero(this.puntero,this.contenido);
			
			Iterator<ParStringPuntero> it = listaDepunteros.iterator();
			
			try {
				dos.write(psp.getPaqueteSerializado().serializar());  //escribo el primer contenedor
				
				while (it.hasNext()) { //itero sobre toda la coleccion de punteros
					dos.write(it.next().getPaqueteSerializado().serializar()); // los serializo en un contenedor y escribo en la variable de salida
				}
				
			} catch (IOException e) {
	
				e.printStackTrace();
			}
			
			Contenedor contgeneral = new Contenedor();
			contgeneral.setDato(bos.toByteArray());
			
			return contgeneral.serializar();
			

			
		}
		
		
		public Contenedor getPaqueteSerializado(){
			
			Contenedor cont = new Contenedor();
			cont.setDato(this.serializar());
			return cont;
			
		}	
		
		public Nodo getNodo(){
			
			if (this.nodoReferenciado ==null){
			Nodo miNodo = new Nodo();
			miNodo.setContenido(this.contenido);
			PunteroSonido ps = new PunteroSonido(this.puntero);
			miNodo.setPuntero(ps);
			miNodo.setRegistroAsociado(this);
			this.nodoReferenciado = miNodo;
			return miNodo;
			}else{
			 return this.nodoReferenciado;
			}
			
		}
	
		public void setNroNodo(long nroBloque) {
			this.nroNodo = nroBloque;
		}

		public long getNroNodo() {
			return nroNodo;
		}
		
		/**
		 * Método que asegura la escritura a disco de cualquier nodo al que se le haya perdido referencia por error
		 * o por obviar su condición de dirty
		 */
		protected void finalize() throws Throwable
		{
		  
			if (this.isDirty){
				
			}
			
		  super.finalize();
		} 


}
