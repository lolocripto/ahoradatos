package ar.com.datos.grupo5.compresion.lz78;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ar.com.datos.grupo5.compresion.ConversorABytes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.Conversiones;

public class CompresorLZ78 implements Compresor {

	private HashMap<String, Integer> tablaLZWCompresion;

	private HashMap<Integer, String> tablaLZWDescompresion;

	private final int capacidadTabla = 65535;

	private int ultimoCodigo;

	private final short codigosReservados = 255; //para los ASCII

	private final String caracterEOF = "^EOF";

	private final int codigoClearing = 256; //codigo perteniciente al caracter de clearing

	private int estadoInicio = 0;

	/**
	 * Constructor
	 *
	 */
	public CompresorLZ78() {
		ultimoCodigo = 0;
		estadoInicio = 0;

	}

	/**
	 * Inicializa la tabla con los primeros 255 caracteres unicode
	 *
	 */
	private void limpiaTabla() {
		int i = 0;
		tablaLZWCompresion = new HashMap<String, Integer>();
		tablaLZWDescompresion = new HashMap<Integer, String>();
		for (i = 0; i < codigosReservados; i++) {
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock
					.of(new Character(Character.toChars(i)[0]))) {
				String valor = new Character(Character.toChars(i)[0])
						.toString();
				tablaLZWCompresion.put(valor, i);
				tablaLZWDescompresion.put(i, valor);
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock
						.of(new Character(Character.toChars(i)[0]))) {
					String valor = new Character(Character.toChars(i)[0])
							.toString();
					tablaLZWCompresion.put(valor, i);
					tablaLZWDescompresion.put(i, valor);
				}
			}
		}

		i++;
		//tablaLZW.put(this.caracterEOF,new ParLZW(i,this.caracterEOF));
		tablaLZWCompresion.put(this.caracterEOF, i);
		tablaLZWDescompresion.put(i, this.caracterEOF);
		ultimoCodigo = i;

	}

	/**
	 * Devuelve el codigo asociado
	 * @param valor asociado al codigo buscado
	 * @return int -1 si n(ParLZW)o).getValor() o se encuentra el codigo
	 */
	private int buscaCodigoCompresion(String valor) {
		Integer codigo = tablaLZWCompresion.get(valor);
		if (codigo == null) {
			return -1;
		}
		return codigo.intValue();
	}

	/**
	 * Devuelve el valor  asociado a un codigo
	 * @param valor asociado al codigo buscado
	 * @return String null si no se encuentra el codigo en la tabla
	 */
	private String buscaValorDescompresion(int valor) {
		String codigo = tablaLZWDescompresion.get(new Integer(valor));
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
		String textoComprimidoEnBytes = new String();
		String textoComprimido = new String();
		int posicion = 0;
		String anterior = new String();
		String actual = new String();
		int ultimoMatch = -1;
		while (posicion <= cadena.length()) {

			if (posicion == 0 && (cadena.length() > 1)) {
				actual = anterior + cadena.substring(0, 2);
				posicion++;
				//ver si vale la pena palabra long 1
			} else if (posicion < cadena.length())
				actual = anterior + cadena.charAt(posicion);
			else
				actual = anterior + this.caracterEOF;
			int codigo = buscaCodigoCompresion(actual);
			if (codigo >= 0) {
				//esta en la tabla
				ultimoMatch = codigo;
				anterior = actual;
			} else {
				//no esta en la tabla
				ultimoCodigo++;
				if (!actual.contains(this.caracterEOF)) {
					this.tablaLZWCompresion.put(actual, ultimoCodigo);
				}
				//me quedo con el ultimo
				anterior = Character.toString(actual
						.charAt(actual.length() - 1));
				//Emito el primer caracter del actual
				if (ultimoMatch < 0) {
					textoComprimidoEnBytes += ConversorABytes
							.charToBinaryString(actual.charAt(0));
					textoComprimido += actual.charAt(0);

				} else {
					if (!actual.contains(this.caracterEOF)) {
						textoComprimido += Integer.toString(ultimoMatch);
						textoComprimidoEnBytes += ConversorABytes
								.shortToBinaryString(ultimoMatch);
					} else {
						//ultimo Caracter a comprimir
						textoComprimido += actual.substring(0, actual.length()
								- this.caracterEOF.length());
						int longDif = actual.length()
								- this.caracterEOF.length();
						for (int i = 0; i < longDif; i++) {
							textoComprimidoEnBytes += ConversorABytes
									.charToBinaryString(actual.charAt(i));
						}

					}

					ultimoMatch = -1;
				}

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
	public String descomprimir(StringBuffer datoscomprimidos) throws SessionException {
		if (estadoInicio == 0)
			throw new SessionException("Error!: Sesion no iniciada");
		
		String anterior = new String();
		String actual = new String();
		String ultimoValor = new String();
		String textoDesComprimido = new String();
		int posicion = 0;
		int ultimoMatch = -1;
		int caracter = -1;
		conversionBitToByte conversor = new conversionBitToByte();
		while (posicion < datoscomprimidos.length()) {
			if (posicion < datoscomprimidos.length()) {
				conversor.setBits(datoscomprimidos.substring(posicion,
						posicion + 16));
				caracter = Conversiones.arrayByteToShort(conversor.getBytes());
			}
			if (caracter < 255 || posicion == datoscomprimidos.length()) {
				//era una letra
				if ((posicion + 16) < datoscomprimidos.length())
					actual = anterior + Character.toString((char) caracter);
				else
					actual = (char) caracter + this.caracterEOF;
				if (!tablaLZWDescompresion.containsValue(actual)) {
					//no esta en la tabla
					ultimoCodigo++;
					if (!actual.contains(this.caracterEOF)) {
						this.tablaLZWDescompresion.put(ultimoCodigo, actual);
						textoDesComprimido += actual
								.charAt(actual.length() - 1);
						anterior = Character.toString(actual.charAt(actual
								.length() - 1));
					} else {
						actual = actual.substring(0, actual.length()
								- this.caracterEOF.length());
						textoDesComprimido += actual;
					}
				} else {
					//esta en la tabla
					anterior = actual;
					if (posicion == 0) {
						textoDesComprimido += actual;
					}

				}

			} else {
				//era un numero
				ultimoValor = buscaValorDescompresion(caracter);
				if (ultimoValor != null)
					actual = anterior + ultimoValor.charAt(0);
				else
					actual = anterior;
				if (!tablaLZWDescompresion.containsValue(actual) /*&& ultimoValor !=null*/) {
					//no esta en la tabla
					ultimoCodigo++;
					if (!actual.contains(this.caracterEOF)) {
						this.tablaLZWDescompresion.put(ultimoCodigo, actual);
					}
					textoDesComprimido += ultimoValor;
					anterior = ultimoValor;
				} else {
					//estaba en tabla
					if ((actual == anterior) && (ultimoValor == null)) {
						if (caracter - 2 > this.codigosReservados) {
							String fix = buscaValorDescompresion(caracter - 1);
							actual = actual + fix.charAt(fix.length() - 1);
						} else
							actual = anterior + actual;
						textoDesComprimido += actual;
						ultimoCodigo++;
						tablaLZWDescompresion.put(ultimoCodigo, actual);
					}
					anterior = actual;

				}

			}
			//recorre de a 2 bytes
			posicion += 16;
		}
		return textoDesComprimido;
	}

	public String finalizarSession() {
		limpiaTabla();
		estadoInicio = 0;
		return "";

	}

	public void iniciarSesion() {
		limpiaTabla();
		estadoInicio = 1;

	}

	public void imprimirHashMap() {
		// TODO Auto-generated method stub

	}
}
