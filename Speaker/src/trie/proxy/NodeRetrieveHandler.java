package trie.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import trie.core.INodo;
import trie.core.Nodo;
import trie.persistence.TrieNodeRegistry;

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
	    		this.levantarNodoDeDisco();
	    	}
	    	
	       System.out.println("A Manager is being invoked:"+method.getName());
	       
	        // Continuamos invocando al Manager real.
	        return method.invoke(nodoReal,args);
	    }
	
	
	private void levantarNodoDeDisco(){
		System.out.println("voy a disco y levanto el hijo");
	}
	
	private void guardarNodoADisco(Nodo unNodo){
		
	}
	
	
	
	
}
