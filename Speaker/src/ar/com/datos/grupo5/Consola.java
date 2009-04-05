package ar.com.datos.grupo5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.parser.ConsolaParser;

/**
 * @author LedZeppeling
 */
public class Consola extends Thread implements InterfazUsuario {

	/**
	 * Clase que implementa los métodos de la consola.
	 */
	private Object invocador = null;
	
	/**
	 * Para parsear la entrada de la consola.
	 */
	private ConsolaParser parser = new ConsolaParser();
	
	/**
	 * Caracter de la primera linea de la consola.
	 */
	private static final String PROMPCHAR = "> ";
	
	/**
	 * Comando para terminar la consola.
	 */
	private static final String ENDWORD = "FIN";
	
	/**
	 * 
	 */
	private final static Logger logger = Logger.getLogger(Consola.class);
	
	/**
	 * Este metodo se llama cuando el hilo comienza.
	 */
	public final void run() {
	
		this.leer();
	}
	/**
	 * Constructor.
	 * @param <clazz> .
	 * @param clazz .
	 */
	public <clazz> Consola(final Class < ? > clazz) {

			try {		
				invocador = clazz.getConstructors()[0]
				                                    .newInstance(new Object[0]);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Para que el que usa la consola pueda pedir datos al usuario.
	 * @param mensaje un mensaje.
	 * @return mensaje de resultado.
	 */
	public final String obtenerDatos(final String mensaje) {
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));

		if (mensaje != null && !mensaje.equals("")) {
			System.out.print(Consola.PROMPCHAR + mensaje);
		}
		
		String result = "";
		
		try {
			result = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Para que el que usa la consola pueda comunicar algo.
	 * @param mensaje El mensaje que va a la salida standar.
	 */
	public final void mensaje(final String mensaje) {
		
		System.out.println(Consola.PROMPCHAR + mensaje);
	}
	
	/**
	 * 
	 * @param mensaje .
	 */
	public final void mensajeSinSalto(final String mensaje) {
		System.out.print(mensaje);
	}
	
	/**
	 * Lee desde la entrada estandar y ejecuta el metodo correspondiente.
	 */
	@SuppressWarnings("unchecked")
	public final void leer() {
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		
		String linea = "";
		int pos = 0;
		/*Parametros para el metodo que voy a invocar.
		El primero es siempre de tipo interfaz "InterfazUsuario".
		Los demas son siempre String.*/
		Object[] params = null;
		//Tipo de parametros.
		Class[] paramsClass = null;
		//Auxiliar para armar los parametros.
		String[] paramsAux = null;
		//El comando que voy a ejecutar.
		String comando = "";
		try {
			
			//Mientras no se ponga "FIN" o "fin".
			do {
				//Escrivo un promp
				System.out.print(Consola.PROMPCHAR);
				//Me quedo esperando por lo que ingrese el usuario.
				linea = br.readLine();
				try {
					
					pos = linea.indexOf(" ");
					//Si es un comando con parametros.
					if (pos != -1) {
						paramsAux = parser.parseLine(linea.substring(pos + 1));
						params = new Object[paramsAux.length + 1];
						params[0] = this;
						for (int i = 0; i < paramsAux.length; i++) {
							params[i + 1] = paramsAux[i];
						}
						paramsClass = new Class[params.length];
						paramsClass[0] = InterfazUsuario.class;
						for (int i = 1; i < params.length; i++) {
							paramsClass[i] = String.class;
						}
						comando = linea.substring(0, pos);

					} else { // Si es un comando sin parametros.
						comando = linea.trim();
						paramsClass = new Class[1];
						paramsClass[0] = InterfazUsuario.class;
						params = new Object[1];
						params[0] = this;
					}

					try {
						
						//Intento llamar al metodo que pide el usuario.
						Object resultado = 
							invocador.getClass().getMethod(comando, paramsClass)
								.invoke(invocador, params);
						
						//Escribo el resultado.
						if (resultado != null) {
							System.out.println(Consola.PROMPCHAR
									+ resultado.toString());
							resultado = null;
						}
						
					} catch (Exception e) {
						//Indico que no conozco el comando y espero por otro.
						if (!comando.equalsIgnoreCase(Consola.ENDWORD)
								&& !comando.equals("")) {
							System.out.println(Consola.PROMPCHAR
									+ "No se encuentra el comando solicitado: "
									+ linea);
						}
						logger.error(e.getMessage(), e);
					}
					
				} catch (Exception e) {
					logger.error("Error: " + e.getMessage(), e);
				}
				
			} while (!linea.equalsIgnoreCase(Consola.ENDWORD));
			
			try {
				paramsClass = new Class[1];
				paramsClass[0] = InterfazUsuario.class;
				invocador.getClass().getMethod("quit", InterfazUsuario.class)
						.invoke(invocador, this);
				
			} catch (Exception e) {
				logger.error("Error: " + e.getMessage(), e);
			}	
			
		} catch (IOException e) {
			logger.error("Error: " + e.getMessage(), e);
		}
	}
}
