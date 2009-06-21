package ar.com.datos.grupo5.compresion.lz78;

import ar.com.datos.grupo5.compresion.ConversorABytes;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

public class CompresorLZ78 implements Compresor {

	private TablaLZ78 tabla;
    /**
     * Se elegió como caracter para indicar el Clearing
     * al primer caracter no imprimible
     */
	private final char caracterClearing = 1;

	private int ultimoCodigo;
	
	private String ultimaDescompresion;

	private final short codigosReservados = 255; //para los ASCII

	private final String caracterEOF = "^EOF";

	//codigo perteniciente al caracter de clearing, primer caracter no imprimible
	private final int codigoClearing = 65536;

	private StringBuffer bufferBits;

	//utilizado para la descompresion final
	//Indica que ha guardado datos en el buffer
	private int estadoInicio = 0;

	private int modo;

	//indica que se esta finalizando la sesion. (ultima descompresion)
	private boolean finalizaSesion;
	/**
	 * Constructor
	 *
	 */
	public CompresorLZ78() {
		/*ultimoCodigoCompresion = 0;
		 ultimoCodigoDescompresion = 0;
		 */
		ultimoCodigo = 0;
		estadoInicio = 0;

	}

	/**
	 * Inicializa la tabla con los primeros 255 caracteres unicode
	 * @param modo, indica el modo en que se quiere iniciar la tabla
	 * 0 para compresion
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
	 * Devuelve el codigo asociado
	 * @param valor asociado al codigo buscado
	 * @return int -1 si n(ParLZW)o).getValor() o se encuentra el codigo
	 */
	private int buscaCodigoCompresion(String valor) {
		Integer codigo = (Integer) tabla.get(valor);
		if (codigo == null) {
			return -1;
		}
		return codigo.intValue();
	}

	/**
	 * Devuelve el valor  asociado a un codputigo
	 * @param valor asociado al codigo buscado
	 * @return String null si no se encuentra el codigo en la tabla
	 */
	private String buscaValorDescompresion(int valor) {
		String codigo = (String) tabla.get(new Integer(valor));
		return codigo;
	}

	/**
	 * Comprime la cadena pasa en LZ78
	 * @param cadena, cadena a comprimir
	 * @throws SessionException, si la sesion no fue iniciada
	 * @return String, string con la cadena comprimida, expresa en bits
	 */
	public String comprimir(String cadena) throws SessionException {
		if (estadoInicio == 0)
			throw new SessionException("Error!: Sesion no iniciada");
		if (ultimoCodigo == 0) {
			this.modo = 0;
			limpiaTabla(this.modo);
		}
		String textoComprimidoEnBytes = new String();
		String textoComprimido = new String();
		int posicion = 0;
		String anterior = new String();
		String actual = new String();
		int ultimoMatch = -1;
		boolean clearing = false;
		while (posicion <= cadena.length()) {
			clearing = false;
			if (posicion == 0 && (cadena.length() > 1)) {
				actual = anterior + cadena.substring(0, 2);
				posicion++;
				//ver si vale la pena palabra long 1
			} else if (posicion < cadena.length()) {
				actual = anterior;
				if ((int)cadena.charAt(posicion) != 0)
					actual +=Character.toString(cadena.charAt(posicion));
			}
			else
				actual = anterior + this.caracterEOF;
			int codigo = buscaCodigoCompresion(actual);
			if (codigo >= 0) {
				//esta en la tabla
				ultimoMatch = codigo;
				anterior = actual;
			} else {
				//no esta en la tabla
				if (!actual.contains(this.caracterEOF)) {
					if (ultimoCodigo == codigoClearing) {
						//Clearing parcial
						clearingParcial();
						clearing = true;
						textoComprimido += completaStringBinaria(Integer
								.toBinaryString('1'));
					}
					
					if (cadena.length() != actual.length() && ((int)actual.charAt(0) !=0)) {
						ultimoCodigo++;
						tabla.put(actual, ultimoCodigo);
					} 
				}
				//me quedo con el ultimo
				anterior = Character.toString(actual
						.charAt(actual.length() - 1));
				//Emito el primer caracter del actual
				if (ultimoMatch < 0) {
					textoComprimidoEnBytes += completaStringBinaria(Integer
							.toBinaryString(actual.charAt(0)));
					textoComprimido += actual.charAt(0);
				} else {
					if (!actual.contains(this.caracterEOF)) {
						textoComprimido += Integer.toString(ultimoMatch);
						textoComprimidoEnBytes += completaStringBinaria(Integer
								.toBinaryString(ultimoMatch));
						
					} else {
						//ultimo Caracter a comprimir
						int longDif = actual.length()- this.caracterEOF.length();
						String ultimoActual =actual.substring(0,longDif);
						textoComprimido += ultimoActual;
						if (!tabla.containsKey(ultimoActual)) {
							ultimoCodigo++;
							tabla.put(ultimoActual, ultimoCodigo);
						}
						for (int i = 0; i < longDif; i++) {
							textoComprimidoEnBytes += completaStringBinaria(Integer
									.toBinaryString(actual.charAt(i)));
							
						}
						//ver si emitir caracter Clearing
					}

					ultimoMatch = -1;
				}

			}
			if (clearing && !actual.contains(this.caracterEOF)) {
				textoComprimidoEnBytes += completaStringBinaria(Integer
						.toBinaryString(this.caracterClearing));
				textoComprimido += this.caracterClearing;
			}

			posicion++;
		}
		return textoComprimidoEnBytes;

	}

	/**
	 * Descomprime una String expresada en bits, comprimida con LZ78.
	 * @param datoscomprimidos, String expresada en bits a descomprimir.
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
			//sumo lo que tenia
			StringBuffer bitsAdescomprimir = bufferBits
					.append(datoscomprimidos);
			datoscomprimidos = bitsAdescomprimir;
			bufferBits = new StringBuffer();
		}
		String textoDescomprimido = new String();
		int posicion = 0;
		String anterior = new String();
		String actual = new String();
		String ultimoValor = new String();
		//while (posicion < bitsAdescomprimir.length()) {
		int bitsAtomar = cantidadDeBitsDescompresion();
		while (posicion < datoscomprimidos.length()) {
			actual = new String();
			if ((datoscomprimidos.length() < posicion + bitsAtomar)) {
				if (!finalizaSesion) {
					this.bufferBits.append(datoscomprimidos.substring(posicion,
							datoscomprimidos.length()));
					// devuelvo lo que tenia
				
				} 					//completa bits
				return textoDescomprimido;
		
			}
			String binaria = datoscomprimidos.substring(posicion, posicion
					+ bitsAtomar);
			int caracter = ConversorABytes.binaryStringtoInt(binaria);
			if (caracter > codigosReservados) {
				//numero
				if (caracter > ultimoCodigo + 2)
					//ocurrio algo inesperado
					break;
				ultimoValor = buscaValorDescompresion(caracter);
				if (ultimoValor == null) {
					String nuevoValor = buscaValorDescompresion(caracter - 1);
					if (nuevoValor != null)
						ultimoValor = anterior
								+ Character.toString(nuevoValor
										.charAt(nuevoValor.length() - 1));
					else
						ultimoValor = anterior + anterior;
				}
				actual = Character.toString(ultimoValor.charAt(0));
			} else {
				//letra
				if (caracter == 1) {
					//clearing
					clearingParcial();
					anterior = new String();
					actual = ultimaDescompresion;
					ultimaDescompresion = new String();

				} else {
					if (caracter == 0) {
						//caracteres de relleno
						this.bufferBits.append(datoscomprimidos.substring(
								posicion + bitsAtomar, datoscomprimidos
										.length())); 
						break;
					}

					if ((char) caracter == '\n') {
						//termino una linea
						//limpio el buffer por las dudas
						//bufferBits = new StringBuffer();
						
						//guardo lo que quedo menos el enter.

						this.bufferBits.append(datoscomprimidos.substring(
								posicion + bitsAtomar, datoscomprimidos
										.length()));
						// ultimaDescompresion = actual+'\n';
						if (anterior.isEmpty())
							anterior = ultimaDescompresion;
						ultimaDescompresion = new String();
						
						//if (posicion != 0) {
							if (!tabla.containsValue(anterior + actual + '\n')
									&& !anterior.isEmpty() && ((posicion - bitsAtomar)!=0)) {
									/*&& (!anterior.isEmpty() || !actual
											.isEmpty())) {*/
								ultimoCodigo++;

								tabla.put(ultimoCodigo,
										(anterior + actual + '\n'));
							}
						//}
						textoDescomprimido +='\n';
			
						break;
					}

					actual = Character.toString((char) caracter);
				}
			}
			//Por si hubo una salida con enter anteriormente
			//if ((!ultimoValor.isEmpty()) && anterior.isEmpty() ) {
			if ((!ultimaDescompresion.isEmpty()) && anterior.isEmpty()) {
				//se habia salido por un \n
				anterior = ultimaDescompresion;
			}
			
			if (!tabla.containsValue(anterior + actual)
					&& (!anterior.isEmpty())) {
				//no estaba en tabla.
				ultimoCodigo++;
				bitsAtomar = cantidadDeBitsDescompresion();
				this.tabla.put(ultimoCodigo, (anterior + actual));
				if (ultimoValor.isEmpty()) {
					textoDescomprimido += actual;
				} else {
					// ultimo fue un numero
					textoDescomprimido += ultimoValor;
					actual = ultimoValor;
					ultimoValor = new String();
				}
			} else {
				//estaba en tabla
				if (anterior.isEmpty()) {
					//fue el primero
					if (ultimoValor.isEmpty() && caracter != 1) {
						textoDescomprimido += actual;
						actual = anterior + actual;
					} else if (caracter != 1) {
						//hago un paso mas, es primero y numero
						actual = ultimoValor;
						textoDescomprimido += ultimoValor;
						ultimoValor = new String();
					}
				}
				else 
					actual = anterior + actual;

			}

			anterior = actual;
			ultimaDescompresion = anterior;
			posicion += bitsAtomar;
		}
		return textoDescomprimido;
	}

	public String finalizarSession() {
		String textoFinal = new String();
		if (estadoInicio == 1) {
			//quedaron cosas para descomprimir
			
			while (!isBufferZero()) {
				
				finalizaSesion = true;
				try {
					textoFinal += descomprimir(new StringBuffer());
				} catch (SessionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
		}
		limpiaTabla(modo);
		estadoInicio = 0;
		ultimaDescompresion = new String();
		return textoFinal;

	}

	public void iniciarSesion() {
		//limpiaTabla();
		estadoInicio = 1;
		ultimaDescompresion = new String();
		bufferBits = new StringBuffer();
		ultimoCodigo = 0;
		finalizaSesion = false;

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

	private int cantidadDeBitsCompresion() {
		int resultado = ((int) (Math.log(ultimoCodigo) / Math.log(2)) + 1);
		if (resultado < 9)
			resultado = 9;
		return resultado;

	}

	private int cantidadDeBitsDescompresion() {
		int resultado = ((int) (Math.log(ultimoCodigo +1) / Math.log(2)) + 1);
		if (resultado < 9)
			resultado = 9;
		return resultado;
	}

	private String completaStringBinaria(String binaria) {
		String ceros = new String();
		int cantidadDeBits = 0;
		if (this.modo == 0) {
			cantidadDeBits = cantidadDeBitsCompresion();
		}
		else
			cantidadDeBits = cantidadDeBitsDescompresion();

		for (int i = 0; i < cantidadDeBits - binaria.length(); i++)
			ceros += "0";
		return (ceros + binaria);
	}

	/**
	 * Determina si es la ultima linea
	 * @param bits , a pasar
	 * @return true si los bits son todos 0
	 */
	private boolean isBufferZero() {
		boolean esIgual = (this.bufferBits.length() == 0);
		StringBuffer ceros = new StringBuffer();
		for (int j = 0; j < bufferBits.length(); j++) {
			esIgual = (bufferBits.charAt(j) == '0');
			if (!esIgual)
				break;
		}
		return esIgual;
	}

}
