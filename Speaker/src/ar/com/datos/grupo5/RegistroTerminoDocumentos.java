
package ar.com.datos.grupo5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

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
	 * Cuantos bytes puedo pasar.
	 */
	private Long moreBytes;
	
	private Collection<ParFrecuenciaDocumento> datosDocumentos;
	
	private int	cantidadDocumentos;
	
	private int idTermino;
	
	private int cantidadDocumentosLeidos;
	
	private void setCantidadDocumentosLeidos(int cantDocumentosLeidos){
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
	 * @param idT_ermino
	 *            El offset a cargar.
	 */
	public final void setIdTermino(final int id_Termino) {
		this.idTermino = id_Termino;
		
		// Acá considero el tamaño de las listas tanto de 
		this.moreBytes = (long) this.datosDocumentos.size()*Constantes.SIZE_OF_LONG*2
				+ Constantes.SIZE_OF_INT*2;
	}
	
	
	/**
	 * Método para cargar la lista invertida.
	 * 
	 * @param offset
	 *            El offset a cargar.
	 */
	public final void setDatosDocumentos(final Collection<ParFrecuenciaDocumento> listasDatosDocumentos) {
		this.datosDocumentos = listasDatosDocumentos;
		
		this.cantidadDocumentos = listasDatosDocumentos.size();
			
		// Acá considero el tamaño de las listas tanto de 
		this.moreBytes = (long) listasDatosDocumentos.size()*Constantes.SIZE_OF_LONG*2
				+ Constantes.SIZE_OF_INT*2;
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
		
		//Intento pasar la información contenida en el registro a una tira de bytes
		try {
			
			//Convertir la cantidad de documentos
			
			byte[] cantidadDocumentosBytes = Conversiones.intToArrayByte(this.cantidadDocumentos);
			byte[] idTerminoByte = Conversiones.intToArrayByte(this.idTermino);
			long longDatosFrecuencia = this.datosDocumentos.size()*2*Constantes.SIZE_OF_LONG;
			
			if (moreBytes == (cantidadDocumentosBytes.length + longDatosFrecuencia)) {
				
				dos.write(idTerminoByte,0,idTerminoByte.length);
				moreBytes -= idTerminoByte.length;
				dos.write(cantidadDocumentosBytes, 0, cantidadDocumentosBytes.length);
				moreBytes -= cantidadDocumentosBytes.length;
				
				//for para recorrer todo el SortedMap
				for( Iterator<ParFrecuenciaDocumento> it = this.datosDocumentos.iterator(); it.hasNext();) { 
					ParFrecuenciaDocumento frecuenciaDocumento = (ParFrecuenciaDocumento)it.next();
					byte[] frecuenciaBytes = Conversiones.longToArrayByte(frecuenciaDocumento.getFrecuencia());
					byte[] offsetDocumentoBytes = Conversiones.longToArrayByte(frecuenciaDocumento.getOffsetDocumento());
	
					dos.write(frecuenciaBytes, 0, frecuenciaBytes.length);
					moreBytes -= frecuenciaBytes.length;
					dos.write(offsetDocumentoBytes, 0, offsetDocumentoBytes.length);
					moreBytes -= offsetDocumentoBytes.length;				
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Método que llena los atributos a partir de lo contenido en el buffer
	 * @param buffer Cadena de Bytes leida en el archivo de bloques
	 * @param offset id del termino que se busca.
	 */
	public void setBytes(byte[] buffer, Long offset) {
		// TODO Ver el tamaño de cantidadDocumentos en todos lados
		byte[] idTerminoByte = new byte[Constantes.SIZE_OF_INT];
		byte[] cantidadDocumentosByte = new byte[Constantes.SIZE_OF_INT];
		byte[] frecuenciaByte = new byte[Constantes.SIZE_OF_LONG];
		byte[] offsetDocumentoByte = new byte[Constantes.SIZE_OF_LONG];
		int offsetByte = 0;
		ParFrecuenciaDocumento parFD = null;
		//byte[] idTerminoByte = new byte[Constantes.SIZE_OF_INT];
		
		//Obtengo el dato del id_termino 
		System.arraycopy(buffer, offset.intValue(), idTerminoByte, 0, Constantes.SIZE_OF_LONG);
		this.idTermino = Conversiones.arrayByteToInt(idTerminoByte);
		
		offsetByte = offset.intValue() + Constantes.SIZE_OF_LONG;
		System.arraycopy(buffer, offsetByte, cantidadDocumentosByte, 0, Constantes.SIZE_OF_INT );
		
		this.cantidadDocumentos = Conversiones.arrayByteToInt(cantidadDocumentosByte);
		offsetByte += Constantes.SIZE_OF_INT;
		while(this.cantidadDocumentos > this.cantidadDocumentosLeidos && offsetByte < Constantes.SIZE_OF_INDEX_BLOCK) {
			/* recorro la cadena de bytes y genero los pares frecuencia, Documento */
			parFD = new ParFrecuenciaDocumento();
			System.arraycopy(buffer, offsetByte, frecuenciaByte, 0, Constantes.SIZE_OF_LONG);
			parFD.setFrecuencia(Conversiones.arrayByteToLong(frecuenciaByte));
			offsetByte += Constantes.SIZE_OF_LONG;
			System.arraycopy(buffer, offsetByte, offsetDocumentoByte, 0, Constantes.SIZE_OF_LONG);
			parFD.setOffsetDocumento(Conversiones.arrayByteToLong(offsetDocumentoByte));
			offsetByte += Constantes.SIZE_OF_LONG;
			this.datosDocumentos.add(parFD);
			this.cantidadDocumentosLeidos++;
		}
		//System.arraycopy(buffer, Constantes.SIZE_OF_LONG+Constantes.SIZE_OF_SHORT, datoEspacioOcupado, 0, Constantes.SIZE_OF_SHORT);
	}

	public void setMoreBytes(byte[] buffer, int offset) {
		byte[] frecuenciaByte = new byte[Constantes.SIZE_OF_LONG];
		byte[] offsetDocumentoByte = new byte[Constantes.SIZE_OF_LONG];
		ParFrecuenciaDocumento parFD = null;
		
		while(this.cantidadDocumentos > this.cantidadDocumentosLeidos && offset < Constantes.SIZE_OF_INDEX_BLOCK) {
			/* recorro la cadena de bytes y genero los pares frecuencia, Documento */
			parFD = new ParFrecuenciaDocumento();
			System.arraycopy(buffer, offset, frecuenciaByte, 0, Constantes.SIZE_OF_LONG);
			parFD.setFrecuencia(Conversiones.arrayByteToLong(frecuenciaByte));
			offset += Constantes.SIZE_OF_LONG;
			System.arraycopy(buffer, offset, offsetDocumentoByte, 0, Constantes.SIZE_OF_LONG);
			parFD.setOffsetDocumento(Conversiones.arrayByteToLong(offsetDocumentoByte));
			offset += Constantes.SIZE_OF_LONG;
			this.datosDocumentos.add(parFD);
			this.cantidadDocumentosLeidos++;
		}
	}
	
	public int getCantidadDocumentos(){
		return this.cantidadDocumentos;
	}

	public int getCantidadDocumentosLeidos(){
		return this.cantidadDocumentosLeidos;
	}

	public boolean incompleto(){
		return (this.cantidadDocumentos > this.cantidadDocumentosLeidos);
	}
}
