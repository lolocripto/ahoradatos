package ar.com.datos.grupo5.compresion.ppmc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.ParCharProb;

/**
 * 
 */

/**
 * @author Led Zeppelin
 *
 */
public class Ppmc {

	private ArrayList<ParCharProb> contextoOrdenMenosUno;
	
	private ArrayList<Orden> listaOrdenes;
	
	private String contextoActual;
	
	private int orden;
	
	/**
	 * Contructor de clase.
	 */
	public Ppmc(){
		//TODO: Llenar contextoOrdenMenosUno con los caracteres validos del UNICODE
		
		//Obtengo el orden del xml de configuración
		this.orden = Constantes.ORDER_MAX_PPMC;
		//Creo tantos Ordenes como dice el XML
		this.listaOrdenes = new ArrayList<Orden>(orden+1);
	}
	
	/**
	 * Genera un lista nueva con las letras posibles en el contexto.
	 * @param listaActual	Lista a la que voy a filtrar por el contexto de orden mayor.
	 * @param listaContextoAnterior Lista del contexto del orden mayor.
	 * @return Lista con la exclusion aplicada.
	 */
	private final ArrayList<ParCharProb> 
		obtenerExclusionCompleta(final ArrayList<ParCharProb> listaActual, final ArrayList<ParCharProb> listaContextoAnterior){
		
		//FIXME: Recibir dos contexto y dentro de esta funcion obtengo las listas para hacer la exclusion
		ArrayList<ParCharProb> nuevaListaContexto = new ArrayList<ParCharProb>();
		
		nuevaListaContexto.addAll(listaActual);
		if (listaContextoAnterior != null) {
			nuevaListaContexto.removeAll(listaContextoAnterior);
		}
		//FIXME: Agregar el escape de listaActual.
		return nuevaListaContexto;
	}
	
	/**
	 * Obtiene el contexto de la posición actual
	 * @param cadena Cadena a comprimir
	 * @param posicion Posición dentro de la cadena a comprimir
	 * @return El contexto en la posición posicion
	 */
	private final void obtenerContexto(final String cadena, final int posicion){
		//FIXME: Ver si es > o >=
		if (posicion > this.orden) {
			//Obtengo un contexto de maximo orden
			// casados -> pos = d => el contexto = casa
			// casados -> pos = o => el contexto = asad
			contextoActual = cadena.substring(posicion - this.orden, posicion);
		} else {
			//Obtengo un contexto parcial por no tener un contexto mayor
			// casados -> pos = s => el contexto = ca
			contextoActual = cadena.substring(0, posicion);
		}
	}
	
	/**
	 * Comprime la cadena recibida bajo el metodo PPMC
	 * @param cadena Cadena a comprimir.
	 * @return Un Array de bytes para escribir a archivo.
	 */
	public final byte[] comprimir(final String cadena){
		int pos = 0;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		while (pos < cadena.length()) {
			this.obtenerContexto(cadena, pos);
			try {
				dos.write(this.recorrerContextos(cadena.charAt(pos)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			pos++;
		}
		
		return bos.toByteArray();
	}

	private final byte[] recorrerContextos(Character letra) {
		int ordenContexto = this.contextoActual.length();
		String contexto = this.contextoActual.substring(0, ordenContexto); //FIXME: Ver el tema de contextoActual, sino se usa despues eliminar contexto
		boolean finalizarRecorrida = false;
		//Recorro los Contextos desde el orden indicado por el largo del contexto
		ArrayList<ParCharProb> listaContextoMasUno;

		while (ordenContexto > -1 && !finalizarRecorrida){
			//null o el contexto
			ArrayList<ParCharProb> listaContextoActual = new ArrayList<ParCharProb>();
			//Recorro los contextos
			
			//FIXME: no se como obtener la lista de values que quiero.
			//listaContextoActual.addAll(this.listaOrdenes.get(ordenContexto).obtenerArrayList(contexto));
			
			//FIXME: no se como obtener la lista de values que quiero.
			if (ordenContexto < this.orden) {
				
				listaContextoMasUno = new ArrayList<ParCharProb>();
			//listaContextoMasUno.add(this.listaOrdenes.get(ordenContexto+1).obtenerArrayList(contexto));
				
			} else {
				listaContextoMasUno = null;
			}
			
			ArrayList<ParCharProb> nuevoOrdenContexto = this.obtenerExclusionCompleta(listaContextoActual, listaContextoMasUno);
			
			//FIXME: LLamar al Compresor Aritmetico, si no hay nada como lo hago?? le mando un ESC o 
			//solo le mando null en el nuevoOrdenContexto
			if (nuevoOrdenContexto == null || nuevoOrdenContexto.size() == 0) {
				//this.comprimir(null,letra);
			} else {
			//this.comprimir(nuevoOrdenContexto,letra);
			}
		
			//Actualizo la frecuencia en el contexto particular
			this.listaOrdenes.get(ordenContexto).incrementarFrecuenciaChar(contexto, letra);
		
			//Actualizo el proximo contexto
			contexto = this.contextoActual.substring(this.orden - ordenContexto, ordenContexto);
		}
		if (ordenContexto == -1 && !finalizarRecorrida){
			//Analizo por separado el ultimo vector 
		}
		
		//FIXME: Convertir NroRes a bytes y devolverlo, NO DEVOLVER NADA
		return new byte[2];
	}
	
}
