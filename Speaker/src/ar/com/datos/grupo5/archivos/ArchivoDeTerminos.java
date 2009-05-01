/**
 * 
 */
package ar.com.datos.grupo5.archivos;

import java.io.IOException;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.registros.RegistroTerminoGlobal;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author Led Zeppelin
 *
 */
public class ArchivoDeTerminos extends Directo{

	
	/**
	 * Método para recuperar un registro de un archivo Directo.
	 * @param offset
	 *              La posición en la cual empieza el registro buscado.
	 * @return 
	 *        Retorna el registro que se encuentra en la posición offset.
	 * @throws IOException .
	 */
	@Override
	public Registro leer(final Long offset) throws IOException {
		Registro reg = null;
		int longitud = 0;
		
		file.seek(offset);
		byte[] bufferShort = new byte[Constantes.SIZE_OF_SHORT];
        byte[] bufferDato = null;
        file.read(bufferShort, 0, Constantes.SIZE_OF_SHORT);
        longitud = Conversiones.arrayByteToShort(bufferShort);
        bufferDato = new byte[longitud];
        
        file.read(bufferDato, 0, longitud);
        reg = new RegistroTerminoGlobal();
        reg.setBytes(bufferDato, (long) longitud);
		return reg;
	}
}
