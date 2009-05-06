package ar.com.datos.trie.persistence;

import java.util.Iterator;

import ar.com.datos.trie.core.Nodo;
import ar.com.datos.trie.core.PunteroSonido;


public class PruebasPersistencia {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
	//creo nodo	
	Nodo unNodo = new Nodo();
	//agrego contenido
	unNodo.setContenido("miContenido");
	unNodo.setNumeroNodo(1);
	
	//creo un puntero
	Long l = new Long(99998888);
	PunteroSonido ps = new PunteroSonido(l);
	//agrego el puntero
	unNodo.setPuntero(ps);
		
	
	TrieNodeRegistry tnr = new TrieNodeRegistry(unNodo);
	
	Long l1 = new Long(2);
	ParStringPuntero psp1 = new ParStringPuntero(l1,"contenido primer hijo");
	Long l2 = new Long(3);
	ParStringPuntero psp2 = new ParStringPuntero(l2,"contenido segundo hijo");
	
	tnr.listaDepunteros.add(psp1);
	tnr.listaDepunteros.add(psp2);
	
	
	
	System.out.println(tnr.getContenido());
	System.out.println(tnr.getNroNodo());
	
	Iterator<ParStringPuntero> it = tnr.listaDepunteros.iterator();
	
	while (it.hasNext()){
		
		ParStringPuntero elem = it.next();
		System.out.println(elem.getLetraOpalabra());
		System.out.println(elem.getNumeroNodo());
	}
	
	
	
	//------------0-------------------------------
	
//	byte[] bloque = new byte [300];
	
//	bloque= tnr.getPaqueteSerializado().serializar();
	
	
	TrieArchiveHandler tah = new TrieArchiveHandler();
	
	tah.guardarNodo(tnr);
	
	tah.closeArchivo();
	
	
	TrieArchiveHandler tah2 = new TrieArchiveHandler();
	
	TrieNodeRegistry tnrnuevo;
	tnrnuevo = tah2.recuperarNodo(1);
	
	
	
	
	
	//--------------0--------------
	
	
//	Contenedor tzt = Contenedor.rehidratar(bloque);
//	TrieNodeRegistry tnrnuevo = new TrieNodeRegistry(tzt.getDato());
	
	System.out.println(tnrnuevo.getContenido());
	System.out.println(tnrnuevo.getNroNodo());
	
	
	Iterator<ParStringPuntero> it2 = tnrnuevo.listaDepunteros.iterator();
	
	while (it2.hasNext()){
		ParStringPuntero elem2 = it2.next();
		System.out.println(elem2.getLetraOpalabra());
		System.out.println(elem2.getNumeroNodo());
	}
	
	tah2.closeArchivo();
		
		
//---------------------------------------------------------------	
		
	/*
	 * prueba 1: 
	 * A) creo un parStringPuntero y lo lleno con informacion
	 * B) imprimo contenido
	 * C) obtengo el Contenedor y lo serializo, guardandolo en la variable entrada que es un array de bytes muy grande
	 * D) genero un nuevo contenedor con el metodo estatico rehidratar pasandole por parametro el array de bytes (simil lo levantado de disco)
	 * E) genero un nuevo ParStringPuntero con el contenido del dato del contenedor
	 * F) imprimo resultados	
	 */
/*		
		Long l = new Long(987654321);
		ParStringPuntero uno = new ParStringPuntero(l,"hola como estas");
		
		System.out.println(uno.getLetraOpalabra());
		System.out.println(uno.getNumeroNodo());
		
		byte[] entrada = new byte [100];
		
		entrada = uno.getPaqueteSerializado().serializar();
		
		
		Contenedor levantadedisco;
		
		levantadedisco = Contenedor.rehidratar(entrada);
		
		
		ParStringPuntero dos = new ParStringPuntero(levantadedisco.getDato());
		
		
		System.out.println(dos.getLetraOpalabra());
		System.out.println(dos.getNumeroNodo());
		
	*/	
		
	//-----------------------------------------------------------	
		
		
		
		
		
		
		
	/*	
		Long l = new Long(783304221);
		ParStringPuntero uno = new ParStringPuntero(l,"sdasdasdavfv");
		
		ParStringPuntero dos = new ParStringPuntero(uno.serializar());
		
		System.out.println(uno.getLetraOpalabra());
		System.out.println(uno.getNumeroNodo());
		
		System.out.println(dos.getLetraOpalabra());
		System.out.println(dos.getNumeroNodo());
		
*/
	}

}
