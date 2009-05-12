package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.TextInterpreter;

public class TestNuevoArchivoDocs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		String ruta1 = "/home/zeke/Escritorio/arcpru/largo.txt";
		String ruta2 = "/home/zeke/Escritorio/arcpru/largo2.txt";
		String ruta3 = "/home/zeke/Escritorio/arcpru/prueba1.txt";
		String ruta4 = "/home/zeke/Escritorio/arcpru/prueba2.txt";
		String ruta5 = "/home/zeke/Escritorio/arcpru/prueba3.txt";
		String ruta6 = "/home/zeke/Escritorio/arcpru/prueba4.txt";
		
		
		ArrayList<String> lista = new ArrayList<String>();
		
		lista.add(ruta1);
		lista.add(ruta2);
		lista.add(ruta3);
		lista.add(ruta4);
		lista.add(ruta5);
		lista.add(ruta6);
		
		Iterator<String> it = lista.iterator();
		
		TextInterpreter ti = new TextInterpreter();
		
		
		Collection<IunidadDeHabla> listapalabras;
		Iterator<IunidadDeHabla> it2;
		
		
//		while (it.hasNext()){
//			
//			String rutaActual = it.next();
//			
//			listapalabras= ti.modolecturaYalmacenamiento(rutaActual);
//			
//			it2 = listapalabras.iterator();
//			
//			
//			while (it2.hasNext()){
//				it2.next();
//			}
//			
//			System.out.println(DocumentsManager.getInstance().getOffsetUltDoc());
//			
//		}
		
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(4)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(41544)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(84954)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85106)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85106)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85196)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85302)));
		
		
		

			listapalabras= ti.modoLecturaDocAlmacenado(4);
			
			it2 = listapalabras.iterator();
			
			while (it2.hasNext()){
				System.out.println(it2.next());
			}
			
			System.out.println(DocumentsManager.getInstance().getOffsetUltDoc());
			
		}
		
		
		

	}
	
	
//	4
//	41544
//	84954
//	85106
//	85196
//	85302	
	

