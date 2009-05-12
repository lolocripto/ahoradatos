package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.TextInterpreter;

public class TestTextInterpreter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	


		String ruta = "/home/zeke/Escritorio/test.txt";
	
		TextInterpreter ti = new TextInterpreter();
		
		DocumentsManager dm = DocumentsManager.getInstance();
		

		
	//	Collection<IunidadDeHabla> coleccion =null;

		
/* prueba lectura directa desde archivo ok!
 * 		
		try {
			coleccion = ti.modoLectura(ruta, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
*/		
		
		
		
		
/* string simple ok	

		try {
			coleccion = ti.modoLectura("hola como ñaña deberíamos estas", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
*/		
	
		
		
/*	Prueba modo lectura desde documento almacenado Ok!
 * 	
		Long offset = new Long(141);
		dm.initReadSession(offset);
		
		Collection<IunidadDeHabla> coleccion =null;
		
		try {
			coleccion = ti.modoLecturaDocAlmacenado(dm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
*/		
		
//	prueba almacenar un nuevo archivo de texto...	Ok!
 		
		
		Collection<IunidadDeHabla> coleccion =null;
		
		
		try {
			coleccion = ti.modolecturaYalmacenamiento(ruta);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		Iterator<IunidadDeHabla> it = coleccion.iterator();
		

		while (it.hasNext()){
			System.out.println(it.next().getTextoEscrito());
		}
	}

}
