package ar.com.datos.grupo5.trie.core;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.trie.proxy.PersistentNodeFactory;


public class Trie {

	private INodo root;
	int profundidad;

	/**
	 * Constructor del Trie,recibe la raiz.
	 */

	public Trie (INodo raiz){
		this.root = raiz;
		profundidad = Constantes.SIZE_OF_TRIE + 1;
		
	}

	public void insert(String s, PunteroSonido punt)
	{
		INodo current = root;	
		
		if(s.length()==0)   // For empty String
			current.setPuntero(punt);
		
		for(int i=0; i<s.length() && i<this.profundidad ;i++)
		{
			
			/* Revisit and first visit are being differentiated
			 * to avoid overwriting child[] values during revisit
			 */
			
			if (i<profundidad -1){
			if(current.getHijo(s.charAt(i)) != null )  // Revisit
			{
				current = current.getHijo(s.charAt(i));
				System.out.println("Caracter encontrado, se omite insercion: "+ current.getContenido());
			}
			
			else  		// First visit
			{
				current.setNuevoHijo( PersistentNodeFactory.getNewNodo(s.charAt(i)));
				current = current.getHijo(s.charAt(i));
				System.out.println("Se inserta el caracter: "+ current.getContenido());
			}
			}
			else{
				if(current.getHijo(s.substring(i)) != null )  // Revisit
				{
					current = current.getHijo(s.substring(i));
					System.out.println("resto de string encontrado, se omite insercion: "+ current.getContenido());
				}
				
				else  		// First visit
				{
					current.setNuevoHijo(PersistentNodeFactory.getNewNodo(s.substring(i)));
					current = current.getHijo(s.substring(i));
					System.out.println("se inserta resto de string: "+ current.getContenido());
				}
				
			}
			
			// en la ultima letra o substring pongo el marcador
			if((i==s.length()-1) || (i==this.profundidad-1) )
				current.setPuntero(punt);
		}	
		System.out.println("se finalizo de insertar la palabra: "+s+"\n");
	}

	

	public PunteroSonido search(String s)
	{
		INodo current = root;
		System.out.println("\nSearching for string: "+s);
		
		while(current != null)
		{
			
			for(int i=0;i<s.length() && i<this.profundidad;i++)
			{	
				
				if (i<profundidad -1){
				
				if(current.getHijo(s.charAt(i)) == null)
				{
					System.out.println("No se ha encontrado el string: "+s);
					return null;
				}
				else
				{
					current = current.getHijo(s.charAt(i));
					System.out.println("Caracter encontrado: "+ current.getContenido());
				}
			}
				else{
					
					if(current.getHijo(s.substring(i)) == null)
					{
						System.out.println("no se ha encontrado el resto del string: "+s.substring(i));
						return null;
					}
					else
					{
						current = current.getHijo(s.substring(i));
						System.out.println("Resto hallado: "+ current.getContenido());
					}
				}
			}
			// If we are here, the string exists.
			// But to ensure unwanted substrings are not found:
			
			if (current.getPuntero() != null)
			{
				System.out.println("Found string: "+s);
				return current.getPuntero();
			}
			else
			{
				System.out.println("Cannot find string: "+s +"(only present as a substring)");
				return null;
			}
		}
		
		return null; 
	}

}

