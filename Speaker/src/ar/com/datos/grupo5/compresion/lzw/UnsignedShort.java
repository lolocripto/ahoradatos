package ar.com.datos.grupo5.compresion.lzw;

import ar.com.datos.grupo5.compresion.aritmetico.UnsignedInt;
import ar.com.datos.grupo5.utils.Conversiones;

public class UnsignedShort {
		private int intAsociado;
		private String Bit32Repr;
		private String Bit16Repr;
 		
		public UnsignedShort(short numero){
			this.intAsociado = (int)numero;
			this.Bit32Repr = Integer.toBinaryString(this.intAsociado);
			this.Bit32Repr = this.setLeadingCeros(Bit32Repr, 32);
			this.Bit16Repr = Bit32Repr.substring(15);
			
		}
		
		public UnsignedShort (int numero){
			this.setIntAsociado(numero);
		}
		
		/**
		 * recibe una representación binaria de 32 bits en un string del numero
		 * @param bits
		 */
		public UnsignedShort(String bits){
			
			bits = this.setLeadingCeros(bits, 32);
			this.Bit32Repr = bits;
			this.intAsociado = Integer.parseInt(bits, 2);
			this.Bit16Repr = Bit32Repr.substring(15);
		}
		
		public int getIntAsociado (){
			return this.intAsociado;
		}
		
		public String get16BitsRepresentation(){
			return this.Bit16Repr;
		}
		
		public String get32BitsRepresentation(){
			return this.Bit32Repr;
		}
		
		public void setIntAsociado (int numero){
			this.intAsociado = numero;
			this.Bit32Repr = Integer.toBinaryString(this.intAsociado);
			this.Bit32Repr = this.setLeadingCeros(Bit32Repr, 32);
			this.Bit16Repr = Bit32Repr.substring(15);
			
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
		 * Setea como valor, el máximo numero representable en
		 * 32 bits
		 */
		public void setMaxNumber(){
			this.setIntAsociado(0xffff);
		}
		
		/**
		 * hace un corrimiento de 1 bit a la izquierda y agrega 
		 * un 0 a la derecha 
		 */
		public void leftShiftCero(){
			
			//corro un bit a la derecha
			int aux = this.intAsociado <<1;
			//saco todos los bits que no me interesan (del 32 en adelante) con un
			//and logico
			aux = aux & 0xffff;
			//seteo el nuevo long
			this.setIntAsociado(aux);
		}
		
		/**
		 * hace un corrimiento de 1 bit a la izquierda y agrega 
		 * un 1 a la derecha 
		 */
		public void leftShiftOne(){
			//corro un bit a la izquierda
			int aux  = this.intAsociado <<1;
			//cambio el ultimo cero por un 1
			aux += 1;
			//saco todos los bits luego del 32
			aux = aux & 0xffff;
			//seteo el nuevo long
			this.setIntAsociado(aux);
		}

		@Override
		public String toString() {
			return this.Bit16Repr;
		}

		public boolean equals(UnsignedShort obj) {
			if(obj.getIntAsociado() == this.getIntAsociado()){
				return true;
			}else return false;
		}
		
		public UnsignedShort mas (UnsignedShort numero){
			int result = this.getIntAsociado() + numero.getIntAsociado();
			UnsignedShort nuevo = new UnsignedShort(result);
			return nuevo;
		}
		
		public UnsignedShort menos (UnsignedShort numero){
			int result = this.getIntAsociado() - numero.getIntAsociado();
			UnsignedShort nuevo = new UnsignedShort(result);
			return nuevo;
		}
		//TODO: chequear
		public byte[] getBytes() {
			short dosPrimerosBytes = (short) (0x0000FFFF & (this.intAsociado));
			return null;
		}

	}

