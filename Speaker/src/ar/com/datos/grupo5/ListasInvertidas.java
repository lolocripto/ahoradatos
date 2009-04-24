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

public class ListasInvertidas {

	/**
	 * Último bloque leido
	 */
	private byte[] datosLeidosPorBloque = null;
	
	/**
	 * Cantidad de bloques dentro del archivo
	 */
	private int cantidadBloques;
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ListasInvertidas.class);

	/**
	 * Offset a la lista invertida dentro del bloque leido
	 */
	private int offsetLista;
	
	/**
	 * Nro de bloque donde empieza la lista invertida del termino
	 */
	private long nroBloque;
	
	/**
	 * Archivo que contendrá las palabras y que será manejado por el
	 * diccionario.
	 */
	private Archivo archivo;
	
	/**
	 * Lista con los espacios libres
	 */
	private List<NodoListaEspacioLibre> espacioLibrePorBloque;
	
	/**
	 * Numero del bloque donde empieza la lista de espacio libre
	 */
	private int NroBloqueLista;
	
	
	/**
	 * Metodo para cargar el diccionario, accediendo al archivo.
	 * 
	 * @param archivo
	 *            La ruta completa del archivo a cargar.
	 * @param modo
	 *            El modo en el cual se debe abrir el archivo.
	 * @return true si pudo abrir el archivo.
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cargar()
	 */
	public final boolean abrir(final String archivo, final String modo)
			throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	
	/**
	 * Método que cierra el diccionario.
	 * 
	 * @throws IOException
	 * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public final void cerrar() throws IOException {
		this.archivo.cerrar();
	}
	
	/**
	 * Lee la lista del termino pedido en el bloque especificado.
	 * 
	 * @param id_termino
	 *            El idtermino al que la lista a buscar esta vinculada.
	 * @param NroBloque
	 * 			  El número del bloque donde se encuentra la lista.
	 * @return null si no encuentra la lista, si no devuelve el registro.
	 */
	public final RegistroTerminoDocumentos leerLista(final long id_termino, final long NroBloque) {
		
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		this.nroBloque = NroBloque;
		long siguiente = this.nroBloque;
		short primerRegistro;
		short espacioOcupado;
		short cantBloquesLeidos = 0;

		try {
				while(reg.incompleto() || reg.getCantidadDocumentos() == 0){
				
					/* Obtengo el bloque según el numero de bloque*/
					//TODO: Ver que se convierta el nroBloque en offset
					datosLeidosPorBloque = this.archivo.leerBloque(siguiente);
					
					/* Saco la información de control del bloque */
					ByteArrayInputStream bis = new ByteArrayInputStream(datosLeidosPorBloque);  
					DataInputStream dis = new DataInputStream(bis);
					
					siguiente = dis.readLong();
					primerRegistro = dis.readShort();
					espacioOcupado = dis.readShort();
					
					if(cantBloquesLeidos == 0){
						/* Busco el termino */
						if( !this.buscarIdTermino(datosLeidosPorBloque,id_termino,primerRegistro,espacioOcupado) ){
							/* Si no lo encuentro en el bloque esta mal el bloque por lo tanto no busco en bloques siguientes */
							return null;
						}
						
						/* Armo la lista para el termino especifico */
						reg.setBytes(this.datosLeidosPorBloque, (long) this.offsetLista);
					} else {
						reg.setMoreBytes(datosLeidosPorBloque, Constantes.SIZE_OF_LONG+Constantes.SIZE_OF_SHORT*2);
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
	 * @throws FileNotFoundException
	 * @link ar.com.datos.grupo5.interfaces.Archivo#insertar(Registro)
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
	
	private void validarEncabezado(){
		//TODO: Implementar
		// CantidadBloques|offset Lista de lugares libres
		leerEncabezadoArchivo();
		
	}
	
	/**
	 * Lee el encabezado del archivo, si no existe entonces lo escribe e inicializa el
	 * archivo de bloques
	 */
	private void leerEncabezadoArchivo(){
		byte[] datosControlArchivo = null;
		char[] Control = new char[7];

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
			if(datosControlArchivo.length > 0){
				ByteArrayInputStream bis = new ByteArrayInputStream(datosControlArchivo);  
				DataInputStream dis = new DataInputStream(bis);
				BufferedReader d
		          = new BufferedReader(new InputStreamReader(bis));
				
				d.read(Control, 0, 7);
				String claveDatoControl = new String(Control);
				if(claveDatoControl.compareTo("Control") == 0) {
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
		} catch ( UnImplementedMethodException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Escribe el encabezado del archivo
	 */
	private void escribirEncabezadoArchivo(){
		//TODO: Implementar
		
	}
	/**
	 * Levanta la lista de espacios libres a memoria.
	 */
	private void levantarListaAMemoria(){
		
	}
	/**
	 * Busca en las listas la lista que tenga el idTerminoBuscado
	 * @param Listas Una cadena de Byte que contiene las listas invertidas de terminos.
	 * @param idTerminoBuscado Es el id de termino relacionado con la lista.
	 * @param offsetPrimerLista	Es el offset donde comienzan las listas en la cadena de bytes
	 * @param espacioOcupado Es el espacio ocupado del byte[]
	 * @return True si encuentra la lista vinculada al idTermino y false si llega al final
	 * de la cadena de bytes sin encontrarla. 
	 */
	private boolean buscarIdTermino( byte[] Listas, long idTerminoBuscado, int offsetPrimerLista, short espacioOcupado){
		
		/* Estructura: idTermino->long, CantidadDocumentos->long, pares(long,long) */
		long idTermino = 0;
		int cantDocumentos = 0;
		int offsetAConsultar = Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT*2 + offsetPrimerLista;
		
		//Empiezo a leer desde la primer lista del bloque
		ByteArrayInputStream bis; 
		DataInputStream dis;
		try {
			while (offsetAConsultar < Listas.length
					&& offsetAConsultar < espacioOcupado) {
				/*
				System.arraycopy(datoLongBytes, 0, Listas, offsetAConsultar, Constantes.SIZE_OF_LONG);
				idTermino = Conversiones.arrayByteToLong(datoLongBytes);
				 */
				
				bis = new ByteArrayInputStream(Listas,offsetAConsultar,espacioOcupado);  
				dis = new DataInputStream(bis);
				
				idTermino = dis.readLong();
				if (idTerminoBuscado == idTermino) {
					/* Son iguales por lo tanto el offset de la lista es igual a la primera lista */
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
		
		if(offsetAConsultar >= Listas.length || offsetAConsultar > espacioOcupado){
			/* la lista no se encuentra */
			return false;
		}
		
		return true;

	}

	public void setNroBloqueLista(int nroBloqueLista) {
		NroBloqueLista = nroBloqueLista;
	}

	public int getNroBloqueLista() {
		return NroBloqueLista;
	}

	public void setCantidadBloques(int cantidadBloques) {
		this.cantidadBloques = cantidadBloques;
	}

	public int getCantidadBloques() {
		return cantidadBloques;
	}

	public void setEspacioLibrePorBloque(List<NodoListaEspacioLibre> espacioLibrePorBloque) {
		this.espacioLibrePorBloque = espacioLibrePorBloque;
	}

	public List<NodoListaEspacioLibre> getEspacioLibrePorBloque() {
		return espacioLibrePorBloque;
	}

}
