package ar.com.datos.grupo5.compresion.lzp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.archivos.ArchivoBloques;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:19 a.m.
 */
public class ListaContextos {
	
	/**
	 * Cantidad de paginas de la lista. si es mas de 1, las demas estas en
	 * disco.
	 */
	private int paginas = 1;
	
	/**
	 * Numero de la pagina que se encuentra en memoria.
	 */
	private int nroPaginaActual = 1;
	
	/**
	 * Archivo de bloques en el cual se guardan las paginas.
	 */
	private ArchivoBloques archivo = null;
	
	/**
	 * Solo puedo tener 4K en memoria, entonces como guardo un String de 2
	 * caracteres (4 byte) y un entero (4 bytes), puedo tener 4096 / 8 = 512
	 * elementos en el map. Tambien necesito 4 bytes para guardar la cantidad de
	 * elementos que hay. Entonces puedo tener 511 elementos.
	 */
	private int maxCantidadPorPagina = 511;
	
	/**
	 * 
	 */
	private Map<String, Integer> mapaContexto;
	
	/**
	 * 
	 */
	public final int size(){
		return mapaContexto.size();
	}

	/**
	 * 
	 */
	public ListaContextos(){
		archivo = new ArchivoBloques(4096);
		mapaContexto = new HashMap<String, Integer>();
	}

	/**
	 * 
	 */
	public Integer getPosicion(String contexto) {
		
		// return mapaContexto.get(contexto)
		
		int result = buscarElemento(contexto);
		if (result == 0) {
			return null;
		} else {
			return result;
		}
	}

	/**
	 * 
	 */
	public void setPosicion(String contexto, Integer posicion) {
		
		//mapaContexto.put(contexto, posicion);
		
		//Primero tengo que ver si esta en alguna pagina.
		buscarElemento(contexto);
		
		if (mapaContexto.size() < maxCantidadPorPagina) {
			mapaContexto.put(contexto, posicion);
		} else {
			nroPaginaActual++;
			paginas++;
			paginarADisco();
		}
	}
	
	/**
	 * 
	 */
	public void clear(){
		mapaContexto.clear();
	}

	/**
	 * 
	 * @param datos
	 */
	public final String toString(){
		String result = new String();
		for (Entry<String, Integer> entry : mapaContexto.entrySet()) {
			   result += "[" + entry.getKey() + "->" + entry.getValue() + "] ";
			  }
		return result;
	}
	
	/**
	 * Busca un elemento en todas las paginas.
	 * 
	 * @param clave
	 *            La clave del elemento.
	 * @return El numero de la pagina en donde se encuentra, 0 si no se
	 *         encuentra.
	 */
	private int buscarElemento(final String clave) {
		
		Integer resultado = null;
		
		//Primero busco en memoria.
		resultado = mapaContexto.get(clave);
		if (resultado != null) {
			return resultado;
		}
		
		int pagina = nroPaginaActual;
		
		//Busco en el resto de las paginas.
		for (int i = 1; i <= paginas; i++) {
			if (i != pagina) {
				cargarPaginaEnMemoria(i);
				resultado = mapaContexto.get(clave);
				if (resultado != null) {
					return resultado;
				}
			}
		}

		return 0;
	}
	
	/**
	 * 
	 * @param nroPagina
	 */
	private void cargarPaginaEnMemoria(int nroPagina) {
		
		try {
			mapaContexto.clear();
			byte[] datos = archivo.leerBloque(nroPagina);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(datos);
			DataInputStream dos = new DataInputStream(bis);

			//Leo la cantidad de elementos.
			int cantidad = dos.readInt();
			int contador = 0;
			byte[] clave = new byte[4];
			int pos = 0;
			Charset charset = Charset.forName(Constantes.CHARSET_UTF16 + "BE");
			
			while (contador < cantidad) {

				dos.read(clave, 0, 4);
				pos = dos.readInt();
				mapaContexto.put(new String(clave, charset), pos);
				contador++;
			}
			
			nroPaginaActual = nroPagina;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Baja a disco la pagina actual.
	 */
	private void paginarADisco() {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {			
			
			//Cantidad de elementos.
			byte[] cantidad = Conversiones.intToArrayByte(mapaContexto.size());
			dos.write(cantidad, 0, cantidad.length);
			
			byte[] regBytes = new byte[4];
			Charset charset = Charset.forName(Constantes.CHARSET_UTF16 + "BE");
			
			for (Entry<String, Integer> entry: mapaContexto.entrySet()) {
				regBytes = entry.getKey().getBytes(charset);
				dos.write(regBytes);
				regBytes = Conversiones.intToArrayByte(entry.getValue());
				dos.write(regBytes);
			}
			
			archivo.escribirBloque(bos.toByteArray(), nroPaginaActual) ;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}