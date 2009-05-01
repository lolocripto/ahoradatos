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
	private byte[] armarDatosBloque(final Long nroSiguienteBloque, final byte[] datos, final short primerRegistro, final short espacioOcupado, final int offset) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
				
		int bytesDisponibles = Constantes.SIZE_OF_LIST_BLOCK - (Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT * 2); 
		try {
			dos.writeLong(nroSiguienteBloque);
			dos.writeShort(primerRegistro);
			
//			dos.write(datos);
			if ((datos.length - offset) > bytesDisponibles) {
				//Copio desde el offset el tamanio del bloque
				dos.writeShort(Constantes.SIZE_OF_LIST_BLOCK);
				dos.write(datos , offset, bytesDisponibles );
			} else {
				dos.writeShort(datos.length - offset + Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT * 2);
				dos.write(datos , offset, datos.length - offset);
			}
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
		int bytesDisponibles = Constantes.SIZE_OF_LIST_BLOCK - tamanioDatosControl;
		
		/* Valido que la cantidad de información a insertar entre 
		 * en un bloque */
		masRegistros = (tamanioRegistro > bytesDisponibles);

		byte[] bytesAEscribir;
		
		/* Genero todos los registros */
		if (masRegistros) {
			//Calculo el total de bloques
			int offsetEscritura = 0;
			totalBloques = tamanioRegistro / bytesDisponibles;
			if ((tamanioRegistro % bytesDisponibles) > 0) {
				totalBloques++;
			}
			
			/* Capturo el primer bloque del Total Bloques */
			this.nroBloque = this.cantidadBloques;
			
			/* Armo los bloques que serán escritos al final del archivo 
			 * pero el ultimo lo escribo a parte ya que el 
			 * "siguiente" es distinto*/
			for (int i = 0; i < totalBloques-1; i++) {

				bytesAEscribir = armarDatosBloque((long) this.cantidadBloques + 1, bytes, (short) 0, (short) (tamanioDatosControl + bytes.length),offsetEscritura);
				try {
					this.archivo.escribirBloque(bytesAEscribir,  this.cantidadBloques);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.cantidadBloques++;
				offsetEscritura += bytesDisponibles;
			}

			bytesAEscribir = armarDatosBloque(0L, bytes, (short) (tamanioDatosControl + bytes.length), (short) (tamanioDatosControl + bytes.length),offsetEscritura);
			try {
				this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.espacioLibre.actualizarListaEspacioLibre(-1, cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK-bytesAEscribir.length));
		} else {
			//Es un solo registro por lo tanto puedo insertarlo en un bloque con algún espacio libre
			int bloqueAInsertar = this.espacioLibre.buscarEspacio((short) tamanioRegistro);
				
			/* Capturo numero de bloque que va a ser modificado o que se va a insertar */
			this.nroBloque = this.cantidadBloques;
			
				//si entra lo agrego en este punto, sino lo agrego como un bloque nuevo
			if (bloqueAInsertar != -1) {
				/* agrego el registro en el bloque
				 * 1)Leo el bloque que tiene el espacio libre
				 * 2)Leo los datos de control y me quedo con espacioOcupado
				 * 3)me paro al final ( espacioOcupado ) y escribo en ese lugar
				 * 4)Para escribir me aseguro que entra y deja 20% del bloque libre.
				 * 5)Actualizo el elemento dentro de la lista de espacio libre en los bloques.
				 */
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
					
					short tamanioControl = Constantes.SIZE_OF_SHORT * 2 + Constantes.SIZE_OF_INT;
					
					
					
					byte[] datos = new byte[espacioOcupado - tamanioControl + bytes.length];
					
					System.arraycopy(bytesTemporales, tamanioControl, datos, 
							0, espacioOcupado - tamanioControl);
					
					System.arraycopy(bytes, 0, datos, 
							espacioOcupado-tamanioControl, bytes.length);
					
					bytesTemporales = this.armarDatosBloque(siguiente, datos, primerRegistro, (short) (tamanioControl + datos.length),0);
					
					this.archivo.escribirBloque(bytesTemporales, bloqueAInsertar);
					
					this.espacioLibre.actualizarEspacio((short) (tamanioControl+datos.length));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				//this.actualizarListaEspacioLibre();
			} else {
				//Inserto un registro nuevo
				byte[] bytesTemporales;
				bytesAEscribir = new byte[Constantes.SIZE_OF_LIST_BLOCK];
				bytesTemporales = armarDatosBloque((long) 0, bytes, (short) 0, (short) (tamanioDatosControl + bytes.length),0);
				System.arraycopy(bytesTemporales, 0, bytesAEscribir, 
						0, bytesTemporales.length);
				try {
					this.archivo.escribirBloque(bytesAEscribir, this.cantidadBloques);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.espacioLibre.actualizarListaEspacioLibre(-1, this.cantidadBloques, (short) (Constantes.SIZE_OF_LIST_BLOCK - (tamanioDatosControl + bytes.length)));
				this.cantidadBloques++;
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
	
		int tamanioControl = Constantes.SIZE_OF_LONG + Constantes.SIZE_OF_SHORT * 2;
		
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
				//TODO:Escribo el bloque teniendo en cuenta que pude necesitar un bloque nuevo.
				return true;
			}
			
			/* Se encarga de correr todas las listas actualizando la actual */
			this.actualizarCaso1(bloqueSiguiente, espacioOcupado, primerRegistro, regActualizado, offsetListaSiguiente,nroBloqueExt);
			
			/*
			//Como hay mas datos en el 
			//Verifico lo correspondiente a los tamaños para volver a insertarla
			//Si encontro el idTermino en el bloque
			if( bloqueSiguiente == 0) {
				//Entonces no hay un bloque siguiente
			} else {
				//Continua en otro bloque
			}
			
			//Miro donde empieza el siguiente registro.
			
			//Si no empieza el siguiente registro.
			
			//Actualizo la lista en todos los bloques.
			 * 
			 */
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
	private Long actualizarCaso1(final Long siguienteExt, final Short espacioOcupadoExt, 
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
			
			//Para cada insercion tengo que validar que los datos de cotnrol
			while (it.hasNext()) {
				//Mientras tenga listas en el vector de listas las agrego
				regLista = it.next();
				datosNuevo = regLista.getBytes();
				if (tamanioControlRegistro <= (Constantes.SIZE_OF_LIST_BLOCK - cantidadEscritaEnBuffer)) {
					dos.write(datosNuevo,0,datosNuevo.length);
					cantidadEscritaEnBuffer += datosNuevo.length;
				}
			}
			
			datos = bos.toByteArray();
			//Ahora armo el bloque y lo inserto
			this.archivo.escribirBloque(
						this.armarDatosBloque(siguiente, datos, primerRegistro, espacioOcupado, offsetEscritura),
						(int) nroBloqueExt);
			
			//Definir que es datosAnteriores.
			bos.reset();
			dos.write(datos,espacioDisponible,datos.length - espacioDisponible);
			
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
