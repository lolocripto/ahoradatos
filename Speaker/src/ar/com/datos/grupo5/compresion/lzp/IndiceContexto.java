package ar.com.datos.grupo5.compresion.lzp;
import ar.com.datos.grupo5.archivos.ArchivoBloques;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 05-Jun-2009 12:58:21 a.m.
 */
public class IndiceContexto {

	private ArchivoBloques archivoBloque;
	private short espacioOcupado;
	private ListaContexto Indice;
	private long ultimoBloque;
	public ListaContexto m_ListaContexto;

	public IndiceContexto(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param contexto
	 */
	public String buscarContexto(String contexto){
		return "";
	}

	public void persistenciaIndice(){

	}

	public String siguiente(){
		return "";
	}

	/**
	 * @param archivoBloque the archivoBloque to set
	 */
	public void setArchivoBloque(ArchivoBloques archivoBloque) {
		this.archivoBloque = archivoBloque;
	}

	/**
	 * @return the archivoBloque
	 */
	public ArchivoBloques getArchivoBloque() {
		return archivoBloque;
	}

	/**
	 * @param espacioOcupado the espacioOcupado to set
	 */
	public void setEspacioOcupado(short espacioOcupado) {
		this.espacioOcupado = espacioOcupado;
	}

	/**
	 * @return the espacioOcupado
	 */
	public short getEspacioOcupado() {
		return espacioOcupado;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(ListaContexto indice) {
		Indice = indice;
	}

	/**
	 * @return the indice
	 */
	public ListaContexto getIndice() {
		return Indice;
	}

	/**
	 * @param ultimoBloque the ultimoBloque to set
	 */
	public void setUltimoBloque(long ultimoBloque) {
		this.ultimoBloque = ultimoBloque;
	}

	/**
	 * @return the ultimoBloque
	 */
	public long getUltimoBloque() {
		return ultimoBloque;
	}

}