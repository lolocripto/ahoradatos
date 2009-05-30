package ar.com.datos.grupo5.trie.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.Constantes;

public class AdministrationRegistry {

	private long cantNodosUsados;
	private int profundidadActual;
	
	
	
	public AdministrationRegistry() {
		this.cantNodosUsados =0;
		this.profundidadActual = Constantes.SIZE_OF_TRIE;
	}
	
	public AdministrationRegistry(byte[] tiraBytes) {
		this.hidratar(tiraBytes);
	}

	public AdministrationRegistry(Contenedor cont) {
		this.hidratar(cont);
	}
	
	
	public void setProfundidadActual(int profundidadActual) {
		this.profundidadActual = profundidadActual;
	}

	public long sumarUnNodo(){
		
		cantNodosUsados++;
		return cantNodosUsados;
	}
	
	public long getCantidadNodosExistentes(){
		return cantNodosUsados;
	}
	
	public void setCantNodos(long cant){
		cantNodosUsados = cant;
	}
	
	public Contenedor getPaqueteSerializado(){
		
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
	
	public int getProfundidadCreacionTrie(){
		return profundidadActual;
	}
	
	private void hidratar (byte[] datosSerializados){
		
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
	
	private void hidratar (Contenedor cont){
		hidratar(cont.getDato());
	}
	
	
}
