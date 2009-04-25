package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.lang.System;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;

/**
 * Clase que administra la inserción, modificación y busqueda de las listas
 * invertidas en un archivo por bloques.
 * @author Led Zeppelin
 *
 */
public class ListasInvertidas {

	/**
	 * Último bloque leido.
	 */
	private byte[] datosLeidosPorBloque = null;
	
	/**
	 * Cantidad de bloques dentro del archivo.
	 */
	private int cantidadBloques;
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ListasInvertidas.class);

	/**
	 * Offset a la lista invertida dentro del bloque leido.
	 */
	private int offsetLista;
	
	/**
	 * Nro de bloque donde empieza la lista invertida del termino.
	 */
	private long nroBloque;
	
	/**
	 * Archivo que contendrá las palabras y que será manejado por el
	 * diccionario.
	 */
	private Archivo archivo;
	
	/**
	 * Lista con los espacios libres.
	 */
	private List<NodoListaEspacioLibre> espacioLibrePorBloque;
	
	/**
	 * Numero del bloque donde empieza la lista de espacio libre.
	 */
	private int nroBloqueLista;
	
	
	/**
	 * Metodo para cargar el diccionario, accediendo al archivo.
	 * 
	 * @param archivoNombre
	 *            La ruta completa del archivo a cargar.
	 * @param modo
	 *            El modo en el cual se debe abrir el archivo.
	 * @return true si pudo abrir el archivo.
	 * @throws FileNotFoundException
	 * 		El archivo no fue encontrado.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cargar()
	 */
	public final boolean abrir(final String archivoNombre, final String modo)
			throws FileNotFoundException {
		return this.archivo.abrir(archivoNombre, modo);
	}
	
	/**
	 * Método que cierra el diccionario.
	 * 
	 * @throws IOException
	 * 			Puede ocurrir un error al cerrarse el archivo
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		this.archivo.cerrar();
	}
	
	/**
	 * Lee la lista del termino pedido en el bloque especificado.
	 * 
	 * @param idTerminoExt
	 *            El idtermino al que la lista a buscar esta vinculada.
	 * @param nroBloqueExt
	 * 			  El número del bloque donde se encuentra la lista.
	 * @return null si no encuentra la lista, si no devuelve el registro.
	 */
	public final RegistroTerminoDocumentos leerLista(final long idTerminoExt, 
			final long nroBloqueExt) {
		
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		this.nroBloque = nroBloqueExt;
		long siguiente = this.nroBloque;
		short primerRegistro;
		short espacioOcupado;
		short cantBloquesLeidos = 0;

		try {
				while (reg.incompleto() || reg.getCantidadDocumentos() == 0) {
				
					/* Obtengo el bloque según el numero de bloque*/
					datosLeidosPorBloque = this.archivo.leerBloque(siguiente);
					
					/* Saco la información de control del bloque */
					ByteArrayInputStream bis 
						= new ByteArrayInputStream(datosLeidosPorBloque);  
					DataInputStream dis = new DataInputStream(bis);
					
					siguiente = dis.readLong();
					primerRegistro = dis.readShort();
					espacioOcupado = dis.readShort();
					
					if (cantBloquesLeidos == 0) {
						/* Busco el termino */
						if (!this.buscarIdTermino(datosLeidosPorBloque, 
								idTerminoExt, primerRegistro, espacioOcupado)) {
							/* Si no lo encuentro en el bloque esta mal el 
							 * bloque por lo tanto no busco en bloques 
							 * siguientes */
							return null;
						}
						
						/* Armo la lista para el termino especifico */
						reg.setBytes(this.datosLeidosPorBloque, 
										(long) this.offsetLista);
						
					} else {
						reg.setMoreBytes(datosLeidosPorBloque, 
								Constantes.SIZE_OF_LONG	
								+ Constantes.SIZE_OF_SHORT * 2);
					}
				}

		} catch (UnImplementedMethodException e) {
			logger.error("Error: " + e.getMessage());
			return null;
		} catch (IOException e) {
			logger.error("Error: " + e.getMessage());
			return null;
		}
		
		/* Devuelvo el registro leido */
		return reg;
	}
	
	/**
	 * Metodo para agregar una palabra al diccionario.
	 * 
	 * @param palabra
	 *            La palabra que se quiere agregar al diccionario.
	 * @param offset
	 *            Es la posición de la palabra dentro del archivo de audio.
	 * @return retorna TRUE si pudo agregr la palabra, o FALSE en caso
	 *         contrario.
	 */
	public final boolean agregar(final String palabra, final Long offset) {
		/*
		 * 
		 */
		RegistroDiccionario reg = new RegistroDiccionario();
		
		reg.setOffset(offset);
		reg.setDato(palabra);
		try {
			this.archivo.insertar(reg);
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	
	/**
	 * Constructor de la clase.
	 *
	 */
	public ListasInvertidas() {
		this.archivo = new ArchivoBloques();
		this.validarEncabezado();
	}
	
	/**
	 * Valida el encabezado del archivo, este esta en el primer bloque.
	 *
	 */
	private void validarEncabezado() {
		//TODO: Implementar
		// CantidadBloques|offset Lista de lugares libres
		leerEncabezadoArchivo();
		
	}
	
	/**
	 * Lee el encabezado del archivo, si no existe 
	 * entonces lo escribe e inicializa el.
	 * archivo de bloques
	 */
	private void leerEncabezadoArchivo() {
		byte[] datosControlArchivo = null;
		char[] control = new char[1];

		try {
			//TODO: Implementar
			/*
			 * 1) Abrir el archivo
			 * 2) Leer el primer bloque
			 * 3) si no existe lo escribo
			 */
			
			//Leo el primer bloque
			datosControlArchivo = this.archivo.leerBloque(0L);
			
			//TODO: Este if debería ser una Exception
			if (datosControlArchivo.length > 0) {
				
				ByteArrayInputStream bis 
				  = new ByteArrayInputStream(datosControlArchivo);  
				DataInputStream dis = new DataInputStream(bis);
				
				BufferedReader d
		          = new BufferedReader(new InputStreamReader(bis));
				
				d.read(control, 0, 1);
				String claveDatoControl = new String(control);
				if (claveDatoControl.compareTo("Control") == 0) {
					this.setCantidadBloques(dis.readInt());
					this.setNroBloqueLista(dis.readInt());
					levantarListaAMemoria();
				} else {
					escribirEncabezadoArchivo();
				}
					
			} else {
				escribirEncabezadoArchivo();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnImplementedMethodException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Escribe el encabezado del archivo.
	 */
	private void escribirEncabezadoArchivo() {
		//TODO: Implementar
		
	}
	/**
	 * Levanta la lista de espacios libres a memoria.
	 */
	private void levantarListaAMemoria() {
		
	}
	
	/**
	 * Busca en las listas la lista que tenga el idTerminoBuscado.
	 * @param listas Una cadena de Byte que contiene las listas 
	 * 			invertidas de terminos.
	 * @param idTerminoBuscado Es el id de termino relacionado con la lista.
	 * @param offsetPrimerLista	Es el offset donde comienzan las listas en la 
	 * 			cadena de bytes
	 * @param espacioOcupado Es el espacio ocupado del byte[]
	 * @return True si encuentra la lista vinculada al idTermino y false si 
	 * 			llega al final de la cadena de bytes sin encontrarla. 
	 */
	private boolean buscarIdTermino(final byte[] listas, 
			final long idTerminoBuscado, final int offsetPrimerLista, 
			final short espacioOcupado) {
		
		/* Estructura: idTermino->long, CantidadDocumentos->long, 
		 * pares(long,long) */
		long idTermino = 0;
		int cantDocumentos = 0;
		int offsetAConsultar = Constantes.SIZE_OF_LONG 
			+ Constantes.SIZE_OF_SHORT * 2 + offsetPrimerLista;
		
		//Empiezo a leer desde la primer lista del bloque
		ByteArrayInputStream bis; 
		DataInputStream dis;
		try {
			while (offsetAConsultar < listas.length
					&& offsetAConsultar < espacioOcupado) {
				/*
				System.arraycopy(datoLongBytes, 0, Listas, 
				offsetAConsultar, Constantes.SIZE_OF_LONG);
				idTermino = Conversiones.arrayByteToLong(datoLongBytes);
				 */
				
				bis = new ByteArrayInputStream(listas, offsetAConsultar, 
							espacioOcupado);  
				dis = new DataInputStream(bis);
				
				idTermino = dis.readLong();
				if (idTerminoBuscado == idTermino) {
					/* Son iguales por lo tanto el offset de la lista es 
					 * igual a la primera lista */
					this.offsetLista = offsetAConsultar;
					return true;
				}

				/* No es el mismo */

				offsetAConsultar += Constantes.SIZE_OF_LONG;
/*
 * 				System.arraycopy(datoLongBytes, 0, Listas, offsetAConsultar,
						Constantes.SIZE_OF_INT);
				cantDocumentos = Conversiones.arrayByteToInt(datoLongBytes);
*/
				cantDocumentos = dis.readInt();
				/* Me muevo un long, cuyo valor tiene cantDocumentos */
				offsetAConsultar += Constantes.SIZE_OF_INT;

				/* Me muevo la lista entera de documentos */
				offsetAConsultar += Constantes.SIZE_OF_LONG * 2
						* cantDocumentos;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (offsetAConsultar >= listas.length 
				|| offsetAConsultar > espacioOcupado) {
			/* la lista no se encuentra */
			return false;
		}
		
		return true;

	}

	/**
	 * Permite establecer el numero de bloque donde se encuentra 
	 * la lista de espacios libres. 
	 * @param nroBloqueListaExt
	 * 		Número del bloque donde se encuentra la lista.
	 */
	public final void setNroBloqueLista(final int nroBloqueListaExt) {
		nroBloqueLista = nroBloqueListaExt;
	}

	/**
	 * Permite obtener el número del bloque donde esta la lista
	 * de espacios libres.
	 * @return
	 * 		El número de bloque.
	 */
	public final int getNroBloqueLista() {
		return nroBloqueLista;
	}

	/**
	 * Permite establecer la cantidad de bloques totales que tiene el archivo.
	 * @param cantidadBloquesExt
	 * 		Devuelve la cantidad de bloques de archivo.
	 */
	private void setCantidadBloques(final int cantidadBloquesExt) {
		this.cantidadBloques = cantidadBloquesExt;
	}

	/**
	 * Permite obtener la cantidad de bloques totales que tiene el archivo. 
	 * @return
	 * 		La cantidad de bloques en el archivo.
	 */
	public final int getCantidadBloques() {
		return cantidadBloques;
	}

	/**
	 * Permite cargar una lista con pares Bloque-EspacioLibre.
	 * @param espacioLibrePorBloqueExt
	 * 		Lista con los pares Bloque-EspacioLibre.
	 */
	public final void setEspacioLibrePorBloque(
			final List<NodoListaEspacioLibre> espacioLibrePorBloqueExt) {
		this.espacioLibrePorBloque = espacioLibrePorBloqueExt;
	}

	/**
	 * Permite obtener la lista con pares Bloque-EspacioLibre.
	 * @return
	 * 		Lista con los pares Bloque-EspacioLibre.
	 */
	public final List<NodoListaEspacioLibre> getEspacioLibrePorBloque() {
		return espacioLibrePorBloque;
	}

}
