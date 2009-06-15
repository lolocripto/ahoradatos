package ar.com.datos.grupo5.compresion.aritmetico;

public class Segmento {

	private UnsignedInt techo;
	private UnsignedInt piso;
	private int bitsUnderflow;
	private boolean overFlow;

	/**
	 * Constructor que recibe por parámetro el numero máximo posible para el
	 * techo y el piso
	 * 
	 * @param piso
	 * @param techo
	 */
	public Segmento(int piso, int techo) {
		
		this.techo = new UnsignedInt(techo);
		this.piso = new UnsignedInt(piso);
		this.bitsUnderflow = 0;
	}

	/**
	 * Constructor sin parámetros, por defecto toma todo el espacio comprendido
	 * en un dato de 32 bits sin signo (0H a ffffffffH) 
	 */
	public Segmento() {
		this.techo = new UnsignedInt(0);
		this.piso = new UnsignedInt(0);
		this.techo.setMaxNumber();
		this.bitsUnderflow = 0;
	}

	public UnsignedInt getTecho() {
		return this.techo;
	}

	public void setTecho(long techo) {
		this.techo.setLongAsociado(techo);
	}

	public UnsignedInt getPiso() {
		return this.piso;
	}

	public void setPiso(long piso) {
		this.piso.setLongAsociado(piso);
	}

	/**
	 * Verifica si los limites del intervalo tienen overflow
	 * 
	 * @return
	 */
	public boolean hayOverflow() {

		String bitsTecho = this.techo.get32BitsRepresentation();
		String bitsPiso = this.piso.get32BitsRepresentation();

		if (bitsPiso.charAt(0) == bitsTecho.charAt(0)) {
			this.overFlow = true;
			return true;
		} else {
			this.overFlow = false;
			return false;	
		}
		

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

		String bitsTecho = this.techo.get32BitsRepresentation();
		String bitsPiso = this.piso.get32BitsRepresentation();
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
		
		this.techo.setMaxNumber();
		this.piso.setLongAsociado(0L);
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
			emision = emision.concat(this.emitirBitOverflow());
		}

		// si procesé overflow y anteriormente el contador de underflow no
		// estaba
		// en cero, entonces debo emitir tantos bits como me diga el contador.
		if (procesarBitsUnderflow) {

			// me fijo cual es el bit mas significativo que estoy emitiendo y lo
			// niego
			String rellenoCon = this.negarBit(emision.substring(0, 1));

			StringBuffer buffer = new StringBuffer(emision);
			
			int i = 1;
			// uso ese bit para rellenar tantas veces como me diga el contador
			// de underflow
			while (this.bitsUnderflow > 0) {
				//emision = emision.concat(rellenoCon);
				buffer.insert(i, rellenoCon.charAt(0));
				i++;
				this.bitsUnderflow--;
			}
			emision = buffer.toString();
			//Antes de devolver la emision no deberia seguir mirando si hay UnderFlow??
//FIXME:			return emision;
			//System.out.println("Luego de Normalizar:");
			//System.out.println("Techo: " + Long.toHexString(new Long(this.techo.getLongAsociado())));
			//System.out.println("Piso: " + Long.toHexString(new Long(this.piso.getLongAsociado())));
			return emision;

		}

		while (this.hayUnderflow()) {
			this.trabajarUnderFlow();
		}
		//System.out.println("Luego de Normalizar:");
		//System.out.println("Techo: " + Long.toHexString(new Long(this.techo.getLongAsociado())));
		//System.out.println("Piso: " + Long.toHexString(new Long(this.piso.getLongAsociado())));
		return emision;
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
		String bitsTecho = this.techo.get32BitsRepresentation();
		String emision = new String();

		// como sé que hay overflow, tomo el primer bit del techo o del piso y
		// lo emito este proceso sera parte de un ciclo manejado desde afuera 
		// por eso solo se emite de a 1 bit
		emision = bitsTecho.substring(0, 1);
		
		// hago una corrimiento de bits
		//en el techo corro a la izq y agrego un 1 al final
		this.techo.leftShiftOne();
		//en el piso corro a la izq y agrego un 0 al final
		this.piso.leftShiftCero();
		
		return emision;
	}

	/**
	 * Este metodo permite quitar el segundo bit mas significativo del piso y el
	 * techo agregando unos y ceros al final, de modo que podamos evitar el
	 * underflow. Para hacerlo se toma como premisa que si estamos en Underflow
	 * el primer bit del techo sera 1 y el primer bit del piso será 0.
	 * al finalizar Suma un bit al contador de underflow
	 */
	//TODO verificar la premisa anterior.
	private void trabajarUnderFlow() {

		// agrego hago un corrimiento de bits a la izquierda y coloco
		// un 1 al final del techo y 0 al final del piso
		this.techo.leftShiftOne();
		this.piso.leftShiftCero();

		//restauro el primer bit del techo a 1 y el del piso a 0
		this.techo.SetBitMasSignificativo(1);
		this.piso.SetBitMasSignificativo(0);
	
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
		String bitsPiso = this.piso.get32BitsRepresentation();

		// si estamos en un caso con underflow al final
		if (this.bitsUnderflow > 0) {
			// tomo el primer bit del piso y lo pongo para emitir
			emision = emision.concat(bitsPiso.substring(0, 1));

			// genero una variable auxiliar con ese primer bit negado
			String rellenoCon = this.negarBit(emision);

			// mientras el contador de underflow no llege a cero itero colocando
			// bits en la emision, que seran el negado del primero
			while (this.bitsUnderflow > 0) {

				emision = emision.concat(rellenoCon);
				this.bitsUnderflow--;

			}
			// por ultimo coloco el resto de los bits del piso para emitir
			emision = emision.concat(bitsPiso.substring(1));
			return emision;
		} else {
			// si no habia bits de underflow, devuelvo directamente el piso
			return bitsPiso;
		}

	}

	
	/**
	 * Permite saber si hay UnderFlow
	 * @return
	 */
	public boolean estadoUnderFlow() {
		if (this.bitsUnderflow > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Permite saber si en la pasada anterior existio OverFlow
	 * @return
	 */
	public boolean estadoOverFlow() {
		return this.overFlow;
	}
	
	public final String generarCadenaSinUndeFlow(StringBuffer binaryString){
		String nuevaCadena = "";
		nuevaCadena += binaryString.charAt(0);
		//nuevaCadena += binaryString.substring(1+this.bitsUnderflow,31 + (1+this.bitsUnderflow));
		nuevaCadena += binaryString.substring(1+this.bitsUnderflow);
		return nuevaCadena;
	}
}
