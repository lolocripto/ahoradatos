package ar.com.datos.grupo5;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.utils.Conversiones;

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
			
			clave.setClave("2");
			reg.setClave(clave);
			tree.insertar(reg);
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("4");
			reg.setClave(clave);
			tree.insertar(reg);
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("3");
			reg.setClave(clave);
			tree.insertar(reg);
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("1");
			reg.setClave(clave);
			tree.insertar(reg);
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("5");
			reg.setClave(clave);
			tree.insertar(reg);
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("0");
			reg.setClave(clave);
			tree.insertar(reg);
			
//			clave = new Clave();
//			reg = new RegistroNodo();
//			clave.setClave("7");
//			reg.setClave(clave);
//			tree.insertar(reg);
					
			((BStar) tree).listar();
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}

}
