package trie.persistence;

import ar.com.datos.grupo5.utils.Conversiones;

public class AdministrationRegistry {

	private static long cantNodosUsados;
	
	
	
	public static void sumarUnNodo(){
		
		cantNodosUsados++;
	}
	
	public static long getCantidadNodosExistentes(){
		return cantNodosUsados;
	}
	
	public static void setCantNodos(long cant){
		cantNodosUsados = cant;
	}
	
	public static Contenedor getPaqueteSerializado(){
		
		Contenedor cont = new Contenedor();
		cont.setDato(Conversiones.longToArrayByte(cantNodosUsados));
		return cont;
	}
	
	
	public static void hidratar (byte[] datosSerializados){
		cantNodosUsados = Conversiones.arrayByteToLong(datosSerializados);
	}
	
	public static void hidratar (Contenedor cont){
		cantNodosUsados = Conversiones.arrayByteToLong(cont.getDato());
	}
	
	
}
