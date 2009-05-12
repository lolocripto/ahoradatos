package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.Diccionario;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.parser.TextInterpreter;

public class TestDiccionario {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		String ruta = "/home/gabriel/workspace/prueba1.txt";
		
		Diccionario dic = new Diccionario();
	
		
		TextInterpreter ti = new TextInterpreter();
		Collection<IunidadDeHabla> lista =null;
		
		try {
			lista = ti.modoLecturaSinAlmacenamiento(ruta, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Iterator<IunidadDeHabla> it = lista.iterator();

/*		
		System.out.println(dic.buscarPalabra("fantasmas"));
		System.out.println(dic.buscarPalabra("triunfal"));
		System.out.println(dic.buscarPalabra("ella"));
		System.out.println(dic.buscarPalabra("arrojaba"));
		System.out.println(dic.buscarPalabra("fantasmas"));
		System.out.println(dic.buscarPalabra("fantasmas"));
		System.out.println(dic.buscarPalabra("años"));
		System.out.println(dic.buscarPalabra("fantasmas"));
		System.out.println(dic.buscarPalabra("mozos"));
		System.out.println(dic.buscarPalabra("fantasmas"));
		
*/		
		
		
/*	
		int i =0;
		Long puntero;
		
		System.out.println(dic.cantNodosUsados());
		System.out.println("");
		while (it.hasNext()){
			
			String texto = it.next().getTextoEscrito();
			
			puntero = dic.buscarPalabra(texto);
			System.out.print("busco:    "+texto+ "     puntero: "+puntero);
			System.out.println("");
			
			if (puntero != null){
				
				System.out.println(puntero.intValue() == i );
				i++;
			}
					
		}
		System.out.println(dic.cantNodosUsados());

*/	
		
/*		

		int i =567;
		while (it.hasNext()){
			
			
			String texto = it.next().getTextoEscrito();
			System.out.println(texto);
			if (dic.agregar(texto, new Long(i))){
				i++;
			}
			
		}
	
*/	
	
		
		while (it.hasNext()){
			
			
			String texto = it.next().getTextoEscrito();
			dic.agregar(texto,new Long(texto.length()));
			System.out.print("p="+dic.buscarPalabra(texto));
			System.out.print(texto+"    ");
		
			
		}
		
		
		

	}

}
