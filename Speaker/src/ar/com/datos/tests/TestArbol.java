package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Main;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.btree.BSharp;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestArbol {

	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(final String[] args) {
		
		try {
			BStar tree = new BStar();
			ArchivoSecuencialSet sec = new ArchivoSecuencialSet();
			tree.setSecuencialSet(sec);
			
			Clave clave = null;
			RegistroNodo reg = null;
			
//			String[] claves = {
//					"15", "30", "1", "10", "13", "50", "35", 
//					"20", "22", "40", "55", "60", "21", "24",
//					"25", "26", "43", "45", "23", "17", "241",
//					"242", "37", "38", "16", "41", "47", "68",
//					"34", "48", "100", "200", "150", "151", "44",
//					};
				
//			for (int i = 0; i < claves.length; i++) {
//				
//				clave = new Clave();
//				reg = new RegistroNodo();
//				
//				System.out.println("Inserto [" + claves[i] + "]");
//				clave.setClave(claves[i]);
//				reg.setClave(clave);
//				tree.insertar(reg);
//				tree.listar();
//			}
//			int cantidad = 500;
//			String claveStr = "";
//			for (int i = 0; i < cantidad; i++) {
//				
//				clave = new Clave();
//				reg = new RegistroNodo();
//				Integer a = new Double(Math.random() * 1000).intValue();
//				claveStr = "pepe" + a.toString();
//				System.out.println("Inserto [" + claveStr + "]");
//				clave.setClave(claveStr);
//				reg.setClave(clave);
//				Thread.sleep(100);
//				tree.insertar(reg);
//				//tree.listar();
//			}
//			tree.listar();
			String palabraVieja = "algo";
			ArrayList<String> lista = new ArrayList<String>();
			String cadena = "";
		
			TextInterpreter tx = new TextInterpreter();
			
			Collection<IunidadDeHabla> lista2 = null;
			
			lista2 = tx.modoLectura("d:\\palabras.txt", true);
			
			Iterator<IunidadDeHabla> it;
			
			it = lista2.iterator();
			
			int u =0;
			
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				cadena = iunidadDeHabla.getTextoEscrito();
				
				System.out.println(cadena);
				clave = new Clave();
				clave.setClave(cadena);
				reg = new RegistroNodo();
				reg.setClave(clave);
				if (u == 27) {
					int a = 0;
					a = 1;
				}
				if (tree.insertar(reg)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				}
				u++;
				tree.listar();
			}
			tree.listar();
			
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}
}
