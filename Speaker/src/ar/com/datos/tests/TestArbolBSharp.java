package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Main;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.btree.BSharp;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestArbolBSharp {
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(final String[] args) {
		int cantidad = 30;
		try {
			BSharp tree = new BSharp();
			String palabraVieja = "algo";
			ArrayList<String> lista = new ArrayList<String>();
			String cadena = "";
			for (int i = 0; i < cantidad; i++) {
				cadena = new String((palabraVieja+Integer.toString((int) (Math.random()*1000))));
				System.out.println(cadena);
				if (tree.insertar(cadena, i)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + Integer.toString(i));
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + Integer.toString(i));
				}
				tree.listar();
				lista.add(cadena);
			}
			
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
						
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}
}
