package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.archivos.ArchivoBloques;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;

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
	 * Lista que contiene los bloques que tienen
	 * espacio libre.
	 */
	private ListaEspacioLibre espacioLibre;
	
	/**
	 * Cantidad de bloques dentro del archivo.
	 */
	private int cantidadBloques;
	
	/**
	 * Lista que contiene los movimientos de los registros
	 * de un bloque a otro.
	 */
	private ArrayList<TerminoBloque> registrosMovidos;
	
	/**
	 * Tamaño que tiene el area de control.
	 */
	private Short tamanioControl;
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
		if (!this.archivo.abrir(archivoNombre, modo)) {
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
		
		this.escribirListaAArchivo();
		this.escribirEncabezadoArchivo();
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
			
				logger.debug("en leer lista se accede al bloque: "+siguiente);
				/* Obtengo el bloque según el numero de bloque*/
				datosLeidosPorBloque = this.archivo.leerBloque((int) siguiente);
					
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
							idTerminoExt, primerRegistro, 
							(short) (espacioOcupado - tamanioControl))) {
						/* Si no lo encuentro en el bloque esta mal el 
						 * bloque por lo tanto no busco en bloques 
						 * siguientes */
						return null;
					}
					
					/* Armo la lista para el termino especifico */
					reg.setBytes(this.datosLeidosPorBloque, 
									(long) this.offsetLista);
					cantBloquesLeidos++;
				} else {
					reg.setMoreBytes(datosLeidosPorBloque, 
							Constantes.SIZE_OF_LONG	
							+ Constantes.SIZE_OF_SHORT * 2);
				}
			}
		} catch (IOException e) {
			logger.debug("Error: " + e.getMessage());
			return null;
		}
		
		/* Devuelvo el registro leido */
		return reg;
	}
	
	/**
	 * Arma los datos del bloque con los parametros que recibe.
	 * @param nroSiguienteBloque Numero del siguiente bloque
	 * @param datos Datos perteneciente al bloque
	 * @param primerRegistro Es el offset a partir de los datos de 
	 * 			control donde empieza el primer registro
	 * @param espacioOcupado Es el espacio ocupado del bloque 
	 * @param offsetInicial	La posicion donde se empieza la copia
	 * 			de los bytes en datos.
	 * @param offsetFinal	La posicion donde se termina la copia
	 * 			de los bytes en datos. 
	 * @return
	 * 		Devuelve los datos en un arreglo de Bytes.
	 */
	private byte[] armarDatosBloque(final Long nroSiguienteBloque, 
			final byte[] datos,	final short primerRegistro, 
			final short espacioOcupado, final int offsetInicial, 
			final int offsetFinal) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
				
		try {
			dos.writeLong(nroSiguienteBloque);
			dos.writeShort(primerRegistro);
			dos.writeShort(espacioOcupado);
			dos.write(datos , offsetInicial, offsetFinal - offsetInicial);

		} catch (Exception e) {
			logger.debug("Error: " + e.getMessage());
		}		
		return bos.toByteArray();
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
	public final boolean agregar(final Long idTerminoExt, 
			final Collection<ParFrecuenciaDocumento> listaExt) {
		
		/* Variable para saber si tengo uno o mas bloque por escribir */
		boolean masBloques = false;
		
		/* Registro que contiene las frecuencias y los documentos */
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		
		reg.setIdTermino(idTerminoExt);
		reg.setDatosDocumentos(listaExt);
		
		byte[] bytes = reg.getBytes();
		int tamanioRegistro = bytes.length;
		
		//Tamaño disponible en el bloque
		int bytesDisponibles;
		bytesDisponibles = Constantes.SIZE_OF_LIST_BLOCK - this.tamanioControl;
		
		/* Valido que la cantidad de información a insertar entre 
		 * en un bloque */
		masBloques = (tamanioRegistro > bytesDisponibles);

		byte[] bytesAEscribir;
		
		
		int offsetEscritura = 0;
		
		
		/* El control esta dado por el numero de termino 
		 * y la cantidad de documentos */
		int tamanioControlRegistro = reg.getTamanioControl();
		
		int tamaniodisponible = bytesDisponibles - tamanioControlRegistro;
		int nodosPorBloques = (tamaniodisponible) / reg.getTamanioNodo();
		int espacioOcupadoNodos = (tamanioControlRegistro + (nodosPorBloques * reg.getTamanioNodo()));
		int bloqueAInsertar = -1;
		this.nroBloque = this.cantidadBloques;
		while (tamanioRegistro > offsetEscritura) {
			if (masBloques) {
				//Tengo mas de un bloque para insertar
				if ((tamanioRegistro - offsetEscritura) <= bytesDisponibles) {
					/*
					 * Como entra todo lo que queda del registro en el bloque 
					 * lo escribo
					 */
					bytesAEscribir = armarDatosBloque(0L, bytes, 
									(short) (bytes.length - offsetEscritura),
									(short) (tamanioControl + bytes.length - offsetEscritura),
									offsetEscritura,bytes.length);
					try {
						this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//Indico que es un nuevo elemento en la lista de espacios libres
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, 
							(short) (bytesAEscribir.length));				
					offsetEscritura += 	bytes.length - offsetEscritura;
					this.cantidadBloques++;
					
					} else {
						/*
						 * Si no entra el bloque entero entonces escribo una
						 * parte en el bloque a escribir y la otra en el 
						 * datosAnteriores. Para ello busco el tamaño que me 
						 * falta para completar el bloque y la cantidad max 
						 * de elementos de las listas para insertar. 
						 */
						
						bytesAEscribir = armarDatosBloque(
								(long) (this.cantidadBloques + 1), 
								bytes, (short) 0, 
								(short) (tamanioControl + espacioOcupadoNodos),
								offsetEscritura,
								espacioOcupadoNodos + offsetEscritura);
						try {
							this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
						} catch (IOException e) {
							e.printStackTrace();
						}
						offsetEscritura += 	espacioOcupadoNodos;
						this.cantidadBloques++;
						
					}
				nodosPorBloques = (bytesDisponibles) / reg.getTamanioNodo();
				espacioOcupadoNodos = (nodosPorBloques * reg.getTamanioNodo());
				} else {
					
					/*
					 * Si es un solo bloque y no es la ultima parte que 
					 * continua a X bloques entonces busco un lugar en algun
					 * bloque para ponerlo.
					 */
					bloqueAInsertar = this.espacioLibre.buscarEspacio((short) tamanioRegistro);
					
					logger.debug("tengo una lista que voy a insertar en el bloque: "+bloqueAInsertar);
					
					if (bloqueAInsertar != -1) {
						this.nroBloque = bloqueAInsertar;
						//Hay lugar en un bloque por lo tanto lo inserto
						byte[] bytesTemporales;
						byte[] datos;
						try {
							// Tengo el bloque al que voy a modificar.
							bytesTemporales = this.archivo.leerBloque(bloqueAInsertar);
							
							ByteArrayInputStream bis 
							  = new ByteArrayInputStream(bytesTemporales);  
							DataInputStream dis = new DataInputStream(bis);
							
							Long siguiente = dis.readLong();
							short primerRegistro = dis.readShort();
							short espacioOcupado = dis.readShort();
							
							ByteArrayOutputStream bos 
							  = new ByteArrayOutputStream();  
							DataOutputStream dos = new DataOutputStream(bos);

							dos.write(bytesTemporales, tamanioControl, espacioOcupado - tamanioControl);
							//System.arraycopy(bytesTemporales, tamanioControl,	datos, 0, espacioOcupado - tamanioControl);
							
							dos.write(bytes, 0, bytes.length);
//							System.arraycopy(bytes, 0, datos,espacioOcupado-tamanioControl, bytes.length);
							datos = bos.toByteArray();
							bytesTemporales = this.armarDatosBloque(siguiente,
									datos, primerRegistro, 
									(short) (tamanioControl + datos.length),
									0, datos.length);
							
							this.archivo.escribirBloque(bytesTemporales, bloqueAInsertar);
							
							this.espacioLibre.actualizarEspacio((short) (tamanioControl+datos.length));
							
							offsetEscritura += 	bytes.length;
							
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					} else {
					
					/*
					 * Como el registro no entra en ningun bloque creo un nuevo 
					 * bloque para insertarlo
					 */
					this.nroBloque = this.cantidadBloques;
					bytesAEscribir = armarDatosBloque(0L, bytes, (short) 0, 
							(short) (tamanioControl + bytes.length - offsetEscritura),
							offsetEscritura, bytes.length);
					try {
						this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//indico que es un nuevo nodo en la lista de espacios 
					//libres-
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, 
							(short) (bytesAEscribir.length));				
					offsetEscritura += 	bytes.length - offsetEscritura;
					this.cantidadBloques++;
					}
				}
		}
		return true;
	}

	/**
	 * Constructor de la clase.
	 *
	 */
	public ListasInvertidas() {
		this.archivo = new ArchivoBloques(Constantes.SIZE_OF_LIST_BLOCK);
		this.cantidadBloques = 2;
		this.datosLeidosPorBloque = null;
		this.nroBloque = 0;
		this.nroBloqueLista = 1;
		this.offsetLista = 0;
		this.espacioLibre = new ListaEspacioLibre(Constantes.SIZE_OF_LIST_BLOCK);
		this.tamanioControl = Constantes.SIZE_OF_SHORT * 2 
					+ Constantes.SIZE_OF_LONG;
		this.registrosMovidos = new ArrayList<TerminoBloque>();
	}
	
	/**
	 * Valida el encabezado del archivo, este esta en el primer bloque.
	 * @return True si el encabezado es correcto. False sino lo és.
	 */
	private boolean validarEncabezado() {
		logger.debug("Intento validar el encabezado.");
		return leerEncabezadoArchivo();
	}
	
	/**
	 * Lee el encabezado del archivo, si no existe 
	 * entonces lo escribe e inicializa el.
	 * archivo de bloques
	 * @return True si el encabezado se pudo leer con informacion 
	 * 			coherente, False en caso contrario.
	 */
	private boolean leerEncabezadoArchivo() {
		byte[] datosControlArchivo = null;
		char control;

		logger.debug("Empiezo a leer el encabezado.");
		try {
			//Leo el primer bloque
			datosControlArchivo = this.archivo.leerBloque(0);

			if (datosControlArchivo != null) {
				
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
					logger.debug("Cantidad de bloques: " + this.cantidadBloques);
					return levantarListaAMemoria();
				} else {

					logger.debug("El encabezado esta corrupto.");
					return (escribirEncabezadoArchivo() 
						&& inicializarListaEspaciosLibre(this.nroBloqueLista));
				}
					
			} else {
				logger.debug("No hay datos en el encabezado.");
				return (escribirEncabezadoArchivo() 
						&& inicializarListaEspaciosLibre(this.nroBloqueLista));
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
		logger.debug("Voy a escribir el encabezado del archivo de bloques.");
		
		byte[] encabezadoBytes = new byte[Constantes.SIZE_OF_LIST_BLOCK];
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		
		try {
			dos.writeChar('C');
			dos.writeInt(this.cantidadBloques);

			dos.writeInt(1);
			logger.debug("Defini los datos del encabezado.");

			System.arraycopy(bos.toByteArray(), 0, encabezadoBytes, 
							0, bos.toByteArray().length);

			this.archivo.escribirBloque(encabezadoBytes, 0);
			logger.debug("Escribi los datos del encabezado.");
			
			return true;
		} catch (IOException e) {
			logger.debug("Error al definir los datos del encabezado " 
					+ "al insertar el encabezado.");
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Escribe la lista de espacios libres al archivo de bloques.
	 * @return
	 * 		True si la logra escribir y False si no puede escribirla.
	 */
	private boolean escribirListaAArchivo() {

		byte[] ListaEspaciosLibresBytes = new byte[Constantes.SIZE_OF_LIST_BLOCK];

		int tamanioDatosControl = Constantes.SIZE_OF_INT * 2 
						+ Constantes.SIZE_OF_CHAR;
		
		int tamanioTotalDisponibleXBloque = Constantes.SIZE_OF_LIST_BLOCK 
						- (tamanioDatosControl);
		
		//Division Entera
		int maxCantidadNodosPorBloque = tamanioTotalDisponibleXBloque / tamanioDatosControl;
		
		int cantidadElementosFaltantes = this.espacioLibre.getSize(); 
		
		int bloqueActual = this.nroBloqueLista;
		int bloqueSiguiente = this.nroBloqueLista;

		NodoListaEspacioLibre nodoLibre;
		Iterator<NodoListaEspacioLibre> it;
		it = this.espacioLibre.obtenerIterador();
		
		//Obtengo el Siguiente del bloque
		try {
		while (it.hasNext() && cantidadElementosFaltantes > 0) {
			
			//Leo el bloque
		    byte[] bloqueLista = this.archivo.leerBloque(bloqueActual);
			ByteArrayInputStream bis = new ByteArrayInputStream(bloqueLista);  
			DataInputStream dis = new DataInputStream(bis);
			
			//Es un bloque de control?
			if (dis.readChar() != 'C') {
				return false;
			}

			bloqueSiguiente = dis.readInt();
			bis.close();
			
			nodoLibre = it.next();
			
			//Empiezo a preparar el nuevo bloque
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			
			//Escribo datos de control
			dos.writeChar('C');
			
			
			if (cantidadElementosFaltantes <= maxCantidadNodosPorBloque) {
				//Escribo parte de un bloque
				dos.writeInt(0);
				dos.writeInt(cantidadElementosFaltantes);
				for (int i = 0; i < cantidadElementosFaltantes && it.hasNext(); i++) {
					dos.writeShort(nodoLibre.getEspacio());
					dos.writeInt(nodoLibre.getNroBloque());
					nodoLibre = it.next();
				}
				dos.writeShort(nodoLibre.getEspacio());
				dos.writeInt(nodoLibre.getNroBloque());
				cantidadElementosFaltantes -= cantidadElementosFaltantes;
				bos.flush();
				System.arraycopy(bos.toByteArray(), 0, ListaEspaciosLibresBytes,
							0, bos.toByteArray().length);
				this.archivo.escribirBloque(ListaEspaciosLibresBytes, bloqueActual);
			} else {
				//Escribo un bloque completo
				/* 
				 * Si es cero entonces pongo el nuevo bloque que voy a crear. 
				 * Como sé que voy a crear un nuevo bloque aumento al 
				 * cantidad de bloques en el archivo
				 */

				if (bloqueSiguiente == 0) {
					bloqueSiguiente = this.cantidadBloques;
					this.cantidadBloques++;
					//Inicializo el bloque como bloque de control.
					inicializarListaEspaciosLibre(bloqueSiguiente);
				}
				
				dos.writeInt(bloqueSiguiente);
				dos.writeInt(maxCantidadNodosPorBloque);
				
				for (int i = 0; i < maxCantidadNodosPorBloque && it.hasNext(); i++) {
					dos.writeShort(nodoLibre.getEspacio());
					dos.writeInt(nodoLibre.getNroBloque());
					nodoLibre = it.next();
				}
				cantidadElementosFaltantes -= maxCantidadNodosPorBloque;
				bos.flush();
				System.arraycopy(bos.toByteArray(), 0, ListaEspaciosLibresBytes,
							0, bos.toByteArray().length);
				this.archivo.escribirBloque(ListaEspaciosLibresBytes, bloqueActual);
				bloqueActual = bloqueSiguiente;
			}
			//Insertar el registro
			bos.reset();
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.espacioLibre.borrarLista();
		return true;
	}
	
	/**
	 * Levanta la lista de espacios libres a memoria.
	 * @return
	 * 			Devuelve True si puede levantar a memoria la 
	 * 			lista de espacio Libres. False si no lo logra.
	 */
	private boolean levantarListaAMemoria() {
		logger.debug("Voy a levantar la lista de espacios a memoria.");
		
		char tipo;
		int siguienteBloque;
		int	cantidadElementos;
		NodoListaEspacioLibre nodo;
		try {
			this.datosLeidosPorBloque = this.archivo
					.leerBloque(this.nroBloqueLista);
			ByteArrayInputStream bis = new ByteArrayInputStream(
					this.datosLeidosPorBloque);
			DataInputStream dis = new DataInputStream(bis);

			tipo = dis.readChar();
			if (tipo != 'C') { 
				return false;
			}
			
			siguienteBloque = dis.readInt();
			cantidadElementos = dis.readInt();

			while (siguienteBloque != 0 && tipo == 'C') {
				for (int i = 0; i < cantidadElementos; i++) {
					nodo = new NodoListaEspacioLibre();
					nodo.setEspacio(dis.readShort());
					nodo.setNroBloque(dis.readInt());
					this.espacioLibre.agregarNodo(nodo);					
				}
				this.datosLeidosPorBloque = this.archivo.
								leerBloque(siguienteBloque);
				
				bis = new ByteArrayInputStream(
								this.datosLeidosPorBloque);
				dis = new DataInputStream(bis);
				tipo = dis.readChar();
				siguienteBloque = dis.readInt();
				cantidadElementos = dis.readInt();
			}
			
			for (int i = 0; i < cantidadElementos; i++) {
				nodo = new NodoListaEspacioLibre();
				nodo.setEspacio(dis.readShort());
				nodo.setNroBloque(dis.readInt());
				this.espacioLibre.agregarNodo(nodo);					
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

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
		int offsetAConsultar = this.tamanioControl + offsetPrimerLista;
		
		//Empiezo a leer desde la primer lista del bloque
		ByteArrayInputStream bis; 
		DataInputStream dis;
		try {
			//Mientras este dentro de la lista y sea menos al espacio ocupado
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
				//FIXME: Es valida la comparacion esta?
				if (idTerminoBuscado == idTermino) {
					/* Son iguales por lo tanto el offset de la lista es 
					 * igual a la primera lista */
					this.offsetLista = offsetAConsultar;
					return true;
				}

				/* No es el mismo */
				offsetAConsultar += Constantes.SIZE_OF_LONG;
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
	 * Inicializa un bloque de control dentro del archivo de bloques,
	 * ya que si no es de control no se puede usuar para la lista de 
	 * espacios libres.
	 * @param bloqueLista
	 * 			El bloque que voy a inicializar para la lista.
	 * @return
	 * 			True si logre inicializar el bloque.
	 */
	private boolean inicializarListaEspaciosLibre(final int bloqueLista) {
		byte[] datos = new byte[Constantes.SIZE_OF_LIST_BLOCK];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeChar('C');
			dos.writeInt(0);
			dos.writeInt(0);
			System.arraycopy(bos.toByteArray(), 0, datos, 
								0, bos.toByteArray().length);
			this.archivo.escribirBloque(datos, bloqueLista);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
	}

	/**
	 * Permite obtener el número de bloque que aca de insertarse.
	 * @return El número de bloque.
	 */
	public final long getBloqueInsertado() {
		return this.nroBloque;
	}
	
	/**
	 * Modifica la lista correspondiente al idTermino que se 
	 * encuentra en el bloque indicado. 
	 * @param nroBloqueExt	Bloque donde se encuentra la lista.
	 * @param idTerminoExt	Id termino asociado a la lista.
	 * @param listaExt 	Nueva lista de documentos.
	 * @return
	 * 		True si se modifico la lista. False si no se pudo 
	 * modificar la lista.
	 */
	public final boolean modificarLista(final int nroBloqueExt, 
										final Long idTerminoExt, 
						final Collection<ParFrecuenciaDocumento> listaExt) {
		
		try {
			this.datosLeidosPorBloque = this.archivo.leerBloque(nroBloqueExt);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(this.datosLeidosPorBloque);  
		DataInputStream dis = new DataInputStream(bis);
		
		RegistroTerminoDocumentos regInt;
		RegistroTerminoDocumentos regActualizado;
		try {
			Long bloqueSiguiente = dis.readLong();
			Short primerRegistro = dis.readShort(); 
			Short espacioOcupado = dis.readShort();
			long offsetListaSiguiente = 0;
			
			//espacioOcupado -= (short) tamanioControl;
			
			// Al leer la lista le agrego los nuevos nodos a la lista 
			//y la vuelvo a grabar
			regInt = this.leerLista(idTerminoExt, nroBloqueExt);
			//Si es null es que no lleyo ninguna lista.
			if (regInt == null) {
				return false;
			}
			this.logger.debug("Registro leido: "+regInt.getIdTermino()+" NroBloque: "+nroBloqueExt);
			//Calculo el nuevo offset lista
			offsetListaSiguiente = (this.offsetLista + regInt.getTamanio() - this.tamanioControl);
			
			regActualizado = new RegistroTerminoDocumentos();
			regActualizado.setIdTermino(idTerminoExt);
			regActualizado.setDatosDocumentos(listaExt);
			/* Se encarga de correr todas las listas actualizando la actual */
			this.administrarBloquesAModificar(bloqueSiguiente, espacioOcupado,
					primerRegistro, regActualizado, (short) offsetListaSiguiente,
					nroBloqueExt);
		
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		
		return true;
	}
	
	/** 
	 * Administra toda la logica para modificar una lsta invertida,
	 * eso incluye el desplazamiento de listas y manejo del desborde
	 * del bloque.
	 * 
	 * @param offsetListaSiguiente Lo que ocupa desde el principio del 
	 * bloque hasta terminar la lista a modificar. Tamanño viejo.
	 * @param siguienteExt el siguiente del bloque donde esta la lista.
	 * @paaram espacioOcupadoExt el espacio ocupado dentro del bloque.
	 * @param primerRegistroExt donde empieza el primer registro dentro del 
	 * bloque nroBloqueExt
	 * @param regActualizado la nueva lista que se va a escribir en lugar 
	 * de la otra.
	 * @param nroBloqueExt el numero de bloque donde esta la lista 
	 * a modificar. 
	 * 
	 */
	private Long administrarBloquesAModificar(Long siguienteExt, final Short espacioOcupadoExt, 
			final Short primerRegistroExt, final RegistroTerminoDocumentos regActualizado,
			Short offsetListaSiguiente, final int nroBloqueExt) {
		//TODO: Ver el tema de los final
		boolean continua = false; 
		boolean registroNuevo = false;
		int overflowRegistro = 0;
		int primerRegistroOculto = 0;
		// datos de la lista a grabar
		byte[] datosListaActualizada = regActualizado.getBytes();
		
		// tamaño de la lista a grabar
		int tamanioListaActualizada = datosListaActualizada.length;
		
		//Espacio disponible por bloque
		int espacioDispBloque = Constantes.SIZE_OF_LIST_BLOCK - tamanioControl;
		
		//Cantidad de bloques que ocupaba por completo el registro anterior 
		int bloquesRegAnterior = (offsetListaSiguiente/(espacioDispBloque));
		
		//bloque en el que voy a insertar los datos
		int bloqueAInsertar = (new Integer(nroBloqueExt)).intValue();
		
		//byte[] para no pisar this.datosLeidosProBloque.
		byte[] datosLeidos;
		
		//FIXME: Borrarme
		this.logger.debug("Cantidad de Bloques totales: "+Integer.toString(this.cantidadBloques));
		
		/*
		 * Me armo los datos anteriores al registro para escribirlos tal cual
		 * mas adelante se usara para lo sobrante de cada bloque que se
		 * tiene que guardar en el siguiente.
		 */
		byte[] datosAnteriores = new byte[this.offsetLista - tamanioControl];
		
		//Tamaño de los datos anteriores
		int tamanioDatosAnteriores = datosAnteriores.length;
		
		//Copia los datos del registro al "datosAnteriores".
		System.arraycopy(this.datosLeidosPorBloque, tamanioControl, datosAnteriores, 0, tamanioDatosAnteriores);
		
		//Donde voy a ir escribiendo los datos
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			
			//escribo los datos anteriores al registro a modificar			
			dos.write(datosAnteriores, 0, datosAnteriores.length);
			//escribo los datos nuevos del registro.
			dos.write(datosListaActualizada, 0, tamanioListaActualizada);
			
		} catch (IOException e2) {
			// Error en la escritura de datos
			return 1L;
		}
		
		datosAnteriores = bos.toByteArray();
		
		//se empieza a copiar desde ...
		int offsetInicial = 0;
		
		//se termina de copiar en ...
		int offsetFinal = 0;
		
		//Donde voy a ir leyendo los datos
		ByteArrayInputStream bis;
		DataInputStream dis;
		
		// datos que tendra el nuevo bloque
		byte[] datosBloque;
		
		Short primerRegistro = primerRegistroExt;
		Short espacioOcupado = espacioOcupadoExt;
		
		//es la cantidad de nodos que puedo insertar en el registro de bloques.
		int cantidadNodos = 0;
		int tamanioNodos = regActualizado.getTamanioNodo(); 
		
		//Trato de actualizar los registros que estaban utilizados por completo
		if (bloquesRegAnterior > 0) {
			
			continua = true;
			
			//Cuantos nodos puedo insertar en el bloque
			cantidadNodos = (espacioDispBloque-tamanioDatosAnteriores);
			cantidadNodos -= regActualizado.getTamanioControl();
			cantidadNodos /= tamanioNodos;
			
			// escribo "bloquesRegAnterior" con los datos nuevos.
			
			/*
			 * termino de copiar en el ultimo nodo que me entra completo
			 * en el bloque 
			 */
			offsetFinal += tamanioDatosAnteriores + regActualizado.getTamanioControl() 
						+ (cantidadNodos * tamanioNodos);
			
			for (int i = 0; i < bloquesRegAnterior; i++) { 

				
				offsetListaSiguiente = (short) (offsetListaSiguiente - offsetFinal);
				offsetListaSiguiente = (short) (offsetListaSiguiente - (short) offsetInicial);
				
				//armo el bloque a insertar
				datosBloque = this.armarDatosBloque(siguienteExt, datosAnteriores, primerRegistro, espacioOcupado, offsetInicial, offsetFinal);
				
				//inserto el bloque
				try {
					this.archivo.escribirBloque(datosBloque, bloqueAInsertar);
				} catch (IOException e) {
					// Error en la escritura del bloque
					return 3L;
				}
				
				//aumento el numero de bloque
				bloqueAInsertar = siguienteExt.intValue();
				
				//leo el siguiente bloque.
				try {
					datosLeidos = this.archivo.leerBloque(siguienteExt.intValue());
				} catch (IOException e) {
					// Error en la lectura del bloque
					return 4L;
				}
				
				//Leo los datos de control del bloque
				bis = new ByteArrayInputStream(datosLeidos);
				dis = new DataInputStream(bis);
				
				try {
					siguienteExt = dis.readLong();
					primerRegistro = dis.readShort();
					espacioOcupado = dis.readShort();
				} catch (IOException e) {
					// Error al leer datos del bloque
					return 5L;
				}
				
				
				//Armo los nuevos offset a escribir.
				//Comienzo donde deje
				offsetInicial = offsetFinal;
				
				/*
				 * en el nuevo bloque no hay datos anteriores porque
				 * ya pertenecia al registro que estoy actualizando
				 */
				tamanioDatosAnteriores = 0;
				
				cantidadNodos = (espacioDispBloque-tamanioDatosAnteriores);
				cantidadNodos /= tamanioNodos;
				
				/*
				 * termino de copiar en el ultimo nodo que me entra completo
				 * en el bloque 
				 */
				offsetFinal += tamanioDatosAnteriores + (cantidadNodos * tamanioNodos);
			}
		}
		
		int cantidadEscritaEnBuffer = 0;
		ArrayList<RegistroTerminoDocumentos> listaListas;
		Iterator<RegistroTerminoDocumentos> it;
		RegistroTerminoDocumentos regLista;
		byte[] datosNuevo;
		byte[] datos;
			
		ByteArrayOutputStream bosaux = new ByteArrayOutputStream();
		DataOutputStream dosaux = new DataOutputStream(bosaux);
			
		/*
		 * Mientras no sea el último bloque. Leo las listas que comparten 
		 * el bloque y las corro segun la sobra del bloque anterior.
		 * 				
		 * Estoy en un bloque que contiene varias listas!!!
		 * 1) Leer Listas del bloque, a lo sumo me trae un pedazo del 
		 * 		otro bloque.
		 * 2) Contacteno los datos que me sobraron del ultimo bloque
		 * 		con los datos de la listas leidas.
		 * 3) inserto validando la cantidad de elementos que pongo.
		 * 4) valido si una lista se paso de bloque y lo agrego al
		 * 		map para que se actulice luego el numero de bloque.
		 */			
		while (siguienteExt != 0) {
			/*
			 * cuando voy restandole a offsetListaSiguiente supuestamente 
			 * en este bloque me tiene que quedar donde comienza el 
			 * primerRegistro, si es mayor pongo a primerRegistro, sino a
			 * offsetListaSiguiente.
			 */
			this.logger.debug("OffsetlistSiguiente "+offsetListaSiguiente+" espacioOcupado: "+espacioOcupado+" primerRegistro: "+primerRegistro);
			if (offsetListaSiguiente > primerRegistro && continua) {
				listaListas = this.leerListasPorBloque(primerRegistro,espacioOcupado,bloqueAInsertar);
			} else {
				listaListas = this.leerListasPorBloque(offsetListaSiguiente,espacioOcupado,bloqueAInsertar);
			}
			//La lista puede venir vacia pero si viene null existio error
			if (listaListas == null) {
				//Error al leer las listas en el bloque
				return 2L;
			}
			
			//Hay un bloque que le sigue por lo tanto tengo que actualizarlo
			try {
				bos.reset();
				dos = new DataOutputStream(bos);
				dos.write(datosAnteriores, offsetInicial, datosAnteriores.length - offsetInicial);
				cantidadEscritaEnBuffer += datosAnteriores.length - offsetInicial;
				if (continua) {
					//FIXME: corroborar que sea correcto decir mayor e igual
					if (overflowRegistro >= 2) {
						primerRegistro = (short) primerRegistroOculto;
						overflowRegistro = 0;
					} else {
						if (!registroNuevo) {
							primerRegistro = (short) (datosAnteriores.length - offsetInicial);	
						} else {
							primerRegistro = 0;
						}
					}
				}
				offsetListaSiguiente = (short) (offsetListaSiguiente - datosAnteriores.length);
				offsetListaSiguiente = (short) (offsetListaSiguiente - offsetInicial);
				
			} catch (IOException e1) {
				// Error al escribir datos
				return 1L;
			}
			
			it = listaListas.iterator();
			
			//Para cada insercion tengo que validar que los datos 
			//de control entren
			//Que los nodos de las listas no queden partidos.
			while (it.hasNext()) {
				//Mientras tenga listas en el vector de listas las agrego
				regLista = it.next();
				datosNuevo = regLista.getBytes();
				//Si entra el registro completo lo inserto.
				if (datosNuevo.length <= (espacioDispBloque - cantidadEscritaEnBuffer)) {
					try {
						dos.write(datosNuevo, 0, datosNuevo.length);
					} catch (IOException e) {
						// error al escribir datos 
						return 1L;
					}
					cantidadEscritaEnBuffer += datosNuevo.length;
				} else {
					/*
					 * Si no entra el bloque entero entonces escribo una parte
					 * en el bloque a escribir y la otra en el datosAnteriores.
					 * Para ello busco el tamaño que me falta para completar el
					 * bloque y la cantidad max de elementos de las listas para
					 * insertar. 
					 */
					if (regLista.getTamanioControl() <= (espacioDispBloque - cantidadEscritaEnBuffer)) {
			
						Long cantidadNodosDisponibles = (long) (espacioDispBloque 
								- cantidadEscritaEnBuffer - regLista.getTamanioControl()) / regLista.getTamanioNodo();
							
						Long espacioCantidadNodos = regLista.getTamanioControl() 
								+ (cantidadNodosDisponibles * regLista.getTamanioNodo());
						//Inserto los nodo que puedo por el tamaño
						try {
							//escribo los datos del bloque a escribir
							dos.write(datosNuevo, 0, espacioCantidadNodos.intValue());
							//escribo los datos sobrantes del bloque
							dosaux.write(datosNuevo, espacioCantidadNodos.intValue(), datosNuevo.length - espacioCantidadNodos.intValue());
							continua = true;
							registroNuevo = false;
						} catch (IOException e) {
							//Error al escribir datos
							return 1L;
						}
						cantidadEscritaEnBuffer += (int) (regLista.getTamanioControl() + (cantidadNodosDisponibles * regLista.getTamanioNodo())+1);
						overflowRegistro++;
						primerRegistroOculto = bosaux.toByteArray().length;
					} else {
						//Escribo toda la lista en un auxiliar.
						TerminoBloque tb = new TerminoBloque(regLista.getIdTermino(),siguienteExt);
						this.registrosMovidos.add(tb);
						try {
							dosaux.write(datosNuevo, 0, datosNuevo.length);
							continua = true;
							registroNuevo = true;
							overflowRegistro++;
						} catch (IOException e) {
							// error al escribir datos
							return 1L;
						}
					}
				}
			} //Fin del while (it.hasNect())
				
			//Ya tengo el registro listo para escribir en el archivo
			datos = bos.toByteArray();
			//Ahora armo el bloque y lo inserto
			try {
				this.archivo.escribirBloque(
							this.armarDatosBloque(siguienteExt, datos, primerRegistro, (short) (datos.length + tamanioControl), 0, datos.length),
							(int) bloqueAInsertar);
			} catch (IOException e) {
				// Error al escribir bloque
				return 3L;
			}
			
			//Definir que es datosAnteriores.
			bos.reset();
			dos = new DataOutputStream(bos);
			datosAnteriores = bosaux.toByteArray();
			offsetInicial = 0;
			bloqueAInsertar = siguienteExt.intValue();
			cantidadEscritaEnBuffer = 0;
			try {
				datosLeidos = this.archivo.leerBloque(siguienteExt.intValue());
			} catch (IOException e) {
				// Error al leer el bloque
				return 0L;
			}
			
			bis = new ByteArrayInputStream(datosLeidos);  
			dis = new DataInputStream(bis);
			
			try {
				siguienteExt = dis.readLong();
				primerRegistro = dis.readShort(); 
				espacioOcupado = dis.readShort();
			} catch (IOException e) {
				// Error al leer datos de control
				return 5L;
			}
			
		}
		/*
		 * Si estamos en el último bloque a modificar leo las listas que 
		 * tiene y las corro segun la sobra del bloque anterior.
		 * Si el bloque desborda entonces crea uno nuevo.
		 */
		if (siguienteExt.compareTo(0L) == 0) {
			//obtengo las listas empezando del primerRegistro
			try {
				bos.reset();
				bosaux.reset();
				dos = new DataOutputStream(bos);
				dosaux = new DataOutputStream(bosaux);
				dos.write(datosAnteriores, offsetInicial, datosAnteriores.length - offsetInicial);
				cantidadEscritaEnBuffer += datosAnteriores.length - offsetInicial;
				
			} catch (IOException e1) {
				// Error al escribir datos
				return 1L;
			}
			
			/*
			 * cuando voy restandole a offsetListaSiguiente supuestamente 
			 * en este bloque me tiene que quedar donde comienza el 
			 * primerRegistro, si es mayor pongo a primerRegistro, sino a
			 * offsetListaSiguiente.
			 */
			if ((offsetListaSiguiente > primerRegistro && continua) || offsetListaSiguiente <= 0) {
				listaListas = this.leerListasPorBloque(primerRegistro,espacioOcupado, bloqueAInsertar);
			} else {
				listaListas = this.leerListasPorBloque(offsetListaSiguiente,espacioOcupado, bloqueAInsertar);
			}
			
			if (continua) {
				//FIXME: corroborar que sea correcto decir mayor e igual
				if (overflowRegistro >= 2) {
					primerRegistro = (short) primerRegistroOculto;
					overflowRegistro = 0;
				} else {
					if (!registroNuevo) {
						primerRegistro = (short) (datosAnteriores.length - offsetInicial);	
					} else {
						primerRegistro = 0;
					}
				}
			}
			
			//La lista puede venir vacia pero si viene null existio error
			if (listaListas == null) {
				//Error al leer las listas en el bloque
				return 2L;
			}
				
			//Itero las listas y voy insertando todo
				it = listaListas.iterator();
			
			//Para cada insercion tengo que validar que los datos 
			//de control entren
			//Que los nodos de las listas no queden partidos.
			while (it.hasNext()) {
				//Mientras tenga listas en el vector de listas las agrego
				regLista = it.next();
				datosNuevo = regLista.getBytes();
				//Si entra el registro completo lo inserto.
				if (datosNuevo.length <= (espacioDispBloque - cantidadEscritaEnBuffer)) {
					try {
						dos.write(datosNuevo, 0, datosNuevo.length);
					} catch (IOException e) {
						// error al escribir datos 
						return 1L;
					}
					cantidadEscritaEnBuffer += datosNuevo.length;
				} else {
					/*
					 * Si no entra el bloque entero entonces escribo una parte
					 * en el bloque a escribir y la otra en el datosAnteriores.
					 * Para ello busco el tamaño que me falta para completar el
					 * bloque y la cantidad max de elementos de las listas para
					 * insertar. 
					 */
					if (regLista.getTamanioControl() <= (espacioDispBloque - cantidadEscritaEnBuffer)) {
						Long cantidadNodosDisponibles = (long) (espacioDispBloque 
								- cantidadEscritaEnBuffer - regLista.getTamanioControl()) / regLista.getTamanioNodo();
						
						Long espacioCantidadNodos = regLista.getTamanioControl() 
									+ (cantidadNodosDisponibles * regLista.getTamanioNodo());
						//Inserto los nodo que puedo por el tamaño
						try {
							//escribo los datos del bloque a escribir
							dos.write(datosNuevo, 0, espacioCantidadNodos.intValue());
							//escribo los datos sobrantes del bloque
							dosaux.write(datosNuevo, espacioCantidadNodos.intValue(), datosNuevo.length - espacioCantidadNodos.intValue());
						} catch (IOException e) {
							//Error al escribir datos
							return 1L;
						}
						//primerRegistro = (short) cantidadEscritaEnBuffer;
//						primerRegistro = (short) (datosNuevo.length - espacioCantidadNodos.intValue());
						cantidadEscritaEnBuffer += espacioCantidadNodos.intValue() +1;
						overflowRegistro++;
						primerRegistroOculto = bosaux.toByteArray().length;
					} else {
						//Escribo toda la lista en un auxiliar.
						TerminoBloque tb = new TerminoBloque(regLista.getIdTermino(),siguienteExt);
						this.registrosMovidos.add(tb);
						try {
							dosaux.write(datosNuevo, 0, datosNuevo.length);
							overflowRegistro++;
						} catch (IOException e) {
							// error al escribir datos
							return 1L;
						}
					}
				}
			} //Fin del while (it.hasNect())
			
			datos = bos.toByteArray();
			if (dosaux.size() > 0) {
				//Inserto el bloque
				try {
					//inserto el primer registro con el offset al fondo
					this.archivo.escribirBloque(
							this.armarDatosBloque((long) this.cantidadBloques, datos, primerRegistro, (short) (datos.length + tamanioControl), 0, datos.length)
							, bloqueAInsertar);
					
					//Actualizo la memoria libre
					if (this.espacioLibre.buscarBloque(bloqueAInsertar)) {
						this.espacioLibre.actualizarEspacio((short) (datos.length));	
					} else {
						this.espacioLibre.setIndex(-1);
						this.espacioLibre.actualizarListaEspacioLibre(bloqueAInsertar,(short) (datos.length));
					}
					
					datos = bosaux.toByteArray();
					primerRegistro = (short) datos.length;
 					bloqueAInsertar = this.cantidadBloques;
 					this.cantidadBloques++;
				} catch (IOException e) {
					//	Error al escribir el bloque
					return 2L;
				}	
			}
			
			try {
				//inserto el primer registro con el offset al fondo
				this.archivo.escribirBloque(
						this.armarDatosBloque(0L, datos, primerRegistro, (short) (datos.length + tamanioControl), 0, datos.length)
						, bloqueAInsertar);
				
				//Actualizo la memoria libre
				if (this.espacioLibre.buscarBloque(bloqueAInsertar)) {
					this.espacioLibre.actualizarEspacio((short) (datos.length));	
				} else {
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(bloqueAInsertar,(short) (datos.length));
				}
			} catch (IOException e) {
				//	Error al escribir el bloque
				return 2L;
			}
			
			
		}
		
	return 0L;
}
	
	/**
	 * Lee todas las listas del bloque, previamente tengo que leer el bloque donde comienza
	 * @param comienzo
	 * @return
	 */
	private ArrayList<RegistroTerminoDocumentos> leerListasPorBloque(final short comienzo, final short espacioOcupadoExt, final int bloque) {
		
		RegistroTerminoDocumentos reg;
		long siguiente = bloque;
		long nroBloqueALeer = bloque;
		@SuppressWarnings("unused")
		short primerRegistro;
		short espacioOcupado = (short) Constantes.SIZE_OF_LIST_BLOCK;
		short cantBloquesLeidos = 0;
		ArrayList<RegistroTerminoDocumentos> listas = new ArrayList<RegistroTerminoDocumentos>();
		Short offsetSiguienteLista = new Short(comienzo);

		//Si el registro acaba en el offset devuelvo la lista vacia
		if (offsetSiguienteLista.shortValue() >= (espacioOcupadoExt - tamanioControl)) {
			return listas;
		}
		
		if (offsetSiguienteLista >= Constantes.SIZE_OF_LIST_BLOCK) {
			return listas;
		}
		
		boolean masListas = true;
		while (masListas) {
			reg  = new RegistroTerminoDocumentos();
			while (reg.incompleto() || reg.getCantidadDocumentos() == 0) {  //TODO entra en un loop infinito aparte de querer leer el bloque cero
				byte[] bloqueALeer = new byte[Constantes.SIZE_OF_LIST_BLOCK];
				bloqueALeer = leerBloque((int) nroBloqueALeer);
				
				/* Saco la información de control del bloque */
				ByteArrayInputStream bis 
					= new ByteArrayInputStream(bloqueALeer);  
				DataInputStream dis = new DataInputStream(bis);
				
				try {
					siguiente = dis.readLong();
					primerRegistro = dis.readShort();
					espacioOcupado = dis.readShort();
					
					if (cantBloquesLeidos == 0) {		
						/* Armo la lista para el termino especifico */
						reg.setBytes(bloqueALeer, 
										offsetSiguienteLista.longValue() + tamanioControl);
						if (reg.incompleto()) {
							/* Obtengo el bloque según el numero de bloque*/
							nroBloqueALeer = (int) siguiente;
							cantBloquesLeidos++;
						}
					} else {
						
						reg.setMoreBytes(bloqueALeer, this.tamanioControl);
						/* Obtengo el bloque según el numero de bloque*/
						if (reg.incompleto()) {
							/* Obtengo el bloque según el numero de bloque*/
							nroBloqueALeer = siguiente;
						}
					}
				} catch (IOException e) {
					return null;
				}
				
			} //Me devuelve la lista leida
			listas.add(reg);
			//Con la lista leida veo si hay mas.
			if (cantBloquesLeidos > 0) {
				//No hay mas listas porque cambie de bloque
				masListas = false;
			} else {

				offsetSiguienteLista = (short) (offsetSiguienteLista + (new Integer( (Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_INT 
							+ (Constantes.SIZE_OF_LONG * reg.getCantidadDocumentos() * 2)))).shortValue());
				if (offsetSiguienteLista >= (espacioOcupado - tamanioControl)) {
					masListas = false;
				}
			}
		}
		return listas;
	}

	private final byte[] leerBloque(final int nroBloque){
		try {
			return this.archivo.leerBloque(nroBloque);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @param registroMovidos the registroMovidos to set
	 */
	@SuppressWarnings("unused")
	private void setRegistrosMovidos(ArrayList<TerminoBloque> registroMovidos) {
		this.registrosMovidos = registroMovidos;
	}

	/**
	 * @return the registroMovidos
	 */
	public final ArrayList<TerminoBloque> getRegistrosMovidos() {
		return registrosMovidos;
	}
	
	public final void persistirDatosAdministrativos() {
		this.escribirListaAArchivo();
		this.escribirEncabezadoArchivo();
	}

}

