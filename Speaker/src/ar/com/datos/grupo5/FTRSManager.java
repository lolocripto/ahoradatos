package ar.com.datos.grupo5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ar.com.datos.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.btree.BStar;
import ar.com.datos.grupo5.btree.BTree;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;
import ar.com.datos.grupo5.utils.comparadorFrecuencias;
import ar.com.datos.sortExterno.Merge;
import ar.com.datos.sortExterno.NodoRS;
import ar.com.datos.sortExterno.ReplacementSelection;

public class FTRSManager {

	/**
	 * Arbol.
	 */
	private BTree arbolFTRS;
	
	/**
	 * Se encarga de administrar los terminos.
	 */
	private TerminosGlobales terminosGlobalesManager;
	
	/**
	 * Administra las listas invertidas en el archivo de bloques.
	 */
	private ListasInvertidas listasInvertidas;
	 
	/**
	 * Archivo de trabajo.
	 */
	private RandomAccessFile archivoTrabajo;

	/**
	 * Constructor.
	 * 
	 */
	public FTRSManager() {
		arbolFTRS = new BStar();
	}

	/**
	 * Busca la palabra dentro del Sistema FTRS.
	 * 
	 * @param palabra
	 *            buscada
	 * @return id del termino correspondiente a la palabra
	 */
	public final long buscarPalabra(final IunidadDeHabla palabra) {
		long id = 0;
		RegistroNodo nodo = new RegistroNodo();
		if (!palabra.isStopWord()) {
			nodo = arbolFTRS.buscar(new Clave(palabra.getTextoEscrito()));
			/* que hago con esto ? */
		}
		return id;
	}

	/**
	 * Verifica la existencia de la palabra.
	 * 
	 * @param palabra
	 * @return true si se encuentra la palabra
	 */
	public final boolean existePalabra(final IunidadDeHabla palabra) {
		boolean existe = false;
		if (!palabra.isStopWord()) {
			RegistroNodo nodo = arbolFTRS.buscar(new Clave(palabra
					.getTextoEscrito()));
			existe = (nodo != null);
		}
		return existe;
	}

	/**
	 * Agrega un termino.
	 * 
	 * @param idTermino
	 * @param termino
	 */
	public final void insertarTermino(final long idTermino, final String termino) {
		
		RegistroFTRS registroFtrs = new RegistroFTRS();
		registroFtrs.setClave(new Clave(termino));
		registroFtrs.setBloqueListaInvertida(-1L);
		registroFtrs.setIdTermino(idTermino);
		try {
			this.arbolFTRS.insertar(registroFtrs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void actualizarTermino(final Long offsetLista, final String termino) {
		//Armar el registro y llamar a actualizar del bTree
		RegistroFTRS registroFtrs = new RegistroFTRS();
		registroFtrs.setClave(new Clave(termino));
		registroFtrs.setBloqueListaInvertida(offsetLista);
		this.arbolFTRS.modificar(registroFtrs);
	}
	
	/**
	 * Resuelve la consulta rankeada.
	 * @return Lista de documentos con sus offset.
	 */
	public final ArrayList<String> consultaRankeada(final String consulta) {
		//TODO: cambiar el tipo del array, debe devolver String = nombre del documento, offset al documento.
		ArrayList<String> lista = new ArrayList<String>();
		String[] terminosConsulta = consulta.split(" ");
		ArrayList<RegistroTerminoDocumentos> listasInvertidas = new ArrayList<RegistroTerminoDocumentos>();
		int cantidadTerminos = terminosConsulta.length;
		
		//Leo las listas invertidas de los terminos de la consulta.
		RegistroTerminoDocumentos regTermDocs;
		for (int i = 0; i < cantidadTerminos; i++) {
			//Busco el termino
			RegistroFTRS registroFtrs = (RegistroFTRS) this.arbolFTRS.buscar(new Clave(terminosConsulta[i]));
			
			//Si no lo encuentra devuelve null
			if (registroFtrs == null) {
				return null;
			}
			
			regTermDocs = this.listasInvertidas.leerLista(registroFtrs.getIdTermino(), registroFtrs.getBloqueListaInvertida());
			
			//TODO: Si no encuentra el termino paso de largo, no?
			if (regTermDocs != null) {
				listasInvertidas.add(regTermDocs);
			}
			
		}
		//TODO:Llamar a Logica Vectorial, pero revisar el funcionamiento, claro 
		//a esta hora no se que es una pelota de futbol
		//Ver el valor total de documentos.
		return lista;
	}
	
	/**
	 * Se encarga de la generacion y actualizacion 
	 * de las listas invertidas.
	 */
	public final void generarListasInvertidas() {
		//Ahora tengo que realizar el replacement Selection
		ReplacementSelection remplacementP
					= new ReplacementSelection(Constantes.ARCHIVO_TRABAJO);
		
		remplacementP.ordenar();
		
		ArrayList<String> listaParticiones 
					= remplacementP.getListaParticiones();
		
		Merge mergeManager 
					= new Merge(listaParticiones, remplacementP.getArch());
		
		mergeManager.ejecutarMerge();
		
		long nRegistros = 0;
		NodoRS nodo = new NodoRS();
		byte[] dataNodo = new byte[nodo.getTamanio()];
		try {
			this.archivoTrabajo = new RandomAccessFile(
					Constantes.ARCHIVO_TRABAJO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
			
			nRegistros = this.archivoTrabajo.length() / nodo.getTamanio();
		
			Long idTermino = 0L;
			Long idDocumento = 0L;
			Long frecuencia = 0L;
			int i = 0;
			this.archivoTrabajo.read(dataNodo, 0, dataNodo.length);
			nodo.setBytes(dataNodo);
			idTermino = nodo.getIdTermino();
			idDocumento = nodo.getIdDocumento();
			
			//El documento siempre es el mismo
			while (i < nRegistros) {
				while (idTermino == nodo.getIdTermino() && idDocumento == nodo.getIdDocumento()&& i < nRegistros) {
					frecuencia++;
					i++;
					this.archivoTrabajo.read(dataNodo, 0, dataNodo.length);
					nodo = new NodoRS();
					nodo.setBytes(dataNodo);
				}
				ParFrecuenciaDocumento parFrecDoc = new ParFrecuenciaDocumento();
				parFrecDoc.setFrecuencia(frecuencia);
				parFrecDoc.setOffsetDocumento(idDocumento);
				
				//Leo el termino
				String termino = this.terminosGlobalesManager.leerTermino(idTermino);
				
				//TODO:Pido al FTRS los datos del termino. Busco el termino
				
				//TODO:Si tengo lista invertida para el termino entonces la leo
				//y la actualizo, sino la inserto. 
				if (1 == 1) {
					//TODO: Tengo lista invertida, ver como agarrar el bloque
					RegistroTerminoDocumentos regTD
						= this.listasInvertidas.leerLista(idTermino, 2);
					
					//Ahora ingreso el ParFrecuenciaDocumento al Registro.
					Collection<ParFrecuenciaDocumento> listaDatosDocumentos = regTD.getDatosDocumentos();
					listaDatosDocumentos.add(parFrecDoc);
					Collections.sort((List<ParFrecuenciaDocumento>) listaDatosDocumentos, (new comparadorFrecuencias()));
					
					//Ya esta ordenada la lista por frecuencias descendiente
					this.listasInvertidas.modificarLista(2, idTermino, listaDatosDocumentos);
					Map<Long,Long> registrosMovidos = this.listasInvertidas.getRegistrosMovidos();
					
					if (!registrosMovidos.isEmpty()) {
						//Si no esta vacio entonces tengo que actualizar el FTRS
						Iterator itr = registrosMovidos.entrySet().iterator();
						while (itr.hasNext()) {
							Map.Entry e = (Map.Entry)itr.next();
							//Map[idtermino,bloquenuevo]
							termino = this.terminosGlobalesManager.leerTermino((Long)e.getKey());
							//TODO: Actualizame el Termino "termino" con el bloque "e.getValue()"
							//TODO: Borrar Map para no repetir
						}
					}
				} else {
					//No tengo lista invertida
					ArrayList<ParFrecuenciaDocumento> listaTemp
						= new ArrayList<ParFrecuenciaDocumento>();
					listaTemp.add(parFrecDoc);
					
					this.listasInvertidas.agregar(idTermino, listaTemp);
					Long bloqueLista = this.listasInvertidas.getBloqueInsertado();
					
					//TODO: Actualizame el Termino "termino" con el bloque "bloqueLista"
					
					
				}
				
				
				RegistroTerminoDocumentos regTerminoDocumentos 
						= new RegistroTerminoDocumentos();
				
				//regTerminoDocumentos.
				
				
				
				idTermino = nodo.getIdTermino();
				idDocumento = nodo.getIdDocumento();
				frecuencia = 0L;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Se encarga de agregar el termino que no existe en el 
	 * archivo de terminos globales y en el FTRS.
	 * @param textoEscritoExt Termino a agregar.
	 */
	private Long agregaTermino(final String termino) {
		// TODO Auto-generated method stub
		//Pasar el parser
		Long idTermino
			= this.terminosGlobalesManager.agregar(termino);
		/*
		 * agregar al FTRS el termino con idTermino y con el 
		 * offset a la lista en null.
		 */
		this.insertarTermino(idTermino, termino);
		
		return idTermino;
	}
	
	/**
	 * 
	 * @param termino
	 * @param offsetDoc
	 */
	public boolean validarTermino(final String termino, final Long offsetDoc) {
		RegistroTerminoDocumentos regTerminoDocumentos;
		NodoRS idTerminoIdDocumento;
		Long idTermino = 0L;
		
		RegistroFTRS registroFtrs = (RegistroFTRS) this.arbolFTRS.buscar(new Clave(termino));
		
		/*
		 * Si es nulo entonces no existe el termino en el ftrs
		 * por lo tanto tampoco existe en el archivo de termino globales.
		 */
		if (registroFtrs == null) {
			//Se encarga de agregar el termino que no existe.
			idTermino = this.agregaTermino(termino);
		}
		
		//Escribo el idTermino junto con el offsetDoc
		idTerminoIdDocumento = new NodoRS(idTermino, offsetDoc);
		try {
			archivoTrabajo.write(idTerminoIdDocumento.getBytes(), 0, idTerminoIdDocumento.getTamanio());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public final boolean abrirArchivos() {
		
		try {
			this.terminosGlobalesManager.abrir(Constantes.ARCHIVO_TERMINOS_GLOBALES,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		try {
			this.listasInvertidas.abrir(Constantes.ARCHIVO_LISTAS_INVERTIDAS,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		//TODO: Pisar el anterior!!
		try {
			archivoTrabajo = new RandomAccessFile(Constantes.ARCHIVO_TRABAJO,Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public final boolean cerrarArchivos() {
		
		try {
			if (this.terminosGlobalesManager != null) {
				this.terminosGlobalesManager.cerrar();
			}
		} catch (Exception g) {
			return false;
		}
		

		try {
			if (this.listasInvertidas != null) {
				this.listasInvertidas.cerrar();
			}
		} catch (Exception g) {
			return false;
		}

		try {
			this.archivoTrabajo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
