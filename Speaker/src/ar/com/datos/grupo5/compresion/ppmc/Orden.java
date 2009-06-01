/**
 * 
 */
import java.util.HashMap;
/**
 * @author xxvkue
 *
 */
public class Orden {

	private int Numero;
	private HashMap<String,ParCharProb> listaContexto;
	
	public Orden(){
		
	}
	
	public final HashMap<String,ParCharProb> obtenerListaContextos(){
		return this.listaContexto;
	}
	
	public final void incrementarFrecuenciaChar(Character letra){
		ParCharProb par;
		if (this.listaContexto.containsKet()){
			par = this.listaContexto.get(letra);
			par.setProbabilidad(par.getProbabilidad() + 1);
		} else {
			par.setSimboloUnicode(letra);
			par.setProbabilidad(1);
		}
		
	}
}
