package ar.com.datos.grupo5.compresion.aritmetico;

import java.util.ArrayList;
import java.util.Iterator;

public class LogicaAritmetica {

	// private ArrayList<ParCharProb> listaCaracterProb;
	private int bitsUnderflow;
	private Segmento intervalo;

	
	private ParCharProb segmentar(ArrayList<ParCharProb> contexto,
			char caracterAcodificar) {

		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);

		// ordeno el contexto por orden alfabetico
		// Collections.sort(contexto);

		Iterator<ParCharProb> it = contexto.iterator();

		boolean encontrado = false;
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
		
		// obtengo el segmento del espacio de probabilidades asociado a la letra
		// que quiero emitir
		ParCharProb subIntervalo = this.segmentar(Contexto, Caracter);

		// con el sub intervalo de la letra seleccionada, hago un zoom, es decir
		// seteo los nuevos piso y techo del segmento mayor o espacio de
		// probabilidades
		this.intervalo.setPiso(subIntervalo.getPiso());
		this.intervalo.setTecho(subIntervalo.getTecho());

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

}
