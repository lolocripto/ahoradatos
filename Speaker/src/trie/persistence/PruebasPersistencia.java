package trie.persistence;

public class PruebasPersistencia {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
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
