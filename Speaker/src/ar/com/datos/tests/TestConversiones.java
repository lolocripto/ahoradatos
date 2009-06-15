package ar.com.datos.tests;

import ar.com.datos.grupo5.utils.Conversiones;

public class TestConversiones {

	
	public static void main(String[] args) {
		
		
		
		
		byte[] b = new byte[4];
		
		b[0]= 0x5A;  //0101 1010
		b[1]= 0x7A;  //0111 1010
		b[2]= 0x6B;  //0110 1011
		
		String s = Conversiones.arrayByteToBinaryString(b);
		
		System.out.println(s);
		
		
		
	}
	
}
