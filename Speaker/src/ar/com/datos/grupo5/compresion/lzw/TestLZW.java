package ar.com.datos.grupo5.compresion.lzw;
import ar.com.datos.grupo5.compresion.lzw.CompresorLzw;
public class TestLZW {
	   
	
	public static void main(String[] args) {
	        // Create application frame.
	        CompresorLzw lzwNew = new CompresorLzw();
	        lzwNew.comprimirArchivo("compresion.txt","compresion.lzw");
	        lzwNew.descomprimirArchivo("compresion.lzw","compresion.wzl");
	        
	}
}
