package trie.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;

public class Contenedor {

	private byte[] dato;
	private int longitud;

	
	public Contenedor(byte[] dato) {
		super();
		this.dato = dato;
		this.longitud = dato.length;
	}
	
	public Contenedor(){
		super();
	}

	
	
	public static Contenedor rehidratar (byte[] ContSerializado){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(ContSerializado);  
		DataInputStream dis = new DataInputStream(bis);
		
		Contenedor cont = new Contenedor();
		int longitud;
		
		try {
			longitud = dis.readInt(); //leo el int que hace referencia a la longitud del contenedor
			byte[] datos = new byte[longitud];
			dis.read(datos, 0, longitud); //pongo en datos desde el offset sizeofint, "longitud" cantidad de bytes
			cont.setDato(datos);
			return cont;
		} catch (IOException e) {
			e.printStackTrace();
		}
	return null;	
		
	}
	
	
	public static ArrayList<Contenedor> rehidratarLista (byte[] listaSerializada){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(listaSerializada);  
		DataInputStream dis = new DataInputStream(bis);
		
		ArrayList<Contenedor> lista = new ArrayList<Contenedor>();
		
		int i =0;
		int longitud;
		
		while (i<listaSerializada.length){
			
		
		
		try {
			
			longitud = dis.readInt();
			byte[] datos = new byte[longitud];
			dis.read(datos, 0, longitud);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
	
	return null;
	}

	public byte[] getDato() {
		return dato;
	}


	public byte[] serializar (){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.write(Conversiones.intToArrayByte(this.longitud));
			dos.write(this.dato);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
		
	}
	
	public void setDato(byte[] dato) {
		this.dato = dato;
		this.longitud = dato.length;
	}


	public int getLongitud() {
		return longitud;
	}

	

	
	
}
