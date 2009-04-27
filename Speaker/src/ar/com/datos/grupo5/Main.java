package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

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
		/*
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
					
			((BStar) tree).listar();
		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}*/
		
		byte[] a = {0,0,0,1,0,0,0};
		ByteArrayInputStream bis = new ByteArrayInputStream(a);  
		DataInputStream dos = new DataInputStream(bis);
		try {
			System.out.println(dos.readInt());
			System.out.println(dos.readInt());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
