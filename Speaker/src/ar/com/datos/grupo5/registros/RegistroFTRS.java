/**
 * 
 */
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author Led Zeppelin
 *
 */
public class RegistroFTRS extends RegistroNodo{

		private Long idTermino;
		
		private Long bloqueListaInvertida;
		
		private ClaveFrontCoding claveNodo;

		/**
		 * @param idTerminoExt the idTermino to set
		 */
		public final void setIdTermino(final Long idTerminoExt) {
			this.idTermino = idTerminoExt;
		}

		/**
		 * @return the idTermino
		 */
		public final Long getIdTermino() {
			return idTermino;
		}

		/**
		 * @param idTerminoExt the idTermino to set
		 */
		public final void setClaveFrontCoding(ClaveFrontCoding claveFrontCodingExt) {
			this.claveNodo = claveFrontCodingExt;
		}

		/**
		 * @return the idTermino
		 */
		public final ClaveFrontCoding getClaveFrontCoding() {
			return this.claveNodo;
		}

		/**
		 * @param bloqueListaInvertidaExt the bloqueListaInvertida to set
		 */
		public final void setBloqueListaInvertida(final Long bloqueListaInvertidaExt) {
			this.bloqueListaInvertida = bloqueListaInvertidaExt;
		}

		/**
		 * @return the bloqueListaInvertida
		 */
		public final Long getBloqueListaInvertida() {
			return bloqueListaInvertida;
		}
		
		@Override
		public final byte[] getBytes() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			DataOutputStream dos = new DataOutputStream(bos);
			try {
				
				//obtengo la clave del nodo
				byte[] aux = this.claveNodo.getBytes();
				
				//escribo un int con su longitud
				dos.writeInt(aux.length);
				
				//escribo el array de bytes auxiliar
				dos.write(aux);
				
			//	byte[] superior = super.getBytes();	
			//	dos.write(superior);
				
				dos.write(Conversiones.longToArrayByte(this.idTermino));
				dos.write(Conversiones.longToArrayByte(this.bloqueListaInvertida));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return bos.toByteArray();
		}
		
		@Override
		public final void setBytes(final byte[] buffer, final int bloqueAnt) {
		
			try {
			
			//genero los manipuladores del array de bytes
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
			DataInputStream dos = new DataInputStream(bis);
			
			//leo la longitud de la clave para poder leerla
			int longClave = dos.readInt();
			
			//leo la clave
			byte[] clave = new byte[longClave];
			dos.read(clave);
		
			//instancio la clave con el array anterior
			this.claveNodo = new ClaveFrontCoding(clave);
			
			//leo el id termino
			this.idTermino = dos.readLong();
			
			//leo el puntero a la lista
			this.bloqueListaInvertida = dos.readLong();
			
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		}
}
