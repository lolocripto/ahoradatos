package ar.com.datos.grupo5.compresion.lzp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.compresion.aritmetico.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.excepciones.CodePointException;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.CodePoint;
//import ar.com.datos.grupo5.utils.CodePoint;

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
	 * Para debug y que la salida no se vea en binario.
	 */
	public boolean simular = false;
	
	/**
	 * flag para saber si es compresion o descompresion.
	 */
	private boolean esCompresion = true;
	
	/**
	 * Lista con longitudes 
	 */
	private Orden listaLongitudes;
	
	/**
	 * Caracteres con contexto
	 */
	private Orden caracteresContexto;
	
	/**
	 * Compresor aritmetica
	 */
	private LogicaAritmetica motorAritmetico;
	
	/**
	 * Ultimo caracter incompleto.
	 */
	private Character ultIncompleto;
	
	/**
	 * @return the finalizada
	 */
	@Override
	public final boolean isFinalizada() {
		return finalizada;
	}

	public Lzp() {
		ultCtx = "";
		ultIncompleto = null;
		motorAritmetico = new LogicaAritmetica();
		listaLongitudes = new Orden();
		caracteresContexto = new Orden();
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

	private void iniciarOrdenes() {
	
		//Cargo la lista de letras con contextos
		this.listaLongitudes.crearContexto("");
		Contexto ctx = this.listaLongitudes.getContexto("");
		for (int i = 0; i < 65534; i++) {
			ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
		}
		ctx.crearCharEnContexto(Constantes.EOF);
		
		Iterator<Character> it = Constantes.LISTA_CHARSET_LATIN.iterator();
		Character letra;
		while (it.hasNext()) {
			letra = it.next();
			//Verifica que exista el contexto
			if (!this.caracteresContexto.existeContexto(letra.toString())) {
				this.caracteresContexto.crearContexto(letra.toString());
				ctx = this.caracteresContexto.getContexto(letra.toString());
			} else {
				ctx = this.caracteresContexto.getContexto(letra.toString());
			}
			
			Iterator<Character> it2 = Constantes.LISTA_CHARSET_LATIN.iterator();
			//Lleno el contexto
			while (it2.hasNext()) {
				letra = it2.next();
				ctx.crearCharEnContexto(letra);
			}
			ctx.crearCharEnContexto(Constantes.EOF);
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
		
		esCompresion = true;
		String resultado = "";
		String resultado2 = "";
		Contexto ctx;

		Character letra;
		
		//Trabajar con un StringBuffer es mas rapido.
		StringBuffer buffer = new StringBuffer(cadena);
		
		//Si no hay nada aca, entonces es la primera iteracion.
		if (ultCtx.length() == 0) {
	
			// Genero el CTX.
			ultCtx = buffer.substring(0, 2);
			
			// Luego de emitir, meto el primer contexto en la tabla. Pos 4
			// porque son 2 inicode de 2 bytes c/u.
			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;
	
			/*
			resultado = motorAritCaracteres.comprimir(cadena.substring(0,1));
			resultado += motorAritCaracteres.comprimir(cadena.substring(1,2));
			if ( cadena.length() > 2) {
				resultado += motorAritLongitudes.comprimir("0");
				resultado +=  motorAritCaracteres.comprimir(String.valueOf(cadena.charAt(2)));
				
				resultado2 = ultCtx + "0" + String.valueOf(cadena.charAt(2));
				
				ultCtx = cadena.substring(1, 3);
			} else {
				resultado2 = ultCtx;
			}
			*/
			
			ctx = caracteresContexto.getContexto(new Character('\b').toString());
			ctx.actualizarProbabilidades();
			
			letra = cadena.substring(0, 1).charAt(0);
			resultado = motorAritmetico.comprimir(ctx.getArrayCharProb(), letra);
			
			ctx.actualizarContexto(letra);
			ctx = this.caracteresContexto.getContexto(letra.toString());
			ctx.actualizarProbabilidades();
			letra = cadena.substring(1, 2).charAt(0);
			
			resultado += this.motorAritmetico.comprimir(ctx.getArrayCharProb(), letra);
			
			ctx.actualizarContexto(letra);
			if ( cadena.length() > 2) {
				
				ctx = caracteresContexto.getContexto(letra.toString());
				ctx.actualizarProbabilidades();
				letra = cadena.charAt(2);
				resultado +=  motorAritmetico.comprimir(ctx.getArrayCharProb(), letra);
				ctx.actualizarContexto(letra);
				resultado2 = ultCtx + "0" + String.valueOf(cadena.charAt(2));
				ultCtx = cadena.substring(1, 3);
			} else {
				resultado2 = ultCtx;
			}

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
			
			String aux = ComprimirInterno(buffer);
			resultado += aux;

			resultado2 += aux;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return simular?resultado2:resultado;
	}

	static String ultimaLetra = "";
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
		String longitud = "0";
		String caracter = "";
		
		while (cadena.length() > 0) {

			posMatch = listaContextos.getPosicion(ultCtx);
			if ( posMatch == null) {
				posActual += 2;
				listaContextos.setPosicion(ultCtx, posActual);
				ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
				longitud = "0";
				caracter = cadena.substring(0, 1);
				cadena.delete(0, 1);
			} else {
				
				if (matchCompleto) {
					posMatch += longMatch * 2;
				}
				longMatchActual = longMatch(cadena, posMatch);
				if (matchCompleto && longMatchActual > 0) {
					longMatch += longMatchActual;
					ultimaLetra = cadena.substring(cadena.length() -1);
					break;
				} else if (longMatchActual > 0){
					posActual += 2;
					listaContextos.setPosicion(ultCtx, posActual);
					//String match = cadena.substring(0, longMatchActual);
					ultCtx = String.valueOf(cadena.charAt(longMatchActual-1));
					cadena.delete(0, longMatchActual);
					ultCtx += String.valueOf(cadena.charAt(0));
					longitud = String.valueOf(longMatch + longMatchActual);
					caracter = String.valueOf(cadena.charAt(0));
					cadena.deleteCharAt(0);
					posActual += ((longMatch + longMatchActual) * 2);
					longMatch = 0;
				} else {
					posActual += 2;
					listaContextos.setPosicion(ultCtx, posActual);
					if (longMatch > 0) {
						posActual += ((longMatch + longMatchActual) * 2);
						longitud = String.valueOf(longMatch);
						ultCtx = ultimaLetra + String.valueOf(cadena.charAt(0));
					} else {
						longitud = "0";
						ultCtx = String.valueOf(ultCtx.charAt(1)) + String.valueOf(cadena.charAt(0));
					}
					longMatch = 0;
					caracter = cadena.substring(0, 1);
					cadena.delete(0, 1);
				}
			}

			emitir(caracter, longitud, result);
			//result.append(motorAritLongitudes.comprimir(longitud));
			//result.append(motorAritCaracteres.comprimir(caracter));
			result2.append(longitud + caracter);
		}
		
		return simular?result2.toString():result.toString();
	}

	/**
	 * Comprime una longitud seguida de una letra
	 * @param caracter
	 * @param longitud
	 * @param result
	 */
	private void emitir(String caracter, String longitud, StringBuffer result) {
		
		Character letra = null;
		Contexto ctx = null;
		
		//Obtengo el contexto vacio
		ctx = listaLongitudes.getContexto("");
		ctx.actualizarProbabilidades();
		
		try {
			letra = CodePoint.getChar(Integer.valueOf(longitud));
		} catch (NumberFormatException e) {
			LOG.error("Error en formato de numero.", e);
		} catch (CodePointException e) {
			LOG.error("Error en codepoint.", e);
		}
		
		//Comprimo la longitud
		result.append(motorAritmetico.comprimir(ctx.getArrayCharProb(),letra));
		//Actualizo el contexto
		ctx.actualizarContexto(letra);
		//Ahora voy por la letra
		//Obtengo el contexto ultCtx[1]
		ctx = caracteresContexto.getContexto(ultCtx.substring(0,1));
		ctx.actualizarProbabilidades();
		//Comprimo la longitud
		result.append(motorAritmetico.comprimir(ctx.getArrayCharProb(),caracter.charAt(0)));
		//Actualizo el contexto
		ctx.actualizarContexto(caracter.charAt(0));
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	@Override
	public String descomprimir(StringBuffer cadena) {
		
		esCompresion = false;
		String result = new String();
		
		try {
			
			result = descomprimirInterno(cadena);
			
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (finalizada){
			return result;
		} else {
			return result.substring(0, result.length() - 2);
		}
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
		Contexto ctx;
		boolean sigoDesc = true;
		//Si no hay nada aca, entonces es la primera iteracion.
		if (listaContextos.size() == 0) {
			// Reseteo el ultimo contexto
			ultCtx = "";
			
			//Pide el primer caracter con contexto \b
			ctx = caracteresContexto.getContexto("\b");
			ctx.actualizarProbabilidades();
			//Comprimo la longitud
			devuelto = motorAritmetico.descomprimir(ctx.getArrayCharProb(),cadena).toString();
			//Actualizo el contexto
			ctx.actualizarContexto(devuelto.charAt(0));

			ultCtx = devuelto;
			
			//Pide la letra 2
			
			ctx = caracteresContexto.getContexto(devuelto.toString());
			
			ctx.actualizarProbabilidades();
			
			devuelto = motorAritmetico.descomprimir(ctx.getArrayCharProb(),cadena).toString();
			//cadena.delete(0, 2);
			if (devuelto == null){
				return ultCtx;
			} else if ( Constantes.EOF.equals(devuelto.charAt(0) ) ){
				finalizada = true;
				return ultCtx;
			}
			
			//Actualizo el contexto
			ctx.actualizarContexto(devuelto.charAt(0));
			
			ultCtx += devuelto;
			resultado = ultCtx;

			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;		
			try {
				archivoTrabajo.seek(0);
				//Escribo en el archivo temporal en unicode.
				byte[] escAux = ultCtx.getBytes(Constantes.CHARSET_UTF16);
				//Parece que el getBytes pone un /0 al final.
				archivoTrabajo.write(escAux, 0, escAux.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			posActual += 2;
		}
		int lon = 0;
		while ( sigoDesc ) {
			if ((ultIncompleto == null)||(ultIncompleto == 'C')) {
				//devuelto = motorAritCaracteres.descomprimir(cadena);
				if (ultIncompleto == null) {
					ctx = caracteresContexto.getContexto(resultado.substring(resultado.length() - 1));
					ctx.actualizarProbabilidades();
				}
				else{
					resultado = ultCtx;
					ultIncompleto = null;
					ctx = caracteresContexto.getContexto(ultCtx.substring(1));
				}

				Character aux = motorAritmetico.descomprimir(ctx.getArrayCharProb(),cadena);
				
				if (aux == null){
					ultIncompleto = 'C';
					ultCtx = resultado.substring(resultado.length() - 2);
					return resultado;
				//} else if ( Constantes.EOF.equals(algo.charAt(0)) ) {
				} else if ( Constantes.EOF.equals(aux) ) {				
					finalizada = true;
					return resultado;
				}
				devuelto = aux.toString();
				//Actualizo el contexto
				ctx.actualizarContexto(devuelto.charAt(0));
				
				resultado += devuelto;
				//LOG.info("Cadena descomprimida: " + resultado);
				ultCtx = resultado.substring(resultado.length() - 2);
				try {
					archivoTrabajo.seek(archivoTrabajo.length());
					//Escribo en el archivo temporal en unicode.
					byte[] escAux = ultCtx.substring(ultCtx.length() - 1).getBytes(Constantes.CHARSET_UTF16);
					//Parece que el getBytes pone un /0 al final.
					archivoTrabajo.write(escAux, 0, escAux.length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if ((ultIncompleto == null)||(ultIncompleto == 'L')) {
				//devuelto = motorAritCaracteres.descomprimir(cadena);
				ctx = listaLongitudes.getContexto("");
				if (ultIncompleto == null) {
					ctx.actualizarProbabilidades();
				} else {
					ultIncompleto = null;
					resultado = ultCtx;
				}
				//Comprimo la longitud
				Character aux = motorAritmetico.descomprimir(ctx.getArrayCharProb(),cadena);
	
				if (aux == null){
					ultIncompleto = 'L';
					ultCtx = resultado.substring(resultado.length() - 2);
					return resultado;
				} else if ( Constantes.EOF.equals(aux) ){
					finalizada = true;
					return resultado;
				}
				longitud = aux.toString();
				//Actualizo el contexto
				ctx.actualizarContexto(longitud.charAt(0));
				lon = CodePoint.getCodePoint(longitud.charAt(0));
				
				// Voy guardando en el archivo de trabajo lo que voy leyendo para luego
				// buscar match.
				//listaContextos.setPosicion(ultCtx, posActual);
				//posActual += 2;
				if (lon > 0) {
						// Matchea con un contexto.
					try {
						//posActual += 2 * lon;
						//archivoTrabajo.seek(archivoTrabajo.length());
						//Escribo en el archivo temporal en unicode.
						byte[] escAux = ultCtx.getBytes(Constantes.CHARSET_UTF16);
						//Parece que el getBytes pone un /0 al final.
						//archivoTrabajo.write(escAux, 0, escAux.length);
						//LOG.info("busco contexto: " + ultCtx);
						int pos = 0;
						try {
							pos = this.listaContextos.getPosicion(ultCtx);
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(1);
						}
						archivoTrabajo.seek(pos);
						// Cuanto leo?
						int totLect = lon;
						long longitudFD = archivoTrabajo.length();
						if ((longitudFD - pos) / 2 < lon) {
							totLect = (int) ((longitudFD - pos) / 2);
						}
						byte[] bytes = new byte[totLect * Constantes.SIZE_OF_SHORT];
						archivoTrabajo.read(bytes, 0, totLect * Constantes.SIZE_OF_SHORT);
						//ir replicando todos los cambios en la cadena, y luego 
						//escribirlos en el archivo
						devuelto = new String(bytes, Constantes.CHARSET_UTF16);
						archivoTrabajo.seek(archivoTrabajo.length());
						if (posActual==82){
							LOG.info("");
						}
						listaContextos.setPosicion(ultCtx, posActual);
						posActual += 2 * lon + 2;
						
						String persit = "";
						while (lon >0) {
							if (devuelto.length() <= lon){
								persit += devuelto;
								lon -= devuelto.length();
							} else {
								persit += devuelto.substring(0, lon);
								lon = 0;
							}
						}
						//Escribo en el archivo temporal en unicode.
						escAux = persit.getBytes(Constantes.CHARSET_UTF16);
						//Parece que el getBytes pone un /0 al final.
						archivoTrabajo.write(escAux, 0, escAux.length);
						resultado += persit;
						//LOG.info("Cadena descomprimida: " + resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					listaContextos.setPosicion(ultCtx, posActual);
					posActual += 2;
				}
				//listaContextos.setPosicion(ultCtx, posActual);
				//posActual += 2;
			}
		}
		return resultado;
	}
	
	@Override
	public String finalizarSession() {
		sesionIniciada = false;
		
		try {
			archivoTrabajo.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//File file = new File(ARCHIVO_TRABAJO);
		
		//file.delete();
		 
		String result = "";
		String result2 = "";
		Character letra;
		Contexto ctx;
		if (matchCompleto && esCompresion) {
			
			//Obtengo el contexto vacio
			ctx = listaLongitudes.getContexto("");
			
			ctx.actualizarProbabilidades();
			
			try {
				
				letra = CodePoint.getChar(longMatch);
				//Comprimo la longitud
				result += motorAritmetico.comprimir(ctx.getArrayCharProb(),letra);
				
				//Actualizo el contexto
				ctx.actualizarContexto(letra);

			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (CodePointException e) {
				e.printStackTrace();
			}
			
			//result += motorAritLongitudes.comprimir(String.valueOf(longMatch));
			result2 = String.valueOf(longMatch);
			
			//result += motorAritLongitudes.finalizarSession();
			//result += motorAritCaracteres.finalizarSession();
			
			ctx = caracteresContexto.getContexto(ultCtx.substring(1, 2));
			
			ctx.actualizarProbabilidades();

			//Comprimo la longitud
			result += motorAritmetico.comprimir(ctx.getArrayCharProb(),Constantes.EOF);
				
			//Actualizo el contexto
			ctx.actualizarContexto(Constantes.EOF);
			result += motorAritmetico.finalizarCompresion();
			
		} else {
			if (esCompresion) {
				//ctx = caracteresContexto.getContexto(ultCtx.substring(1, 2));
				ctx = listaLongitudes.getContexto("");
				
				ctx.actualizarProbabilidades();

				//Comprimo la longitud
				result += motorAritmetico.comprimir(ctx.getArrayCharProb(),Constantes.EOF);
					
				//Actualizo el contexto
				ctx.actualizarContexto(Constantes.EOF);
				result += motorAritmetico.finalizarCompresion();
			}
		}
		
		return simular?result2:result;
	}

	@Override
	public void iniciarSesion() {
		try {
			listaContextos = new ListaContextos();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		motorAritmetico = new LogicaAritmetica();
		listaLongitudes = new Orden();
		caracteresContexto = new Orden();
		iniciarOrdenes();
		sesionIniciada = true;
		finalizada = false;
		ultCtx = "";
		ultIncompleto = null;
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
		//LOG.info("Me posiciono en el archivo temporal: " + pos);
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
			//LOG.info("Lei del temporal: " + charsLeidos);
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