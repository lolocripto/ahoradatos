package ar.com.datos.grupo5.archivos;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.trie.persistence.Contenedor;

public class BloqueFTRS {

	private ArrayList<RegistroFTRS> listaTerminosFrontCodeados;
	private ArrayList<CFFTRS> listaTerminosSinFrontCodear;
	
	private int numeroBloque;
	
	
	public int getNumeroBloque() {
		return numeroBloque;
	}

	public void setNumeroBloque(int numeroBloque) {
		this.numeroBloque = numeroBloque;
	}

	public BloqueFTRS(byte[] chocloBytes){
		this.setBytes(chocloBytes);
	}
	
	public BloqueFTRS(){
		this.listaTerminosFrontCodeados = new ArrayList<RegistroFTRS>();
		this.listaTerminosSinFrontCodear = new ArrayList<CFFTRS>();
	}
	
	public BloqueFTRS (ArrayList<CFFTRS> lista){
		//codifico la lista
		
		this.listaTerminosSinFrontCodear = lista;
		this.listaTerminosFrontCodeados = (ArrayList<RegistroFTRS>) CodificadorFrontCoding.codificar(lista);
		
	}
	
	public Collection<RegistroFTRS> getListaTerminosFrontCodeados() {
		return listaTerminosFrontCodeados;
	}

	public void setListaTerminosFrontCodeados(
			ArrayList<RegistroFTRS> listaTerminosFrontCodeados) {
		this.listaTerminosFrontCodeados = listaTerminosFrontCodeados;
	}
	
	
	
	public void generarListaTerminosFroncodeados(){
		this.listaTerminosFrontCodeados = (ArrayList<RegistroFTRS>) 
		CodificadorFrontCoding.codificar(this.listaTerminosSinFrontCodear);
	}
	
	
	public byte[] getBytes(){
		
		//genero los manipuladores de bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		
		//obtengo un iterador de la lista de registros
		Iterator<RegistroFTRS> it = listaTerminosFrontCodeados.iterator();
		
		//itero sobre la lista serializando los contenedores.
		while(it.hasNext()){
			try {
				
				dos.write(it.next().getContenidoEmpaquetado().serializar());
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		//genero un contenedor general que va a almacenar la coleccion de registros serializados
		//es un contenedor de contenedores
		Contenedor contGeneral = new Contenedor();
		
		//le seteo el dato
		contGeneral.setDato(bos.toByteArray());
		
		//devuelvo el contenedor serializado
		return contGeneral.serializar();
		
	}
	
	
	public void setBytes (byte[] tiraBytes){
		
		ArrayList<Contenedor> listaConts = Contenedor.rehidratarLista(tiraBytes);		
		
		Iterator<Contenedor> it = listaConts.iterator();
		
		this.listaTerminosFrontCodeados = new ArrayList<RegistroFTRS>();
		
		while (it.hasNext()){
			
			RegistroFTRS rf = new RegistroFTRS(it.next());
			listaTerminosFrontCodeados.add(rf);
		}
		this.listaTerminosSinFrontCodear = (ArrayList<CFFTRS>) CodificadorFrontCoding
		.decodificar(listaTerminosFrontCodeados);
		
	}
	
	/**
	 *  @param listaTerminosSinFrontCodear the listaTerminosSinFrontCodear to set
	 */
	public void setListaTerminosSinFrontCodear(
				ArrayList<CFFTRS> listaTerminosSinFrontCodear) {
			this.listaTerminosSinFrontCodear = listaTerminosSinFrontCodear;
		}

		/**
		 * @return the listaTerminosSinFrontCodear
		 */
		public Collection<CFFTRS> getListaTerminosSinFrontCodear() {
			return listaTerminosSinFrontCodear;
		}
	
	
	
}
