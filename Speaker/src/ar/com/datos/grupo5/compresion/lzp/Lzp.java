package ar.com.datos.grupo5.compresion.lzp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:24 a.m.
 */
public class Lzp implements Compresor {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Lzp.class);
	
	private IndiceContexto Indice;
	
	/**
	 * Contexto para las letra? porque carajo se llama Orden?
	 */
	private Orden letrasCtx;
	
	/**
	 * Contextos para las longitudes, para pasar al compresor aritmetico.
	 */
	private Contexto longitudesCtx;
	
	/**
	 * Motor aritmetico para los cararteres emitodos.
	 */
	private LogicaAritmetica motorAritCaracteres;
	
	/**
	 * Motor aritmetico para las longitudes.
	 */
	private LogicaAritmetica motorAritLongitudes;
	
	/**
	 * Contextos y posiciones.
	 */
	private ListaContextos listaContextos;
	
	/**
	 * Flag para saber si la session fue inicializada. 
	 */
	private boolean sesionIniciada = true;
	
	/**
	 * Ultimo Contexto, para saber que emito. 
	 */
	private String ultCtx;
	
	/**
	 * Archivo temporal de trabajo.
	 */
	private RandomAccessFile archivoTrabajo = null;
	
	/**
	 * Posicion en el archivo.
	 */
	private int posActual = 0;
	
	/**
	 * Esto undica que toda la cadena matcheo. 
	 */
	private boolean matchCompleto = false;
	
	/**
	 * Longitud de match
	 */
	private int longMatch = 0;

	public Lzp(){
		listaContextos = new ListaContextos();
		motorAritCaracteres = new LogicaAritmetica();
		motorAritLongitudes = new LogicaAritmetica();
		letrasCtx = new Orden();
		longitudesCtx = new Contexto();
		ultCtx = "";
		try {
			archivoTrabajo = new RandomAccessFile("./lzpTemp.txt", "rw");
			archivoTrabajo.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	public void imprimirHashMap() {
		if (listaContextos.size() == 0) {
			System.out.println("La lista de contextos estaba vacía");
			return;
		}
		System.out.println("Valores de la lista de contextos:");
		System.out.println(listaContextos.toString());
		}
	
	/**
	 * 
	 * @param cadena
	 */
	public String comprimir(String cadena) throws SessionException{
		if (!sesionIniciada) {
			throw new SessionException();
		}
		
		String resultado = "";

		//Trabajar con un StringBuffer es mas rapido.
		StringBuffer buffer = new StringBuffer(cadena);
		
		//Si no hay nada aca, entonces es la primera iteracion.
		if (ultCtx.length() == 0) {
			//char primero = buffer.charAt(0);
			//char segundo = buffer.charAt(1);

			// Genero el CTX.
			ultCtx = buffer.substring(0, 2);

			//TODO: Hay que emitir estos caracteres sin longitudes.
			
			// Luego de emitir, meto el primer contexto en la tabla. Pos 4
			// porque son 2 inicode de 2 bytes c/u.
			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;
			
			resultado = ultCtx + "0" + String.valueOf(cadena.charAt(2));
			ultCtx = cadena.substring(1, 3);
			
			//Saco los 3 primeros.
			buffer.delete(0, 3);
		}
		
		// Voy guardando en el archivo de trabajo lo que voy leyendo para luego
		// buscar match.
		try {
			long pos = archivoTrabajo.length();
			archivoTrabajo.seek(pos);
			//Escribo en el archivo temporal en unicode.
			byte[] bytes = cadena.getBytes(Constantes.CHARSET_UTF16 + "BE");
			//Parece que el getBytes pone un /0 al final.
			archivoTrabajo.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			//TODO: Hacer algo
			e.printStackTrace();
		}
		
		try {
			resultado += ComprimirInterno2(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	private String ComprimirInterno(StringBuffer cadena) throws IOException {
		
		StringBuffer result = new StringBuffer();
		char charActual = 0;
		char charAnterior = ultCtx.charAt(1);
		Integer posMatch = 0;
		int longMatchActual = 0;
		
		while (cadena.length() > 0){
			
			if (!matchCompleto) {
				// Leer de a uno e ir revisando y comprimiendo en la salida
				charAnterior = ultCtx.charAt(1);
				charActual = cadena.charAt(0);
				ultCtx = String.valueOf(charAnterior) + String.valueOf(charActual);
				//Sumo 1 posicion en el archivo.
				posActual += 2;
			}
			LOG.info("Ultimo Contexto: " + ultCtx);
			// Buscar el contexto...
			posMatch = listaContextos.getPosicion(ultCtx);
			if ( posMatch == null) {
				// Creo el contexto y emito con long de match 0
				listaContextos.setPosicion(ultCtx, posActual);
				result.append("0" + charActual);
				// Lo saco porque ya lo procese
				cadena.delete(0, 1);
				longMatch = 0;
			} else {
				//Busco la longitud de match.
				if (matchCompleto) {
					posMatch += (longMatch * 2);
					longMatchActual = longMatch(cadena, posMatch);
				} else {
					longMatchActual = longMatch(new StringBuffer(cadena.substring(1)), posMatch);
				}
				
				if (matchCompleto ) {
					longMatch += longMatchActual;
					posActual += (longMatchActual * 2) - 2;
				} else {
					longMatch += longMatchActual;
					charActual = cadena.charAt(longMatchActual + 1);
					result.append(String.valueOf(longMatch) + charActual);
					if (longMatchActual > 0) {
						posActual += (longMatchActual * 2) + 2;
						posMatch += listaContextos.getPosicion(ultCtx) + (longMatch*2) - 2;
						listaContextos.setPosicion(ultCtx, posMatch);
					}
					longMatch = 0;
				}
				// Lo saco porque ya lo procese
				cadena.delete(0, longMatchActual + 1);
			}

			// Actualizo el ultimo contexto
			
			// La pos del contexto que se modifico es:
			// posActual - (length(match) + 1)
		}
		
		return result.toString();
	}

	private String ComprimirInterno2(StringBuffer cadena) throws IOException {
		
		StringBuffer result = new StringBuffer();
		int longMatchActual = 0;
		int posMatch = 0;
		
		while (cadena.length() > 0){
			
			Integer pos = listaContextos.getPosicion(ultCtx);
			if ( pos == null) {
				posActual += 2;
				listaContextos.setPosicion(ultCtx, posActual);
				ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
				result.append("0" + cadena.substring(0, 1));
				cadena.delete(0, 1);
			} else {
				posMatch = listaContextos.getPosicion(ultCtx);
				if (posMatch > 0) {
					if (matchCompleto) {
						posMatch += longMatch * 2;
					}
					longMatchActual = longMatch(cadena, posMatch);
					if (matchCompleto) {
						longMatch += longMatchActual;
						break;
					} else {
						String match = cadena.substring(0, longMatchActual);
						ultCtx = String.valueOf(cadena.charAt(longMatchActual-1));
						cadena.delete(0, longMatchActual);
						ultCtx += String.valueOf(cadena.charAt(0));
						result.append(String.valueOf(longMatch + longMatchActual) + String.valueOf(cadena.charAt(0)));
						cadena.deleteCharAt(0);
						posActual += (longMatch + longMatchActual) * 2;
						longMatch = 0;
					}
				} else {
					posActual += 2;
					ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
					result.append("0" + cadena.substring(0, 1));
					cadena.delete(0, 1);
					listaContextos.setPosicion(ultCtx, posActual);
				}
			}
		}
		
		return result.toString();
	}

	@Override
	public String descomprimir(String datos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizarSession() {
		sesionIniciada = false;
		File file = new File("./lzpTemp.txt");
		file.delete();
	}

	@Override
	public void iniciarSesion() {
		listaContextos = new ListaContextos();
		motorAritCaracteres = new LogicaAritmetica();
		motorAritLongitudes = new LogicaAritmetica();
		letrasCtx = new Orden();
		longitudesCtx = new Contexto();
		sesionIniciada = true;
		ultCtx = "";
	}
		
	/**
	 * Calcula la long de match de una cadena en un stream.
	 * 
	 * @param cadena
	 *            cadena para machear.
	 * @param pos
	 *            posicion a partir de la cual busco match.
	 * @return la longitud de match.
	 * @throws IOException 
	 */
	private int longMatch(StringBuffer cadena, long pos) throws IOException {
	
		//Buffer para leer del tamaño de la cadena.
		int longCadena = cadena.length() * 2;
		byte[] datos = new byte[longCadena];
		int leidos = 0;
		// Voy a la posicion en la cual puede haber un match.
		archivoTrabajo.seek(pos);
		LOG.info("Me posiciono en el archivo temporal: " + pos);
		int longitudMatch = 0;
		String charsLeidos = "";
		matchCompleto = false;
		
		// Leo la cantidad de caracteres que tiene la cadena de entrada, como
		// maximo.
		leidos = archivoTrabajo.read(datos, 0, longCadena);
		while ((leidos > 0) && (leidos <= longCadena) && 
				((this.longMatch + longitudMatch) < Constantes.MAX_LONGITD_MATCH)) {
			
			//Me armo un string con los datos leidos en UNICODE.
			charsLeidos = new String(datos, Constantes.CHARSET_UTF16 + "BE");
			LOG.info("Lei del temporal: " + charsLeidos);
			for (int i = 0; i < charsLeidos.length() && i < cadena.length(); i++) {
				if (charsLeidos.charAt(i) == cadena.charAt(i)) {
					longitudMatch++;
				} else {
					return longitudMatch;
				}
				if ((this.longMatch + longitudMatch) >= Constantes.MAX_LONGITD_MATCH) {
					return longitudMatch;
				}
			}
			leidos += archivoTrabajo.read(datos, 0, longCadena);
		}
		
		matchCompleto = true;
		return longitudMatch;
	}
	
}