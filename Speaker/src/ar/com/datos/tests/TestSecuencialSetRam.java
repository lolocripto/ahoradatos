/**
 * 
 */
package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * @author xxvkue
 *
 */
public class TestSecuencialSetRam {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		Collection<RegistroNodo> listaPalabras = new ArrayList<RegistroNodo>();
		Collection<RegistroNodo> registros = new ArrayList<RegistroNodo>();
		ArrayList<Nodo> nodos = new ArrayList<Nodo>();
		
		CodificadorFrontCoding codi = new CodificadorFrontCoding();
		
		ArchivoSecuencialSet miArchivo = new ArchivoSecuencialSet();
		
		Nodo nodo = new Nodo();
		
		miArchivo.primeraInsercion("codazo", 0, -1);
		
		RegistroNodo reg = new RegistroNodo();
		reg.setClave(new Clave("codazo"));
		listaPalabras.add(reg);

		reg = new RegistroNodo();
		reg.setClave(new Clave("codearse"));
		listaPalabras.add(reg);

		reg = new RegistroNodo();
		reg.setClave(new Clave("codera"));
		listaPalabras.add(reg);

		reg = new RegistroNodo();
		reg.setClave(new Clave("codazo"));
		listaPalabras.add(reg);

		
		reg = new RegistroNodo();
		reg.setClave(new Clave("codicia"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("codiciar"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("codiciosa"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("dale"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("dar"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("digale"));
		listaPalabras.add(reg);
		
		Iterator<RegistroNodo> it = registros.iterator();
		while (it.hasNext()) {
			//TODO: VER PORQUE CUELGA ATRIBUTO PRIVATE
			nodo.registros.add(it.next());
		}
		
		nodo.setPunteroBloque(1);
		
		nodos.add(nodo);
		
		/* Segundo nodo*/
		listaPalabras.clear();
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("luna"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("liciernaga"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("lenceria"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("luz"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("mama"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("mamita"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("marmota"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("mima"));
		listaPalabras.add(reg);
		
		reg = new RegistroNodo();
		reg.setClave(new Clave("saludo"));
		listaPalabras.add(reg);
		
		it = registros.iterator();
		
		while (it.hasNext()) {
			nodo.registros.add(it.next());
		}
		
		nodo.setPunteroBloque(2);
		nodos.add(nodo);
		
		miArchivo.bloquesActualizados(nodos, "hola", 0, -1);

	}

}
