package ar.com.datos.grupo5.compresion.aritmetico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.compresion.ppmc.Contexto;

public class LogicaAritmetica {

	// private ArrayList<ParCharProb> listaCaracterProb;
	//private int bitsUnderflow;
	private Segmento intervalo;
	private String bits;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(LogicaAritmetica.class);
	
	private ParCharProb segmentar(Collection<ParCharProb> contexto,
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
			//System.out.println("Letra: "+elemAnterior.getSimboloUnicode()+", techo: "+elemAnterior.getTecho()+", piso: "+elemAnterior.getPiso());
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
			//System.out.println("Letra: "+elemActual.getSimboloUnicode()+", techo: "+elemActual.getTecho()+", piso: "+elemActual.getPiso());
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
	public String comprimir(Collection<ParCharProb> Contexto, char Caracter) {
		
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

	private ParCharProb segmentarMetodoAlternativoLZP(Collection<ParCharProb> contexto,
			StringBuffer binaryString) {
		ParCharProb par = null;

		//Establezco el piso y el techo hasta el momento
		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);
		
		String V = "";
		//Lo siguiente lo hago para no modificar el string real aún.
		//Valido si tengo que trabajar con una cadena normal o con una cadena con UndeFLow
		if (!this.intervalo.estadoOverFlow() && this.intervalo.estadoUnderFlow()) {
			//Si hay bits en UnderFlow es que no huvo overFlow!!! no bastaría con preguntar solo por underFlow? 
			V = this.intervalo.generarCadenaSinUndeFlow(binaryString);
		} else {
			
			V = binaryString.toString();
		}
		//System.out.println("Techo:            "+techo.get32BitsRepresentation());
		//System.out.println("Cadena bits de V: "+V);
		//System.out.println("Piso:             "+piso.get32BitsRepresentation());
		
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
		} 
	
		while (it.hasNext()) {
			// tomo el objeto actual
			elemActual = it.next();

			// el piso del elemento actual, sera el techo + 1 del elemento
			// anterior
			elemActual.setTecho(elemAnterior.getTecho() + 1, longitudInterv.getLongAsociado());

			//Preparo para la siguiente iteracion.
			elemAnterior = elemActual;
		}
		
		//A estas alturas ya generé todos los techos y piso, ahora comienzo a ver los bits que tengo.
		it = contexto.iterator();

		//boolean encontrado = false;
		elemAnterior = null;
		elemActual = null;		
		
		//Itero para encontrar el intervalo
		while (it.hasNext()) {
			elemActual = it.next();
			
			if (validarBitsIntervalo(elemActual,V)) {
				
			}
			
			
			
			elemAnterior = elemActual;
		}
		
		return par;
	}
	private boolean validarBitsIntervalo(ParCharProb elemActual, String v) {
		UnsignedInt techo = new UnsignedInt(elemActual.getTecho());
		UnsignedInt piso = new UnsignedInt(elemActual.getPiso());
		
		//Puntero al bit analizado
		int posBit = 0;
		
		
		
		return false;
	}

	/**
	 * Se encarga de buscar cual es el elemento que fue comprimido.
	 * @param contexto
	 * @param binaryString
	 * @return
	 * @throws Exception 
	 */
	private ParCharProb segmentar(Collection<ParCharProb> contexto,
			StringBuffer binaryString) throws Exception {
		
		//Establezco el piso y el techo hasta el momento
		UnsignedInt piso = this.intervalo.getPiso();
		UnsignedInt techo = this.intervalo.getTecho();
		
		UnsignedInt longitudInterv = techo.menos(piso);
		
		String V = "";
		
		try {
		
		//Valido si tengo que trabajar con una cadena normal o con una cadena con UndeFLow
		if (!this.intervalo.estadoOverFlow() && this.intervalo.estadoUnderFlow()) {
			//Si hay bits en UnderFlow es que no hubo overFlow!!! no bastaría con preguntar solo por underFlow?
				
			String cadena = this.intervalo.generarCadenaSinUndeFlow(binaryString);
			V = cadena.substring(0, 32);
			
		} else {
			//Corto el valor de 32bits
			V = binaryString.substring(0, 32);
		}

		}catch(Exception e){
			
			//si al momento de hacer un substring no hay suficientes datos se lanza excepcion
			throw new Exception();
			
		}
		//System.out.println("Techo:            "+techo.get32BitsRepresentation());
		//System.out.println("Cadena bits de V: "+V);
		//System.out.println("Piso:             "+piso.get32BitsRepresentation());

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
			//System.out.println("Letra: "+elemAnterior.getSimboloUnicode()+" Piso: "+elemAnterior.getPiso());
			//System.out.println("Techo: "+elemAnterior.getTecho()+" Valor: "+valor.getLongAsociado());
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
	public final Character descomprimir(Collection<ParCharProb> Contexto, StringBuffer cadenaBits){
		
		//System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
		//System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		// obtengo el segmento del espacio de probabilidades del contexto y luego busco el intervalo donde esta la letra
		ParCharProb subIntervalo;
		try {
			subIntervalo = this.segmentar(Contexto, cadenaBits);
		} catch (Exception e) {
			
			return null;
		}

		// con el sub intervalo de la letra seleccionada, hago un zoom, es decir
		// seteo los nuevos piso y techo del segmento mayor o espacio de
		// probabilidades
		this.intervalo.setPiso(subIntervalo.getPiso());
		this.intervalo.setTecho(subIntervalo.getTecho());

//		System.out.println("Techo: " + Long.toHexString(new Long(this.intervalo.getTecho().getLongAsociado())));
//		System.out.println("Piso: " + Long.toHexString(new Long(this.intervalo.getPiso().getLongAsociado())));
		
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
