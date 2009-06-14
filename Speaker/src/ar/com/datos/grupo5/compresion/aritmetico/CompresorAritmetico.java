/**
 * 
 */
package ar.com.datos.grupo5.compresion.aritmetico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.ppmc.Contexto;
import ar.com.datos.grupo5.compresion.ppmc.Orden;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;

/**
 * @author Led Zeppelin
 *
 */
public class CompresorAritmetico implements Compresor {

	private Character contexto;
	private List<Orden> listaOrdenes;
	private LogicaAritmetica motorAritmetico;
	private int orden;
	private String bits;
	private boolean charset;
	private StringBuffer bitsBuffer;
	private boolean sessionInit;
	private boolean sessionCompresion;
	
	public CompresorAritmetico(){
		contexto = new Character('\b');
		listaOrdenes = null;
		this.motorAritmetico = new LogicaAritmetica();
		this.sessionInit = false;
		this.bitsBuffer = new StringBuffer();
	}
	
	public CompresorAritmetico(final int ordenCompresor){
		this.charset = true;
		this.orden = ordenCompresor;
		this.sessionInit = false;
		this.bitsBuffer = new StringBuffer();
	}

	public CompresorAritmetico(final int ordenCompresor, final boolean charSet){
		this.orden = ordenCompresor;
		this.charset = charSet;
		this.sessionInit = false;
		this.bitsBuffer = new StringBuffer();
	}
	
	@Override
	public String comprimir(String cadena) throws SessionException {
		
		if	(!this.sessionInit) {
			throw new SessionException();
		}
		//Indico que la sesion actual es de compresion
		this.sessionCompresion = true;
		
		this.bits = "";
		
		Orden ordenActual = this.listaOrdenes.get(0);
		Contexto ctx = null;
		int pos = 0;
		//Recorro el String de entrada
		while (pos < cadena.length()) {
			/*
			 *  Obtengo el contexto con el cual voy a trabajar, si era 
			 * 	orden-0 entonces el contexto debe ser ""
			 *  orden-1 el contexto debe ser != ""
			 */
			switch(this.orden){
			case 1:
				ctx = ordenActual.getContexto(contexto.toString());
				break;
			default:
				//Orden 0
				ctx = ordenActual.getContexto("");
				break;
			}
			
			//El contexto existe, verifico que exista la letra a agregar en el contexto
			if (ctx.existeChar(cadena.charAt(pos))){
				//Exite la letra por lo tanto actualizo la frecuencia
				ctx.actualizarProbabilidades();
					
				//FIXME: Genero una lista temporal porque no puedo castear de un HASHMAP$Values a ArrayList 
				ArrayList<ParCharProb> temp = new ArrayList<ParCharProb>(ctx.getArrayCharProb());
					
				this.bits += this.motorAritmetico.comprimir(temp, cadena.charAt(pos));
				ctx.actualizarContexto(cadena.charAt(pos));
			}
			
			if (this.orden > 0) {
				//Si es de orden mayor a 0 entonces actualizo el contexto
				this.contexto = cadena.charAt(pos);
			}
		//Termina la iteracion de los caracteres del string
			pos++;
		}
		return this.bits;
	}

	
	@Override
	public String descomprimir(StringBuffer datos) throws SessionException{
		
		if	(!this.sessionInit) {
			throw new SessionException();
		}
		
		this.sessionCompresion = false;
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
		
		Orden ordenActual = this.listaOrdenes.get(0);
		Contexto ctx = null;
		
		/*
		 *  Obtengo el contexto con el cual voy a trabajar, si era 
		 * 	orden-0 entonces el contexto debe ser ""
		 *  orden-1 el contexto debe ser != ""
		 */
		switch(this.orden){
		case 1:
			ctx = ordenActual.getContexto(contexto.toString());
			break;
		default:
			//Orden 0
			ctx = ordenActual.getContexto("");
			break;
		}
		//Ya tengo donde trabajar con el motorAritmetico
		
		ctx.actualizarProbabilidades();
		//FIXME: Genero una lista temporal porque no puedo castear de un HASHMAP$Values a ArrayList 
		ArrayList<ParCharProb> temp = new ArrayList<ParCharProb>(ctx.getArrayCharProb());
		
		Character letra = this.motorAritmetico.descomprimir(temp, datos);
		
		ctx.actualizarContexto(letra);
		
		if (this.orden > 0) {
			//Si es de orden mayor a 0 entonces actualizo el contexto
			this.contexto = letra;
		}
		
		return letra.toString();
	}
	
	
	
	public String StringCompleto(StringBuffer datos){
		
		
		String salida =new String();
		String intermedio = new String();
		boolean llegoEOF = false;
		
		while (salida != null && !llegoEOF){
			
			try {
				intermedio = this.descomprimir(datos);
				if (intermedio != null)	{
					salida += intermedio;
					llegoEOF = Constantes.EOF.equals(intermedio);
				}
			} catch (SessionException e) {
				e.printStackTrace();
			}
		}
		
		return salida;
		
	}
		
		
		
		
		
		

	//Al finalizar la compresion me manda el EOF o simplemente lo mando yo
	//internamente?? Es que en realidad es un caracter mas. lo unico que tendría que
	//hacer aca es finalizar la compresion devolviendo el piso ultimo
	private String finalizarCompresion(){
		String datos = "";
		try {
			datos = this.comprimir(Constantes.EOF.toString());
		} catch (SessionException e) {
			//Inicio la sesion
			this.iniciarSesion();
			
			try {
				datos = this.comprimir(Constantes.EOF.toString());
			} catch (SessionException i) {
				//FIXME:Tiro la excepcion para afuera? 
				//No se pudo iniciar la session
				i.printStackTrace();
				return null;
			}
		}
		return (datos + this.motorAritmetico.finalizarCompresion());
	}
	
	@Override
	public String finalizarSession() {
		String datos = new String();
		
		if (this.sessionInit) {
			if (this.sessionCompresion) {
				datos = this.finalizarCompresion();
			} 			
			this.sessionInit = false;
		}
		if (datos.length() > 0) {
			return datos;
		}
		return "";
	}

	@Override
	public void imprimirHashMap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iniciarSesion() {
		if (!this.sessionInit) {
			this.sessionInit = true;	
		}
		// TODO Auto-generated method stub
		this.listaOrdenes = new ArrayList<Orden>(0);
		Orden ordenContexto;
		//Al final solo tengo un contexto o vacio o una letra
		ordenContexto = new Orden();
		this.listaOrdenes.add(ordenContexto);
		this.inicializarContexto();
		this.contexto = new Character('\b');
		this.motorAritmetico = new LogicaAritmetica();
		this.bits = new String();
	}

	private void cargarCtxConUnicodeBlock(Contexto ctx){
		//for (int i = 0; i < 300; i++) {
		
		Iterator<Character> it = Constantes.LISTA_CHARSET_LATIN.iterator();
		Character letra;
		while (it.hasNext()) {
			letra = it.next();
			ctx.crearCharEnContexto(letra);
			//System.out.println(letra);
		}
		/*
		for (int i = 0; i < 300; i++) {
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
				ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
				//System.out.println(new Character(Character.toChars(i)[0]));
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
					ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
					//System.out.println(new Character(Character.toChars(i)[0]));
				}
			}
		}
		*/
		ctx.crearCharEnContexto(Constantes.EOF);
	}
	
	private void cargarCtxConUnicodeCompleto(Contexto ctx){
		for (int i = 0; i < 65534; i++) {
			ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
		}
		ctx.crearCharEnContexto(Constantes.EOF);
	}
	
	/**
	 * Verifico que el 
	 * @param contextoString
	 * @return
	 */
	private Contexto verificarCtx(final String contextoString){
		Contexto ctx = null;
		if (!this.listaOrdenes.get(0).existeContexto(contextoString)) {
			this.listaOrdenes.get(0).crearContexto(contextoString);
			ctx = this.listaOrdenes.get(0).getContexto(contextoString);
		} else {
			ctx = this.listaOrdenes.get(0).getContexto(contextoString);
		}
		return ctx;
	}
	
	private void inicializarContexto() {
		// TODO Auto-generated method stub
		Contexto ctx = null;
		if (this.charset) {
			switch(this.orden){
			case 0:
				
				//Cargo todo el orden con contexto ""
				ctx = verificarCtx("");
				
				//Cargo el contexto
				//Crear letras dentro del contexto del charset!
				this.cargarCtxConUnicodeBlock(ctx);
				break;
			case 1:
				//bucle Crear contextos dentro del charset!
				//bucle crear letras dentro del contexto del charset!
				Iterator<Character> it = Constantes.LISTA_CHARSET_LATIN.iterator();
				Character letra;
				while (it.hasNext()) {
					letra = it.next();
					ctx = this.verificarCtx(letra.toString());
					this.cargarCtxConUnicodeBlock(ctx);
				}
				/*
				for (int i = 0; i < 300; i++) {
					if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
						ctx = this.verificarCtx(new Character(Character.toChars(i)[0]).toString());
						this.cargarCtxConUnicodeBlock(ctx);
					} else {
						if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
							ctx = this.verificarCtx(new Character(Character.toChars(i)[0]).toString());
							this.cargarCtxConUnicodeBlock(ctx);
						}
					}
				}
				*/
				break;
			}
			//Cargo el charset LATIN
		} else {
			//Cargo el UNICODE completo
			switch(this.orden){
			case 0:
				//Cargo todo el orden con contexto ""
				ctx = verificarCtx("");
				//Cargo el contexto
				//Crear letras dentro del UNICODE!
				this.cargarCtxConUnicodeCompleto(ctx);
				break;
			case 1:
				//bucle Crear contextos dentro del charset!
				//bucle crear letras dentro del contexto del charset!
				for (int i = 0; i < 65534; i++) {
					if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
						ctx = this.verificarCtx(new Character(Character.toChars(i)[0]).toString());
						this.cargarCtxConUnicodeCompleto(ctx);
					} else {
						if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
							ctx = this.verificarCtx(new Character(Character.toChars(i)[0]).toString());
							this.cargarCtxConUnicodeCompleto(ctx);
						}
					}
				}
				break;
			}
		}
	}

	@Override
	public boolean isFinalizada() {
		// TODO Auto-generated method stub
		return false;
	}

}
