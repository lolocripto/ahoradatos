package ar.com.datos.trie.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import ar.com.datos.trie.core.INodo;
import ar.com.datos.trie.core.Nodo;
import ar.com.datos.trie.persistence.ParStringPuntero;
import ar.com.datos.trie.persistence.TrieNodeRegistry;


public class NodeRetrieveHandler implements  InvocationHandler {

	
      TrieNodeRegistry registro =null;	
	  INodo nodoReal = null;
	   
	    public NodeRetrieveHandler(INodo miNodo) {
	        this.nodoReal = miNodo;
	    }
	   
	    /* Este es el método callback que será invocado previamente a cada método de          los managers. */
	    public Object invoke(Object proxy, Method method, Object[] args)
	        throws Throwable {       
	       
	    	if (method.getName() == "getHijo"){
	    		this.levantarNodoDeDisco((INodo)proxy, args[0]);
	    	}
	    	
	    	if (method.getName() == "setNuevoHijo"){
	    		this.agregarNuevoHijo( (INodo)proxy, (INodo) args[0] );
	    	}
	    	
	    	
	       System.out.println("A Manager is being invoked:"+method.getName());
	       
	        // Continuamos invocando al Manager real.
	        return method.invoke(nodoReal,args);
	    }
	
	
	/**
	 * Cuando se captura una llamada al metodo setNuevoHijo se ejecuta esta función
	 * que permite actualizar la lista de punteros del padre.    
	 * @param objetoRealActual
	 * @param nuevoHijo
	 */
	private void agregarNuevoHijo(INodo objetoRealActual, INodo nuevoHijo ){
		
		TrieNodeRegistry registroDelObjetoActual = objetoRealActual.getRegistroAsociado();
		
		ParStringPuntero psp = new ParStringPuntero(nuevoHijo.getNumeroNodo(), nuevoHijo.getContenido() );
		
		registroDelObjetoActual.listaDepunteros.add(psp);
		
		registroDelObjetoActual.setDirty(true);
		
	}
	    
	private void levantarNodoDeDisco(INodo objetoRealActual,Object palabra){
		
		if (palabra instanceof Character){
			
		}
		
		
		TrieNodeRegistry registroDelObjetoActual = objetoRealActual.getRegistroAsociado();
		
		
		
		System.out.println("voy a disco y levanto el hijo");
	}
	
	private void guardarNodoADisco(Nodo unNodo){
		
	}
	
	
	
	
}
