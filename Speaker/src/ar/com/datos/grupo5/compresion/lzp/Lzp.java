package ar.com.datos.grupo5.compresion.lzp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmeticoRamiro.CompresorAritmetico;
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
	 * Flag para saber si compresion o descompresion fue finalizada. 
	 */
	private boolean finalizada = false;
	
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
	
	/**
	 * Archivo de trabajo temporal.
	 */
	private static String ARCHIVO_TRABAJO = "./lzp.tmp";

	/**
	 * @return the finalizada
	 */
	@Override
	public final boolean isFinalizada() {
		return finalizada;
	}

	public Lzp(){
		motorAritCaracteres = new CompresorAritmetico(1, true);
		motorAritLongitudes = new CompresorAritmetico(0, false);
		motorAritCaracteres.iniciarSesion();
		motorAritLongitudes.iniciarSesion();
		ultCtx = "";
		try {
			listaContextos = new ListaContextos();
			archivoTrabajo = new RandomAccessFile(ARCHIVO_TRABAJO, "rw");
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
	
			// Genero el CTX.
			ultCtx = buffer.substring(0, 2);

			//TODO: Hay que emitir estos caracteres sin longitudes.
			
			// Luego de emitir, meto el primer contexto en la tabla. Pos 4
			// porque son 2 inicode de 2 bytes c/u.
			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;
	
			resultado = motorAritCaracteres.comprimir(cadena.substring(0,1));
			resultado += motorAritCaracteres.comprimir(cadena.substring(1,2));
			resultado += motorAritLongitudes.comprimir("0");
			resultado +=  motorAritCaracteres.comprimir(String.valueOf(cadena.charAt(2)));
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
			byte[] bytes = cadena.getBytes(Constantes.CHARSET_UTF16);
			//Parece que el getBytes pone un /0 al final.
			archivoTrabajo.write(bytes, 0, bytes.length);
			
			resultado += ComprimirInterno(buffer);
		} catch (IOException e) {
			//TODO: Hacer algo
			e.printStackTrace();
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param cadena
	 * @return
	 * @throws IOException
	 * @throws SessionException 
	 */
	private String ComprimirInterno(StringBuffer cadena) throws IOException, SessionException {
		
		StringBuffer result = new StringBuffer();
		StringBuffer result2 = new StringBuffer();
		int longMatchActual = 0;
		Integer posMatch = null;
		String resultAux = "";
		
		while (cadena.length() > 0){
			
			posMatch = listaContextos.getPosicion(ultCtx);
			if ( posMatch == null) {
				posActual += 2;
				listaContextos.setPosicion(ultCtx, posActual);
				ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
				result2.append("0" + cadena.substring(0, 1));
				resultAux = motorAritLongitudes.comprimir("0");
				resultAux += motorAritCaracteres.comprimir(cadena.substring(0, 1));
				result.append(resultAux);
				cadena.delete(0, 1);
			} else {
				if (posMatch != null) {
					if (matchCompleto) {
						posMatch += longMatch * 2;
					}
					longMatchActual = longMatch(cadena, posMatch);
					if (matchCompleto && longMatchActual > 0) {
						longMatch += longMatchActual;
						break;
					} else if (longMatchActual > 0){
						//String match = cadena.substring(0, longMatchActual);
						ultCtx = String.valueOf(cadena.charAt(longMatchActual-1));
						cadena.delete(0, longMatchActual);
						ultCtx += String.valueOf(cadena.charAt(0));
						result2.append(String.valueOf(longMatch + longMatchActual) + String.valueOf(cadena.charAt(0)));
						resultAux = motorAritLongitudes.comprimir(String.valueOf(longMatch + longMatchActual));
						resultAux += motorAritCaracteres.comprimir(String.valueOf(cadena.charAt(0)));
						result.append(resultAux);
						cadena.deleteCharAt(0);
						posActual += (longMatch + longMatchActual) * 2;
						longMatch = 0;
					} else {
						posActual += 2;
						ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
						result2.append("0" + cadena.substring(0, 1));
						resultAux = motorAritLongitudes.comprimir("0");
						resultAux += motorAritCaracteres.comprimir(cadena.substring(0, 1));
						result.append(resultAux);
						cadena.delete(0, 1);
						listaContextos.setPosicion(ultCtx, posActual);
					}
				} else {
					posActual += 2;
					ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
					result2.append("0" + cadena.substring(0, 1));
					resultAux = motorAritLongitudes.comprimir("0");
					resultAux += motorAritCaracteres.comprimir(cadena.substring(0, 1));
					result.append(resultAux);
					cadena.delete(0, 1);
					listaContextos.setPosicion(ultCtx, posActual);
				}
			}
		}
		
		//return result2.toString();
		return result.toString();
	}

	/**
	 * 
	 * @param cadena
	 * @return
	 */
	@Override
	public String descomprimir(StringBuffer cadena) {
		String result = new String();
		
		// Solo la primera vez.
		listaContextos.clear();
		try {
			
			result = descomprimirInterno(cadena);
			
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 * @throws SessionException
	 */
	public String descomprimirInterno(StringBuffer cadena) throws SessionException{
		if (!sesionIniciada) {
			throw new SessionException();
		}
		
		String resultado = "";
		String longitud = "";
		String devuelto = "";
		boolean sigoDesc = true;
		//Si no hay nada aca, entonces es la primera iteracion.
		if (listaContextos.size() == 0) {
			// Reseteo el ultimo contexto
			ultCtx = "";
			// Genero el CTX.
			
			devuelto = motorAritCaracteres.descomprimir(cadena);
			ultCtx = devuelto;
			
			devuelto = motorAritCaracteres.descomprimir(cadena);
			if (devuelto == null){
				return ultCtx;
			} else if ( devuelto.charAt(0) == Constantes.EOF ){
				finalizada = true;
				return ultCtx;
			}
			ultCtx += devuelto;
			resultado = ultCtx;

			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;		
			longitud = motorAritLongitudes.descomprimir(cadena);
			if (longitud == null){
				return resultado;
			} else if ( longitud.charAt(0) == Constantes.EOF ){
				finalizada = true;
				return resultado;
			}
			// Que hago aca para descomprimir
			posActual += 2;
		}
		while ( sigoDesc ) {
			
			devuelto = motorAritCaracteres.descomprimir(cadena);
			if (devuelto == null){
				return resultado;
			} else if ( devuelto.charAt(0) == Constantes.EOF ){
				finalizada = true;
				return resultado;
			}
			resultado += devuelto;
			ultCtx = resultado.substring(resultado.length() - 2);
			listaContextos.setPosicion(ultCtx, posActual);
			posActual += 2;
			longitud = motorAritLongitudes.descomprimir(cadena);
			//long pos = archivoTrabajo.length();
			int lon = CodePoint.getCodePoint(longitud.charAt(0));		
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
					
					byte[] bytes = new byte[lon * Constantes.SIZE_OF_SHORT];
					archivoTrabajo.read(bytes, 0, lon * Constantes.SIZE_OF_SHORT);
					//ir replicando todos los cambios en la cadena, y luego 
					//escribirlos en el archivo
					resultado += bytes.toString();
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
		}
		return resultado;
	}
	
	@Override
	public String finalizarSession() {
		sesionIniciada = false;
		File file = new File("./lzpTemp.txt");
		file.delete();
		String result = "";
		
		motorAritCaracteres.finalizarSession();
		motorAritLongitudes.finalizarSession();
		
		if (matchCompleto) {
			
			result += String.valueOf(longMatch);
		}
		
		return result;
	}

	@Override
	public void iniciarSesion() {
		try {
			listaContextos = new ListaContextos();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		motorAritCaracteres = new CompresorAritmetico(1, true);
		motorAritLongitudes = new CompresorAritmetico(0, false);
		motorAritCaracteres.iniciarSesion();
		motorAritLongitudes.iniciarSesion();
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
			charsLeidos = new String(datos, Constantes.CHARSET_UTF16);
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