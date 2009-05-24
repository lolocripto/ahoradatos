package ar.com.datos.tests;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import sun.rmi.runtime.Log;

import ar.com.datos.grupo5.Main;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestArbol {

	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(final String[] args) {
		
		try {
			BStar tree = new BStar();
			ArchivoSecuencialSet sec = new ArchivoSecuencialSet();
			tree.setSecuencialSet(sec);
			
			Clave clave = null;
			RegistroNodo reg = null;

			TextInterpreter tx = new TextInterpreter();
			
			Collection<IunidadDeHabla> lista2 = null;
			
			//lista2 = tx.modoLecturaSinAlmacenamiento("palabras.txt", true);
			lista2 = tx.modoLecturaSinAlmacenamiento("numeros.txt", true);
			
			Iterator<IunidadDeHabla> it;
			
			it = lista2.iterator();
			
			int u =0;

			//while (u < cantidad) {
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				String cadena = iunidadDeHabla.getTextoEscrito();
				
				//System.out.println(cadena);
				clave = new Clave();
				clave.setClave(cadena);
				reg = new RegistroNodo();
				reg.setClave(clave);
				if (u == 43) {
					logger.debug("");
				}
				if (tree.insertar(reg)) {
					System.out.println("Insertada la palabra: " + cadena + " Cantidad: " + u);
				} else {
					System.out.println("No se inserto la palabra: " + cadena + " Cantidad: " + u);
				}
				reg = tree.buscar(clave);
				if (reg == null) {
					System.out.println("##########################No encontre: " + clave.getClave());
				}
				u++;
				tree.listar();
			}
			tree.listar();
			//lista2 = tx.modoLecturaSinAlmacenamiento("palabras.txt", true);
			lista2 = tx.modoLecturaSinAlmacenamiento("numeros.txt", true);
			it = lista2.iterator();
			System.out.println("Ahora comienzo a buscar en el diccionario");
			u = 0;
			//while (u < cantidad){
			while (it.hasNext()) {
				IunidadDeHabla iunidadDeHabla = (IunidadDeHabla) it.next();
				
				String cadena = iunidadDeHabla.getTextoEscrito();
				
				//System.out.println(cadena);
				clave = new Clave();
				clave.setClave(cadena);
				
				reg = tree.buscar(clave);
				if (reg != null) {
					System.out.println("Encontre: " + clave.getClave());
				} else {
					System.out.println("##########################No encontre: " + clave.getClave());
				}
				u++;
				//Thread.sleep(50);
			}

		} catch (Exception e) {
			logger.error("Error: " + e);
			e.printStackTrace();
		}
	}
}
