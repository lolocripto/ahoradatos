/**
 * .
 */
package ar.com.datos.grupo5.btree;

import java.io.IOException;
import java.util.ArrayList;

import sun.security.x509.CertAndKeyGen;

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
	private BStar arbolBStar;
	
	/**
	 * Archivo por bloques que almacena los registros 
	 * de las hojas del arbol.
	 */
	private ArchivoSecuencialSet secuencialSet;
	
	/**
	 * 
	 */
	public BSharp() {
		try {
			this.arbolBStar = new BStar();
			this.secuencialSet = new ArchivoSecuencialSet();
			this.arbolBStar.setSecuencialSet(this.secuencialSet);
		} catch (Exception e) {
			System.out.println("Imposible inicialiar el Arbol BSharp.");
		}
	}

	
	public RegistroFTRS buscar(String termino) throws IOException {
		//Voy a buscar una maldita clave.
		RegistroNodo nodo = this.arbolBStar.buscar(new Clave(termino));
		if (nodo == null) {
			return null;
		}
		int punteroABloqueRegistro = nodo.getPunteroBloque();
		BloqueFTRS bloqueRegistrosFTRS = this.secuencialSet.leerBloque(punteroABloqueRegistro);
		return bloqueRegistrosFTRS.buscarRegistro(new Clave(termino));
	}

	public boolean insertar(String nuevaPalabraExt, long idTerminoExt) throws IOException {
		RegistroNodo reg = new RegistroNodo();
		reg.setClave(new Clave(nuevaPalabraExt));
		if (this.arbolBStar.insertar(reg)) {
			this.secuencialSet.bloquesActualizados(this.arbolBStar.getNodosModificados(), nuevaPalabraExt, idTerminoExt, -1);
			return true;
		}
		return false;
	}
	
// Saque long idTerminoExt, 
	public boolean modificar(String palabraExt, int offsetListaInvertida) {
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
			e.printStackTrace();
		}
		return true;
	}
	
	public void listar() {
		this.arbolBStar.listar();
	}
	
	public void cerrar(){
		this.secuencialSet.cerrar();
	}
	
	
}
