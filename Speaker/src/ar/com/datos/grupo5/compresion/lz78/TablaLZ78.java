package ar.com.datos.grupo5.compresion.lz78;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TablaLZ78 implements Map{
	
	private HashMap<String, Integer> tablaLZ78Compresion;

	private HashMap<Integer, String> tablaLZ78Descompresion;
	
	int modo ; // 0 para compresion
 
    public TablaLZ78(int modo) {
    	this.modo = modo ;
         if (this.modo == 0) {
        	   //modo compresion
        	   tablaLZ78Compresion = new HashMap<String, Integer>(); 
           }
           else {
        	   //modo descompresion
        	   tablaLZ78Descompresion = new HashMap<Integer, String>();
           }
        	   
    }
	public void clear() {
		// TODO Auto-generated method stub
		if (modo == 0) {
			tablaLZ78Compresion.clear();
		}
		else {
			tablaLZ78Descompresion.clear();
		}
			
		
	}

	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		if (modo == 0) {
			return tablaLZ78Compresion.containsKey(arg0);
		}
		else {
			return tablaLZ78Descompresion.containsKey(arg0);
		}
	}

	public boolean containsValue(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.containsValue((Integer)arg0);
		}
		else {
			return tablaLZ78Descompresion.containsValue((String)arg0);
		}
		
	}

	public Set entrySet() {
	if (modo == 0) {
		return tablaLZ78Compresion.entrySet();
	}
	else {
		return tablaLZ78Descompresion.entrySet();
	}
		
	}

	public Object get(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.get((String)arg0);
		}
		else {
			return tablaLZ78Descompresion.get((Integer)arg0);
		}	
	}

	public boolean isEmpty() {
		if (modo == 0) {
			return tablaLZ78Compresion.isEmpty();
		}
		else {
			return tablaLZ78Descompresion.isEmpty();
		}	
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		if (modo == 0) {
			return tablaLZ78Compresion.keySet();
		}
		else {
			return tablaLZ78Descompresion.keySet();
		}
		
	}

	public Object put(Object arg0, Object arg1) {
		String cadena = new String();
		Integer numero = new Integer(0);
		if (cadena.getClass() == arg0.getClass()) {
			cadena = (String)arg0;
			numero = (Integer)arg1;
		}
		else {
			numero = (Integer) arg0;
			cadena = (String)  arg1;
			
		}
		
		if (modo == 0) {
			
			return tablaLZ78Compresion.put(cadena,numero);
		}
		else {
			return tablaLZ78Descompresion.put(numero,cadena);
		}
			
	}

	public void putAll(Map arg0) {
		if (modo == 0) {
			tablaLZ78Compresion.putAll(arg0);
		}
		else {
			tablaLZ78Descompresion.putAll(arg0);
		}
	}

	public Object remove(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.remove((String)arg0);
		}
		else {
			return tablaLZ78Descompresion.remove((Integer)arg0);
		}
			
	}

	public int size() {
		if (modo == 0) {
			return tablaLZ78Compresion.size();
		}
		else {
			return tablaLZ78Descompresion.size();
		}
		
	}

	public Collection values() {
		if (modo == 0) {
			return tablaLZ78Compresion.values();
		}
		else {
			return tablaLZ78Descompresion.values();
		}
		
	}

}
