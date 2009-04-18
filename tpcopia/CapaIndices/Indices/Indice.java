package tpdatos.CapaIndices.Indices;
import tpdatos.CapaIndices.Manager.BloqueManager.BloqueManager;
import tpdatos.CapaIndices.Manager.IndiceManager.IndiceManager;
import tpdatos.Common.ListaInfoRegistro;

/**
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:50:54 a.m.
 */
public class Indice {

	protected BloqueManager* bloqueManager;
	protected IndiceManager* indiceManager;
	protected ListaInfoRegistro* listaInfoReg;
	protected unsigned short tamBloqueDatos;
	protected unsigned short tamBloqueLista;
	protected unsigned char tipoEstructura;
	protected unsigned char tipoIndice;



	public void finalize() throws Throwable {

	}

	public Indice(){

	}

	/**
	 * 
	 * @param clave
	 * @param registro
	 * @param tamanioRegistro
	 */
	public abstract int buscar(Clave* clave, char*& registro, unsigned short& tamanioRegistro);

	/**
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
	 * 
	 * @param clave
	 */
	public abstract int eliminar(Clave* clave);

	/**
	 * 
	 * @param claveSecundaria
	 * @param clavePrimaria
	 */
	public abstract int eliminar(Clave* claveSecundaria, Clave* clavePrimaria);

	public abstract SetEnteros getConjuntoBloques();

	public ListaInfoRegistro* getListaInfoReg(){
		return null;
	}

	public ListaInfoRegistro* getListaInfoRegClavePrimaria(){
		return null;
	}

	public ListaTipos* getListaTipos(){
		return null;
	}

	public ListaTipos* getListaTiposClavePrimaria(){
		return null;
	}

	public unsigned short getTamanioBloqueDatos(){
		return null;
	}

	public unsigned short getTamanioBloqueLista(){
		return null;
	}

	public unsigned char getTipoEstructura(){
		return null;
	}

	public unsigned char getTipoIndice(){
		return null;
	}

	public abstract ~Indice();

	/**
	 * 
	 * @param clave
	 * @param registro
	 * @param tamanioRegistroNuevo
	 */
	public abstract int insertar(Clave* clave, char*& registro, unsigned short tamanioRegistroNuevo);

	/**
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
	 * 
	 * @param claveVieja
	 * @param claveNueva
	 * @param registroNuevo
	 * @param tamanioRegistroNuevo
	 */
	public abstract int modificar(Clave* claveVieja, Clave* claveNueva, char*& registroNuevo, unsigned short tamanioRegistroNuevo);

	/**
	 * Este método modifica claves secundarias y sus correspondientes listas de
	 * claves primarias.
	 * 
	 * @param claveSecundariaVieja
	 * @param claveSecundariaNueva
	 * @param clavePrimariaVieja
	 * @param clavePrimariaNueva
	 */
	public abstract int modificar(Clave* claveSecundariaVieja, Clave* claveSecundariaNueva, Clave* clavePrimariaVieja, Clave* clavePrimariaNueva);

	public abstract void primero();

	public abstract Clave* siguiente();

	/**
	 * 
	 * @param bloque
	 */
	public abstract int siguienteBloque(Bloque*& bloque);

}