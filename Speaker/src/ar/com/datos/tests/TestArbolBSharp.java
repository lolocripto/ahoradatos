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
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestArbolBSharp {
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(final String[] args) {

		int cantidad = 300;
		try {
			
			
			
			BSharp tree = new BSharp();
			String palabraVieja = "algo";
			ArrayList<String> lista = new ArrayList<String>();
			String cadena = "";
		
			TextInterpreter tx = new TextInterpreter();
			
			Collection<IunidadDeHabla> lista2 = null;
			
			lista2 = tx.modoLecturaSinAlmacenamiento("/home/zeke/Escritorio/prueba", true);
			
			Iterator<IunidadDeHabla> it;
			
			it = lista2.iterator();
			
			int u =0;
			
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				cadena = iunidadDeHabla.getTextoEscrito();
				
				System.out.println(cadena);
				if (tree.insertar(cadena, u)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + Integer.toString(u));
				}
				u++;
				tree.listar();
				
			}
			
			
			
			it = lista2.iterator();
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				cadena = iunidadDeHabla.getTextoEscrito();
				
				RegistroFTRS reg = new RegistroFTRS();
				reg = tree.buscar(cadena);

				System.out.println(cadena);
				if (reg ==null) {

					System.out.println("no se encontro cadena");
				} else {
					System.out.println("se encontro la palabra: " + cadena + " puntero: " + reg.getBloqueListaInvertida());
				}
				u++;
				
				
			}
			
			
			
			
			
			
			
			
			
			
			
			
	/*		
			for (int i = 0; i < cantidad; i++) {
			//	cadena = new String((palabraVieja+Integer.toString((int) (Math.random()*1000))));
				cadena = new String((palabraVieja+Integer.toString(i)));
				System.out.println(cadena);
				if (tree.insertar(cadena, i)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + Integer.toString(i));
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + Integer.toString(i));
				}
<<<<<<< .mine
				Thread.sleep(50);
				tree.listar();
=======
			//	tree.listar();
>>>>>>> .r807
				lista.add(cadena);
			}
			tree.listar();
	*/		
			
/*			
			System.exit(0);
			RegistroFTRS reg;
			Iterator<String> it = lista.iterator();
			int i = 0;
			while (it.hasNext()) {
				cadena = it.next();
				reg = new RegistroFTRS();
				reg = tree.buscar(cadena);
				if (reg != null) {
					System.out.println("Registro distinto de null.");
					System.out.println("IdTermino leido: " + reg.getIdTermino());
					System.out.println("IdTermino pedido: " + Integer.toString(i));
				} else {
					System.out.println("Registro nulo, termino no encontrado.");
				}	
				i++;
			}
		*/
			
			
			
			
			
			
			
			tree.cerrar();
			
			
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}
}
