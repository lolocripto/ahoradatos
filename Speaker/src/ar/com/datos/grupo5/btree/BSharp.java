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
	
	/**
	 * Arbol B*.
	 */
	private BTree arbolBStar;
	
	/**
	 * Archivo por bloques que almacena los registros 
	 * de las hojas del arbol.
	 */
	private ArchivoSecuencialSet secuencialSet;
	
	
	public BSharp() {
		try {
			//FIXME Agregar referencia al SecuencialSet
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
		//FIXME Debe devolver la lista de nodosActualizados.
		this.arbolBStar.insertar(reg);
		this.secuencialSet.bloquesActualizados(listaNodosActualizados, nuevaPalabraExt, idTerminoExt, -1);
		return false;
	}

	public boolean modificar(String palabraExt, long idTerminoExt, int offsetListaInvertida) {
		try {
			//Busco el nodo que contiene la clave que necesito.
			RegistroNodo nodo = this.arbolBStar.buscar(new Clave(palabraExt));
			//Busco el bloque donde supuestamente se encuentra el termino buscado.
			BloqueFTRS bloque = this.secuencialSet.leerBloque(nodo.getPunteroBloque());
			//busco el registro dentro del bloque.
			RegistroFTRS registro = bloque.buscarRegistro(new Clave(palabraExt));
			if (registro != null) {
				//Seteo el nuevo bloque para las lista invertida del termino.
				registro.setBloqueListaInvertida((long) offsetListaInvertida);
				//Grabo el bloque con los nuevos datos.
				this.secuencialSet.escribirBloque(bloque, nodo.getPunteroBloque());
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public RegistroNodo siguiente() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
