/**
 * .
 */
package ar.com.datos.grupo5.btree;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.FTRSManager;
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
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(FTRSManager.class);
	
	
	/**
	 * 
	 */
	public BSharp() {
		try {
			this.arbolBStar = new BStar();
			this.secuencialSet = new ArchivoSecuencialSet();
			this.arbolBStar.setSecuencialSet(this.secuencialSet);
		} catch (Exception e) {
			logger.debug("Imposible inicializar el Arbol BSharp.");
		}
	}

	
	public RegistroFTRS buscar(String termino) throws IOException {
		//Voy a buscar una maldita clave.
		RegistroNodo nodo = this.arbolBStar.buscar(new Clave(termino));
		if (nodo == null) {
			return null;
		}
		int punteroABloqueRegistro = nodo.getPunteroBloque();
		
		System.out.println("el secuencial intenta acceder al bloque: " + punteroABloqueRegistro);
		
		BloqueFTRS bloqueRegistrosFTRS = this.secuencialSet.leerBloque(punteroABloqueRegistro);
		return bloqueRegistrosFTRS.buscarRegistro(new Clave(termino));
	}

	public boolean insertar(String nuevaPalabraExt, long idTerminoExt) throws IOException {
		RegistroNodo reg;
		//Busco si existe la clave primero!!
		Clave claveABuscar = new Clave(nuevaPalabraExt);
		
		reg = this.arbolBStar.buscar(claveABuscar);
		
		//si no existe la inserto, en caso contrario
		if (reg == null) {
			//No existe, armo la clave y la intento insertar.
			reg = new RegistroNodo();
			reg.setClave(new Clave(nuevaPalabraExt));

			
			if (this.arbolBStar.insertar(reg)) {
				//Se pudo insertar, por lo tanto ahora modifico el SecuencialSer.
				ArrayList<Nodo> lista = this.arbolBStar.getNodosModificados();
				
				System.out.println(" elems pasados al sec: "+lista.size());
				
				this.secuencialSet.bloquesActualizados(lista, nuevaPalabraExt, idTerminoExt, -1);
				return true;
			} else {
				//no se pudo insertar
				return false;
			}
		} else {
			//la clave ya existia, pero hacia afuera solo importa que este en el arbol.
			return true;
		}

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
