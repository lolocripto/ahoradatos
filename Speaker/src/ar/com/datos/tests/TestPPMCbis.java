package ar.com.datos.tests;

import ar.com.datos.grupo5.compresion.CompresorFactory;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestPPMCbis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		String linea1 ="Hola esta es una prueba de un archivo comprimido.\n";
		String linea2 ="Hola es una cadorna. La cadorna es que no lo podamos probar.";
		String comprimida;
		
		Compresor comp = CompresorFactory.getCompresor(MetodoCompresion.PPMC);
		
		comp.iniciarSesion();
		
		try {
			
			comprimida = comp.comprimir(linea1);
			
			comprimida += comp.comprimir(linea2);
			
			comprimida += comp.finalizarSession();
			
			System.out.println(comprimida);
			
			comp.iniciarSesion();
			
			StringBuffer sb = new StringBuffer(comprimida);
			
			String nueva = comp.descomprimir(sb);
			
			System.out.println(nueva);
			
			
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
