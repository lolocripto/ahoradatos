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
		
		/* Ejemplos para poner mensajes de log */
//		logger.debug("Este es un mensaje de degug.");
//		logger.info("Este en un mensaje de informacion.");
//		logger.warn("Este es un mensaje de advertencia.");
//		logger.error("Este en un mensaje de error sin la excepcion.");
//		logger.error("Este en un mensaje de error con la excepcion.", new Exception("Error!!"));
//		logger.fatal("Aca se rompio todo.");
//		
		/* TODO Ejemplo de tarea pendiente.
		 * Este tipo de comentario se ve en la solapa "task",
		 * es muy util para no olvidarse de hacer las
		 * cosas que quedan colgadas.
		 * */
		
		//TODO Todo el trabajo practico.
		
		//Creo la consola y le paso la clase que ejecuta los metodos.
		Consola consola = new Consola(Ejecutador.class);
		
		//Me quedo leyendo la entrada.
		consola.leer();
		
	}

}
