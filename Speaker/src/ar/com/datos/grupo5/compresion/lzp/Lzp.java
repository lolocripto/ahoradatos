package ar.com.datos.grupo5.compresion.lzp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

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
	
	/**
	 * @return the ultCtx
	 */
	public final String getUltCtx() {
		return ultCtx;
	}

	/**
	 * @param ultCtx the ultCtx to set
	 */
	public final void setUltCtx(String ultCtx) {
		this.ultCtx = ultCtx;
	}

	public Lzp(){
		listaContextos = new ListaContextos();
		motorAritCaracteres = new LogicaAritmetica();
		motorAritLongitudes = new LogicaAritmetica();
		letrasCtx = new Orden();
		longitudesCtx = new Contexto();
		ultCtx = "";
		try {
			archivoTrabajo = new RandomAccessFile("./lzpTemp.txt", "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
		
		// Voy guardando en el archivo de trabajo lo que voy leyendo para luego
		// buscar match.
		try {
			archivoTrabajo.seek(archivoTrabajo.length());
			//Escribo en el archivo temporal en unicode.
			archivoTrabajo.write(cadena.getBytes(Charset.forName(Constantes.CHARSET_UTF16)));
		} catch (IOException e) {
			//TODO: Hacer algo
			e.printStackTrace();
		}

		//Trabajar con un StringBuffer es mas rapido.
		StringBuffer buffer = new StringBuffer(cadena);
		
		//Si no hay nada aca, entonces es la primera iteracion.
		if (ultCtx.length() == 0) {
			char primero = buffer.charAt(0);
			char segundo = buffer.charAt(1);

			// Genero el CTX.
			ultCtx = buffer.substring(0, 2);
			
			//Saco los 2 primeros.
			buffer.delete(0, 2);

			//TODO: Hay que emitir estos caracteres sin longitudes.
			
			// Luego de emitir, meto el primer contexto en la tabla. Pos 4
			// porque son 2 inicode de 2 bytes c/u.
			listaContextos.setPosicion(ultCtx, 4);
			posActual = 4;
		}
		
		try {
			resultado += ComprimirInterno(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultado;
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

	private String ComprimirInterno(StringBuffer cadena) throws IOException {
		
		String result = "";
		char charActual = 0;
		char charAnterior = ultCtx.charAt(1);
		String nuevoCtx = "";
		Integer posMatch = 0;
		int longMatchActual = 0;
		
		while (cadena.length() > 0){
			// Leer de a uno e ir revisando y comprimiendo en la salida
			charActual = cadena.charAt(0);
			nuevoCtx = String.valueOf(charAnterior) + String.valueOf(charActual);
			//Sumo 1 posicion en el archivo.
			posActual += 2;
			
			// Buscar el contexto...
			posMatch = listaContextos.getPosicion(nuevoCtx);
			if ( posMatch == null) {
				// Creo el contexto y emito con long de match 0
				listaContextos.setPosicion(nuevoCtx, posActual);
			} else {
				//Busco la longitud de match.
				longMatchActual = longMatch(cadena, posMatch);
				if (longMatchActual == cadena.length()) {
					matchCompleto = true;
					longMatch += longMatchActual;
				}
			}
			
			charAnterior = charActual;
			// Lo saco porque ya lo procese
			cadena.delete(0, 1);
			// Actualizo el ultimo contexto
			
			// La pos del contexto que se modifico es:
			// posActual - (length(match) + 1)
		}
		ultCtx = new String(nuevoCtx);
		return result;
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
	
		//Buffer para leer.
		byte[] datos = new byte[8];
		int leidos = 0;
		// Voy a la posicion en la cual puede haber un match.
		archivoTrabajo.seek(pos);
		String a = "";
		int longitudMatch = 0;
		String charsLeidos = "";
		int longCadena = cadena.length() * 2;

		//Leo algunos 4 caracters.
		leidos += archivoTrabajo.read(datos, 0, 8);
		while (leidos > 0 && leidos < longCadena) {
			
			//Me armo un string con los datos leidos en UNICODE.
			charsLeidos = new String(datos, Charset.forName(Constantes.CHARSET_UTF16));
			for (int i = 0; i < datos.length; i++) {
				if (charsLeidos.charAt(i) == cadena.charAt(i)) {
					longitudMatch++;
				} else {
					return longitudMatch;
				}
			}
			leidos = archivoTrabajo.read(datos, 0, 8);
		}
		
		return longitudMatch;
	}
	
}