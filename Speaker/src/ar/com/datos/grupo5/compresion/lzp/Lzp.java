package ar.com.datos.grupo5.compresion.lzp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmeticoRamiro.CompresorAritmetico;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.CodePoint;

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
	private CompresorAritmetico motorAritCaracteres;
	
	/**
	 * Motor aritmetico para las longitudes.
	 */
	private CompresorAritmetico motorAritLongitudes;
	
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
		motorAritCaracteres = new CompresorAritmetico();
		motorAritLongitudes = new CompresorAritmetico();
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
			resultado += ComprimirInterno(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param cadena
	 * @return
	 * @throws IOException
	 */
	private String ComprimirInterno(StringBuffer cadena) throws IOException {
		
		StringBuffer result = new StringBuffer();
		int longMatchActual = 0;
		Integer posMatch = null;
		
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
				if (posMatch != null) {
					if (matchCompleto) {
						posMatch += longMatch * 2;
					}
					longMatchActual = longMatch(cadena, posMatch);
					if (matchCompleto && longMatchActual > 0) {
						longMatch += longMatchActual;
						break;
					} else if (longMatchActual > 0){
						String match = cadena.substring(0, longMatchActual);
						ultCtx = String.valueOf(cadena.charAt(longMatchActual-1));
						cadena.delete(0, longMatchActual);
						ultCtx += String.valueOf(cadena.charAt(0));
						result.append(String.valueOf(longMatch + longMatchActual) + String.valueOf(cadena.charAt(0)));
						cadena.deleteCharAt(0);
						posActual += (longMatch + longMatchActual) * 2;
						longMatch = 0;
					} else {
						posActual += 2;
						ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
						result.append("0" + cadena.substring(0, 1));
						cadena.delete(0, 1);
						listaContextos.setPosicion(ultCtx, posActual);
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

	/**
	 * 
	 * @param cadena
	 * @return
	 */
	@Override
	public String descomprimir(String cadena) {
		
		String result = new String();
		int longMatchActual = 0;
		int posMatch = 0;
		
		// Los primeros 2 son literales, y con ellos armo el prim contexto
		// -> como sabe el aritmetico que son literales?
		while (cadena.length() > 0){
			//aun quedan strings
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 * @throws SessionException
	 */
	public String descomprimirInterno(String cadena) throws SessionException{
		if (!sesionIniciada) {
			throw new SessionException();
		}
		
		String resultado = "";
		String longitud = "";
		
		//Si no hay nada aca, entonces es la primera iteracion.
		if (ultCtx.length() == 0) {
			// Genero el CTX.
			ultCtx = motorAritCaracteres.descomprimir(cadena);
			ultCtx += motorAritCaracteres.descomprimir(cadena);

			resultado = ultCtx;

			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;
		}
		longitud = motorAritLongitudes.descomprimir(cadena);
		//long pos = archivoTrabajo.length();
		long lon = CodePoint.getCodePoint(longitud.charAt(0));		
		// Voy guardando en el archivo de trabajo lo que voy leyendo para luego
		// buscar match.
		posActual += 2;
		if (lon > 0) {
				// Matchea con un contexto.
			try {
				
				// TODO Arreglar lo que va aca
				int pos = this.listaContextos.getPosicion(ultCtx);
				archivoTrabajo.seek(pos);
				// Cuanto leo?
				
				byte[] bytes = null;
				archivoTrabajo.read(bytes, 0, posActual - (int)pos);
				//ir replicando todos los cambios en la cadena, y luego 
				//escribirlos en el archivo
			} catch (IOException e) {
				//TODO: Hacer algo
				e.printStackTrace();
			}
		} else {
			// No matchea con ningun contexto -> long 0 
			
			resultado += motorAritCaracteres.descomprimir(cadena);
			ultCtx = resultado.substring(resultado.length() - 2);
			listaContextos.setPosicion(ultCtx, posActual);
		}
		//resultado += descomprimirInterno(cadena);
		return resultado;
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
		motorAritCaracteres = new CompresorAritmetico();
		motorAritLongitudes = new CompresorAritmetico();
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