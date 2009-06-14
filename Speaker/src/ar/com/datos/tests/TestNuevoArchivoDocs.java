package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class TestNuevoArchivoDocs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

//		String ruta1 = "./test.txt";
//		String ruta2 = "./test2.txt";
//		String ruta3 = "./test3.txt";
//		
//		
//		DocumentsManager.getInstance().setTipoCompresion(MetodoCompresion.ARIT);
//		
//		ArrayList<String> lista = new ArrayList<String>();
//		
//		lista.add(ruta1);
//		lista.add(ruta2);
//		lista.add(ruta3);
////		lista.add(ruta4);
////		lista.add(ruta5);
////		lista.add(ruta6);
//		
//		Iterator<String> it = lista.iterator();
//		
//		TextInterpreter ti = new TextInterpreter();
//		
//		
//		Collection<IunidadDeHabla> listapalabras =null;
//		Iterator<IunidadDeHabla> it2;
//		
//		while (it.hasNext()){
//			
//			String rutaActual = it.next();
//			
//			try {
//				listapalabras= ti.modolecturaYalmacenamiento(rutaActual);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
//			DocumentsManager.getInstance().finalizaEscritura();
//			
//		}
//	}
//}

		
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(4)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(41544)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(84954)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85106)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85106)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85196)));
//		System.out.println(DocumentsManager.getInstance().getNombreDoc(new Long(85302)));
		
		
		
	//	DocumentsManager.getInstance().setTipoCompresion(MetodoCompresion.ARIT);
		
		
		Collection<IunidadDeHabla> listapalabras =null;
		TextInterpreter ti = new TextInterpreter();
		Iterator<IunidadDeHabla> it2;
		
		
		listapalabras = ti.modoLecturaDocAlmacenado(4);
			
			it2 = listapalabras.iterator();
			
			while (it2.hasNext()){
				System.out.println(it2.next());
			}
			
			System.out.println(DocumentsManager.getInstance().getOffsetUltDoc());
			
		}
}
		
		
	
//4
//116
//179
	
//4
//81
//124	
	

