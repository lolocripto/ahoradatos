/**
 * 
 */
package ar.com.datos.grupo5.compresion.ffff;

/**
 * @author Led Zeppelin
 *
 */
public class nodoTablaLZ78W {
	
		String cadena;
		int noSeRepite;
		int vecesQueSeRepitio;
		
		public nodoTablaLZ78W(String cadenaExt) {
			super();
			this.cadena = cadenaExt;
			this.noSeRepite = 0;
			this.vecesQueSeRepitio = 1;
		}
		
		public final int getNoSeRepite() {
			return this.noSeRepite;
		}

		public final void incrementarNoSeRepite() {
			this.noSeRepite++;
		}
		
		public final int getVecesQueSeRepite() {
			return vecesQueSeRepitio;
		}
		
		public final void incrementarVecesQueSeRepitio() {
			this.vecesQueSeRepitio++; 
		}
		
		public final void setCadena(String cadenaExt) {
			this.cadena = cadenaExt;
		}
		
		public final String getCadena() {
			return cadena;
		}
		
		//@Override
		public boolean equals(Object o) {
			nodoTablaLZ78W obj = (nodoTablaLZ78W) o;
			return getCadena().equals(obj.getCadena());
			
		}
		/*
		@Override
		public int compareTo(Object o) {
			nodoTablaLZ78W obj = (nodoTablaLZ78W) o;
			return this.cadena.compareTo(obj.getCadena()); 
		}
		*/
		
		//@Override
		public int compareTo(nodoTablaLZ78W o) {
			return getCadena().compareTo(o.getCadena()); 
		}
		
		
		public boolean equals(nodoTablaLZ78W o) {
			return getCadena().equals(o.getCadena());
		}
}
