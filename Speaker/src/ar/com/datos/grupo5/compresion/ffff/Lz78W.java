/**
 * 
 */
package ar.com.datos.grupo5.compresion.ffff;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.CodePointException;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.CodePoint;

/**
 * @author Led Zeppelin
 *
 */
public class Lz78W implements Compresor{
	
	private int bitsEmision;
	private String contexto;
	private boolean finalizada;
	private tablaLz78W tabla;
	private boolean sesionIniciada;
	private boolean esCompresion;
	private StringBuffer bitsBuffer;
	private int indexAnterior;
	private int indexNuevo;
	
	private Character caracterClearing;
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Lz78W.class);
	
	
	/*
	 * Agregar una estructura para bufferizar los bits
	 */
	
	public Lz78W() {
		this.sesionIniciada = false;
		try {
			caracterClearing = CodePoint.getChar(65535);
		} catch (CodePointException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String comprimir(String cadena) throws SessionException {
		if (!this.sesionIniciada) {
			throw new SessionException();
		}
		this.esCompresion = true;
		this.tabla.setEsCompresion(esCompresion);
		int pos = 0;
		String tiraBits = "";
		if (this.sesionIniciada) {
			Character letra;
			//Si es la primera vez entonces me cargo la primera letra
			if (this.contexto.length() == 0) {
				this.contexto = cadena.substring(0,1);
				pos++;
			}
			String valueABuscar;
			while (pos < cadena.length()) {
				
				letra = cadena.charAt(pos);
				valueABuscar = this.contexto + letra;
				if (!this.tabla.existKey(valueABuscar)) {
					//No la encontre
					//Emito el codigo del contexto, para ello obtengo el indice del contexto
					tiraBits += this.emitir(this.tabla.get(this.contexto));
					
					//Agrego el contexto nuevo a la tabla
					this.tabla.add(valueABuscar);
					
					if (tabla.isClearing()) {
						//emito el codigo de clearing
						tiraBits += this.emitir(CodePoint.getCodePoint(caracterClearing));
					}
					//el contexto ahora es la letra
					this.contexto = letra.toString();
					
				} else {
					//La encontre
					this.contexto = valueABuscar;
				}
				pos++;
			}
		}
		
		return tiraBits;
	}

	@Override
	public String descomprimir(StringBuffer datos) throws SessionException {
		if (!this.sesionIniciada) {
			throw new SessionException();
		}
		
		this.esCompresion = false;
		this.tabla.setEsCompresion(esCompresion);

		//Tengo algo en el buffer que quedo de otra pasada
		//lo concateno con lo nuevo
		if (this.bitsBuffer.length() > 0) {
			datos.insert(0,this.bitsBuffer);
			this.bitsBuffer = new StringBuffer();
		}
		
		//Si los datos del buffer mas los datos de entrada son menores
		//a la malla de bits entonces devuelvo null ya que no puedo seguir
		if (datos.length() < this.bitsEmision) {
			this.bitsBuffer = new StringBuffer(datos);
			datos.delete(0, datos.length());
			return "";
		}
		
		boolean llegoEOF = false;
		String emision = "";
		StringBuffer descompresion = new StringBuffer();
		Character letra;
		
		//Si es la primera vez entonces me cargo la primera letra
		if (this.indexAnterior == -1 && !llegoEOF) {
			//OLDCODE (indexAnterior) = input first Code
			this.indexAnterior = Integer.parseInt(datos.substring(0,this.bitsEmision),2);
			datos.delete(0, this.bitsEmision);
			//output OLDCODE (indexAnterior)
			descompresion.insert(descompresion.length(), this.tabla.get(this.indexAnterior));
		}
		
		//Mientras tenga igual o mas bist que los 
		//que necesito para leer la emision y no sea fin de archivo
		while (datos.length() >= this.bitsEmision && !llegoEOF ) {
			//Sigo con la descompresion
			
			//Valido la cantidad de bits que debo leer
			this.validarBitsEmision();
			//NEW CODE (indexNuevo) = get next input code
			this.indexNuevo = Integer.parseInt(datos.substring(0,this.bitsEmision),2);
			datos.delete(0,this.bitsEmision);
			
			if (this.indexNuevo == CodePoint.getCodePoint(caracterClearing)) {
				this.tabla.aplicarClearing();
			}
			
			//STRING (contexto) = translation of NEWCODE
			this.contexto = this.tabla.get(this.indexNuevo);
			if (this.contexto == null) {
				//No encuentro el indice nuevo por lo tanto lo creo
				//Char + index
				//index + index
				
				// Genero el nuevo string con char (indexAnterior) + 1er letra del indexAnterior
				this.contexto = tabla.get(this.indexAnterior) + tabla.get(indexAnterior).charAt(0);
				
				//FIXME: ver si despues de esta situacion la proxima iteracion deberia agarrar desde el ultimo o el primero
			}
			//output STRING (contexto)
			descompresion.insert(descompresion.length(), this.contexto);
			//BYTE (letra) = 1st byte of STRING
			letra = this.contexto.charAt(0);
			//add OLDCODE translation + BYTE (letra) to the code table
			logger.debug("\nAgrego a la tabla: " + this.tabla.get(this.indexAnterior) + letra);
			this.tabla.add(this.tabla.get(this.indexAnterior) + letra);
			//OLDCODE = NEWCODE
			this.indexAnterior = this.indexNuevo;
		}
		
		if (datos.length() < this.bitsEmision) {
			this.bitsBuffer.append(datos);
			datos.delete(0, datos.length());
		}
		//Devuelvo todo el string generado
		return descompresion.toString();
	}

	@Override
	public String finalizarSession() {
		
		if (this.esCompresion) {
			String bitsFaltantes;
			//Emitir el EOF
			bitsFaltantes = this.finalizarCompresion();
			this.sesionIniciada = false;
			return bitsFaltantes;
		} else {
			return "";
		}
	}

	private String finalizarCompresion() {
		try {
			return this.comprimir(Constantes.EOF.toString());
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void imprimirHashMap() {
		
	}

	@Override
	public void iniciarSesion() {
		this.sesionIniciada = true;
		this.finalizada = false;
		this.bitsEmision = 9;
		this.contexto = "";
		this.tabla = new tablaLz78W();
		this.indexAnterior = -1;
		this.bitsBuffer = new StringBuffer();
	}

	@Override
	public boolean isFinalizada() {
		return finalizada;
	}

	/**
	 * Tranforma el string en una tira de bits
	 * @param emision String a convertir
	 * @return tira de bits a devolver
	 */
	private final String emitir(String emision){
		int pos = 0;
		StringBuffer binaryString = new StringBuffer();
		while (pos < emision.length()) {
			binaryString.append(this.completarXBits(Integer.toBinaryString(CodePoint.getCodePoint(emision.charAt(pos)))));
		}
		return binaryString.toString();
	}

	/**
	 * Tranforma el string en una tira de bits
	 * @param emision String a convertir
	 * @return tira de bits a devolver
	 */
	private final String emitir(int emision){
		String temp = this.completarXBits(Integer.toBinaryString(emision));
		return temp;
	}
	
	/**
	 * Completo a los bits actuales de emision la emision
	 * @param emision
	 * @return
	 */
	private final String completarXBits(String emision){
		//Valido a cuatos bits tengo que emitir el resultado
		this.validarBitsEmision();
		int faltante = this.bitsEmision - emision.length();
		StringBuffer buffer = new StringBuffer(emision);
		for (int i = 0; i < faltante; i++){
			buffer.insert(0, "0");
		}
		return buffer.toString();
	}
	
	private final boolean bufferInsuficiente(StringBuffer buffer) {
		if (buffer.length() < this.bitsEmision) {
			return true;
		}
		return false;
	}
	
	/**
	 * Valida con cuantos bits debe ser la emision actual 
	 * o la lectura actual de bits
	 */
	private final void validarBitsEmision(){
		
		int tamanio; 
		//Este if lo meto porque al leer un elemento que pasa de x a x+1 bits
		//lo hace antes de guardar en la tabla por lo que necesito adelantarme
		if (esCompresion) {
			tamanio = this.tabla.getSize();
		} else {
			tamanio = this.tabla.getSize() + 1;
		}
		
		/*
		
		if (this.esCompresion) {
			this.bitsEmision = (int) (Math.ceil(Math.log(tamanio)/Math.log(2)));
		} else {
			this.bitsEmision = (int) (Math.ceil(Math.log(tamanio)/Math.log(2)));
		}
		
		*/
		
		if (tamanio < Math.pow(2, 9)) {
			this.bitsEmision = 9;
		} else {
			if (tamanio < Math.pow(2, 10)) {
				this.bitsEmision = 10;
			} else {
				if (tamanio < Math.pow(2, 11)) {
					this.bitsEmision = 11;
				} else {
					if (tamanio < Math.pow(2, 12)) {
						this.bitsEmision = 12;
					} else {
						if (tamanio < Math.pow(2, 13)) {
							this.bitsEmision = 13;
						} else {
							if (tamanio < Math.pow(2, 14)) {
								this.bitsEmision = 14;
							} else {
								if (tamanio < Math.pow(2, 15)) {
									this.bitsEmision = 15;
								} else {
									if (tamanio < Math.pow(2, 16)) {
										this.bitsEmision = 16;
									} else {
										//Debería auto ejecutarse el Clearing!!! Jamas debería pasar
									}
								}
							}
						}
					}
				}
			}	
		}
		
	}
	
	public final Integer test(){
		this.tabla.setEsCompresion(true);
		if (!this.tabla.existKey("P")) {
			return -1;
		} else {
			return this.tabla.get("P");
		}
	}
	
	private boolean identificarValorChar(int actual) {
		if ( actual > 255 ) {
			return false;
		} else {
			return true;
		}
	}
}
