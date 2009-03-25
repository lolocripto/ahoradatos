package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Constantes de la aplicacion.
 * @author cristian
 *
 */
public class Constantes {

	/**
	 * Constructor.
	 */
	public Constantes() {
		super();
	}
	
	/**
	 * Tamaño del buffer de lectura.
	 */
	public static final int TAMANIO_BUFFER_LECTURA = 128;
	
	/**
	 * Tamaño del buffer de escritura.
	 */
	public static final int TAMANIO_BUFFER_ESCRITURA = 128;
	
	/**
	 * Abrir un archivo para lectura.
	 */
	public static final String ABRIR_PARA_LECTURA = "r";
	
	/**
	 * Abrir un archivo para lectura y escritura.
	 */
	public static final String ABRIR_PARA_LECTURA_ESCRITURA = "rw";
	
}
