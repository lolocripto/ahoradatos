package ar.com.datos.grupo5.registros;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ar.com.datos.grupo5.utils.Conversiones;

public class RegistroAdmSecSet {

	private long cantBloques;
	
	
	
	
	public RegistroAdmSecSet(byte[] tira){
		
		this.setBytes(tira);
	}
	
	//constructor usado para la primera instanciacion
	public RegistroAdmSecSet(){
		this.cantBloques =0;
	}
	
	
	
	public void setBytes(byte[] tiraBytes){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(tiraBytes);  
		DataInputStream dos = new DataInputStream(bis);
		
		try {
			this.cantBloques = dos.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getBytes (){
		
		return Conversiones.longToArrayByte(cantBloques);
		
	}
	
	public long reservarNuevoBloque (){
		
		this.cantBloques++;
		return this.cantBloques;
	}
	
	
	
}
