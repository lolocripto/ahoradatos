/**
 * 
 */
package ar.com.datos.grupo5.btree;

import java.io.IOException;
import java.util.ArrayList;

import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * @author Led Zeppelin
 *
 */
public class BSharp {
	
	
	private BTree arbolBStar;
	
	private ArchivoSecuencialSet secuencialSet;
	
	
	public BSharp() {
		try {
			this.arbolBStar = new BStar();
			this.secuencialSet = new ArchivoSecuencialSet();
		} catch (Exception e) {
			System.out.println("Imposible inicialiar el Arbol BSharp.");
		}
	}

	
	public RegistroNodo buscar(String termino) throws IOException {
		// TODO Auto-generated method stub
		//Voy a buscar una maldita clave.
		RegistroNodo nodo = this.arbolBStar.buscar(new Clave(termino));
		int punteroABloqueRegistro = nodo.getPunteroBloque();
		BloqueFTRS bloqueRegistrosFTRS = this.secuencialSet.leerBloque(punteroABloqueRegistro);
		return bloqueRegistrosFTRS.buscarRegistro(new Clave(termino));
	}

	public boolean insertar(String nuevaPalabraExt, long idTerminoExt) throws IOException {
		ArrayList<Nodo> listaNodosActualizados;
		//FIXME: El siguiente new hay que borrarlo
		listaNodosActualizados = new ArrayList<Nodo>();
		RegistroNodo reg = new RegistroNodo();
		reg.setClave(new Clave(nuevaPalabraExt));
		this.arbolBStar.insertar(reg);
		this.secuencialSet.bloquesActualizados(listaNodosActualizados, nuevaPalabraExt, idTerminoExt, -1);
		return false;
	}

	public boolean modificar(RegistroNodo registro) {
		// TODO Auto-generated method stub
		return false;
	}

	public RegistroNodo siguiente() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
