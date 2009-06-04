package ar.com.datos.grupo5.compresion.aritmetico;

public class PruebaUnsigned {

	
	public static void main (String[] Args){
		
		
		UnsignedInt uno = new UnsignedInt(1);
		UnsignedInt dos = new UnsignedInt("100000000000000000000000000000000000000000000000000000000000000");
		UnsignedInt tres = new UnsignedInt(4);
		UnsignedInt cuatro = new UnsignedInt(8);
		UnsignedInt cinco = new UnsignedInt(16);
		UnsignedInt seis = new UnsignedInt(32);
		UnsignedInt siete = new UnsignedInt(64);
		UnsignedInt ocho = new UnsignedInt(128);
		
		
		dos.leftShiftOne();
		dos.leftShiftOne();
		dos.leftShiftOne();
		dos.leftShiftOne();
		
		System.out.println(dos.get64BitsRepresentation());
		System.out.println(dos.getLongAsociado());
		
		dos.leftShiftOne();
		System.out.println(dos.get64BitsRepresentation());
		System.out.println(dos.getLongAsociado());
		
		
		
	}
	
	
}
