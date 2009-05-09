package ar.com.datos.grupo5;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * Esta clase es de ejemplo.
 */
public final class Main {

	/**
	 * El constructor lo agrego para que checkstyle no me rompa las bolas.
	 */
	private Main() {
		super();
	}
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(final String[] args) {
		
		/*
		try {
			
			Consola consola = new Consola(Core.class);
			
			consola.start();
			
			consola.join();

		} catch (Exception e) {
			logger.error("Error en main: " + e.getMessage());
		}*/

		try {
			BTree tree = new BStar();
			
			Clave clave = null;
			RegistroNodo reg = null;
			
			String[] claves = { "15", "30", "1", "10", "13", "50", "35", 
								"20", "22", "40", "55", "60", "21", "24",
								"25", "26", "43", "45", "23" , "17" };
				
			for (int i = 0; i < claves.length; i++) {
				
				clave = new Clave();
				reg = new RegistroNodo();
				
				System.out.println("Inserto [" + claves[i] + "]");
				clave.setClave(claves[i]);
				reg.setClave(clave);
				tree.insertar(reg);
				((BStar) tree).listar();
			}
			
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}

}
