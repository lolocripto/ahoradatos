package ar.com.datos.compresion.lzw;
import ar.com.datos.compresion.lzw.CompresorLzw;
public class TestLZW {
	   
	
	public static void main(String[] args) {
	        // Create application frame.
	        CompresorLzw lzwNew = new CompresorLzw();
	        lzwNew.comprimirArchivo("prueba1.txt","prueba1.lzw");
	        lzwNew.descomprimirArchivo("prueba1.lzw","prueba2.wzl");
	              
	}
}
