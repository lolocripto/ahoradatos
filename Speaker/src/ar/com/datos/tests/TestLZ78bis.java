package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.lz78.CompresorLZ78;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

public class TestLZ78bis {

	
	public static void main (String[] args){
		
		Compresor comp = new CompresorLZ78();
		//inicio sesion
		comp.iniciarSesion();
		
		String cadena = "Hola diccionario complejo\n";
		String cadena2 = "esto es una mierda no funciona";
		
		try {
			
			//comprimo la cadena 1 y despues la 2
			String binario = comp.comprimir(cadena);
			binario += comp.comprimir(cadena2);
			
			binario += comp.finalizarSession();
			System.out.println("quedo "+binario);
			System.out.println("quedo de long "+binario.length());
			System.out.println(binario);
			
			//descomprimo
			comp.iniciarSesion();
			
			//genero un string buffer
			StringBuffer sb = new StringBuffer ();
			
			//para simular el funcionamiento real le agrego solo algunos bytes
			sb.append(binario.substring(0, 15));
			
			//y descomprimo
			
			
			String descomprimido = comp.descomprimir(sb);
			
			
			sb = new StringBuffer(binario.substring(15));
			//y descomprimo
			descomprimido += comp.descomprimir(sb);
			
			System.out.println(descomprimido);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
