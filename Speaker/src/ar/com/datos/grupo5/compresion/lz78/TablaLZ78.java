package ar.com.datos.grupo5.compresion.lz78;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase encargada del manejo de la tabla para el metodo de Compresion
 * LZ78.
 * @author LedZeppelin
 */

public class TablaLZ78 implements Map{
	
	private HashMap<String, Integer> tablaLZ78Compresion;

	private HashMap<Integer, String> tablaLZ78Descompresion;
	
	int modo ; // 0 para compresion
	
	/**
	 * Constructor
	 * @param modo, 0 para compresion 1 para descompresion
	 */
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
    
    /**
     * Limpia la tabla
     */
	public void clear() {
		// TODO Auto-generated method stub
		if (modo == 0) {
			tablaLZ78Compresion.clear();
		}
		else {
			tablaLZ78Descompresion.clear();
		}
			
		
	}
	/**
	 * Indica si se contiene el indice de la tabla
	 * @return boolean, true si se contiene la clave
	 */
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		if (modo == 0) {
			return tablaLZ78Compresion.containsKey(arg0);
		}
		else {
			return tablaLZ78Descompresion.containsKey(arg0);
		}
	}
	/**
	 * Indica si se tiene el valor en la tabla
	 * @return boolean, true si se contiene la clave
	 */
	public boolean containsValue(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.containsValue((Integer)arg0);
		}
		else {
			return tablaLZ78Descompresion.containsValue((String)arg0);
		}
		
	}
	/**
	 * Devuelve un conjunto con las entradas de las tabla
	 * @return Set, conunto con las entradas
	 */
	public Set entrySet() {
	if (modo == 0) {
		return tablaLZ78Compresion.entrySet();
	}
	else {
		return tablaLZ78Descompresion.entrySet();
	}
		
	}
	
	/**
	 * Obtiene el valor asociado al indice
	 * @param arg0 , indice
	 * @return Object, objeto asociado al indice
	 */
	public Object get(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.get((String)arg0);
		}
		else {
			return tablaLZ78Descompresion.get((Integer)arg0);
		}	
	}
	
	/**
	 * Indica si la tabla esta vacia
	 * @return boolean , true si esta vacia la tabla.
	 */
	public boolean isEmpty() {
		if (modo == 0) {
			return tablaLZ78Compresion.isEmpty();
		}
		else {
			return tablaLZ78Descompresion.isEmpty();
		}	
	}
	/**
	 * Devuelve un conjunto con todas las claves de la tabla
	 * @return Set, conjunto con las claves.
	 */
	public Set keySet() {
		// TODO Auto-generated method stub
		if (modo == 0) {
			return tablaLZ78Compresion.keySet();
		}
		else {
			return tablaLZ78Descompresion.keySet();
		}
		
	}
	/**
	 * Ingresa un elemento a la tabla
	 * @param arg0 , arg1 Indice y valor respectivamente
	 * @return Object, objecto Insertado
	 */
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
	/**
	 * Ingresa un conjunto de duplas (clave, valor) 
	 * a la tabla
	 * @param arg0 conunto de duplas 
	 */
	public void putAll(Map arg0) {
		if (modo == 0) {
			tablaLZ78Compresion.putAll(arg0);
		}
		else {
			tablaLZ78Descompresion.putAll(arg0);
		}
	}
	/**
	 * Elimina el elemento asociado a la clave
	 * @param arg-, clave de la tabla
	 * @return Object, elemento eliminado
	 */
	public Object remove(Object arg0) {
		if (modo == 0) {
			return tablaLZ78Compresion.remove((String)arg0);
		}
		else {
			return tablaLZ78Descompresion.remove((Integer)arg0);
		}
			
	}
	/**
	 * Devuelve el tamaño actual de la tabla
	 * @return int , tamaño de la tabla
	 */
	public int size() {
		if (modo == 0) {
			return tablaLZ78Compresion.size();
		}
		else {
			return tablaLZ78Descompresion.size();
		}
		
	}
	/**
	 * Devuelve una coleccion con todos los valores de la tabla.
	 * @return Collection
	 */
	public Collection values() {
		if (modo == 0) {
			return tablaLZ78Compresion.values();
		}
		else {
			return tablaLZ78Descompresion.values();
		}
		
	}

}
