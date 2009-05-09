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
			
			Clave clave = new Clave();
			RegistroNodo reg = new RegistroNodo();
			
			clave.setClave("15");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [15]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("30");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [30]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("1");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [1]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("10");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [10]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("13");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [13]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("50");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [50]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("35");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [35]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("20");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [20]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("22");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [22]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("40");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [40]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("50");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [50]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("55");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [55]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("60");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [60]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("21");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [21]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("24");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [24]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("25");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [25]");
			((BStar) tree).listar();
			
			clave = new Clave();
			reg = new RegistroNodo();
			clave.setClave("26");
			reg.setClave(clave);
			tree.insertar(reg);
			System.out.println("Inserto [26]");
			((BStar) tree).listar();
			
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}

}
