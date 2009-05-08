/**
 * 
 */
package ar.com.datos.grupo5.registros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
				byte[] superior = super.getBytes();	
				dos.write(superior);
				
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
				super.setBytes(buffer, bloqueAnt);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);  
			DataInputStream dos = new DataInputStream(bis);
			byte[] datos = null;
			
			try {
				//TODO:saltear la longitud del super!!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
