package ar.com.datos.trie.proxy;

import java.lang.reflect.Proxy;

import ar.com.datos.trie.core.INodo;
import ar.com.datos.trie.core.Nodo;
import ar.com.datos.trie.persistence.AdministrationRegistry;
import ar.com.datos.trie.persistence.TrieNodeRegistry;


public class PersistentNodeFactory {


    public INodo createNodo(Class claseAdministracion) {
        
        INodo nodoReal = null;
       
        try {
            // Creamos un objeto de la clase que recibimos.
            nodoReal = (INodo)claseAdministracion.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
       
      /* Creamos el CallBack Handler y le pasamos el objeto real para ser invocado               posteriormente en su método invoke. */
     
        NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);
       
        // Creamos el proxy.
        Class[] interfacesQueEncapsulo = new Class[] {INodo.class};
        return (INodo)Proxy.newProxyInstance(
                       claseAdministracion.getClassLoader(),
                        interfacesQueEncapsulo,handler);                  
    }     
	
	
	
	
	public static INodo getNewNodo(char i){
		
		   INodo nodoReal = null;
		   Class claseAdministracion = Nodo.class;
	       
	        try {
	            // creo un nuevo nodo con el valor del char pasado por parametro
	        	nodoReal = (INodo) new Nodo(i);
	        	//agrego al nodo un numero que voy a obtener del registro de administracion
	        	nodoReal.setNumeroNodo(AdministrationRegistry.sumarUnNodo());
	        	
	        	//creo un registro que lo represente
	        	TrieNodeRegistry t = new TrieNodeRegistry(nodoReal);
	        	//se lo agrego al nodo
	        	nodoReal.setRegistroAsociado(t);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	       
	      /* Creamos el CallBack Handler y le pasamos el objeto real para ser invocado               posteriormente en su método invoke. */
	     
	        NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);
	       
	        // Creamos el proxy.
	        Class[] interfacesQueEncapsulo = new Class[] {INodo.class};
	        return (INodo)Proxy.newProxyInstance(
	                       claseAdministracion.getClassLoader(),
	                        interfacesQueEncapsulo,handler);            
		
		
	}
	
	public static INodo getNewNodo(String i){
		
		   INodo nodoReal = null;
		   Class claseAdministracion = Nodo.class;
	       
	        try {
	        	
	        	nodoReal = (INodo) new Nodo(i);
	        	nodoReal.setNumeroNodo(AdministrationRegistry.sumarUnNodo());
	        	
	        	//creo un registro que lo represente
	        	TrieNodeRegistry t = new TrieNodeRegistry(nodoReal);
	        	//se lo agrego al nodo
	        	nodoReal.setRegistroAsociado(t);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	       
	      /* Creamos el CallBack Handler y le pasamos el objeto real para ser invocado               posteriormente en su método invoke. */
	     
	        NodeRetrieveHandler handler = new NodeRetrieveHandler(nodoReal);
	       
	        // Creamos el proxy.
	        Class[] interfacesQueEncapsulo = new Class[] {INodo.class};
	        return (INodo)Proxy.newProxyInstance(
	                       claseAdministracion.getClassLoader(),
	                        interfacesQueEncapsulo,handler);            
		
		
	}
	
	
	
}
