package ar.com.datos.grupo5.compresion.ppmc;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.LogicaAritmetica;
import ar.com.datos.grupo5.compresion.aritmetico.ParCharProb;
import ar.com.datos.grupo5.excepciones.SessionException;
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
	
	private boolean sessionCompresion;

	private StringBuffer bitsBuffer;
	
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
		Iterator<Character> it = Constantes.LISTA_CHARSET_LATIN.iterator();
		Character letra;
		while (it.hasNext()) {
			letra = it.next();
			this.contextoOrdenMenosUno.crearCharEnContexto(letra);
			//System.out.println(letra);
		}
		/*
		for (int i = 0; i < 65533; i++) {
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
				this.contextoOrdenMenosUno.crearCharEnContexto(new Character(Character.toChars(i)[0]));
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
					this.contextoOrdenMenosUno.crearCharEnContexto(new Character(Character.toChars(i)[0]));
				}
			}
		}
		*/
		
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
	 * @throws SessionException 
	 */
	public final String comprimir(final String cadena) throws SessionException{
		int pos = 0;
		if (this.initSession) {
			sessionCompresion = true;
			while (pos < cadena.length() ) {
				//Obtengo el contexto
				this.getContexto(cadena, pos);
				//Recorro los contextos para las emisiones
				this.recorrerContextos(cadena.charAt(pos));
				//Actualizo los contextos para la próxima recorrida.
				this.actualizarOrdenes(cadena.charAt(pos));
				//FIXME: Imprimo los ordenes, es solo para debug. Por lo tanto borrarlo.
				this.imprimirEstado();
				logger.debug("Letra: " + cadena.charAt(pos) + ", Emision: " + this.tiraBits);
				pos++;
			}
			//FIXME: Probar
			this.getContexto(cadena, pos);
			return this.tiraBits;
		} else {
			throw new SessionException();
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
		String contextoString = this.contextoActual.substring(0, ordenContexto);
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
		//Obtengo la longitud del contexto para saber en que nivel de orden empiezo
		int ordenContexto = this.contextoActual.length();
		//obtengo el contexto para no modificar el contexto original
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
	
	private final String finalizarCompresion(){
		
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
	public String descomprimir(StringBuffer datos) throws SessionException {
		if (!this.initSession) {
			throw new SessionException();
		}
		//Indico que la session es de descompresion.
		sessionCompresion = false;
		
		//Tengo algo en el buffer que quedo de otra pasada
		//lo concateno con lo nuevo
		if (this.bitsBuffer.length() > 0) {
			datos.insert(0,this.bitsBuffer);
			this.bitsBuffer = null;
		}
		
		//Si los datos del buffer mas los datos de entrada son menores
		//a la malla de bits entonces devuelvo null ya que no puedo seguir
		if (datos.length() < 32) {
			this.bitsBuffer = new StringBuffer(datos);
			datos.delete(0, datos.length());
			return null;
		}
		
		String emision = "";
		
		int pos = 0;
		if (this.initSession) {
			sessionCompresion = true;
				
			//Recorro los contextos para las emisiones
			emision = this.recorrerContextosDescompresion(datos);

			//Si es null entonces lo devuelvo porque necesito mas bits
			//TODO: Buffer para manter los bits anteriores.
			if (emision == null) {
				return null;
			}
				
			//Actualizo los contextos para la próxima recorrida.
			this.actualizarOrdenes(emision.charAt(0));
				
			//FIXME: Imprimo los ordenes, es solo para debug. Por lo tanto borrarlo.
			this.imprimirEstado();
			
			this.logger.debug("Letra: " + emision.charAt(pos) + ", Contexto: " + this.contextoActual);
			
			//FIXME: Probar
			//this.getContexto(emision, pos);
			
			//Actualizo el contexto
			this.actualizarContexto(emision);
			return emision;
		} else {
			throw new SessionException();
		}
	}

	/**
	 * Actualiza el contexto actual dependiendo de la salida del descompresor
	 * @param emision Caracter emitido que será parte del contexto en la proxima iteracion
	 */
	private void actualizarContexto(String emision) {
		if (this.contextoActual.length() == this.orden){
			//El maximo permitido es 4 entonces tengo que sacar el elemento 0
			this.contextoActual = this.contextoActual.substring(1);
		}
		this.contextoActual += emision.charAt(0);
	}

	/**
	 * Recorro los contextos para la descompresion.
	 * @param datos Buffer con los bits a descomprimir
	 * @return Devuelvo 
	 */
	private final String recorrerContextosDescompresion(StringBuffer datos) {
		
		//Obtengo el largo del contexto para saber donde empiezo
		int ordenContexto = this.contextoActual.length();
		//Obtengo mi contexto actual
		String contextoString = this.contextoActual.substring(0, ordenContexto);
		
		logger.debug("Nuevo contexto: " + contextoString);
		
		boolean finalizarRecorrida = false;
		Contexto contexto;
		Contexto contextoMasUno;
		ArrayList<ParCharProb> nuevoOrdenContexto;
		Character emision = new Character(' ');
		
		
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
				
				emision = this.compresorAritmetico.descomprimir(temp, datos);
				
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
			
			emision = this.compresorAritmetico.descomprimir(nuevoOrdenContexto, datos);
			
			if (Constantes.ESC.compareTo(emision) != 0) {
				finalizarRecorrida = true;	
				return emision.toString();
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

			emision = this.compresorAritmetico.descomprimir(nuevoOrdenContexto, datos);
		}
		
		return emision.toString();
	}
	
	@Override
	public String finalizarSession() {
		// TODO Auto-generated method stub
		if (!this.initSession){
			return "";
		}
		
		String datosFinales = new String();
		if (this.sessionCompresion) {
			 datosFinales = this.finalizarCompresion();
		}
		
		// Lleno contextoOrdenMenosUno con todos los caracteres del UNICODE
		this.contextoOrdenMenosUno = null;
		//Obtengo el orden del xml de configuración
		this.orden = -1;
		//Creo tantos Ordenes como dice el XML
		this.listaOrdenes = null;
		this.compresorAritmetico = null;
		this.initSession = false;
		this.tiraBits = "";
		this.contextoActual = "";
		//Si finalizo una sesion de compresion entonces llamo a finalizar compresion
		if (this.sessionCompresion) {
			 return datosFinales;
		}
		return "";
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
		this.bitsBuffer = new StringBuffer();
	}

	@Override
	public boolean isFinalizada() {
		// TODO Auto-generated method stub
		return false;
	}
}
