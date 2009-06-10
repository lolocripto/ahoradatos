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
	private String bits;
	private boolean charset;
	//private String bits;
	
	public CompresorAritmetico(){
		contexto = new Character('\b');
		listaOrdenes = null;
		this.motorAritmetico = new LogicaAritmetica();
	}
	
	public CompresorAritmetico(final int ordenCompresor){
		this.charset = true;
		this.orden = ordenCompresor;
	}

	public CompresorAritmetico(final int ordenCompresor, final boolean charSet){
		this.orden = ordenCompresor;
		this.charset = charSet;
	}
	
	@Override
	public String comprimir(String cadena) throws SessionException {
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
		
		
		
		//Si el contexto no existe lo creo, no debería entrar nunca aca
		if (ctx == null){
			//Creo el contexto 
			ctx = this.listaOrdenes.get(orden).crearContexto(contexto.toString());
		} else {
			//El contexto existe, verifico que exista la letra a agregar en el contexto
			if (ctx.existeChar(cadena.charAt(0))){
				//Exite la letra por lo tanto actualizo la frecuencia
				ctx.actualizarProbabilidades();
				
				//FIXME: Genero una lista temporal porque no puedo castear de un HASHMAP$Values a ArrayList 
				ArrayList<ParCharProb> temp = new ArrayList<ParCharProb>(ctx.getArrayCharProb());
				
				this.bits = this.motorAritmetico.comprimir(temp, cadena.charAt(0));
				ctx.actualizarContexto(cadena.charAt(0));
			} else {
				//Creo la letra. No debería entrar nunca.
				ctx.crearCharEnContexto(cadena.charAt(0));
			}
		}
		
		if (this.orden > 0) {
			//Si es de orden mayor a 0 entonces actualizo el contexto
			this.contexto = cadena.charAt(0);
		}

		return this.bits;
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
		//Al final solo tengo un contexto o vacio o una letra
		ordenContexto = new Orden();
		this.listaOrdenes.add(ordenContexto);
		this.inicializarContexto();
	
		this.contexto = new Character('\b');
		this.motorAritmetico = new LogicaAritmetica();
	}

	private void cargarCtxConUnicodeBlock(Contexto ctx){
		for (int i = 0; i < 65533; i++) {
			if (Character.UnicodeBlock.forName("BASIC_LATIN") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
				ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
			} else {
				if (Character.UnicodeBlock.forName("LATIN_1_SUPPLEMENT") == Character.UnicodeBlock.of(new Character(Character.toChars(i)[0]))) {
					ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
				}
			}
		}
	}
	
	private void cargarCtxConUnicodeCompleto(Contexto ctx){
		//for (int i = 0; i < 65533; i++) {
		for (int i = 0; i < 655; i++) {
			ctx.crearCharEnContexto(new Character(Character.toChars(i)[0]));
		}
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
				for (int i = 0; i < 65533; i++) {
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
				for (int i = 0; i < 65533; i++) {
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
}
