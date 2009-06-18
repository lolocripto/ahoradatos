package ar.com.datos.grupo5.compresion.lz78;

import java.util.HashMap;

import ar.com.datos.grupo5.compresion.ConversorABytes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.Conversiones;

public class CompresorLZ78 implements Compresor {
	/*
	private HashMap<String, Integer> tablaLZWCompresion;

	private HashMap<Integer, String> tablaLZWDescompresion;
	*/
	private TablaLZ78 tabla;
	private final char caracterClearing = 1; 
	private int ultimoCodigo;

	/*private int ultimoCodigoCompresion;
	private int ultimoCodigoDescompresion;
	*/
    //Guarda la ultima descompresion al salir del descompresor
	// se requiere por el tamaño de buffer variable
	
	private String ultimaDescompresion;
	private final short codigosReservados = 255; //para los ASCII

	private final String caracterEOF = "^EOF";
	//codigo perteniciente al caracter de clearing, primer caracter no imprimible
	private final int codigoClearing = 65536; 

	private StringBuffer bufferBits ;
	//utilizado para la descompresion final
	//Indica que ha guardado datos en el buffer
	private int estadoInicio = 0;
	private int modo ;

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
	 *
	 */
	private void limpiaTabla(int modo) {
		int i = 0;
		/*tablaLZWCompresion = new HashMap<String, Integer>();
		tablaLZWDescompresion = new HashMap<Integer, String>();
		*/
		tabla = new TablaLZ78(modo);
		for (i = 0; i < codigosReservados; i++) {
			String valor = new String();
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock
					.of(new Character(Character.toChars(i)[0]))) {
				valor = new Character(Character.toChars(i)[0])
						.toString();
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock
						.of(new Character(Character.toChars(i)[0]))) {
					valor = new Character(Character.toChars(i)[0])
							.toString();
				}
			}
			tabla.put(i, valor);
		}

		//i++;
		//tablaLZW.put(this.caracterEOF,new ParLZW(i,this.caracterEOF));
		//tablaLZWCompresion.put(this.caracterEOF, i);
		//tablaLZWDescompresion.put(i, this.caracterEOF);
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
	 * Devuelve el valor  asociado a un codigo
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
					if (ultimoCodigo == codigoClearing ) {
						//Clearing parcial
						clearingParcial();
						clearing = true;
					}
					ultimoCodigo++;
					if (cadena.length() != actual.length()) {
						System.out.println("inserta: "+ultimoCodigo+","+actual);
						tabla.put(actual, ultimoCodigo);
					}
					else 
						break;
				}
				//me quedo con el ultimo
				anterior = Character.toString(actual
						.charAt(actual.length() - 1));
				//Emito el primer caracter del actual
				if (ultimoMatch < 0) {
					
					textoComprimidoEnBytes += completaStringBinaria(Integer.toBinaryString(actual.charAt(0)));
					System.out.println("emite "+actual.charAt(0)+" >>"+completaStringBinaria(Integer.toBinaryString(actual.charAt(0))));
					textoComprimido += actual.charAt(0);
				} else {
					if (!actual.contains(this.caracterEOF)) {
						textoComprimido += Integer.toString(ultimoMatch);
						textoComprimidoEnBytes += completaStringBinaria(Integer.toBinaryString(ultimoMatch));
						System.out.println("emite "+ultimoMatch+" >>"+completaStringBinaria(Integer.toBinaryString(ultimoMatch)));
						//ConversorABytes.shortToBinaryString(ultimoMatch);
					} else {
						//ultimo Caracter a comprimir
						textoComprimido += actual.substring(0, actual.length()
								- this.caracterEOF.length());
						
						int longDif = actual.length()
								- this.caracterEOF.length();
						for (int i = 0; i < longDif; i++) {
							textoComprimidoEnBytes += completaStringBinaria(Integer.toBinaryString(actual.charAt(i)));
							System.out.println("emite "+actual.charAt(i)+">>"+completaStringBinaria(Integer.toBinaryString(actual.charAt(i))));
							textoComprimido += actual.charAt(i);
						}
						//ver si emitir caracter Clearing
					}

					ultimoMatch = -1;
				}

			}
			if ( clearing  && !actual.contains(this.caracterEOF)) {
				textoComprimidoEnBytes +=completaStringBinaria(Integer.toBinaryString(this.caracterClearing));
				
				textoComprimido += this.caracterClearing;
				}
	
			posicion++;
		}
		System.out.println("quedo "+textoComprimido);
		System.out.println("quedo "+textoComprimidoEnBytes);
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
        if (ultimoCodigo == 0) {
        	this.modo = 1;
        	limpiaTabla(this.modo);
        }
        	
		if (bufferBits.length() > 0) {
			//sumo lo que tenia
			System.out.println("entro "+datoscomprimidos);
			System.out.println(bufferBits);
			StringBuffer bitsAdescomprimir = bufferBits.append(datoscomprimidos);
			datoscomprimidos = bitsAdescomprimir;
			System.out.println("quedo "+datoscomprimidos);
			bufferBits = new StringBuffer();
		}
		String textoDescomprimido = new String();
		int posicion = 0;
		String anterior = new String();
		String actual = new String();
		String ultimoValor = new String();
		//while (posicion < bitsAdescomprimir.length()) {
		while (posicion < datoscomprimidos.length()) {
			actual = new String();
			int bitsAtomar = cantidadDeBitsDescompresion();
			System.out.println("tomar "+bitsAtomar);
			if ((datoscomprimidos.length() < posicion+bitsAtomar)) {
				System.out.println("guarda datois ");
				this.bufferBits.append(datoscomprimidos.substring(posicion,datoscomprimidos.length()));
				//devuelvo lo que tenia
				System.out.println(datoscomprimidos.substring(posicion,datoscomprimidos.length()));
				return textoDescomprimido;
			}
			String binaria = datoscomprimidos.substring(posicion,
					posicion + bitsAtomar);
			System.out.println(binaria);
			int caracter = ConversorABytes.binaryStringtoInt(binaria);
			System.out.println(caracter);
			if ( caracter >codigosReservados ) {
				//numero
				System.out.println("entra "+caracter);
				if (caracter > ultimoCodigo+2)
					//ocurrio algo inesperado
					 break;
				ultimoValor = buscaValorDescompresion(caracter);
				if (ultimoValor == null) {
					String nuevoValor = buscaValorDescompresion(caracter -1 );
					if (nuevoValor !=null)
					   ultimoValor = anterior+Character.toString(nuevoValor.charAt(nuevoValor.length()-1));
					else
					   ultimoValor = anterior+anterior;
				}
				actual = Character.toString(ultimoValor.charAt(0));
			}
			else {
				//letra
				System.out.println("letra "+(char)caracter);
				if (caracter == 1) {
					//clearing
					clearingParcial();
					actual = anterior;
				}
				else {
					if (caracter == 0) {
						//caracteres de relleno
						break;
					}
					
					if( (char) caracter == '\n' ) {
						//termino una linea
						//limpio el buffer por las dudas
							//bufferBits = new StringBuffer();
							ultimaDescompresion = new String();
							//guardo lo que quedo menos el enter.
							this.bufferBits.append(datoscomprimidos.substring(
								posicion+bitsAtomar, datoscomprimidos.length()));
							// ultimaDescompresion = actual+'\n';
							if (posicion !=0) {
							ultimoCodigo++;
							System.out.println("se ingreso "
									+ ultimoCodigo + ","
									+ (anterior+actual + '\n'));
							tabla.put(
									ultimoCodigo, (anterior+actual + '\n'));
							}
							ultimaDescompresion = new String();
							textoDescomprimido+='\n';
							break;
						}			
					

					actual = Character.toString((char)caracter);
				}
			}
			//Por si hubo una salida con enter anteriormente
			//if ((!ultimoValor.isEmpty()) && anterior.isEmpty() ) {
			if ((!ultimaDescompresion.isEmpty()) && anterior.isEmpty() ) {
				//se habia salido por un \n
				anterior = ultimaDescompresion;
			}
			System.out.println("caracter: "+caracter);
			System.out.println("actual: "+actual);
			System.out.println("anteri: "+anterior);
			System.out.println("ultimoValor: "+ultimoValor);
			System.out.println("ultimaDesco: "+ultimaDescompresion);
			if (!tabla.containsValue(anterior+actual) && (!anterior.isEmpty())) {
				//no estaba en tabla.
				ultimoCodigo++;
				System.out.println("se ingreso "+ultimoCodigo+","+(anterior+actual));
				this.tabla.put(ultimoCodigo, (anterior+actual));
					if (ultimoValor.isEmpty())
						textoDescomprimido += actual;
					else {
						// ultimo fue un numero
						textoDescomprimido += ultimoValor;
						actual = ultimoValor;
						ultimoValor = new String();
					}
			}
			else {
				//estaba en tabla
				if (anterior.isEmpty()) {
					//fue el primero
					if (ultimoValor.isEmpty()){
					   textoDescomprimido+=actual;
					   actual = anterior+actual;
					}
					else {
						//hago un paso mas, es primero y numero
					   actual = ultimoValor;
					   textoDescomprimido+=ultimoValor;
					}
				}
				
			}
			
			anterior = actual;
			ultimaDescompresion = anterior;
			posicion+=bitsAtomar;
		}
		System.out.println("envio "+textoDescomprimido);
	return textoDescomprimido;		
	}

	public String finalizarSession() {
		String textoFinal = new String();
		if (estadoInicio == 1) {
			//quedaron cosas para descomprimir
			System.out.println(isFinArchivo());
			while (!isFinArchivo()) {

				try {
					System.out.println("}}} finaliza sesion");
					textoFinal += descomprimir(new StringBuffer());
				} catch (SessionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		limpiaTabla(modo);
		estadoInicio = 0;
		ultimaDescompresion= new String();
		return textoFinal;

	}

	public void iniciarSesion() {
		//limpiaTabla();
		estadoInicio = 1;
		ultimaDescompresion= new String();
		bufferBits = new StringBuffer();
		ultimoCodigo = 0;

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
		int resultado = ((int) (Math.log(ultimoCodigo)/Math.log(2)) +1);
		if (resultado < 9 )
			resultado = 9;
		return resultado;
		
	}
	
	private int cantidadDeBitsDescompresion() {
		int resultado = ((int) (Math.log(ultimoCodigo+2)/Math.log(2)) +1);
		if (resultado < 9 )
			resultado = 9;
		return resultado;
	}
	
	private String completaStringBinaria(String binaria) {
		String ceros = new String();
		int cantidadDeBits = cantidadDeBitsCompresion();
		for (int i=0; i<cantidadDeBits-binaria.length(); i++)
			ceros+="0";
		return ( ceros + binaria);
	}
	
	
	/**
	 * Determina si es la ultima linea
	 * @param bits , a pasar
	 * @return true si los bits son todos 0
	 */
	private boolean isFinArchivo() {
		boolean esIgual = (this.bufferBits.length() == 0);
		StringBuffer ceros = new StringBuffer();
		System.out.println("es? "+bufferBits+ esIgual);
	     for (int j=0 ; j<bufferBits.length(); j++) {
	    	 esIgual = (bufferBits.charAt(j) == '0');
	    	 if (!esIgual)
	    		 break;
	     }
	     return esIgual;
	}
}

