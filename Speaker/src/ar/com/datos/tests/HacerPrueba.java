package ar.com.datos.tests;

public class HacerPrueba {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		for (int i = 0; i < 300; i++) {
			
			String cadena = new String(("hola"+Integer.toString((int) (Math.random()*1000))));
			System.out.println(cadena);
			
			
		}
		

	}

}
