package tpdatos.CapaFisica.ArchivoBase;
import tpdatos.Common.Archivo.Archivo;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: ArchivoBase (Abstracta. Implementa primitivas para el manejo de
 * archivos organizados en bloques fijos en disco).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 11:05:54 a.m.
 */
public class ArchivoBase {

	/**
	 * Referencia al archivo
	 */
	private Archivo archivo;
	/**
	 * Tamaño en bytes del bloque
	 */
	private unsigned short tamBloque;

	public ArchivoBase(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param nombre
	 * @param tamBloque
	 */
	public ArchivoBase(string nombre, unsigned short tamBloque){

	}

	public abstract ~ArchivoBase();

	/**
	 * Metodos publicos
	 * 
	 * @param bloque
	 */
	public char escribir(final void* bloque){
		return 0;
	}

	public bool esValido(){
		return null;
	}

	public bool fin(){
		return null;
	}

	/**
	 * Getters
	 */
	public unsigned short getTamanioBloque(){
		return null;
	}

	/**
	 * 
	 * @param bloque
	 */
	public char leer(void* bloque){
		return 0;
	}

	public short posicion(){
		return 0;
	}

	/**
	 * 
	 * @param numBloque
	 */
	public char posicionarse(unsigned short numBloque){
		return 0;
	}

	public void posicionarseFin(){

	}

	public size_t size(){
		return null;
	}

}