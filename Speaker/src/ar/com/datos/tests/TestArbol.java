package ar.com.datos.tests;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Main;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestArbol {

	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(final String[] args) {
		
		try {
			BTree tree = new BStar();
			
			Clave clave = null;
			RegistroNodo reg = null;
			
			String[] claves = {
					"15", "30", "1", "10", "13", "50", "35", 
					"20", "22", "40", "55", "60", "21", "24",
					"25", "26", "43", "45", "23", "17", "241",
					"242", "37", "38", "16", "41", "47", "68",
					"34", "48", "100", "200", "150", "151", "44",
					};
				
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
