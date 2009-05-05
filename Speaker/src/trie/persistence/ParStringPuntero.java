package trie.persistence;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;

public class ParStringPuntero {

	private String letraOpalabra;
	private Long numeroNodo;
	
	public ParStringPuntero(Long numeroNodo, String letraOpalabra) {
		super();
		this.letraOpalabra = letraOpalabra;
		this.numeroNodo = numeroNodo;
	}
	
	//sobrecarga del constructor que rehidrata un nodo serializado
	public ParStringPuntero (byte[] nodoSerializado){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(nodoSerializado);  
		DataInputStream dis = new DataInputStream(bis);
		
		//la longitud del string sera la longitud total del array de bytes menos la longitud
		//del long que lo precede
		int longString = nodoSerializado.length - Constantes.SIZE_OF_LONG;
		
		
		try {
			this.numeroNodo = dis.readLong(); //leo el long que hace referencia al numero de nodo
			
			byte[] aux = new byte[longString]; //genero un array auxiliar para leer el resto
			dis.read(aux); //copio el resto del buffer en el aray auxiliar
			this.letraOpalabra = new String(aux); //cargo la letra con el equivalente en string del arraya auxiliar
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public String getLetraOpalabra (){
		return this.letraOpalabra;
	}
	
	public Long getNumeroNodo(){
		return this.numeroNodo;
	}
	
	
	private int longitudSerializacion(){
		int tamanioString = letraOpalabra.length();
		return Constantes.SIZE_OF_LONG + tamanioString;
		
	}
	
	//serializa el contenido de este objeto
	private byte[] serializar(){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.write(Conversiones.longToArrayByte(this.numeroNodo));
			dos.write(this.letraOpalabra.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	public Contenedor getPaqueteSerializado(){
		Contenedor nuevoContenedor = new Contenedor();
		
		byte[] tiraDeBytes;
		tiraDeBytes = this.serializar();
		
		nuevoContenedor.setDato(tiraDeBytes);
		return nuevoContenedor;
		
	}
	
}
