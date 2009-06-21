package ar.com.datos.grupo5.compresion.lz78;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

public class CompresorLZ78 implements Compresor {

	private TablaLZ78 tabla;
	
	private String anterior;

	/**
	 * Se elegió como caracter para indicar el Clearing al primer caracter no
	 * imprimible
	 */
	private final char caracterClearing = 1;

	private int ultimoCodigo;

	private int ultimaDescompresion;

	private final short codigosReservados = 255; // para los ASCII

	private final String caracterEOF = "^EOF";

	// codigo perteniciente al caracter de clearing, primer caracter no
	// imprimible
	private final int codigoClearing = 65536;

	private StringBuffer bufferBits;

	// utilizado para la descompresion final
	// Indica que ha guardado datos en el buffer
	private int estadoInicio = 0;
	
	private int bitsAemitir;

	private int modo;

	// indica que se esta finalizando la sesion. (ultima descompresion)
	private boolean finalizaSesion;

	// Debido a no linealidad entre compresion y descompresion.
	private int cantidadBitsDescompresion;

	/**
	 * Constructor
	 * 
	 */
	public CompresorLZ78() {
		/*
		 * ultimoCodigoCompresion = 0; ultimoCodigoDescompresion = 0;
		 */
		ultimoCodigo = 0;
		estadoInicio = 0;

	}

	/**
	 * Inicializa la tabla con los primeros 255 caracteres unicode
	 * 
	 * @param modo,
	 *            indica el modo en que se quiere iniciar la tabla 0 para
	 *            compresion
	 */
	private void limpiaTabla(int modo) {
		int i = 0;
		tabla = new TablaLZ78(modo);
		for (i = 0; i < codigosReservados; i++) {
			String valor = new String();
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock
					.of(new Character(Character.toChars(i)[0]))) {
				valor = new Character(Character.toChars(i)[0]).toString();
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock
						.of(new Character(Character.toChars(i)[0]))) {
					valor = new Character(Character.toChars(i)[0]).toString();
				}
			}
			tabla.put(i, valor);
		}
		ultimoCodigo = i;
	}


	/**
	 * Comprime la cadena pasa en LZ78
	 * 
	 * @param cadena,
	 *            cadena a comprimir
	 * @throws SessionException,
	 *             si la sesion no fue iniciada
	 * @return String, string con la cadena comprimida, expresa en bits
	 */
	public String comprimir(String cadena) throws SessionException {
		if (estadoInicio == 0)
			throw new SessionException("Error!: Sesion no iniciada");
		if (ultimoCodigo == 0) {
			this.modo = 0;
			limpiaTabla(this.modo);
		}
		int pos = 0;
		String textoComprimidoBits = new String();
		Character caracter;
		String actual = new String();
		if ( anterior.length() == 0 ) {
			anterior = cadena.substring(0,1);
			pos++;
		}
		
		while (pos < cadena.length()) {
			caracter = cadena.charAt(pos);
			actual = anterior + caracter;
			
			if (!tabla.containsKey(actual)) {
				//no estaba en la tabla
				ultimoCodigo++;
				tabla.put(ultimoCodigo, actual);
				textoComprimidoBits += emitir((Integer)tabla.get(anterior));
				if (isClearing()) {
					textoComprimidoBits += emitir(Character
							.getNumericValue(this.caracterClearing));

				}
				anterior = caracter.toString();
			}
			else { 
				//se encontraba en la tabla
				anterior = actual;
			}
			
			pos++;
		}
		return textoComprimidoBits;

	}

	
	/**
	 * Descomprime una String expresada en bits, comprimida con LZ78.
	 * 
	 * @param datoscomprimidos,
	 *            String expresada en bits a descomprimir.
	 * @return String, texto descomprimido.
	 */
	public String descomprimir(StringBuffer datoscomprimidos)
			throws SessionException {
		if (estadoInicio == 0)
			throw new SessionException("Error!: Sesion no iniciada");
		if (ultimoCodigo == 0) {
			this.modo = 1;
			limpiaTabla(this.modo);
		}
		if (bufferBits.length() > 0) {
			// cargo lo que quedo
			datoscomprimidos.insert(0, this.bufferBits);
			bufferBits = new StringBuffer();
		}
		if (datoscomprimidos.length() <  this.bitsAemitir ) {
			bufferBits.append(datoscomprimidos);
			return new String();
		}
		String textoDescomprimido = new String();
		if ( ultimaDescompresion == -1 ) {
			// era el primero
			ultimaDescompresion = Integer.parseInt(datoscomprimidos.substring(0,bitsAemitir),2);
			//borro lo que saque
			datoscomprimidos.delete(0, this.bitsAemitir);
			textoDescomprimido = (String) tabla.get(ultimaDescompresion);
		}
		int nuevaDescompresion = -1;
		while (datoscomprimidos.length() >= this.bitsAemitir) {
			cantidadDeBits();
			nuevaDescompresion = Integer.parseInt(datoscomprimidos.substring(0,bitsAemitir),2);
			datoscomprimidos.delete(0, this.bitsAemitir);
			if (nuevaDescompresion == this.caracterClearing)
				clearingParcial();
			this.anterior = (String) tabla.get(nuevaDescompresion);
			if (anterior == null ) {
				//caso especial, no se encuentra entrada
				//genero la nueva entrada 
				String nuevoValor = (String)tabla.get(ultimaDescompresion);
				anterior = nuevoValor + Character.toString(nuevoValor.charAt(0));
			}
			textoDescomprimido += anterior ;
			if (!tabla.containsValue((String)tabla.get(ultimaDescompresion)+anterior.charAt(0))) {
				ultimoCodigo++;
				tabla.put(ultimoCodigo,(String)tabla.get(ultimaDescompresion)+anterior.charAt(0));
				ultimaDescompresion = nuevaDescompresion;
				
			}
		}
		if (datoscomprimidos.length() < this.bitsAemitir) {
			//no queda para tomar el proximo.
			this.bufferBits.append(datoscomprimidos);
			datoscomprimidos.delete(0, datoscomprimidos.length());
		}
	
		return textoDescomprimido;
	}

	public String finalizarSession() {
		if (estadoInicio == 1 && this.modo == 0) {
			try {
				String textoFinal =  this.comprimir(Constantes.EOF.toString());
				return textoFinal;
			} catch (SessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}		
		
		limpiaTabla(modo);
		estadoInicio = 0;
		return "";

	}

	public void iniciarSesion() {
		// limpiaTabla();
		estadoInicio = 1;
		ultimaDescompresion = -1;
		bufferBits = new StringBuffer();
		ultimoCodigo = 0;
		finalizaSesion = false;
		anterior = new String();
		bitsAemitir = 9; //minimo

	}

	/**
	 * Realiza el clearing parcial sobre la tabla
	 * 
	 */
	private void clearingParcial() {
		limpiaTabla(modo);

	}

	public void imprimirHashMap() {
		// TODO Auto-generated method stub

	}

	public boolean isFinalizada() {
		// TODO Auto-generated method stub
		return (estadoInicio == 0);
	}

	private void cantidadDeBits() {
		int resultado ;
		
		if (this.modo == 0)
			resultado = ((int) (Math.log(ultimoCodigo) / Math.log(2)) + 1);
		else
			resultado = ((int) (Math.log(ultimoCodigo+2) / Math.log(2)) + 1);
		bitsAemitir = resultado;

	}
	private String completaStringBinaria(String binaria) {
		String ceros = new String();
		cantidadDeBits();
		for (int i = 0; i < bitsAemitir - binaria.length(); i++)
			ceros += "0";
		return (ceros + binaria);
	}

	/**
	 * Determina si es la ultima linea
	 * 
	 * @param bits ,
	 *            a pasar
	 * @return true si los bits son todos 0
	 */
	private boolean isZero(StringBuffer buffer) {
		boolean esIgual = (buffer.length() == 0);
		StringBuffer ceros = new StringBuffer();
		for (int j = 0; j < buffer.length(); j++) {
			esIgual = (buffer.charAt(j) == '0');
			if (!esIgual)
				break;
		}
		return esIgual;
	}
	private boolean isClearing() {
		// TODO Auto-generated method stub
		if ( ultimoCodigo == this.codigoClearing ) {
			clearingParcial();
			return true;
		}
		else
			 return false;
	}

	private String emitir(Integer codigo) {
		// TODO Auto-generated method stub
		return completaStringBinaria(Integer.toBinaryString(codigo));
	}

}
