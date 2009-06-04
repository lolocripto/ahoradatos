package ar.com.datos.grupo5.compresion.aritmetico;


/**
 * 
 * @author Exequiel Leite
 *
 */
public class UnsignedInt {

	private long longAsociado;
	private String Bit64Repr;
	private String Bit32Repr;
	
	
	public UnsignedInt(int numero){
		this.longAsociado = (long)numero;
		this.Bit64Repr = Long.toBinaryString(this.longAsociado);
		this.Bit64Repr = this.setLeadingCeros(Bit64Repr, 64);
		this.Bit32Repr = Bit64Repr.substring(31);
		
	}
	
	public UnsignedInt (long numero){
		this.setLongAsociado(numero);
	}
	
	/**
	 * recibe una representación binaria de 32 bits en un string del numero
	 * @param bits
	 */
	public UnsignedInt(String bits){
		
		bits = this.setLeadingCeros(bits, 64);
		this.Bit64Repr = bits;
		this.longAsociado = Long.parseLong(bits, 2);
		this.Bit32Repr = Bit64Repr.substring(31);
	}
	
	public long getLongAsociado (){
		return this.longAsociado;
	}
	
	public String get32BitsRepresentation(){
		return this.Bit32Repr;
	}
	
	public String get64BitsRepresentation(){
		return this.Bit64Repr;
	}
	
	public void setLongAsociado (long numero){
		this.longAsociado = numero;
		this.Bit64Repr = Long.toBinaryString(this.longAsociado);
		this.Bit64Repr = this.setLeadingCeros(Bit64Repr, 64);
		this.Bit32Repr = Bit64Repr.substring(31);
		
	}
	
	/**
	 * Agrega los ceros a la izquierda necesarios en la representacion
	 * @param cadena
	 * @param largo
	 */
	private String setLeadingCeros (String cadena, int largo){
		String cero = new String ("0");
		while (cadena.length() < largo){
			cadena = cero.concat(cadena);
		}
		return cadena;
	}
	
	/**
	 * Multiplica this por un valor con coma, redondea para abajo
	 * y devuelve un Unsigned32bits con la informacion
	 * @param numero
	 * @return
	 */
	public UnsignedInt porFloat (float numero){
		double sky = Math.floor(this.longAsociado*numero);
		return new UnsignedInt ((long)sky);
	}
	
	/**
	 * Setea como valor, el máximo numero representable en
	 * 32 bits
	 */
	public void setMaxNumber(){
		this.setLongAsociado(0xffffffffL);
	}
	
	/**
	 * hace un corrimiento de 1 bit a la izquierda y agrega 
	 * un 0 a la derecha 
	 */
	public void leftShiftCero(){
		
		//corro un bit a la derecha
		long aux = this.longAsociado <<1;
		//saco todos los bits que no me interesan (del 32 en adelante) con un
		//and logico
		aux = aux & 0xffffffffL;
		//seteo el nuevo long
		this.setLongAsociado(aux);
	}
	
	/**
	 * hace un corrimiento de 1 bit a la izquierda y agrega 
	 * un 1 a la derecha 
	 */
	public void leftShiftOne(){
		//corro un bit a la izquierda
		long aux  = this.longAsociado <<1;
		//cambio el ultimo cero por un 1
		aux += 1;
		//saco todos los bits luego del 32
		aux = aux & 0xffffffffL;
		//seteo el nuevo long
		this.setLongAsociado(aux);
	}

	@Override
	public String toString() {
		return this.Bit32Repr;
	}

	public boolean equals(UnsignedInt obj) {
		if(obj.getLongAsociado() == this.getLongAsociado()){
			return true;
		}else return false;
	}
	
	public UnsignedInt  mas (UnsignedInt numero){
		long result = this.getLongAsociado() + numero.getLongAsociado();
		UnsignedInt nuevo = new UnsignedInt(result);
		return nuevo;
	}
	
	public UnsignedInt menos (UnsignedInt numero){
		long result = this.getLongAsociado() - numero.getLongAsociado();
		UnsignedInt nuevo = new UnsignedInt(result);
		return nuevo;
	}

}
