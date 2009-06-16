package ar.com.datos.grupo5.compresion.lz78;

import java.util.HashMap;

import ar.com.datos.grupo5.compresion.ConversorABytes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.Conversiones;

public class CompresorLZ78 implements Compresor {

	private HashMap<String, Integer> tablaLZWCompresion;

	private HashMap<Integer, String> tablaLZWDescompresion;

	private final char caracterClearing = 1; 

	private int ultimoCodigoCompresion;
	private int ultimoCodigoDescompresion;
    //Guarda la ultima descompresion al salir del descompresor
	// se requiere por el tamaño de buffer variable
	private String ultimaDescompresion;
	private final short codigosReservados = 255; //para los ASCII

	private final String caracterEOF = "^EOF";
	//codigo perteniciente al caracter de clearing, primer caracter no imprimible
	private final int codigoClearing = 65536; 

	private StringBuffer bufferBits ;
	private int estadoInicio = 0;

	private Short anterior;

	private String resultado;

	/**
	 * Constructor
	 *
	 */
	public CompresorLZ78() {
		ultimoCodigoCompresion = 0;
		ultimoCodigoDescompresion = 0;
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
		ultimoCodigoCompresion = i;
		ultimoCodigoDescompresion = i;

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
		boolean clearing = false;
		while (posicion <= cadena.length()) {
            clearing = false;
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
				
				if (!actual.contains(this.caracterEOF)) {
					if (ultimoCodigoCompresion == codigoClearing ) {
						//Clearing parcial
						clearingParcial();
						clearing = true;
					}
					ultimoCodigoCompresion++;
					System.out.println("inserta: "+ultimoCodigoCompresion+","+actual);
					
					this.tablaLZWCompresion.put(actual, ultimoCodigoCompresion);
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
						//ver si emitir caracter Clearing
					}

					ultimoMatch = -1;
				}

			}
			if ( clearing  && !actual.contains(this.caracterEOF)) {
				textoComprimidoEnBytes += ConversorABytes
				.charToBinaryString(this.caracterClearing);
				
				textoComprimido += this.caracterClearing;
				}
	
			posicion++;
		}
		System.out.println("quedo "+textoComprimido);
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
		StringBuffer bitsAdescomprimir = new StringBuffer();
		if (datoscomprimidos.length() < 16) {
			this.bufferBits.append(datoscomprimidos);
		//	System.out.println("guarda "+bufferBits);
			
			return new String();
		}
		else {
		//	System.out.println("era   "+bufferBits);
		//	System.out.println("erA "+datoscomprimidos+" long "+bitsAdescomprimir.length());
			bitsAdescomprimir = bufferBits.append(datoscomprimidos);
		//	System.out.println("quedo "+bitsAdescomprimir+" long "+bitsAdescomprimir.length());
			bufferBits = new StringBuffer();
		}
		System.out.println("descomprime "+bitsAdescomprimir.length());
		String anterior = new String();
		String actual = new String();
		String ultimoValor = new String();
		String textoDesComprimido = new String();
		int posicion = 0;
		int caracter = -1;
		conversionBitToByte conversor = new conversionBitToByte();
		while (posicion < bitsAdescomprimir.length()) {
			
			if (posicion < bitsAdescomprimir.length()) {
				
				if (posicion + 16 > bitsAdescomprimir.length()) {
					StringBuffer aux = new StringBuffer();
					//completa a 2 bytes
					for (int i = 0 ; i<(posicion + 16 - bitsAdescomprimir.length()); i++)
						aux.append("0");
					aux.append(bitsAdescomprimir);
					bitsAdescomprimir = aux;
				//	System.out.println("long "+bitsAdescomprimir.length()+"p"+posicion);	
					conversor.setBits(bitsAdescomprimir.substring(posicion,posicion+16));
				}
				else {
					conversor.setBits(bitsAdescomprimir.substring(posicion,
						posicion + 16));
				}
				caracter = Conversiones.arrayByteToShort(conversor.getBytes());
			}
			
			if (caracter < 255 || posicion == datoscomprimidos.length()) {
				//era una letra
				System.out.println("letra "+(char)caracter);
				if ( caracter == 1 ){
					//Hubo un Clearing de la tabla
					
					clearingParcial();
					actual = anterior;

				}
				else {

					if ((posicion + 16) < bitsAdescomprimir.length())
						actual = anterior + Character.toString((char) caracter);
					else {
						actual = (char) caracter + this.caracterEOF;
						System.out.println("es el puto EOF "+actual+","+ ultimoCodigoDescompresion);
						System.out.println("ant="+anterior);
						ultimoCodigoDescompresion++;
						
						System.out.println("inserta EOF: "+ultimoCodigoDescompresion+","+ anterior+actual.substring(0, actual.length()
								- this.caracterEOF.length()));
						this.tablaLZWDescompresion
						.put(ultimoCodigoDescompresion, anterior+actual.substring(0, actual.length()
								- this.caracterEOF.length()));
						
						ultimaDescompresion = actual.substring(0, actual.length()
								- this.caracterEOF.length());
					}
					
					if (!tablaLZWDescompresion.containsValue(actual)) {
						// no esta en la tabla

						
						if (!actual.contains(this.caracterEOF)) {
							ultimoCodigoDescompresion++;
							System.out.println("inserta: "+ultimoCodigoDescompresion+","+actual);
							this.tablaLZWDescompresion
									.put(ultimoCodigoDescompresion, actual);
							System.out.println("emite >> "+actual
									.charAt(actual.length() - 1));
							textoDesComprimido += actual
									.charAt(actual.length() - 1);
							anterior = Character.toString(actual.charAt(actual
									.length() - 1));
						} else {
							actual = actual.substring(0, actual.length()
									- this.caracterEOF.length());
							if (tablaLZWDescompresion.containsValue(anterior)
									&& (anterior.charAt(anterior.length() - 1) == actual
											.charAt(0))) {
								System.out.println("emite >> "+anterior.substring(0,anterior.length()-1));
								textoDesComprimido += anterior.substring(0,anterior.length()-1);
							}
							System.out.println("emite >> "+actual);
							textoDesComprimido += actual;
						}
					} else {
						// esta en la tabla
						if (ultimaDescompresion.isEmpty()) {
							anterior = actual;
							if (posicion == 0) {
								System.out.println("emite >> "+actual);
								textoDesComprimido += actual;
							}
								
						}
						else {
							System.out.println("emite >> "+actual);
						   textoDesComprimido +=actual;
						   ultimoCodigoDescompresion++;
						   System.out.println("inserta CO: "+ultimoCodigoDescompresion+","+(ultimaDescompresion+actual));
						   tablaLZWDescompresion.put(ultimoCodigoDescompresion,ultimaDescompresion+actual);
						   ultimaDescompresion = new String();
						   anterior = actual;
						   
						   
						}

					}
				}

			} else {
				//era un numero
				System.out.println("numero " + caracter);
				ultimoValor = buscaValorDescompresion(caracter);
				if (!ultimaDescompresion.isEmpty()){
					//quedo algo de antes
				
					if (ultimoValor != null)
					   actual = ultimaDescompresion+ultimoValor.charAt(0);
					else
						actual = ultimaDescompresion;
					if (!tablaLZWDescompresion.containsValue(actual)) {
						ultimoCodigoDescompresion++;
						System.out.println("inserta UC: "+ultimoCodigoDescompresion+","+actual);
						this.tablaLZWDescompresion.put(ultimoCodigoDescompresion, actual);
						System.out.println("emite >> "+actual.charAt(actual.length()-1));
						textoDesComprimido+=actual.charAt(actual.length()-1);
						ultimaDescompresion= new String();
					}
				}
				
				if (ultimoValor != null && posicion !=0 ) {
					actual = anterior + ultimoValor.charAt(0);
					
					
				}	
				else {
					if (posicion == 0)
						actual = ultimoValor;
					else
						actual = anterior;
				}
				System.out.println("estado: ");
				System.out.println("actual: "+actual);
				System.out.println("anteri: "+anterior);
				System.out.println("ulvalo: "+ultimoValor);
				if (actual.contains(new Character('\n').toString())) {
					// fin de linea
					
					textoDesComprimido +=ultimoValor;
					actual = ultimoValor;
					
				}
				if (!tablaLZWDescompresion.containsValue(actual) ) {
					//no esta en la tabla
					ultimoCodigoDescompresion++;
					System.out.println("pos "+posicion+" de "+ bitsAdescomprimir.length());
						if (!actual.contains(this.caracterEOF)) {
							System.out.println("inserta b b: "+ultimoCodigoDescompresion+","+actual);
							this.tablaLZWDescompresion.put(ultimoCodigoDescompresion, actual);
						}
						else {
							if (tablaLZWDescompresion.containsValue(anterior)
								&& (anterior.charAt(anterior.length() - 1) == actual
										.charAt(0)))
							System.out.println("emite >> " + anterior.substring(0,anterior.length()-1));
							textoDesComprimido += anterior.substring(0,anterior.length()-1);
						}	
						System.out.println("emite >> " +ultimoValor);
						textoDesComprimido += ultimoValor;
						anterior = ultimoValor;
						if (posicion+16 ==  bitsAdescomprimir.length()) 
							ultimaDescompresion = ultimoValor;
					
				} else {
					//estaba en tabla
					if ((actual == anterior) && (ultimoValor == null)) {
						if (caracter - 2 > this.codigosReservados) {
							String fix = buscaValorDescompresion(caracter - 1);
							if (fix !=null)
							actual = actual + fix.charAt(fix.length() - 1);
						} else
							actual = anterior + actual;
						/*System.out.println("emite >> "+actual);
						textoDesComprimido += actual;
						ultimoCodigoDescompresion++;
						System.out.println("inserta nhjhjh: "+ultimoCodigoDescompresion+","+actual);
						tablaLZWDescompresion.put(ultimoCodigoDescompresion, actual);
						*/
					}
					if (posicion == 0)
						textoDesComprimido += actual.charAt(actual.length()-1);
					anterior = actual;

				}

			}
			//recorre de a 2 bytes
			
			posicion += 16;
		}
		System.out.println("va quedando .. "+textoDesComprimido);
		return textoDesComprimido;
	}

	public String finalizarSession() {
		limpiaTabla();
		estadoInicio = 0;
		ultimaDescompresion= new String();
		return "";

	}

	public void iniciarSesion() {
		limpiaTabla();
		estadoInicio = 1;
		ultimaDescompresion= new String();
		bufferBits = new StringBuffer();

	}
	/**
	 * Realiza el clearing parcial sobre la tabla
	 *
	 */
    private void clearingParcial() {
    	limpiaTabla();
    	ultimoCodigoCompresion++;
    	ultimoCodigoDescompresion++;
    	
    }
	public void imprimirHashMap() {
		// TODO Auto-generated method stub

	}

	public boolean isFinalizada() {
		// TODO Auto-generated method stub
		return (estadoInicio == 0);
	}
	
	public String compresionRamiro(StringBuffer datoscomprimidos) throws SessionException{
		
		//Si no tengo la session iniciada lanzo la exception
		if (estadoInicio == 0)
			throw new SessionException("Error!: Sesion no iniciada");

		/*if (this.anterior == null)
			this.anterior = null;
		*/
		short indiceAnteriorDeLaTablaADosChars;
		short actual = 0;
		String contexto;
		//TODO: ver condicion
		if (1==1) {
			this.resultado = "";
		}
		conversionBitToByte conversor = new conversionBitToByte();
		
		if (this.bufferBits.length() > 0) {
			datoscomprimidos.insert(0,this.bufferBits);
			this.bufferBits = new StringBuffer();
		}
		
		//Si los datos del buffer mas los datos de entrada son menores
		//a la malla de bits entonces devuelvo null ya que no puedo seguir
		if (datoscomprimidos.length() < 16) {
			this.bufferBits = new StringBuffer(datoscomprimidos);
			datoscomprimidos.delete(0, datoscomprimidos.length());
			return null;
		}
		
		while (datoscomprimidos.length() > 16) {
			try {
				conversor.setBits(datoscomprimidos.substring(0, 16));
			} catch (Exception e) {
				//Datos insuficiones
				//TODO: fijar estado actual
				return new String();
			}
			actual = Conversiones.arrayByteToShort(conversor.getBytes());
			
			if( this.identificarValorChar(actual) ) {
				//Es una letra
				String buscar;
				if(anterior == null) {
					//FIXME: VALIDAR QUE ESTE BIEN
					this.resultado += Short.toString(actual);
					contexto = Short.toString(actual);
					anterior = actual;
					continue;
				} else {
					if (this.identificarValorChar(anterior)) {
						//Tengo el string a buscar en el map
						buscar = contexto + Short.toString(actual);
						
						//TODO: Ver que buscar es el nuevo contexto, creo que esto no va
						
						if(!this.tablaLZWDescompresion.containsValue(buscar)) {
							//No esta en la tabla
							//Agrego buscar a la tabla
							this.tablaLZWDescompresion.put(this.tablaLZWDescompresion.size() + 1, buscar);
							//Emiti el char leido
							this.resultado += Short.toString(actual);
							//actualizo mi anterior
							contexto = Short.toString(actual);	
						} else {
							contexto += buscar;	
						}
						anterior = actual;
					} else {
						//tenemos un numero como anterior
						buscar = this.tablaLZWDescompresion.get(anterior) + Short.toString(actual);
						if(!this.tablaLZWDescompresion.containsValue(buscar)) {
							//No esta en la tabla
							//Agrego buscar a la tabla
							this.tablaLZWDescompresion.put(this.tablaLZWDescompresion.size() + 1, buscar);
							//Emiti el char leido
							this.resultado += Short.toString(actual);
						}
						//actualizo mi anterior
						anterior = actual;	
					}
				}
			} else {
				if (this.identificarValorChar(anterior)) {
					//es numero
					String buscar = this.tablaLZWDescompresion.get(anterior) + this.tablaLZWDescompresion.get(actual).substring(0, 1);
					if (this.tablaLZWDescompresion.containsValue(buscar)) {
						anterior = actual;
					} else {
					
					}
					/*
					 * agarro el valor del anterior concateno el primer caracter del numero nuevo
					 * busco en el hashmap
					 * No -> creo una nueva entrada en la tabla, emito el value de key numero actual
					 * 			anterior = posicion de la nueva entrada
					 * Si -> anterior = posicion del match
					 */
					
				} else {
					//es letra el anterior
					//es un numero
					/*
					 * busco el valor asociado a ese numero
					 * anterior + substring(0,1)
					 * busco en el hashmap
					 * No -> creo una nueva entrada en la tabla, 
					 * 			anterior = posicion de la nueva entrada
					 * Si -> anterior = posicion del match
					 */
					String buscar = Short.toString(actual) + this.tablaLZWDescompresion.get(anterior);
					if (this.tablaLZWDescompresion.containsValue(buscar)) {
						//anterior = actual;
					} else {
						//No lo contiene
						// emito el value de key numero
						this.resultado += this.tablaLZWDescompresion.get(actual);
						
						//Agrego el nuevo elemento a la tabla
						String temp = new String(Short.toString(anterior) + this.tablaLZWDescompresion.get(actual).substring(0, 1));
						this.tablaLZWDescompresion.put(this.tablaLZWDescompresion.size() + 1, temp);

						//anterior = posicion de la nueva entrada
						anterior = actual;
					}
	
				}
				
			}
		}
		return new String();
	}

	private boolean identificarValorChar(short actual) {
		if ( actual > 255 ) {
			return false;
		} else {
			return true;
		}
	}
}
