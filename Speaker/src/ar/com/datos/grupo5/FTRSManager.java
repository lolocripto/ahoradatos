package ar.com.datos.grupo5;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
public class FTRSManager {
	BTree ArbolFTRS;
	public FTRSManager() {
		ArbolFTRS = new BStar();
	}
	/**
	 * Busca la palabra dentro del Sistema FTRS
	 * @param palabra buscada
	 * @return id del termino correspondiente a la palabra
	 */
	public final long buscarPalabra(final IunidadDeHabla palabra) {
		return 0;
	}
	/**
	 * Agrega un termino.
	 * @param idTermino
	 * @param termino 
	 */
	public void AgregarTermino(final long idTermino, final String termino) {
		Clave clave = new Clave(termino);
		
	}
	
	
}
