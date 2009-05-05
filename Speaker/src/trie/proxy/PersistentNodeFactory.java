package trie.proxy;

import java.lang.reflect.Proxy;

import trie.core.INodo;
import trie.core.Nodo;

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
	            // Creamos un objeto de la clase que recibimos.
	        	
	        	nodoReal = (INodo) new Nodo(i);
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
	            // Creamos un objeto de la clase que recibimos.
	        	
	        	nodoReal = (INodo) new Nodo(i);
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
