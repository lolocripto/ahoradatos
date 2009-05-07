package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sound.sampled.Line;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.parser.Parser;

public class TestArchivoDocs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		File archivo; 
		BufferedReader buffer=null;
		
	    archivo = new File("/home/zeke/Escritorio/prueba.txt");

		try {
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			buffer = new BufferedReader(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		DocumentsManager adm = DocumentsManager.getInstance();
		
		
		
		Long cero = new Long(0);
		adm.initReadSession(cero);
		
		String Linea = adm.leerLinea();
		
		while (Linea !=null){
			System.out.println(Linea);
			Linea = adm.leerLinea();
		}
	
		
/* carga		
		
		adm.initWriteSession();
		

		String linea=null;
		try {
			linea = buffer.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		while ((linea != null)) {
			try {
				
				System.out.println("escribo: "+linea);
				adm.escribirLinea(linea);
				linea = (buffer.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
*/		
		adm.cerrarSesion();
		
		
		System.out.println(adm.getLongDocUltDoc());
		System.out.println(adm.getOffsetUltDoc());
		
	}
}
