/**
 * 
 */
package ar.com.datos.grupo5.btree;

import java.io.IOException;

import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.registros.RegistroNodo;

/**
 * @author Led Zeppelin
 *
 */
public class BSharp implements BTree{
	
	
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

	
	public RegistroNodo buscar(Clave clave) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean insertar(RegistroNodo registro) throws IOException {
		// TODO Auto-generated method stub
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
