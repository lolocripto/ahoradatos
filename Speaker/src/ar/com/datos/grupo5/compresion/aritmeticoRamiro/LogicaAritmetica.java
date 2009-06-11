package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.compresion.ppmc.Orden;

public class LogicaAritmetica {

	// private ArrayList<ParCharProb> listaCaracterProb;
	//private int bitsUnderflow;
	private Segmento intervalo;
	
	/**
	 * Mantiene la cantidad de bits que machean con el codigo a descomprimir.
	 */
	private int cantidadMatchBits;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(LogicaAritmetica.class);
	
	private ParCharProb segmentar(ArrayList<ParCharProb> contexto,
			char caracterAcodificar) {

		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);

		// ordeno el contexto por orden alfabetico
		// Collections.sort(contexto);

		Iterator<ParCharProb> it = contexto.iterator();

		//boolean encontrado = false;
		ParCharProb elemAnterior = null;
		ParCharProb elemActual = null;

		// inicializo el primer elemento
		if (it.hasNext()) {
			elemAnterior = it.next();
			elemAnterior.setTecho(piso, longitudInterv);
			if (elemAnterior.getSimboloUnicode() == caracterAcodificar) {
				return elemAnterior;
			}
		}

		while (it.hasNext()) {
			// tomo el objeto actual
			elemActual = it.next();

			// el piso del elemento actual, sera el techo + 1 del elemento
			// anterior
			elemActual.setTecho(elemAnterior.getTecho() + 1, longitudInterv.getLongAsociado());

			// me fijo si es el que debo codificar para no seguir segmentando
			// sin sentido
			if (elemActual.getSimboloUnicode() == caracterAcodificar) {
				return elemActual;
			}

			elemAnterior = elemActual;
		}

		return null;
	}

	/**
	 * Constructor que inicializa el segmento con todo el espacio disponible.
	 */
	public LogicaAritmetica() {

		// creo un intervalo, que por defecto tendra todo el espacio disponible
		// en 32 bits
		this.intervalo = new Segmento();

	}

	/**
	 * Devuelve los bits obtenidos de la compresion del caracter actual en el
	 * contexto pasado por parametro
	 * 
	 * @param Contexto
	 *            lista de objetos del tipo ParCharProb que contienen un
	 *            caracter y la probabilidad del mismo en el contexto
	 * @param Caracter
	 *            caracter leido que pretende codificarse
	 * @return un string con los bits correspondientes.
	 */
	public String comprimir(ArrayList<ParCharProb> Contexto, char Caracter) {
		
		System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
		System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		// obtengo el segmento del espacio de probabilidades asociado a la letra
		// que quiero emitir
		ParCharProb subIntervalo = this.segmentar(Contexto, Caracter);

		// con el sub intervalo de la letra seleccionada, hago un zoom, es decir
		// seteo los nuevos piso y techo del segmento mayor o espacio de
		// probabilidades
		this.intervalo.setPiso(subIntervalo.getPiso());
		this.intervalo.setTecho(subIntervalo.getTecho());

		System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
		System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		
		// con los nuevos piso y techo, estoy en condiciones de proceder a la
		// normalizacion y emitir bits en el proceso.
		return this.intervalo.normalizar();

	}

	/**
	 * Realiza las tareas finales de la compresion emitiendo los bits restantes
	 * y resetea la estructuras para comenzar una nueva compresion o
	 * descompresion.
	 * 
	 * @return bits restantes
	 */
	public String finalizarCompresion() {

		String emision = this.intervalo.emitirRestoBits();
		this.intervalo.resetear();
		return emision;
	}

	/**
	 * Se encarga de buscar cual es el elemento que fue comprimido.
	 * @param contexto
	 * @param binaryString
	 * @return
	 */
	private ParCharProb segmentar(ArrayList<ParCharProb> contexto,
			String binaryString) {
		
		//Establezco el piso y el techo hasta el momento
		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);
		
		this.cantidadMatchBits = 0;

		// ordeno el contexto por orden alfabetico
		// Collections.sort(contexto);

		Iterator<ParCharProb> it = contexto.iterator();

		//boolean encontrado = false;
		ParCharProb elemAnterior = null;
		ParCharProb elemActual = null;

		// inicializo el primer elemento
		if (it.hasNext()) {
			elemAnterior = it.next();
			elemAnterior.setTecho(piso, longitudInterv);
			//Valido el match
		}

		//Si tiene un solo elemento entonces directamente devuelvo ese elemento
		//no actualizo los bits dado que al ser un solo elemento entonces no
		if (contexto.size() == 1) {
			//Valido el match
			//FIXME: Esto es asi? tengo que validar si hay un solo elemento?
			 
			this.validarMatch(elemAnterior.getPiso(),elemAnterior.getTecho(), binaryString);
			this.actualizarBinaryString(binaryString);
			return elemAnterior;
		}

		ParCharProb mejorMatch = null;

		while (it.hasNext()) {
			// tomo el objeto actual
			elemActual = it.next();

			// el piso del elemento actual, sera el techo + 1 del elemento
			// anterior
			elemActual.setTecho(elemAnterior.getTecho() + 1, longitudInterv.getLongAsociado());

			//Valido el match
			if (this.validarMatch(elemAnterior.getPiso(),elemActual.getPiso(), binaryString)){
				mejorMatch = elemActual;
			}
			
			elemAnterior = elemActual;
		}
		this.actualizarBinaryString(binaryString);
		return mejorMatch;
	}
	
	/**
	 * Elimina los bits usados para decodificar el caracter.
	 * @param binaryString
	 */
	private void actualizarBinaryString(String binaryString) {
		binaryString = binaryString.substring(this.cantidadMatchBits);
	}

	/**
	 * . 
	 * @param pisoAnterior
	 * @param pisoActual
	 * @param binaryString
	 * @return True si el caracter analizado es el mas acorde a emitir, false si no.
	 */
	private boolean validarMatch(long pisoAnterior, long pisoActual, String binaryString) {
		// TODO Auto-generated method stub
		UnsignedInt anterior = new UnsignedInt(pisoAnterior);
		UnsignedInt actual = new UnsignedInt(pisoActual);
		int longitudMatchBits = 0;
		
		//Obtengo la representacion de cada piso
		String anterior32Rep = anterior.get32BitsRepresentation();
		String actual32Rep = actual.get32BitsRepresentation();
		
		for (int i = 0; i < anterior32Rep.length(); i++) {
			if(anterior32Rep.charAt(i) == actual32Rep.charAt(i) && actual32Rep.charAt(i) == binaryString.charAt(i)){
				//Tengo un match entre techo, piso y string a descomprimir 
				longitudMatchBits++;
			} else {
				break;
			}
		}
		
		if (longitudMatchBits > this.cantidadMatchBits) {
			this.cantidadMatchBits = longitudMatchBits;
			return true; 
		}
		return false;
	}

	/**
	 * Recibe un BinaryString del cual va a leer los bits y los va modificar
	 * dependiendo de los bits que use.
	 * @param cadenaBits bit emitidos en la compresion.
	 * @return El caracter encontrado
	 */
	public final Character descomprimir(ArrayList<ParCharProb> Contexto, String cadenaBits){
		
		System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
		System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		// obtengo el segmento del espacio de probabilidades del contexto y luego busco el intervalo donde esta la letra
		
		ParCharProb subIntervalo = this.segmentar(Contexto, cadenaBits);

		// con el sub intervalo de la letra seleccionada, hago un zoom, es decir
		// seteo los nuevos piso y techo del segmento mayor o espacio de
		// probabilidades
		this.intervalo.setPiso(subIntervalo.getPiso());
		this.intervalo.setTecho(subIntervalo.getTecho());

		System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
		System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		
		// con los nuevos piso y techo, estoy en condiciones de proceder a la
		// normalizacion y emitir bits en el proceso.
		this.intervalo.normalizarDescompresion(cadenaBits);
		
		//Devuelvo el elemento descomprimido
		return subIntervalo.getSimboloUnicode();
	}
}
