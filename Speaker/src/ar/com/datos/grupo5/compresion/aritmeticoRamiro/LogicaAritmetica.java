package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class LogicaAritmetica {

	// private ArrayList<ParCharProb> listaCaracterProb;
	//private int bitsUnderflow;
	private Segmento intervalo;
	private String bits;
	
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
			StringBuffer binaryString) {
		
		//Establezco el piso y el techo hasta el momento
		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);
		
		String V = "";
		//Valido si tengo que trabajar con una cadena normal o con una cadena con UndeFLow
		if (!this.intervalo.estadoOverFlow() && this.intervalo.estadoUnderFlow()) {
			//Si hay bits en UnderFlow es que no huvo overFlow!!! no bastaría con preguntar solo por underFlow? 
			V = this.intervalo.generarCadenaSinUndeFlow(binaryString);
		} else {
			//Corto el valor de 32bits
			V = binaryString.substring(0, 32);
		}
		
		//Lo convierto a un entero sin signo.
		UnsignedInt valor = new UnsignedInt(V);

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

			if (this.ValidarIntervalo(elemAnterior,valor)) {
				return elemAnterior;
			}
		} 
	
		while (it.hasNext()) {
			// tomo el objeto actual
			elemActual = it.next();

			// el piso del elemento actual, sera el techo + 1 del elemento
			// anterior
			elemActual.setTecho(elemAnterior.getTecho() + 1, longitudInterv.getLongAsociado());
			
			//Valido que el numero este entre el techo y el piso del elemento. Si esta es el elemento a emitir
			//sino sigo buscando
			if (this.ValidarIntervalo(elemActual, valor)) {
				return elemActual;
			}
			
			//Preparo para la siguiente iteracion.
			elemAnterior = elemActual;
		}
		return null;
	}
	
	private boolean ValidarIntervalo(ParCharProb elemAnterior, UnsignedInt valor) {
		if (elemAnterior.getPiso() <= valor.getLongAsociado() && elemAnterior.getTecho() >= valor.getLongAsociado()) {
			return true;
		}
		return false;
	}

	/**
	 * Elimina los bits usados para decodificar el caracter.
	 * @param binaryString
	 * @param cantidadMatchBits 
	 */
	private void actualizarBinaryString(StringBuffer binaryString, int cantidadMatchBits) {
		binaryString.delete(0, cantidadMatchBits);
	}

	/**
	 * Recibe un BinaryString del cual va a leer los bits y los va modificar
	 * dependiendo de los bits que use.
	 * @param cadenaBits bit emitidos en la compresion.
	 * @return El caracter encontrado
	 */
	public final Character descomprimir(ArrayList<ParCharProb> Contexto, StringBuffer cadenaBits){
		
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
		String emision = this.intervalo.normalizar();
		
		if (emision.length() > 0)  {
			//Corto la emision del string de bits
			this.actualizarBinaryString(cadenaBits, emision.length());
		}
		
		//Devuelvo el elemento descomprimido
		return subIntervalo.getSimboloUnicode();
	}
}
