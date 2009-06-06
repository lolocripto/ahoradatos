package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Main;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.btree.BStar;
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
//					"20", "22", "40", "55", "23", "21", "24",
//					"25", "26", "43", "45", "23", "17", "241",
//					"242", "37", "38", "16", "41", "47", "68",
//					"34", "48", "100", "200", "150", "151", "44"
//					};
//				
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
//			for (int i = 0; i < claves.length; i++) {
//				
//				clave = new Clave();
//				reg = new RegistroNodo();
//				
//				System.out.println("Busco: " + claves[i]);
//				clave.setClave(claves[i]);
//				reg = tree.buscar(clave);
//				if (reg != null) {
//					System.out.println("Encontrada la clave: " + reg.getClave().getClave());
//				} else {
//					System.out.println("No se encontro la clave: " + claves[i]);
//				}
//			}
			int cantidad = 500;
			String claveStr = "";
			TextInterpreter tx = new TextInterpreter();
			
			Collection<IunidadDeHabla> lista2 = null;
			
			lista2 = tx.modoLecturaSinAlmacenamiento("palabras.txt", true);
			
			Iterator<IunidadDeHabla> it;
			
			it = lista2.iterator();
			
			int u =0;
			
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				String cadena = iunidadDeHabla.getTextoEscrito();
				
				System.out.println(cadena);
				clave = new Clave();
				clave.setClave(cadena);
				reg = new RegistroNodo();
				reg.setClave(clave);
				if (tree.insertar(reg)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				}
				u++;
				//tree.listar();
			}

			lista2 = tx.modoLecturaSinAlmacenamiento("d:\\poemasreturns.txt", true);
			it = lista2.iterator();
			
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				String cadena = iunidadDeHabla.getTextoEscrito();
				
				//System.out.println(cadena);
				clave = new Clave();
				clave.setClave(cadena);
				
				reg = tree.buscar(clave);
				if (reg != null) {
					System.out.println("Encontre: " + clave.getClave());
				} else {
					System.out.println("##########################No encontre: " + clave.getClave());
				}
				//Thread.sleep(50);
			}
			
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}
}
