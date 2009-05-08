package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStreamReader;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.DocumentsManager;

public class TestArchivoDocs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		File archivo; 
		BufferedReader buffer=null;
		
	    archivo = new File("/home/zeke/Escritorio/arcpru/prueba1.txt");

		try {
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			buffer = new BufferedReader(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
		DocumentsManager adm = DocumentsManager.getInstance();

		
		Long cero = new Long(521);
		adm.initReadSession(cero);

		
		System.out.println(adm.getNombreDoc(new Long(283)));
		System.out.println(adm.getNombreDoc(new Long(521)));
		System.out.println(adm.getNombreDoc(new Long(394)));
		System.out.println(adm.getNombreDoc(new Long(156)));
		
		
/*		
		String Linea = adm.leerLinea();
		
		while (Linea != null){
			System.out.println(Linea);
			Linea = adm.leerLinea();
		}

*/


/*
		adm.initWriteSession();
		

		adm.setNombreDoc(archivo.getName());
		
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
		
		System.out.println(adm.getCantidadDocsAlmacenados());
		System.out.println(adm.getLongDocUltDoc());
		System.out.println(adm.getOffsetUltDoc());
		
	}
}
