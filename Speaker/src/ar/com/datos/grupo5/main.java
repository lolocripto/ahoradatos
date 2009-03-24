package ar.com.datos.grupo5;

import org.apache.log4j.Logger;

/**
 * Esta clase es de ejemplo.
 */
public class main {

	/**
	 * El constructor lo agrego para que checstyle no me rompa las bolas.
	 */
	public main() {
		super();
	}
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(main.class);

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(String[] args) {
		
		//Creo la consola y le paso la clase que ejecuta los metodos.
		Consola consola = new Consola(Ejecutador.class);
		
		//Me quedo leyendo la entrada.
		consola.leer();
		
	}

}
