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
		
		
		/*try {
			
			Consola consola = new Consola(Core.class);
			
			consola.start();
			
			consola.join();

		} catch (Exception e) {
			logger.error("Error en main: " + e.getMessage());
		}*/

		try {
			BTree tree = new BStar();
			
			Clave clave = new Clave();
			RegistroNodo reg = new RegistroNodo();
			
			clave.setClave("1");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [1]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("3");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [3]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("5");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [5]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("9");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [9]");
			((BStar) tree).listar();
			
//			clave = new Clave();
//			reg = new RegistroNodo();
//			clave.setClave("0");
//			reg.setClave(clave);
//			tree.insertar(reg);
//			System.out.println("Inserto [0]");
//			((BStar) tree).listar();
//			
//			clave = new Clave();
//			reg = new RegistroNodo();
//			clave.setClave("8");
//			reg.setClave(clave);
//			tree.insertar(reg);
//			System.out.println("Inserto [8]");
//			((BStar) tree).listar();
						
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}

}
