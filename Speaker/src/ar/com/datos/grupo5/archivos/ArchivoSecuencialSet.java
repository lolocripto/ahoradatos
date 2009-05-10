package ar.com.datos.grupo5.archivos;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.ClaveFrontCoding;
import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.parser.CodificadorFrontCoding;
import ar.com.datos.grupo5.registros.RegistroAdmSecSet;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.trie.persistence.Contenedor;

public class ArchivoSecuencialSet {

	private ArchivoBloques miArchivo;
	
	private RegistroAdmSecSet regAdm;
	


	public ArchivoSecuencialSet(){
		
		miArchivo = new ArchivoBloques(Constantes.SIZE_OF_SECUENCIAL_SET_BLOCK);
		try {
			miArchivo.abrir(Constantes.ARCHIVO_ARBOL_BSTAR_SECUENCIAL_SET, 
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			System.out.println("no se ha podido abrir el archivo de secuencialsetBstar");
			e.printStackTrace();
		}
		
		//instancio el bloque administrativo
		try {
			
			if (miArchivo.file.length() ==0){
				this.regAdm = new RegistroAdmSecSet();
			}else{
			
			byte[] aux = miArchivo.leerBloque(0);
			this.regAdm = new RegistroAdmSecSet(aux);
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	public long getProxBloqueLibre(){
		
		return this.regAdm.reservarNuevoBloque();
	}
	
	
	public void cerrar(){
		
		//guardo el registro administrativo
		try {
			this.miArchivo.escribirBloque(this.regAdm.getBytes(), 0);
			//cierro el archivo
			this.miArchivo.cerrar();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public void bloquesActualizados(ArrayList<Nodo>listaNodosActualizados, 
			String nuevaPalabra, long IdTermino, long PunteroAlistaInv){

		
		//genero un registro con la nueva palabra que voy a arreglar
		RegistroFTRS registroNuevaPalabra = new RegistroFTRS();
		//le agrego el IdTermino
		registroNuevaPalabra.setIdTermino(IdTermino);
		
		//le agrego el puntero a la lista invertida
		registroNuevaPalabra.setBloqueListaInvertida(PunteroAlistaInv);
		
		//genero una nueva clave de frontcoding, aunque va a quedar vacia. y se la asigno al nuevo registro
		ClaveFrontCoding cf = new ClaveFrontCoding();
		registroNuevaPalabra.setClave(cf);
		
		//genero un objeto CFFTRS para manejar toda la info
		CFFTRS objetoNuevo = new CFFTRS();
		objetoNuevo.setPalabraDecodificada(nuevaPalabra);
		objetoNuevo.setRegistroAsociado(registroNuevaPalabra);
		
		
		//genero un array con para contener la lista de bloques sucios
		ArrayList<Integer>listaDeBloquesSucios = new ArrayList<Integer>();
		
		
		//obtengo un iterador sobre la lista de nodos actualizados
		Iterator<Nodo> it = listaNodosActualizados.iterator();
		while (it.hasNext()){
			Nodo actual = it.next();
			
			//agrego el bloque sucio
			listaDeBloquesSucios.add(actual.getPunteroBloque());

		}
		
		//obtengo las listas de elementos decodificados
		ArrayList<CFFTRS> listaBloquesDecodificada;
		
		listaBloquesDecodificada = this.obtenerListaDesdeBloques(listaDeBloquesSucios);
		//agrego el objeto nuevo que inserte al arbol
		listaBloquesDecodificada.add(objetoNuevo);
		
		
		//con los elementos de todos los bloques obtenidos, genero una lista con los nuevos bloques a almacenar
		//y la obtengo redistribuyendo los registros traidos de disco
		ArrayList<BloqueFTRS> listaBloquesAGuardar;
		listaBloquesAGuardar = this.buscarYalmacenar(listaNodosActualizados, listaBloquesDecodificada);
		
		this.guardarListaDeBloques(listaBloquesAGuardar);
	
		}

	
	
	//desde una lista de nodos y una lista de elementos que deben distribuirse
	//genera nuevos bloques con los elementos representados por cada nodo
	private ArrayList<BloqueFTRS> buscarYalmacenar (ArrayList<Nodo> listaNodos, ArrayList<CFFTRS> listaElementosRedistribuir){
		
		
		ArrayList<BloqueFTRS> listaDevolver = new ArrayList<BloqueFTRS>();
		
		
		//pido un iterador
		Iterator<Nodo> it = listaNodos.iterator();
		
		while (it.hasNext()){
			
			//obtengo el nodo actual
			Nodo actual = it.next();
			
			Iterator<RegistroNodo> itSobreRegNodo = actual.getRegistros().iterator();

			BloqueFTRS nuevoBloque = new BloqueFTRS();
			
			nuevoBloque.setNumeroBloque(actual.getPunteroBloque());
			
			CFFTRS objetoTemporario = new CFFTRS();
			
			
			//itero sobre cada registro nodo
			while (itSobreRegNodo.hasNext()){
				
				RegistroNodo unRegistroNodo = itSobreRegNodo.next();
				
				//tomo la palabra que devo buscar en la lista de elementos aredistribuir
				String palabraParaBuscar = unRegistroNodo.getClave().getClave();
				
				//creo un objeto temporario solo para buscar
				objetoTemporario.setPalabraDecodificada(palabraParaBuscar);
				
				//obtengo la posicion del elemento buscado
				int i = listaElementosRedistribuir.indexOf(objetoTemporario);
				
				if (i != -1){
					
					CFFTRS objcf = listaElementosRedistribuir.get(i);
					//agrego el elemento encontrado a la lista
					nuevoBloque.getListaTerminosSinFrontCodear().add(objcf);
					
					//elimino el elemento de la lista
					//listaElementosRedistribuir.remove(i);
					
				}
			}
			
			nuevoBloque.generarListaTerminosFroncodeados();
			listaDevolver.add(nuevoBloque);

	}
		
		return listaDevolver;
	}
	
	
	
	private void guardarListaDeBloques(ArrayList<BloqueFTRS> listaBLoq){
		
		Iterator<BloqueFTRS> it = listaBLoq.iterator();
		
		while (it.hasNext()){
			
			BloqueFTRS actual = it.next();
			this.escribirBloque(actual, actual.getNumeroBloque());
		}
		
	}
	
	
	
	//devuelve una lista de palabras descodificadas desde varios bloques
	private ArrayList<CFFTRS> obtenerListaDesdeBloques (ArrayList<Integer> listaBloques){
		

		ArrayList<CFFTRS> listaParaDevolver = new ArrayList<CFFTRS>();
		
		//obtengo el iterador sobre la lista de bloques sucios
		Iterator<Integer> it = listaBloques.iterator();

		BloqueFTRS bloque;
		//itero sobre ella
		while(it.hasNext()){
			
			int numBloq = it.next();
			
			bloque = this.leerBloque(numBloq);
			
			bloque.setNumeroBloque(numBloq);
			
			//obtengo la lista de terminos comprimidos del bloque
			ArrayList<RegistroFTRS> listaTerminosFrontCodeados = (ArrayList<RegistroFTRS>) bloque.getListaTerminosFrontCodeados();
			
			//la descomprimo
			ArrayList<CFFTRS> listaTerminosDesFrontcodeados = (ArrayList<CFFTRS>) CodificadorFrontCoding.
			decodificar(listaTerminosFrontCodeados);
			
			//agregos todos los terminos descomprimidos en una lista de trabajo
			listaParaDevolver.addAll(listaTerminosDesFrontcodeados);
			
		}
		return listaParaDevolver;
		
	}
	
	

	public void escribirBloque (BloqueFTRS bloque, int numBloque){
		
		try {
			this.miArchivo.escribirBloqueSalteado(bloque.getBytes(), numBloque);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	public BloqueFTRS leerBloque (int numeroBloque){
		
		try {
			Contenedor cont = Contenedor.rehidratar(this.miArchivo.leerBloque(numeroBloque));
			
			BloqueFTRS bloqueAdevolver = new BloqueFTRS(cont.getDato());
			
			bloqueAdevolver.setNumeroBloque(numeroBloque);
			
			return bloqueAdevolver;
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	private ArrayList<RegistroFTRS> obtenerListaElementosBloque(int numBloque){
		
		BloqueFTRS miBloque = this.leerBloque(numBloque);
		
		return (ArrayList<RegistroFTRS>) miBloque.getListaTerminosFrontCodeados();
		
		
	}
	
	
	
	
}
