package ar.com.datos.grupo5;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.io.IOException;

import ar.com.datos.grupo5.utils.Conversiones;

public class NodoListaEspacioLibre implements Comparator{

	private short espacio;
	
	private int nroBloque;
	
	private int moreBytes;
	
	public short getEspacio(){
		return espacio;
	}
	
	public int getNroBloque(){
		return nroBloque;
	}
	
	public void setNroBloque(int NroBloque) {
		this.nroBloque = NroBloque;
		this.moreBytes = Constantes.SIZE_OF_INT + Constantes.SIZE_OF_SHORT;
	}
	
	public void setEspacio(short Espacio) {
		this.espacio = Espacio;
		this.moreBytes = Constantes.SIZE_OF_INT + Constantes.SIZE_OF_SHORT;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		NodoListaEspacioLibre n1 = (NodoListaEspacioLibre) arg0;
		NodoListaEspacioLibre n2 = (NodoListaEspacioLibre) arg1;
		if(n1.getEspacio() == n2.getEspacio()){
			return 0;
		}
		if(n1.getEspacio() > n2.getEspacio()){
			return 1;
		}
		if(n1.getEspacio() < n2.getEspacio()){
			return -1;
		}
		return 0;
	}
	
	public boolean equals(Object arg0){
		NodoListaEspacioLibre n1 = (NodoListaEspacioLibre) arg0;
		return (this.espacio == n1.getEspacio() && this.nroBloque == n1.getNroBloque());
	}
	
	
	public byte[] getBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {

			
			byte[] espacioBytes = Conversiones.shortToArrayByte(this.espacio);
			byte[] nroBloqueByte = Conversiones.intToArrayByte(this.nroBloque);
			if (moreBytes == (espacioBytes.length + nroBloqueByte.length)) {
				
				dos.write(espacioBytes,0,espacioBytes.length);
				moreBytes -= espacioBytes.length;
				dos.write(nroBloqueByte, 0, nroBloqueByte.length);
				moreBytes -= nroBloqueByte.length;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	public void setBytes(byte[] buffer) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dis = new DataInputStream(bis);

		try {
			this.espacio = dis.readShort();
			this.nroBloque = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
