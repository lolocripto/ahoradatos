package ar.com.datos.grupo5.compresion.ppmc;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmeticoRamiro.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.aritmeticoRamiro.ParCharProb;
import ar.com.datos.grupo5.interfaces.Compresor;

/**
 * 
 */

/**
 * @author Led Zeppelin
 *
 */
public class Ppmc implements Compresor{

	private Contexto contextoOrdenMenosUno;
	
	private ArrayList<Orden> listaOrdenes;
	
	private String contextoActual;
	
	private int orden;
	
	private LogicaAritmetica compresorAritmetico;

	private boolean initSession;
	
	private String tiraBits;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(Ppmc.class);
	
	/**
	 * Contructor de clase.
	 */
	public Ppmc(){
		this.iniciarSesion();
	}

	/**
	 * 
	 * 
	 */
	public void imprimirHashMap() {

	}
	/**
	 * Inicializa el array con todos los elementos del UNICODE
	 * y el array de Ordenes
	 */
	private final void inicializarListas() {
		//Cargo la Lista de orden menos uno
		for (int i = 0; i < 65533; i++) {
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
				this.contextoOrdenMenosUno.crearCharEnContexto(new Character(Character.toChars(i)[0]));
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
					this.contextoOrdenMenosUno.crearCharEnContexto(new Character(Character.toChars(i)[0]));
				}
			}
		}
		this.contextoOrdenMenosUno.crearCharEnContexto(Constantes.EOF);
		
		this.contextoOrdenMenosUno.actualizarProbabilidades();
		
		//Cargo las listas de ordenes desde 0 al orden definido en la configuración
		Orden ordenContexto;
		for (int i = 0; i <= this.orden; i++) {
			ordenContexto = new Orden();
			this.listaOrdenes.add(ordenContexto);
		}
	}

	/**
	 * Genera un lista nueva con las letras posibles en el contexto.
	 * @param listaActual	Lista a la que voy a filtrar por el contexto de orden mayor.
	 * @param listaContextoAnterior Lista del contexto del orden mayor.
	 * @return Lista con la exclusion aplicada.
	 */
	private final ArrayList<ParCharProb> 
		obtenerExclusionCompleta(final Contexto contextoActual, final Contexto contextoAnterior){
	
		ArrayList<ParCharProb> nuevaListaContexto = new ArrayList<ParCharProb>();
		//Agrego todos los elementos del contexto actual
		nuevaListaContexto.addAll(contextoActual.getArrayCharProb());
		
		if (contextoAnterior != null) {
			if (!contextoAnterior.getArrayCharProb().isEmpty()) {		
				//Me copio el elemento ESC porque se va a borrar al hacer removeAll
				ParCharProb par = contextoActual.getChar(Constantes.ESC);
		
				nuevaListaContexto.removeAll(contextoAnterior.getArrayCharProb());
				nuevaListaContexto.add(par);
			}
		}
		return nuevaListaContexto;
	}
	
	/**
	 * Obtiene el contexto de la posición actual
	 * @param cadena Cadena a comprimir
	 * @param posicion Posición dentro de la cadena a comprimir
	 * @return El contexto en la posición posicion
	 */
	private final void getContexto(final String cadena, final int posicion){
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
	public final String comprimir(final String cadena){
		int pos = 0;
		if (this.initSession) {
			while (pos < cadena.length() ) {
				//Obtengo el contexto
				this.getContexto(cadena, pos);
				//Recorro los contextos para las emisiones
				this.recorrerContextos(cadena.charAt(pos));
				//Actualizo los contextos para la próxima recorrida.
				this.actualizarOrdenes(cadena.charAt(pos));
				//FIXME: Imprimo los ordenes, es solo para debug. Por lo tanto borrarlo.
				this.imprimirEstado();
				this.logger.debug("Letra: " + cadena.charAt(pos) + ", Emision: " + this.tiraBits);
				pos++;
			}
			//FIXME: Probar
			this.getContexto(cadena, pos);
			return this.tiraBits;
		} else {
			return "";
		}
	}

	private void imprimirEstado() {
		Iterator<Orden> it = this.listaOrdenes.iterator();
		Orden ordenAImprimir;
		int i = 0;
		while (it.hasNext()) {
			System.out.println("\tOrden " + i);
			ordenAImprimir = it.next();
			ordenAImprimir.meImprimo();
			System.out.println("");
			i++;
		}
	}

	/**
	 * Actualiza toda la estructura de ordenes.
	 * @param letra Letra a actualizar en la estructura de Ordenes.
	 */
	private void actualizarOrdenes(Character letra) {
		int ordenContexto = this.contextoActual.length();
		String contextoString = this.contextoActual.substring(0, ordenContexto); //FIXME: Ver el tema de contextoActual, sino se usa despues eliminar contexto
		logger.debug("Nuevo contexto: " + contextoString);
		Contexto contexto;
		boolean finalizarActualizacion = false;
		
		while (ordenContexto > -1 && !finalizarActualizacion) {
			contexto = this.listaOrdenes.get(ordenContexto).getContexto(contextoString);
			if (contexto.getArrayCharProb().isEmpty()) {
				//Agrego el Escape para el contexto vacio
				contexto.crearCharEnContexto(Constantes.ESC);
				//Agrego la letra al contexto ya que se que no exite por estar vacio el contexto
				contexto.crearCharEnContexto(letra);
			} else {
				//Busco la letra, si no esta la agrego y aumento ESC. Si esta Aumento la letra.
				if (contexto.existeChar(letra)) {
					//La encontro entonces actualizamos la letra
					contexto.actualizarContexto(letra);
					finalizarActualizacion = true;
					continue;
				} else {
					//Como la letra no esta, la creo y actualizo el ESC
					contexto.actualizarContexto(Constantes.ESC);
					contexto.crearCharEnContexto(letra);
				}
			}
			
			//Preparo para el siguiente contexto
			if (ordenContexto > 0){
				ordenContexto--;
				contextoString = this.contextoActual.substring(this.contextoActual.length() - ordenContexto, this.contextoActual.length());
				logger.debug("Nuevo contexto: " + contextoString);
			} else {
				ordenContexto--;
			}
		}
	}

	/**
	 * Recorre los contextos.
	 * @param letra
	 * @return
	 */
	private final void recorrerContextos(Character letra) {
		int ordenContexto = this.contextoActual.length();
		String contextoString = this.contextoActual.substring(0, ordenContexto);
		logger.debug("Nuevo contexto: " + contextoString);
		boolean finalizarRecorrida = false;
		Contexto contexto;
		Contexto contextoMasUno;
		ArrayList<ParCharProb> nuevoOrdenContexto;
		
		while (ordenContexto > -1 && !finalizarRecorrida) {
			
			//null o el contexto
			//Recorro los contextos
			contexto = this.listaOrdenes.get(ordenContexto).getContexto(contextoString);
			
			if (contexto == null) {
				/*
				 * No existe el contexto, por lo tanto emito ESC, lo agrego al contexto,
				 * como el contexto no existia no tiene sentido usar exclusion completa,
				 * y luego de emitir agrego la letra en cuestión
				 * y paso al siguiente contexto
				 */
				//Creo el contexto contextoString
				contexto = this.listaOrdenes.get(ordenContexto).crearContexto(contextoString);
				
				//Creo una estructura temporaria para evitar tener que cambiar todo el algoritmo. 
				ArrayList<ParCharProb> temp = new ArrayList<ParCharProb>();
				ParCharProb par = new ParCharProb(Constantes.ESC,1);
				temp.add(par);
				
				this.tiraBits += this.compresorAritmetico.comprimir(temp,Constantes.ESC);
				
				if (ordenContexto > 0){
					ordenContexto--;
					contextoString = this.contextoActual.substring(this.contextoActual.length() - ordenContexto, this.contextoActual.length());
					logger.debug("Nuevo contexto: " + contextoString);
				} else {
					ordenContexto--;
				}
				continue;
			}
				
			//El contexto buscado existe! Entonces busco el contexto anterior en el orden anterior
			if (ordenContexto < this.orden) {					
				contextoMasUno = this.listaOrdenes.get(ordenContexto + 1).getContexto(contextoActual.substring(this.contextoActual.length() - (ordenContexto + 1), this.contextoActual.length()));
			} else {
				//No existe orden anterior porque estoy en el ultimo orden (el orden mas grande)
				contextoMasUno = null;
			}
			
			contexto.actualizarProbabilidades();
			nuevoOrdenContexto = this.obtenerExclusionCompleta(contexto, contextoMasUno);
			
			/*
			//FIXME: LLamar al Compresor Aritmetico, si no hay nada como lo hago?? le mando un ESC como letra o 
			//solo le mando null en el nuevoOrdenContexto.
			 * La corroboracion de si la letra esta en la lista que le mando la hago yo o la hace el compresor??
			 * si la hace el compresor, deberia buscar y al no encontrar emitir ESC
			*/
			
			//Busco la letra en el contexto
			if (contexto.existeChar(letra)) {
				this.tiraBits += this.compresorAritmetico.comprimir(nuevoOrdenContexto,letra);	
			} else {
				this.tiraBits += this.compresorAritmetico.comprimir(nuevoOrdenContexto,Constantes.ESC);
			}
			
			
			
			if (contexto.existeChar(letra)) {
				finalizarRecorrida = true;
				continue;
			}
			
			//Actualizo el string del proximo contexto
			if (ordenContexto > 0){
				ordenContexto--;
				contextoString = this.contextoActual.substring(this.contextoActual.length() - ordenContexto, this.contextoActual.length());
				logger.debug("Nuevo contexto: " + contextoString);
			} else {
				ordenContexto--;
			}
		}
		//Analizo por separado el ultimo vector
		if (ordenContexto == -1 && !finalizarRecorrida){
	
			contextoMasUno = this.listaOrdenes.get(0).getContexto(contextoActual.substring(this.contextoActual.length() - (0), this.contextoActual.length()));
			
			nuevoOrdenContexto = this.obtenerExclusionCompleta(this.contextoOrdenMenosUno, contextoMasUno);

			this.tiraBits += this.compresorAritmetico.comprimir(nuevoOrdenContexto,letra);
		}
	}
	
	public final String finalizarCompresion(){
		
		//Recorro los contextos para las emisiones
		this.recorrerContextos(Constantes.EOF);
		//Actualizo los contextos para la próxima recorrida.
		this.actualizarOrdenes(Constantes.EOF);
		//FIXME: Imprimo los ordenes, es solo para debug. Por lo tanto borrarlo.
		this.imprimirEstado();
		
		this.tiraBits = this.compresorAritmetico.finalizarCompresion();
		
		return this.tiraBits;
	}

	@Override
	public String descomprimir(String datos) {
		
		return null;
	}

	@Override
	public void finalizarSession() {
		// TODO Auto-generated method stub
		// Lleno contextoOrdenMenosUno con todos los caracteres del UNICODE
		this.contextoOrdenMenosUno = null;
		//Obtengo el orden del xml de configuración
		this.orden = -1;
		//Creo tantos Ordenes como dice el XML
		this.listaOrdenes = null;
		this.compresorAritmetico = null;
		this.initSession = false;
		this.tiraBits = "";
	}

	@Override
	public void iniciarSesion() {
		//Lleno contextoOrdenMenosUno con todos los caracteres del UNICODE
		this.contextoOrdenMenosUno = new Contexto();
		//Obtengo el orden del xml de configuración
		this.orden = Constantes.ORDER_MAX_PPMC;
		//Creo tantos Ordenes como dice el XML
		this.listaOrdenes = new ArrayList<Orden>(this.orden+1);
		this.inicializarListas();
		this.compresorAritmetico = new LogicaAritmetica();
		this.initSession = true;
		this.tiraBits = "";
	}
}
