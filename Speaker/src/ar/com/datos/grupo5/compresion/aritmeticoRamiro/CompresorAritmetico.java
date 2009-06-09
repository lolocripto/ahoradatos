/**
 * 
 */
package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

import java.util.ArrayList;
import java.util.List;

import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

/**
 * @author Led Zeppelin
 *
 */
public class CompresorAritmetico implements Compresor{

	private Character contexto;
	private List<Orden> listaOrdenes;
	private LogicaAritmetica motorAritmetico;
	private int orden;
	//private String bits;
	
	public CompresorAritmetico(){
		contexto = new Character('\b');
		listaOrdenes = null;
		this.motorAritmetico = new LogicaAritmetica();
	}
	
	public CompresorAritmetico(final int ordenCompresor){
		this.orden = ordenCompresor;
		this.iniciarSesion();
	}

	@Override
	public String comprimir(String cadena) throws SessionException {
		Orden ordenActual = this.listaOrdenes.get(this.orden);;
		Contexto ctx = null;
		
		/*
		 *  Obtengo el contexto con el cual voy a trabajar, si era 
		 * 	orden-0 entonces el contexto debe ser ""
		 *  orden-1 el contexto debe ser != ""
		 */
		switch(this.orden){
		case 1:
			//FIXME: Ver el famoso \b
			ctx = ordenActual.getContexto(contexto.toString());
			break;
		default:
			//Orden 0
			ctx = ordenActual.getContexto("");
			break;
		}
		
		
		
		//Si el contexto no existe lo creo
		if (ctx == null){
			//Creo el contexto 
			ctx = this.listaOrdenes.get(orden).crearContexto(contexto.toString());
			//Agrego la letra al contexto.
			ctx.crearCharEnContexto(cadena.charAt(0));
		} else {
			//El contexto existe, verifico que exista la letra a agregar en el contexto
			if (ctx.existeChar(cadena.charAt(0))){
				//Exite la letra por lo tanto actualizo la frecuencia
				ctx.actualizarContexto(cadena.charAt(0));
			} else {
				//Creo la letra.
				ctx.crearCharEnContexto(cadena.charAt(0));
			}
		}
		if (this.orden > 0) {
			//Si es de orden mayor a 0 entonces actualizo el contexto
			this.contexto = cadena.charAt(0);
		}
		ctx.actualizarProbabilidades();
		return this.motorAritmetico.comprimir( (ArrayList<ParCharProb>) ctx.getArrayCharProb(), cadena.charAt(0));
		
	}

	@Override
	public String descomprimir(String datos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizarSession() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimirHashMap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iniciarSesion() {
		// TODO Auto-generated method stub
		this.listaOrdenes = new ArrayList<Orden>(this.orden);
		Orden ordenContexto;
		for (int i = 0; i <= this.orden; i++) {
			ordenContexto = new Orden();
			this.listaOrdenes.add(ordenContexto);
		}
		this.contexto = new Character('\b');
		this.motorAritmetico = new LogicaAritmetica();
	}
}
