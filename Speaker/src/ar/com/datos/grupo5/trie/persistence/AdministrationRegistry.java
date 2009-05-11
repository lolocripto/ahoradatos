package ar.com.datos.grupo5.trie.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.utils.Conversiones;

public class AdministrationRegistry {

	private static long cantNodosUsados;
	private static int profundidadActual;
	
	
	public static void setProfundidadActual(int profundidadActual) {
		AdministrationRegistry.profundidadActual = profundidadActual;
	}

	public static long sumarUnNodo(){
		
		cantNodosUsados++;
		return cantNodosUsados;
	}
	
	public static long getCantidadNodosExistentes(){
		return cantNodosUsados;
	}
	
	public static void setCantNodos(long cant){
		cantNodosUsados = cant;
	}
	
	public static Contenedor getPaqueteSerializado(){
		
		Contenedor cont = new Contenedor();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeLong(cantNodosUsados);
			dos.writeInt(profundidadActual);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cont.setDato(bos.toByteArray());
		return cont;
	}
	
	public static int getProfundidadCreacionTrie(){
		return profundidadActual;
	}
	
	public static void hidratar (byte[] datosSerializados){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(datosSerializados);  
		DataInputStream dis = new DataInputStream(bis);
		
		try {
			cantNodosUsados = dis.readLong();
			profundidadActual = dis.readInt();
		} catch (IOException e) {
	//		System.out.println("no se ha podido recuperar el registro Administrativo del trie");
			e.printStackTrace();
		}
		
	}
	
	public static void hidratar (Contenedor cont){
		hidratar(cont.getDato());
	}
	
	
}
