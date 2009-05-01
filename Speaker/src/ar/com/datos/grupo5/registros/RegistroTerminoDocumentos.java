
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.ListasInvertidas;
import ar.com.datos.grupo5.ParFrecuenciaDocumento;
import ar.com.datos.grupo5.interfaces.Registro;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * Esta clase implementa el registro para el diccionario.
 * 
 * @see ar.com.datos.grupo5.interfaces.Registro
 * @author LedZeppeling
 */
public class RegistroTerminoDocumentos implements Registro {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ListasInvertidas.class);

	/**
	 * Cuantos bytes puedo pasar.
	 */
	private Long moreBytes;
	
	/**
	 * Contiene todos los pares de frecuencia y documentos.
	 */
	private Collection<ParFrecuenciaDocumento> datosDocumentos;
	
	/**
	 * Contiene la cantidad de documentos de la lista invertida.
	 */
	private int	cantidadDocumentos;
	
	/**
	 * El id del termino de la lista. 
	 */
	private long idTermino;
	
	/**
	 * Contiene la cantidad de documentos leidos del bloque.
	 */
	private int cantidadDocumentosLeidos;
	
	/**
	 * Constructor de la clase.
	 */
	public RegistroTerminoDocumentos() {
		this.cantidadDocumentos = 0;
		this.cantidadDocumentosLeidos = 0;
		this.datosDocumentos = new ArrayList<ParFrecuenciaDocumento>();
		this.idTermino = 0L;
		this.moreBytes = 0L;
	}
	/**
	 * Perminte setear la cantidad de documentos leidos.
	 * @param cantDocumentosLeidos
	 * 			La cantidad de documentos leidos.
	 */
	@SuppressWarnings("unused")
	private void setCantidadDocumentosLeidos(final int cantDocumentosLeidos) {
		this.cantidadDocumentosLeidos = cantDocumentosLeidos;
	}
	
	/**
	 * En este caso se devuelve de una vez todos los bytes. Devuelvo true la
	 * primera vez y pongo en false, despues cuando se pregunta nuevamente
	 * devuelvo false, pero pongo en true para que el registro pueda ser usado
	 * denuevo.
	 * 
	 * @return true si hay mas bytes para pedir con getBytes.
	 */
	public final boolean hasMoreBytes() {
		
		if (moreBytes > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Método para cargar el idTermino.
	 * 
	 * @param idTerminoExt
	 *            El offset a cargar.
	 */
	public final void setIdTermino(final Long idTerminoExt) {
		this.idTermino = idTerminoExt;
		
		// Acá considero el tamaño de las listas tanto de 
		this.moreBytes = new Long(this.datosDocumentos.size() 
				* Constantes.SIZE_OF_LONG * 2 
				+ Constantes.SIZE_OF_INT + Constantes.SIZE_OF_LONG);
	}
	
	/**
	 * Método para obtener el idTermino del registro.
	 * @return
	 * 		el idTermino
	 */
	public final long getIdTermino() {
		return idTermino;
	}
	
	/**
	 * Método para cargar la lista invertida.
	 * 
	 * @param listasDatosDocumentos
	 *            Una colleccion.
	 */
	public final void setDatosDocumentos(final 
			Collection<ParFrecuenciaDocumento> listasDatosDocumentos) {
		this.datosDocumentos = listasDatosDocumentos;
		
		this.cantidadDocumentos = listasDatosDocumentos.size();
			
		// Acá considero el tamaño de las listas tanto de 
		this.moreBytes = (long) listasDatosDocumentos.size() 
				* Constantes.SIZE_OF_LONG * 2
				+ Constantes.SIZE_OF_INT + Constantes.SIZE_OF_LONG;
	}
	
	/**
	 * Método para devolver el dato.
	 * 
	 * @return El dato.
	 */
	public final Collection<ParFrecuenciaDocumento> getDatosDocumentos() {
		return this.datosDocumentos;
	}
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#setBytes(byte[], Long)
	 * @param buffer
	 *            la tira de bytes.
	 * @param offset
	 *            El offset en el que se encuentra el dato de audio asociado.
	 */
/*	
	public final void setBytes(final byte[] buffer, final Long offset) {
		this.setFrecuenciaPorDocumento(offset);
		this.setListaOffset(new String(buffer));
	}
*/	
	
	/**
	 * @see ar.com.datos.grupo5.interfaces.Registro#toBytes()
	 * @return los bytes que representan al registro.
	 */
	public final byte[] getBytes() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		//Intento pasar la información contenida en el 
		//registro a una tira de bytes
		try {
			
			//Convertir la cantidad de documentos
			
			byte[] cantidadDocumentosBytes = Conversiones.intToArrayByte(this.cantidadDocumentos);
			byte[] idTerminoByte = Conversiones.longToArrayByte(this.idTermino);
			long longDatosFrecuencia = this.datosDocumentos.size() * 2 
						* Constantes.SIZE_OF_LONG;
			
			if (moreBytes == (cantidadDocumentosBytes.length + idTerminoByte.length
					+ longDatosFrecuencia)) {
				
				dos.write(idTerminoByte, 0, idTerminoByte.length);
				moreBytes -= idTerminoByte.length;
				dos.write(cantidadDocumentosBytes, 0, 
							cantidadDocumentosBytes.length);
				moreBytes -= cantidadDocumentosBytes.length;
				
				//for para recorrer todo el SortedMap
				Iterator<ParFrecuenciaDocumento> it = this.datosDocumentos.iterator();
				ParFrecuenciaDocumento frecuenciaDocumento;
				while (it.hasNext()) {
					frecuenciaDocumento = (ParFrecuenciaDocumento)it.next();
					byte[] frecuenciaBytes = Conversiones.longToArrayByte(frecuenciaDocumento.getFrecuencia());
					byte[] offsetDocumentoBytes = Conversiones.longToArrayByte(frecuenciaDocumento.getOffsetDocumento());
	
					dos.write(frecuenciaBytes, 0, frecuenciaBytes.length);
					moreBytes -= frecuenciaBytes.length;
					dos.write(offsetDocumentoBytes, 0, 
								offsetDocumentoBytes.length);
					moreBytes -= offsetDocumentoBytes.length;				
	
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer.
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param offset id del termino que se busca.
	 */
	public final void setBytes(final byte[] buffer, final Long offset) {
		// TODO Ver el tamaño de cantidadDocumentos en todos lados
		int offsetByte = 0;
		ParFrecuenciaDocumento parFD = null;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer,offset.intValue(),Constantes.SIZE_OF_INDEX_BLOCK);  
		DataInputStream dis = new DataInputStream(bis);
		
		try {
			
				//Obtengo el dato del id_termino 
				this.idTermino = dis.readLong();
				offsetByte = offset.intValue() + Constantes.SIZE_OF_LONG;
				this.cantidadDocumentos = dis.readInt();
				offsetByte += Constantes.SIZE_OF_INT;
				
				while (this.cantidadDocumentos > this.cantidadDocumentosLeidos 
						&& offsetByte < Constantes.SIZE_OF_INDEX_BLOCK) {
					/* recorro la cadena de bytes y genero los 
					 * pares frecuencia, Documento */
					parFD = new ParFrecuenciaDocumento();
					//Leo la frecuencia
					parFD.setFrecuencia(dis.readLong());
					offsetByte += Constantes.SIZE_OF_LONG;
					
					//Leo el offset a documentos
					parFD.setOffsetDocumento(dis.readLong());
					offsetByte += Constantes.SIZE_OF_LONG;
					
					//Agrego el par a la coleccion
					this.datosDocumentos.add(parFD);
					this.cantidadDocumentosLeidos++;
				}
			
			} catch (Exception e) {
				logger.error("Error: " + e.getMessage());
			}		
	}

	/**
	 * En el caso de ocupar varios bloques esta función agrega mas datos
	 * al registro a partir de los datos de otros bloques.
	 * @param buffer
	 * 			Datos leidos de un bloque que no es el primero.
	 * @param offset
	 * 			Offset dentro de les array de byte donde empiezan
	 * 			los datos.
	 */
	public final void setMoreBytes(final byte[] buffer, final int offset) {

		ParFrecuenciaDocumento parFD = null;
		int offsetDatos = offset;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
		DataInputStream dis = new DataInputStream(bis);
		
		try {
			while (this.cantidadDocumentos > this.cantidadDocumentosLeidos
					&& offsetDatos < Constantes.SIZE_OF_INDEX_BLOCK) {
				/* recorro la cadena de bytes y genero los 
				 * pares frecuencia, Documento */
				parFD = new ParFrecuenciaDocumento();
				parFD.setFrecuencia(dis.readLong());
				offsetDatos += Constantes.SIZE_OF_LONG;
				parFD.setOffsetDocumento(dis.readLong());
				offsetDatos += Constantes.SIZE_OF_LONG;
				this.datosDocumentos.add(parFD);
				this.cantidadDocumentosLeidos++;
			}
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
		}		
	}
	
	/**
	 * Permite obtener la cantidad de documentos para el idTermino.
	 * @return
	 * 		La cantidad de documentos para el termino
	 */
	public final int getCantidadDocumentos() {
		return this.cantidadDocumentos;
	}

	/**
	 * Permite obtener la cantidad de documentos leidos de archivo.
	 * @return
	 * 		La cantidad de documentos leidos
	 */
	public final int getCantidadDocumentosLeidos() {
		return this.cantidadDocumentosLeidos;
	}

	/**
	 * Permite saber si no se leyeron todos los elementos de 
	 * frecuencia-documentos.
	 * @return
	 * 		True si se leyeron todos los elementos, False si
	 * faltan elementos.
	 */
	public final boolean incompleto() {
		return (this.cantidadDocumentos > this.cantidadDocumentosLeidos);
	}
	
	/**
	 * Obtiene el tamaño del registro.
	 * @return el tamaño del registro
	 */
	public final Short getTamanio() {
		return (short) (this.getBytes().length);
	}
}
