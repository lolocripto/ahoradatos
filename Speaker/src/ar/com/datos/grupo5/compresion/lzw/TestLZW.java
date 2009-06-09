package ar.com.datos.grupo5.compresion.lzw;
import ar.com.datos.grupo5.compresion.lzw.CompresorLzw;
public class TestLZW {
	   
	
	public static void main(String[] args) {
	        // Create application frame.
	        CompresorLzw lzwNew = new CompresorLzw();
	        lzwNew.comprimirArchivo("compresion.txt","compresion2.lzw");
	        lzwNew.descomprimirArchivo("compresion2.lzw","compresion2.wzl");
            //for (int i =0 ; i<1000; i++) {
           // 	System.out.println("char= "+Character.toChars(i)[0]+" pos="+i);
            //}
	        
	}
}
