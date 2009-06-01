package ar.com.datos.grupo5.compresion.aritmetico;

public class Segmento {

	private int techo;
	private int piso;
	private int bitsUnderflow;

	/**
	 * Constructor que recibe por parámetro el numero máximo posible para el
	 * techo y el piso
	 * 
	 * @param piso
	 * @param techo
	 */
	public Segmento(int piso, int techo) {
		this.techo = techo;
		this.piso = piso;
		this.bitsUnderflow = 0;
	}

	/**
	 * Constructor sin parámetros, por defecto toma todo el espacio comprendido
	 * por el tipo de dato Integer [-2147483648, +2147483647] que son 32 bits
	 * tomando al bit mas significativo como signo.
	 */
	public Segmento() {
		this.techo = Integer.MAX_VALUE;
		this.piso = Integer.MIN_VALUE;
		this.bitsUnderflow = 0;
	}

	public int getTecho() {
		return techo;
	}

	public void setTecho(int techo) {
		this.techo = techo;
	}

	public int getPiso() {
		return piso;
	}

	public void setPiso(int piso) {
		this.piso = piso;
	}

	/**
	 * Verifica si los limites del intervalo tienen overflow
	 * 
	 * @return
	 */
	public boolean hayOverflow() {

		String bitsTecho = Integer.toBinaryString(this.techo);
		String bitsPiso = Integer.toBinaryString(this.piso);

		if (bitsPiso.charAt(0) == bitsTecho.charAt(0))
			return true;
		else
			return false;

	}

	/**
	 * verifica si los limites del intervalo tienen underFlow, para que haya
	 * underflow deben verificarse las siguientes 3 condiciones: 
	 * 1) el primer bit del piso y el techo deben ser diferentes. 
	 * 2) el segundo bit del techo, debe ser el negado del segundo bit del piso
	 * 3) se agrega la condicion de que el segundo bit del techo sea el 
	 * negado del primer bit del techo, porque de lo contrario no podría 
	 * diferenciar el caso inicial ( techo: 11111111111  piso: 00000000000) de un
	 * underflow y entraria en un ciclo infinito.
	 * 
	 * @return
	 */
	public boolean hayUnderflow() {

		String bitsTecho = Integer.toBinaryString(this.techo);
		String bitsPiso = Integer.toBinaryString(this.piso);
		//primer bit diferente
		boolean condicionUno = bitsPiso.charAt(0) != bitsTecho.charAt(0);
		//segundo bit diferente
		boolean condicionDos = bitsPiso.charAt(1) != bitsTecho.charAt(1);
		//primer y segundo bit del techo diferente
		boolean condicionTres = bitsTecho.charAt(0) != bitsTecho.charAt(1);

		if (condicionUno && condicionDos && condicionTres)
			return true;
		else
			return false;
	}
	
	/**
	 * Deja preparado el intervalo para un nuevo proceso de compresion o descompresion
	 * reinicializando el piso, el techo y el contador de overflow
	 */
	public void resetear(){
		
		this.techo = Integer.MAX_VALUE;
		this.piso = Integer.MIN_VALUE;
		this.bitsUnderflow = 0;
	}

	/**
	 * normaliza el segmento devolviendo los bits emitidos en formato de string
	 * con unos y ceros
	 * 
	 * @return
	 */
	public String normalizar() {

		String emision = new String();
		boolean procesarBitsUnderflow = false;

		// verifico si luego de procesar el overflow debo emitir bits de
		// underflow
		if (this.hayOverflow() && (this.bitsUnderflow != 0)) {
			procesarBitsUnderflow = true;
		}

		// proceso el overflow actual, bit por bit
		while (this.hayOverflow()) {
			emision.concat(this.emitirBitOverflow());
		}

		// si procesé overflow y anteriormente el contador de underflow no
		// estaba
		// en cero, entonces debo emitir tantos bits como me diga el contador.
		if (procesarBitsUnderflow) {

			// me fijo cual es el bit mas significativo que estoy emitiendo y lo
			// niego
			String rellenoCon = this.negarBit(emision.substring(0, 1));

			// uso ese bit para rellenar tantas veces como me diga el contador
			// de underflow
			while (this.bitsUnderflow > 0) {
				emision.concat(rellenoCon);
				this.bitsUnderflow--;
			}

			return emision;

		}

		while (this.hayUnderflow()) {
			this.trabajarUnderFlow();
		}
		return null;
	}

	/**
	 * Recibe un bit en formato de string y lo niega
	 * 
	 * @param bit
	 * @return
	 */
	private String negarBit(String bit) {

		if (bit.charAt(0) == '0') {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * Este metodo permite quitar el primer bit mas significativo del piso y el
	 * techo devolviendolo como emision. Tambien agrega ceros y unos para
	 * agrandar nuevamente el intervalo
	 * 
	 * @return
	 */
	private String emitirBitOverflow() {

		// genero los strings con el numero binario
		String bitsTecho = Integer.toBinaryString(this.techo);
		String bitsPiso = Integer.toBinaryString(this.piso);
		String emision = new String();

		// como sé que hay overflow, tomo el primer bit del techo o del piso y
		// lo emito
		// este proceso sera parte de un ciclo manejado desde afuera por eso
		// solo se
		// emite de a 1 bit
		emision.concat(bitsTecho.substring(0, 1));

		// agrego 1 al final del techo y cero al final del piso
		bitsTecho.concat("1");
		bitsPiso.concat("0");

		// convierto mis strings en enteros, modificando el viejo
		// techo y piso, para luego devolver el bit emitido
		this.techo = Integer.parseInt(bitsTecho.substring(1), 2);
		this.piso = Integer.parseInt(bitsPiso.substring(1), 2);

		return emision;

	}

	/**
	 * Este metodo permite quitar el segundo bit mas significativo del piso y el
	 * techo agregando unos y ceros al final, de modo que podamos evitar el
	 * underflow. al finalizar Suma un bit al contador de underflow
	 */
	private void trabajarUnderFlow() {

		// genero los strings con el numero binario
		String bitsTecho = Integer.toBinaryString(this.techo);
		String bitsPiso = Integer.toBinaryString(this.piso);

		// tomo los bits mas significativos del techo y el piso para
		// preservarlos
		String bitMasSignTecho = bitsTecho.substring(0, 1);
		String bitMasSignPiso = bitsPiso.substring(0, 1);

		// agrego 1 al final del techo y cero al final del piso
		bitsTecho.concat("1");
		bitsPiso.concat("0");

		// una vez que guarde los bits mas significativos remuevo los 2 bits mas
		// sign
		// de cada uno, debido a que no tengo forma de remover solo el segundo,
		// debo remover
		// ambos y luego volver a unir el string.
		bitsTecho = bitsTecho.substring(2);
		bitsPiso = bitsPiso.substring(2);

		// por ultimo coloco nuevamente los bits que guardé anteriormente
		// poniendolos adelante
		bitsTecho = bitMasSignTecho.concat(bitsTecho);
		bitsPiso = bitMasSignPiso.concat(bitsPiso);

		// convierto mis strings en enteros, modificando el viejo
		// techo y piso, para luego devolver el bit emitido
		this.techo = Integer.parseInt(bitsTecho, 2);
		this.piso = Integer.parseInt(bitsPiso, 2);

		// sumo 1 bit al contador de underflow
		this.bitsUnderflow++;

	}

	/**
	 * Método que permite emitir los bits restantes, considerando que pueda
	 * haber bits en underflow
	 * 
	 * @return
	 */
	public String emitirRestoBits() {

		// genero un string para emitir y otro que es el piso en binario
		String emision = new String();
		String bitsPiso = Integer.toBinaryString(this.piso);

		// si estamos en un caso con underflow al final
		if (this.bitsUnderflow > 0) {
			// tomo el primer bit del piso y lo pongo para emitir
			emision.concat(bitsPiso.substring(0, 1));

			// genero una variable auxiliar con ese primer bit negado
			String rellenoCon = this.negarBit(emision);

			// mientras el contador de underflow no llege a cero itero colocando
			// bits en la emision, que seran el negado del primero
			while (this.bitsUnderflow > 0) {

				emision.concat(rellenoCon);
				this.bitsUnderflow--;

			}
			// por ultimo coloco el resto de los bits del piso para emitir
			emision.concat(bitsPiso.substring(1));
			return emision;
		} else {
			// si no habia bits de underflow, devuelvo directamente el piso
			return bitsPiso;
		}

	}

}
