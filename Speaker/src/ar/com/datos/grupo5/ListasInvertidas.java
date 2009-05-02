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
	
	private ListaEspacioLibre espacioLibre;
	/**
	 * Cantidad de bloques dentro del archivo.
	 */
	private int cantidadBloques;
	
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
		
		short tamanioControl = Constantes.SIZE_OF_LONG + (Constantes.SIZE_OF_SHORT * 2);
		

		try {
				while (reg.incompleto() || reg.getCantidadDocumentos() == 0) {
				
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
								idTerminoExt, primerRegistro, (short) (espacioOcupado - tamanioControl))) {
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
	 * Arma los datos del bloque con los parametros que recibe
	 * @param nroSiguienteBloque Numero del siguiente bloque
	 * @param datos Datos perteneciente al bloque
	 * @param primerRegistro Es el offset a partir de los datos de 
	 * 			control donde empieza el primer registro
	 * @param espacioOcupado Es el espacio ocupado del bloque 
	 * @return
	 * 		Devuelve los datos en un arreglo de Bytes.
	 */
	private byte[] armarDatosBloque(final Long nroSiguienteBloque, final byte[] datos, final short primerRegistro, 
			final short espacioOcupado, final int offsetInicial, 
			final int offsetFinal) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
				
		try {
			dos.writeLong(nroSiguienteBloque);
			dos.writeShort(primerRegistro);
//			dos.writeShort(offsetFinal - offsetInicial + tamanioControl);
			dos.writeShort(offsetFinal - offsetInicial);
			dos.write(datos , offsetInicial, offsetFinal - offsetInicial );
			
			/*
//			dos.write(datos);
			if ((datos.length - offsetInicial) > bytesDisponibles) {
				//Copio desde el offset el tamanio del bloque
				dos.writeShort(Constantes.SIZE_OF_LIST_BLOCK);
				dos.write(datos , offsetInicial, offsetFinal - offsetInicial );
			} else {
				dos.writeShort(datos.length - offsetInicial + Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT * 2);
				dos.write(datos , offsetInicial, datos.length - offsetInicial);
			}
			*/
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
	public final boolean agregar(final Long idTerminoExt, final Collection<ParFrecuenciaDocumento> listaExt) {
		
		/* Variable para saber si tengo uno o mas bloque por escribir */
		boolean masBloques = false;
		
		/* Registro que contiene las frecuencias y los documentos */
		RegistroTerminoDocumentos reg = new RegistroTerminoDocumentos();
		
		reg.setIdTermino(idTerminoExt);
		reg.setDatosDocumentos(listaExt);
		
		byte[] bytes = reg.getBytes();
		int tamanioRegistro = bytes.length;
		
		//Tamaño disponible en el bloque
		int bytesDisponibles = Constantes.SIZE_OF_LIST_BLOCK - this.tamanioControl;
		
		/* Valido que la cantidad de información a insertar entre 
		 * en un bloque */
		masBloques = (tamanioRegistro > bytesDisponibles);

		byte[] bytesAEscribir;
		
		
		int offsetEscritura = 0;
		
		
		/* El control esta dado por el numero de termino y la cantidad de documentos */
		int tamanioControlRegistro = Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_INT;
		
		int nodosPorBloques = (bytesDisponibles)/reg.getTamanioNodo();
		int espacioOcupadoNodos = (tamanioControlRegistro + (nodosPorBloques * reg.getTamanioNodo()));
		int bloqueAInsertar = -1;
		this.nroBloque = this.cantidadBloques;
		while (tamanioRegistro > offsetEscritura){
			
			if (masBloques) {
				
				//Tengo mas de un bloque para insertar
				if ((tamanioRegistro - offsetEscritura) <= (Constantes.SIZE_OF_LIST_BLOCK - tamanioControl)) {
					/*
					 * Como entra todo lo que queda del registro en el bloque lo escribo
					 */
					bytesAEscribir = armarDatosBloque(0L, bytes, (short) (tamanioControl + bytes.length - offsetEscritura), (short) (tamanioControl + bytes.length - offsetEscritura),offsetEscritura,bytes.length);
					try {
						this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//Indico que es un nuevo elemento en la lista de espacios libres
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK-bytesAEscribir.length));				
					offsetEscritura += 	bytes.length - offsetEscritura;
					this.cantidadBloques++;
					
					} else {
						/*
						 * Si no entra el bloque entero entonces escribo una parte en el bloque a escribir
						 * y la otra en el datosAnteriores. Para ello busco el tamaño que me falta para
						 * completar el bloque y la cantidad max de elementos de las listas para insertar. 
						 */
						
						bytesAEscribir = armarDatosBloque((long) (this.cantidadBloques + 1), bytes, (short) 0, (short) (this.tamanioControl + espacioOcupadoNodos),offsetEscritura,espacioOcupadoNodos+offsetEscritura);
						try {
							this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
						} catch (IOException e) {
							e.printStackTrace();
						}
						offsetEscritura += 	espacioOcupadoNodos;
						this.cantidadBloques++;
						
					}
					
				} else {
					
					/*
					 * Si es un solo bloque y no es la ultima parte que continua a X bloques entonces
					 * busco un lugar en algun bloque para ponerlo.
					 */
					bloqueAInsertar = this.espacioLibre.buscarEspacio((short) tamanioRegistro);
					
					if (bloqueAInsertar != -1) {
						this.nroBloque = bloqueAInsertar;
						//Hay lugar en un bloque por lo tanto lo inserto
						byte[] bytesTemporales;
						try {
							// Tengo el bloque al que voy a modificar.
							bytesTemporales = this.archivo.leerBloque(bloqueAInsertar);
							
							ByteArrayInputStream bis 
							  = new ByteArrayInputStream(bytesTemporales);  
							DataInputStream dis = new DataInputStream(bis);
							
							Long siguiente = dis.readLong();
							short primerRegistro = dis.readShort();
							short espacioOcupado = dis.readShort();
							
							byte[] datos = new byte[espacioOcupado - tamanioControl + bytes.length];
							
							System.arraycopy(bytesTemporales, tamanioControl, datos, 
									0, espacioOcupado - tamanioControl);
							
							System.arraycopy(bytes, 0, datos, 
									espacioOcupado-tamanioControl, bytes.length);
							
							bytesTemporales = this.armarDatosBloque(siguiente, datos, primerRegistro, (short) (tamanioControl + datos.length),0,datos.length);
							
							this.archivo.escribirBloque(bytesTemporales, bloqueAInsertar);
							
							this.espacioLibre.actualizarEspacio((short) (tamanioControl+datos.length));
							
							offsetEscritura += 	bytes.length;
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					} else {
					
					/*
					 * Como el registro no entra en ningun bloque creo un nuevo 
					 * bloque para insertarlo
					 */
					this.nroBloque = this.cantidadBloques;
					bytesAEscribir = armarDatosBloque(0L, bytes, (short) 0, (short) (tamanioControl + bytes.length - offsetEscritura),offsetEscritura,bytes.length);
					try {
						this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//indico que es un nuevo nodo en la lista de espacios libres-
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK-bytesAEscribir.length));				
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
		this.espacioLibre = new ListaEspacioLibre();
		this.tamanioControl = Constantes.SIZE_OF_SHORT * 2 + Constantes.SIZE_OF_LONG;
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
			
			//TODO: Este if debería ser una Exception
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
					logger.debug("Lei cantidad de bloques y bloque en que " +
							"esta la lista de espacios libres.");
					return levantarListaAMemoria();
				} else {

					logger.debug("El encabezado esta corrupto.");
					return (escribirEncabezadoArchivo() && inicializarListaEspaciosLibre(this.nroBloqueLista));
				}
					
			} else {
				logger.debug("No hay datos en el encabezado.");
				return (escribirEncabezadoArchivo() && inicializarListaEspaciosLibre(this.nroBloqueLista));
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

		int tamanioDatosControl = Constantes.SIZE_OF_INT * 2 + Constantes.SIZE_OF_CHAR;
		int tamanioTotalDisponibleXBloque = Constantes.SIZE_OF_LIST_BLOCK - (tamanioDatosControl);
		//Division Entera
		int maxCantidadNodosPorBloque = tamanioTotalDisponibleXBloque/tamanioDatosControl;
		
		int cantidadElementosFaltantes = this.espacioLibre.getSize(); 
		
		int bloqueActual = this.nroBloqueLista;
		int bloqueSiguiente = this.nroBloqueLista;

		NodoListaEspacioLibre nodoLibre;
		
		Iterator<NodoListaEspacioLibre> it = this.espacioLibre.obtenerIterador();
		//Obtengo el Siguiente del bloque
		try {
		while ( it.hasNext() && cantidadElementosFaltantes > 0) {
			
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
				System.arraycopy(bos.toByteArray(), 0, ListaEspaciosLibresBytes, 0, bos.toByteArray().length);
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
				System.arraycopy(bos.toByteArray(), 0, ListaEspaciosLibresBytes, 0, bos.toByteArray().length);
				this.archivo.escribirBloque(ListaEspaciosLibresBytes, bloqueActual);
				bloqueActual = bloqueSiguiente;
			}
			//Insertar el registro
			bos.reset();
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		int offsetAConsultar = Constantes.SIZE_OF_LONG 
			+ Constantes.SIZE_OF_SHORT * 2 + offsetPrimerLista;
		
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
	 * Modifica la lista correspondiente al idTermino que se encuentra en el bloque indicado. 
	 * @param nroBloqueExt	Bloque donde se encuentra la lista.
	 * @param idTerminoExt	Id termino asociado a la lista.
	 * @param listaExt 	Nueva lista de documentos.
	 * @return
	 */
	public final boolean modificarLista(final int nroBloqueExt, final Long idTerminoExt, final Collection<ParFrecuenciaDocumento> listaExt) {
		
		try {
			this.datosLeidosPorBloque = this.archivo.leerBloque(nroBloqueExt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(this.datosLeidosPorBloque);  
		DataInputStream dis = new DataInputStream(bis);
	
		//TODO: int tamanioControl = Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT * 2;
		
		RegistroTerminoDocumentos regInt;
		RegistroTerminoDocumentos regActualizado;
		try {
			Long bloqueSiguiente = dis.readLong();
			Short primerRegistro = dis.readShort(); 
			Short espacioOcupado = dis.readShort();
			Short offsetListaSiguiente = 0;
			
			//espacioOcupado -= (short) tamanioControl;
			
			// Al leer la lista le agrego los nuevos nodos a la lista y la vuelvo a grabar
			regInt = this.leerLista(idTerminoExt, nroBloqueExt);
			//Si es null es que no lleyo ninguna lista.
			if (regInt == null) {
				return false;
			}
			
			//Calculo el nuevo offset lista
			offsetListaSiguiente = (short) (this.offsetLista + regInt.getTamanio());
			
			regActualizado = new RegistroTerminoDocumentos();
			regActualizado.setIdTermino(idTerminoExt);
			regActualizado.setDatosDocumentos(listaExt);
			
			if (offsetListaSiguiente >= espacioOcupado && bloqueSiguiente == 0){
				byte[] bytesAEscribir;
				byte[] datos = regActualizado.getBytes();
				//Armar datos
				bytesAEscribir = this.armarDatosBloque(bloqueSiguiente, datos, primerRegistro, (short) (this.tamanioControl + datos.length), 0, datos.length);
				
				try {
					this.archivo.escribirBloque(bytesAEscribir, nroBloqueExt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (this.espacioLibre.buscarBloque(nroBloqueExt)) {
						this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK-bytesAEscribir.length));	
				} else {
					this.espacioLibre.setIndex(-1);
					this.espacioLibre.actualizarListaEspacioLibre(cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK-bytesAEscribir.length));
				}
				return true;
			}
			
			/* Se encarga de correr todas las listas actualizando la actual */
			this.administrarBloquesAModificar(bloqueSiguiente, espacioOcupado, primerRegistro, regActualizado, offsetListaSiguiente,nroBloqueExt);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		
		return true;
	}
	
	/**
	 * Caso 1:
	 *      ||datosControl|L1                |L2               |              ||
	 *      Tenemos los datos de control seguidos de la primer lista del registro,
	 *      seguido la segunda. Solo tenemos dos listas y espacio libre, el doble |
	 *      simboliza el inicio y fin de bloque.
	 *      
	 *      El caso uno es actualizar la L2.
	 *            Caracteristicas:
	 *                   a) Espacio libre disponible para la extensión de la lista.
	 *                   b) Siguiente es 0
	 *                   c) Tope de busqueda es el espacioOcupado.
	 *                   d) Puede extenderse el bloque a uno nuevo.
	 */
	private Long administrarBloquesAModificar(final Long siguienteExt, final Short espacioOcupadoExt, 
					final Short primerRegistroExt, final RegistroTerminoDocumentos regActualizado,
					final Short offsetListaSiguiente, final int nroBloqueExt) {
		/*
		 * Datos que tengo:
		 * 		offsetLista (previa busqueda).
		 * 		datos del bloque (byte[]).
		 * 		datos de control.
		 * 		datos del registro actualizados.
		 */
		
		/*
		 * Si entre aca es porque tengo mas listas en el bloque
		 * Armar el registro con lo viejo + lo nuevo
		 */
		byte[] datosActualizados = regActualizado.getBytes();
		/* Longitud de la lista a actualizar */
		Short longitudNuevoRegistro = (short) datosActualizados.length;
		/* Longitud total del nuevo registro */
		Short espacioOcupadoNewConDatosControl = (short) (longitudNuevoRegistro + this.offsetLista);
		
		/* El tamaño final me exige crear otro bloque */
		boolean masBloques = espacioOcupadoNewConDatosControl > Constantes.SIZE_OF_LIST_BLOCK;
		
		int datosControl = Constantes.SIZE_OF_SHORT * 2 + Constantes.SIZE_OF_LONG;
		
		int bytesDisponibles = Constantes.SIZE_OF_LIST_BLOCK - (datosControl);
		
		short espacioOcupadoNew = (short) (espacioOcupadoNewConDatosControl - datosControl);
		
		int totalBloques = 0;
		
		//TODO: Lo nuevo es a partir de aquí
		Long siguiente = siguienteExt;
		Short primerRegistro = primerRegistroExt;
		Short espacioOcupado = espacioOcupadoExt;
		byte[] siguienteBloque;
		byte[] datos;
		short espacioDisponible = (short) (Constantes.SIZE_OF_LIST_BLOCK - datosControl);
		int offsetEscritura = 0;
		
		//Leo las listas que le siguen en el mismo bloque.
		RegistroTerminoDocumentos regLista;
		ArrayList<RegistroTerminoDocumentos> listas;
		Iterator<RegistroTerminoDocumentos> it;
		
		listas = leerListasPorBloque(offsetListaSiguiente);
		
		Long cantidadEscritaEnBuffer = 0L;
		/* El control esta dado por el numero de termino y la cantidad de documentos */
		int tamanioControlRegistro = Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_INT;
		
		byte[] datosNuevo = regActualizado.getBytes();
		byte[] datosAnteriores = new byte[this.offsetLista - datosControl + datosNuevo.length];
		System.arraycopy(this.datosLeidosPorBloque, datosControl, datosAnteriores, 0, this.offsetLista - datosControl);
		System.arraycopy(datosNuevo, 0, datosAnteriores, this.offsetLista - datosControl, datosNuevo.length);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		ByteArrayOutputStream bosaux = new ByteArrayOutputStream();
		DataOutputStream dosaux = new DataOutputStream(bosaux);
		
		
		try {
			
		while (siguiente != 0) {
			//Hay un bloque que le sigue por lo tanto tengo que actualizarlo
			//TODO: la proxima vez que llame tengo que pasarle como comienzo el offset del primerRegistro.
			try {
				dos.write(datosAnteriores, 0, datosAnteriores.length);
				cantidadEscritaEnBuffer += datosAnteriores.length;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			it = listas.iterator();
			
			//Para cada insercion tengo que validar que los datos de control entren
			//Que los nodos de las listas no queden partidos.
			while (it.hasNext()) {
				//Mientras tenga listas en el vector de listas las agrego
				regLista = it.next();
				datosNuevo = regLista.getBytes();
				//Si entra el registro completo lo inserto.
				if (datosNuevo.length <= (Constantes.SIZE_OF_LIST_BLOCK - cantidadEscritaEnBuffer)) {
					dos.write(datosNuevo,0,datosNuevo.length);
					cantidadEscritaEnBuffer += datosNuevo.length;
				} else {
					/*
					 * Si no entra el bloque entero entonces escribo una parte en el bloque a escribir
					 * y la otra en el datosAnteriores. Para ello busco el tamaño que me falta para
					 * completar el bloque y la cantidad max de elementos de las listas para insertar. 
					 */
					if (tamanioControlRegistro <= (Constantes.SIZE_OF_LIST_BLOCK - cantidadEscritaEnBuffer)) {
						Long cantidadNodosDisponibles = (Constantes.SIZE_OF_LIST_BLOCK - cantidadEscritaEnBuffer)/regLista.getTamanioNodo();
						//Inserto los nodo que puedo por el tamaño
						dos.write(datosNuevo,0,(int) (tamanioControlRegistro + (cantidadNodosDisponibles * regLista.getTamanioNodo())));
						cantidadEscritaEnBuffer += (int) (tamanioControlRegistro + (cantidadNodosDisponibles * regLista.getTamanioNodo()));
					} else {
						//Escribo toda la lista en un auxiliar.
						dosaux.write(datosNuevo, 0, datosNuevo.length);
					}
				}
			}
			
			datos = bos.toByteArray();
			//Ahora armo el bloque y lo inserto
			this.archivo.escribirBloque(
						this.armarDatosBloque(siguiente, datos, primerRegistro, (short) datos.length, offsetEscritura, datos.length),
						(int) nroBloqueExt);
			
			//Definir que es datosAnteriores.
			bos.reset();
			datosAnteriores = bosaux.toByteArray();
			
			//escribo lo que sobro del registro,pero supuestamente ahora no va a sobrar nada
			//dos.write(datos,espacioDisponible,datos.length - espacioDisponible);
			
			siguienteBloque = this.archivo.leerBloque(siguiente.intValue());
			
			ByteArrayInputStream bis = new ByteArrayInputStream(siguienteBloque);  
			DataInputStream dis = new DataInputStream(bis);
			
			siguiente = dis.readLong();
			primerRegistro = dis.readShort(); 
			espacioOcupado = dis.readShort();
				
			//Definir el nuevo offset
			listas = leerListasPorBloque((short) primerRegistro);
			primerRegistro = (short) (datos.length - espacioDisponible);
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
	
	/**
	 * Lee todas las listas del bloque, previamente tengo que leer el bloque donde comienza
	 * @param comienzo
	 * @return
	 */
	private ArrayList<RegistroTerminoDocumentos> leerListasPorBloque(final short comienzo) {
		
		RegistroTerminoDocumentos reg;
		long siguiente = this.nroBloque;
		short primerRegistro;
		short espacioOcupado = (short) Constantes.SIZE_OF_LIST_BLOCK;
		short cantBloquesLeidos = 0;
		ArrayList<RegistroTerminoDocumentos> listas = new ArrayList<RegistroTerminoDocumentos>();
		Short offsetSiguienteLista = new Short(comienzo);
		
		short tamanioControl = Constantes.SIZE_OF_LONG + (Constantes.SIZE_OF_SHORT * 2);

		boolean masListas = true;
		while (masListas) {
			reg  = new RegistroTerminoDocumentos();
			while (reg.incompleto() || reg.getCantidadDocumentos() == 0) {
				
				/* Saco la información de control del bloque */
				ByteArrayInputStream bis 
					= new ByteArrayInputStream(datosLeidosPorBloque);  
				DataInputStream dis = new DataInputStream(bis);
				
				try {
					siguiente = dis.readLong();
					primerRegistro = dis.readShort();
					espacioOcupado = dis.readShort();
					if (cantBloquesLeidos == 0) {		
						/* Armo la lista para el termino especifico */
						reg.setBytes(this.datosLeidosPorBloque, 
										offsetSiguienteLista.longValue());
						if (reg.incompleto()) {
							/* Obtengo el bloque según el numero de bloque*/
							datosLeidosPorBloque = this.archivo.leerBloque((int) siguiente);
							cantBloquesLeidos++;
						}
					} else {
						
						reg.setMoreBytes(datosLeidosPorBloque, 
								Constantes.SIZE_OF_LONG	
								+ Constantes.SIZE_OF_SHORT * 2);
						/* Obtengo el bloque según el numero de bloque*/
						if (reg.incompleto()) {
							/* Obtengo el bloque según el numero de bloque*/
							datosLeidosPorBloque = this.archivo.leerBloque((int) siguiente);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return null;
				}
				
			} //Me devuelve la lista leida
			listas.add(reg);
			//Con la lista leida veo si hay mas.
			if (cantBloquesLeidos > 1) {
				//No hay mas listas porque cambie de bloque
				masListas = false;
			} else {
				offsetSiguienteLista = (short) (Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_INT 
							+ (Constantes.SIZE_OF_LONG * reg.getCantidadDocumentos() * 2));
				if (offsetSiguienteLista >= espacioOcupado) {
					masListas = false;
				}
			}
		}
		return listas;
	}
}
