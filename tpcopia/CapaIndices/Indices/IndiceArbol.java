package tpdatos.CapaIndices.Indices;
import tpdatos.Common.Bloque.Bloque;
import tpdatos.CapaIndices.BTree.BTree;

/**
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:55:29 a.m.
 */
public class IndiceArbol extends Indice {

	private Bloque* bloque;
	private BTree* bTree;
	private unsigned char tipoOrg;

	public IndiceArbol(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param tipoIndice
	 * @param tipoDato
	 * @param listaTiposClave
	 * @param listaTipos
	 * @param tipoEstructura
	 * @param tamBloqueDatos
	 * @param tamNodo
	 * @param tamBloqueLista
	 * @param nombreArchivo
	 * @param tipoOrg
	 */
	public IndiceArbol(unsigned char tipoIndice, int tipoDato, ListaTipos* listaTiposClave, ListaInfoRegistro* listaTipos, unsigned char tipoEstructura, unsigned short tamBloqueDatos, unsigned short tamNodo, unsigned short tamBloqueLista, final string& nombreArchivo, unsigned char tipoOrg){

	}

	/**
	 * Este metodo busca una clave dentro del indice primario, y devuelve el registro
	 * de datos correspondiente a dicha clave.
	 * 
	 * @param clave
	 * @param registro
	 * @param tamanioRegistro
	 */
	public abstract int buscar(Clave* clave, char*& registro, unsigned short& tamanioRegistro);

	/**
	 * Este método busca una clave secundaria y devuelve la lista de claves primarias
	 * correspondiente a dicha clave.
	 * 
	 * @param clave
	 * @param setClavesPrimarias
	 */
	public abstract int buscar(Clave* clave, SetClaves*& setClavesPrimarias);

	/**
	 * 
	 * @param clave
	 */
	public abstract int buscar(Clave* clave);

	/**
	 * 
	 * @param tamRegistro
	 * @param bloqueDatos
	 */
	public abstract int buscarBloqueDestino(unsigned short tamRegistro, char* bloqueDatos);

	/**
	 * Este metodo elimina una clave del indice primario. Si la encuentra devuelve OK;
	 * y si no, devuelve ERROR_ELIMINACION.
	 * 
	 * @param clave
	 */
	public abstract int eliminar(Clave* clave);

	/**
	 * Este método elimina una clave primaria de la lista de claves primarias
	 * correspondiente a una clave secundaria. Si la lista queda vacía, también se
	 * elimina la clave secundaria.
	 * 
	 * @param claveSecundaria
	 * @param clavePrimaria
	 */
	public abstract int eliminar(Clave* claveSecundaria, Clave* clavePrimaria);

	public abstract SetEnteros getConjuntoBloques();

	public unsigned char getTipoOrganizacion(){
		return null;
	}

	public abstract ~IndiceArbol();

	/**
	 * Este metodo inserta una clave en un indice.
	 * 
	 * @param clave
	 * @param registro
	 * @param tamanioRegistro
	 */
	public abstract int insertar(Clave* clave, char*& registro, unsigned short tamanioRegistro);

	/**
	 * Este metodo inserta una clave primaria en la lista de claves primarias de una
	 * clave secundaria.
	 * 
	 * @param claveSecundaria
	 * @param clavePrimaria
	 */
	public abstract int insertar(Clave* claveSecundaria, Clave* clavePrimaria);

	/**
	 * 
	 * @param nroBloque
	 */
	public abstract Bloque* leerBloque(unsigned int nroBloque);

	/**
	 * 
	 * @param clave
	 */
	public abstract void mayor(Clave* clave);

	/**
	 * 
	 * @param clave
	 */
	public abstract void mayorOIgual(Clave* clave);

	/**
	 * Devuelve ResultadosIndices::ERROR_MODIFICACION si claveVieja no se encuentra en
	 * el indice. En caso contrario devuelve ResultadosIndices::OK, reemplaza
	 * claveVieja por claveNueva y reemplaza el viejo registro correspondiente a
	 * "claveVieja" por "registroNuevo". Este método se usa para claves primarias.
	 * 
	 * @param claveVieja
	 * @param claveNueva
	 * @param registroNuevo
	 * @param tamanioRegistroNuevo
	 */
	public abstract int modificar(Clave* claveVieja, Clave* claveNueva, char*& registroNuevo, unsigned short tamanioRegistroNuevo);

	public abstract void primero();

	public abstract Clave* siguiente();

	/**
	 * Método que devuelve el siguiente bloque de disco que contiene registros de
	 * datos.
	 * 
	 * @param bloque
	 */
	public abstract int siguienteBloque(Bloque*& bloque);

}