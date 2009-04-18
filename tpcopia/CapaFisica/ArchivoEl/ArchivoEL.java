package tpdatos.CapaFisica.ArchivoEL;
import tpdatos.CapaFisica.ArchivoBase.ArchivoBase;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: ArchivoEL (Clase que define el comportamiento del manejo de los
 * archivos de control de espacio libre).
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 11:06:31 a.m.
 */
public class ArchivoEL extends ArchivoBase {

	public ArchivoEL(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructor/Destructor
	 * 
	 * @param nombre
	 * @param tamBloque
	 */
	public ArchivoEL(string nombre, unsigned short tamBloque){

	}

	/**
	 * Metodos publicos
	 * 
	 * @param registro
	 */
	public abstract char agregarRegistro(final void* registro);

	public abstract ~ArchivoEL();

	/**
	 * 
	 * @param registro
	 * @param numRegistro
	 */
	public abstract char modificarRegistro(final void* registro, unsigned short numRegistro);

}