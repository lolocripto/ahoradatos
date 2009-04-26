package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.archivos.ArchivoBloques;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

//TODO: Excepciones, falta tirar las excepciones
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
	private ArchivoBloques archivo;
	
	/**
	 * Lista con los espacios libres.
	 */
	private List<NodoListaEspacioLibre> espacioLibrePorBloque;
	//TODO: Separar la implementacion de la lista de espacioLibrePorbloque en otra clase
	
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
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#cargar()
	 */
	public final boolean abrir(final String archivoNombre, final String modo)
			throws FileNotFoundException {
		if (!this.archivo.abrir(archivoNombre, modo)){
			logger.debug("No se pudo abrir archivo de listas invertidas.");
			return false;
		}
		return this.validarEncabezado();
	}
	
	/**
	 * Método que cierra el diccionario.
	 * 
	 * @throws IOException
	 * 			Puede ocurrir un error al cerrarse el archivo
	 * @see ar.ar.com.datos.grupo5.archivos.Archivo#cerrar()
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

		} catch (IOException e) {
			logger.error("Error: " + e.getMessage());
			return null;
		}
		
		/* Devuelvo el registro leido */
		return reg;
	}
	
	/**
	 * Arma los datos del bloque con los parametros que recibe
	 * @param nroSiguienteBloque Numero del siguiente bloque
	 * @param datos Datos perteneciente al bloque
	 * @param primerRegistro Es el offset a partir de los datos de 
	 * 			control donde empieza el primer registro
	 * @param espacioOcupado Es el espacio ocupado del bloque 
	 * @return
	 * 		Devuelve los datos en un arreglo de Bytes.
	 */
	private byte[] armarDatosBloque(final Long nroSiguienteBloque, final byte[] datos, final short primerRegistro, final short espacioOcupado) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
				
		try {
			dos.writeLong(nroSiguienteBloque);
			dos.writeShort(primerRegistro);
			dos.writeShort(espacioOcupado);
			dos.write(datos);
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
		}		
		return bos.toByteArray();
	}
	
	/**
	 * Se encarga de actualizar un nodo o agregar un nodo en la lista de espacios libres
	 * para mantener actualizada la lista.
	 */
	private void actualizarListaEspacioLibre(final int index, final int nroBloque, final short espacio){
		NodoListaEspacioLibre nodo;
		switch(index){
			case -1:
				/* Agrego un elementos mas a la lista */
				nodo = new NodoListaEspacioLibre();
				nodo.setEspacio(espacio);
				nodo.setNroBloque(nroBloque);
				this.espacioLibrePorBloque.add(nodo);
			break;
			default:
				/* Modifico un elemento mas a la lista */
				nodo = this.espacioLibrePorBloque.get(index);
				nodo.setEspacio(espacio);
			break;
		}
		//TODO: Ordenar lista de espacios libres
		
	}
	
	
	/**
	 * Metodo para agregar una palabra al diccionario.
	 * 
	 * @param idTerminoExt
	 *            Id del termino global del cual se va a insertar la 
	 *            lista invertida.
	 * @param listaExt
	 *            Es la lista que contiene las frecuencias del termino
	 *            en los documentos asociados.
	 * @return retorna TRUE si pudo agregar la lista invertida, 
	 * 			o FALSE en caso contrario.
	 */
	public final boolean agregar(final Long idTerminoExt, final Collection<ParFrecuenciaDocumento> listaExt) {
		
		/* Variable para saber si tengo uno o mas bloque por escribir */
		boolean masRegistros = false;
		
		/* Total de bloques a escribir */
		int		totalBloques = 1;
		
		/* Tamaño total de los datos de control */
		int 	tamanioDatosControl = Constantes.SIZE_OF_LONG
				+ (Constantes.SIZE_OF_SHORT * 2);
		/* Registro que contiene las frecuencias y los documentos */
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		
		reg.setIdTermino(idTerminoExt);
		reg.setDatosDocumentos(listaExt);
		
		byte[] bytes = reg.getBytes();
		int tamanioRegistro = bytes.length;
		
		//Tamaño disponible en el bloque
		int bytesDisponibles = Constantes.SIZE_OF_INDEX_BLOCK - tamanioDatosControl;
		
		/* Valido que la cantidad de información a insertar entre 
		 * en un bloque */
		masRegistros = (tamanioRegistro > bytesDisponibles);

		byte[] bytesAEscribir;
		
		/* Genero todos los registros */
		if (masRegistros) {
			//Calculo el total de bloques
			totalBloques = tamanioRegistro / bytesDisponibles;
			if ((tamanioRegistro % bytesDisponibles) > 0) {
				totalBloques++;
			}
			
			/* Armo los bloques que serán escritos al final del archivo 
			 * pero el ultimo lo escribo a parte ya que el 
			 * "siguiente" es distinto*/
			for (int i = 0; i < totalBloques-1; i++) {
				//TODO: Tengo que definir bytes, tengo que cortarlo, Donde comienza el primer registro? en el primer bloque en 0 en los demas? en -1
				//TODO: Actualizar la lista de espacios libres con lo que queda de este bloque				
				bytesAEscribir = armarDatosBloque((long) this.cantidadBloques+2, bytes, (short) 0, (short) (tamanioDatosControl + bytes.length));
				try {
					this.archivo.insertar(bytesAEscribir, (long) this.cantidadBloques+1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.cantidadBloques++;
				
			}
			//TODO: Tengo que definir bytes, tengo que cortarlo
			bytesAEscribir = armarDatosBloque(0L, bytes, (short) (tamanioDatosControl + bytes.length), (short) (tamanioDatosControl + bytes.length));
			try {
				this.archivo.insertar(bytesAEscribir, (long) this.cantidadBloques+1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//TODO:Inserto el ultimo elementos
			this.actualizarListaEspacioLibre(-1, cantidadBloques, (short) (Constantes.SIZE_OF_INDEX_BLOCK-bytesAEscribir.length));
		} else {
			//Es un solo registro por lo tanto puedo insertarlo en un bloque con algún espacio libre
			if (!this.espacioLibrePorBloque.isEmpty()) {
				NodoListaEspacioLibre nodo; 
				nodo = this.espacioLibrePorBloque.get(0);
				
				//si entra lo agrego en este punto, sino lo agrego como un bloque nuevo
				if (nodo.getEspacio() > tamanioRegistro) {
					//TODO: Tengo que definir bytes, tengo que cortarlo
					/* agrego el registro en el bloque
					 * 1)Leo el bloque que tiene el espacio libre
					 * 2)Leo los datos de control y me quedo con espacioOcupado
					 * 3)me paro al final ( espacioOcupado ) y escribo en ese lugar
					 * 4)Para escribir me aseguro que entra y deja 20% del bloque libre.
					 * 5)Actualizo el elemento dentro de la lista de espacio libre en los bloques.
					 */
					//this.actualizarListaEspacioLibre();
				} else {
					//TODO: Tengo que definir bytes, tengo que cortarlo
					//TODO: Actualizar la lista de espacios libres con lo que queda de este bloque
					bytesAEscribir = armarDatosBloque((long) 0, bytes, (short) 0, (short) (tamanioDatosControl + bytes.length));
					try {
						this.archivo.insertar(bytesAEscribir, (long) this.cantidadBloques+1);
					} catch (IOException e) {
						e.printStackTrace();
					}
					this.cantidadBloques++;
					//this.actualizarListaEspacioLibre();
				}
			} else {
				//Inserto un registro nuevo
				//TODO: Tengo que definir bytes, tengo que cortarlo
				//TODO: Actualizar la lista de espacios libres con lo que queda de este bloque
				bytesAEscribir = armarDatosBloque((long) 0, bytes, (short) 0, (short) (tamanioDatosControl + bytes.length));
				try {
					this.archivo.insertar(bytesAEscribir, (long) this.cantidadBloques+1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.cantidadBloques++;
				//this.actualizarListaEspacioLibre();
			}
			
			
		}
		
		//Defino el dato de control
		//bytesDeControl = Conversiones.longToArrayByte(0L);
		
		//Datos del Registro
		//file.write(bytesAEscribir, (int)(offset + (totalBloques-1)*Constantes.SIZE_OF_INDEX_BLOCK) , Constantes.SIZE_OF_INDEX_BLOCK);

		/*
		RegistroDiccionario reg = new RegistroDiccionario();
		
		reg.setOffset(offset);
		reg.setDato(palabra);
		try {
			this.archivo.insertar(reg);
			return true;
		} catch (Exception e) {
			return false;
		}	
		*/
		return true;
	}

	/**
	 * Constructor de la clase.
	 *
	 */
	public ListasInvertidas() {
		this.archivo = new ArchivoBloques();
		this.cantidadBloques = 2;
		this.datosLeidosPorBloque = null;
		this.nroBloque = 0;
		this.nroBloqueLista = 0;
		this.offsetLista = 0;
	}
	
	/**
	 * Valida el encabezado del archivo, este esta en el primer bloque.
	 *
	 */
	private boolean validarEncabezado() {
		//TODO: Implementar
		// CantidadBloques|offset Lista de lugares libres
		logger.debug("Intento validar el encabezado.");
		return leerEncabezadoArchivo();
	}
	
	/**
	 * Lee el encabezado del archivo, si no existe 
	 * entonces lo escribe e inicializa el.
	 * archivo de bloques
	 */
	private boolean leerEncabezadoArchivo() {
		byte[] datosControlArchivo = null;
		char control;

		logger.debug("Empiezo a leer el encabezado.");
		try {
			//TODO: Implementar
			//Leo el primer bloque
			datosControlArchivo = this.archivo.leerBloque(0L);
			
			//TODO: Este if debería ser una Exception
			if (datosControlArchivo.length > 0) {
				
				logger.debug("Hay datos en el encabezado.");
				
				ByteArrayInputStream bis 
				  = new ByteArrayInputStream(datosControlArchivo);  
				DataInputStream dis = new DataInputStream(bis);
				
				logger.debug("Leo el char de control.");
				control = dis.readChar();
				if (control == 'C') {
					logger.debug("Es bloque de control.");
					this.setCantidadBloques(dis.readInt());
					this.setNroBloqueLista(dis.readInt());
					logger.debug("Lei cantidad de bloques y bloque en que " +
							"esta la lista de espacios libres.");
					return levantarListaAMemoria();
				} else {
					logger.debug("El encabezado esta corrupto.");
					return escribirEncabezadoArchivo();
				}
					
			} else {
				logger.debug("No hay datos en el encabezado.");
				return escribirEncabezadoArchivo();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Escribe el encabezado del archivo.
	 * @return
	 * 		Devuelve True si escribe correctamente el encabezado, 
	 * 		False si no lo puede escribir.
	 */
	private boolean escribirEncabezadoArchivo() {
		//TODO: Implementar
		logger.debug("Voy a escribir el encabezado del archivo de bloques.");
		
		byte[] encabezadoBytes = new byte[Constantes.SIZE_OF_INDEX_BLOCK];
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		
		try {
			dos.writeChar('C');
			dos.writeInt(this.cantidadBloques);
			dos.writeInt(2);
			logger.debug("Defini los datos del encabezado.");
			System.arraycopy(bos.toByteArray(), 0, encabezadoBytes, 0, bos.toByteArray().length);

			this.archivo.insertar(encabezadoBytes, 0L);
			logger.debug("Escribi los datos del encabezado.");
			return true;
		} catch (IOException e) {
			logger.debug("Error al definir los datos del encabezad o al insertar el encabezado.");
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Levanta la lista de espacios libres a memoria.
	 * @return
	 * 			Devuelve True si puede levantar a memoria la 
	 * 			lista de espacio Libres. False si no lo logra.
	 */
	private boolean levantarListaAMemoria() {
		logger.debug("Voy a levantar la lista de espacios a memoria.");
		return true;
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
