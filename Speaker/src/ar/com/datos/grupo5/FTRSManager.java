package ar.com.datos.grupo5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.btree.BSharp;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;
import ar.com.datos.grupo5.registros.RegistroTerminoDocumentos;
import ar.com.datos.grupo5.sortExterno.Merge;
import ar.com.datos.grupo5.sortExterno.NodoRS;
import ar.com.datos.grupo5.sortExterno.ReplacementSelection;
import ar.com.datos.grupo5.utils.LogicaVectorial;
import ar.com.datos.grupo5.utils.comparadorFrecuencias;

public class FTRSManager {

	/**
	 * Arbol.
	 */
	private BSharp arbolFTRS;

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
	 * @throws Exception
	 * 
	 */
	public FTRSManager() throws Exception {
		try {
			this.arbolFTRS = new BSharp();
			this.terminosGlobalesManager = new TerminosGlobales();
			this.listasInvertidas = new ListasInvertidas();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Busca la palabra dentro del Sistema FTRS.
	 * 
	 * @param palabra
	 *            buscada
	 * @return id del termino correspondiente a la palabra
	 * @throws Exception
	 */
	public final long buscarPalabra(final IunidadDeHabla palabra)
			throws Exception {
		long id = 0;
		RegistroNodo nodo = new RegistroNodo();
		if (!palabra.isStopWord()) {
			nodo = arbolFTRS.buscar(palabra.getEquivalenteFonetico());
			/* que hago con esto ? */
		}
		return id;
	}

	/**
	 * Verifica la existencia de la palabra.
	 * 
	 * @param palabra
	 * @return true si se encuentra la palabra
	 * @throws IOException
	 *             ,
	 */
	public final boolean existePalabra(final IunidadDeHabla palabra)
			throws IOException {
		boolean existe = false;
		if (!palabra.isStopWord()) {
			RegistroNodo nodo = arbolFTRS.buscar(palabra
					.getEquivalenteFonetico());
			existe = (nodo != null);
		}
		return existe;
	}
	
	/**
	 * Resuelve la consulta rankeada.
	 * 
	 * @return Lista de documentos con sus offset.
	 * @throws IOException .
	 */
	@SuppressWarnings("unchecked")
	public final ArrayList<SimilitudDocumento> consultaRankeada(
			final String consulta, final Long cantidadDocumentos)
			throws IOException {

		ArrayList<String> lista = new ArrayList<String>();
		String[] terminosConsulta = consulta.split(" ");
		ArrayList<ParPesoGlobalTermino> pesoTerminoListas = new ArrayList<ParPesoGlobalTermino>();
		int cantidadTerminos = terminosConsulta.length;

		//this.abrirArchivos();
		
		// Leo las listas invertidas de los terminos de la consulta.
		RegistroTerminoDocumentos regTermDocs;
		ParPesoGlobalTermino pesoGlobalPorTermino;
		
		for (int i = 0; i < cantidadTerminos; i++) {
			// Busco el termino
			RegistroFTRS registroFtrs = arbolFTRS.buscar(terminosConsulta[i]);

			// Si no lo encuentra devuelve null
			if (registroFtrs == null) {
				return null;
			}

			regTermDocs = this.listasInvertidas.leerLista(registroFtrs
					.getIdTermino(), registroFtrs.getBloqueListaInvertida());

			pesoGlobalPorTermino = new ParPesoGlobalTermino();
			pesoGlobalPorTermino.setPesoGlobal(LogicaVectorial
					.calcularPesoglobal(cantidadDocumentos.intValue(),
							regTermDocs.getCantidadDocumentos()));
			pesoGlobalPorTermino.setRegTermDocs(regTermDocs);

			// FIXME: Si no encuentra el termino paso de largo, no?
			if (regTermDocs != null) {
				pesoTerminoListas.add(pesoGlobalPorTermino);
			}

		}
		
		// ordeno por pesoGlobal
		Collections.sort((List<ParPesoGlobalTermino>) pesoTerminoListas);

		// Ver que documentos se van a agarrar ( los primeros X documentos segun
		// la bibliografia)
		// Primero busco en el termino de mayor peso global
		int i = cantidadTerminos - 1;

		int cantidadDocumentosSeleccionados = 0;

		Iterator<ParFrecuenciaDocumento> it;

		SimilitudDocumento simDocs;
		ArrayList<SimilitudDocumento> listResultado = new ArrayList<SimilitudDocumento>();
		while (i >= 0
				&& cantidadDocumentosSeleccionados < Constantes.TOP_RANKING) {
			// obtengo el la lista invertida del termino de mayor peso global
			pesoGlobalPorTermino = pesoTerminoListas.get(i);

			regTermDocs = pesoGlobalPorTermino.getRegTermDocs();

			it = regTermDocs.getDatosDocumentos().iterator();
			ParFrecuenciaDocumento parFD;
			// Busco hasta que tenga X documentos, la X es el Ranking
			while (it.hasNext() && cantidadDocumentos < Constantes.TOP_RANKING) {
				parFD = it.next();
				simDocs = new SimilitudDocumento();
				//TODO: Verificar que no este repetido
				if (!repitoElemento(listResultado,parFD.getOffsetDocumento())) {
					simDocs.setDocumento(parFD.getOffsetDocumento());
					cantidadDocumentosSeleccionados++;
					listResultado.add(simDocs);
				}
			}
			i--;
		}

		Iterator<SimilitudDocumento> itResultado = listResultado.iterator();

		while (itResultado.hasNext()) {
			simDocs = itResultado.next();
			simDocs.setSimilitud(LogicaVectorial.calcularSimilitud(simDocs
					.getDocumento(), pesoTerminoListas));
		}

		Collections.sort((List<SimilitudDocumento>) listResultado);

		//this.cerrarArchivos();
		return listResultado;
	}

	/**
	 * Se encarga de la generacion y actualizacion de las listas invertidas.
	 */
	public final void generarListasInvertidas() {
		// Ahora tengo que realizar el replacement Selection
		ReplacementSelection remplacementP = new ReplacementSelection(
				Constantes.ARCHIVO_TRABAJO);

		remplacementP.ordenar();

		ArrayList<String> listaParticiones = remplacementP
				.getListaParticiones();

		Merge mergeManager = new Merge(listaParticiones, remplacementP
				.getArch());

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

			// El documento siempre es el mismo
			while (i < nRegistros) {
				while (idTermino == nodo.getIdTermino()
						&& idDocumento == nodo.getIdDocumento()
						&& i < nRegistros) {
					frecuencia++;
					i++;
					this.archivoTrabajo.read(dataNodo, 0, dataNodo.length);
					nodo = new NodoRS();
					nodo.setBytes(dataNodo);
				}
				ParFrecuenciaDocumento parFrecDoc = new ParFrecuenciaDocumento();
				parFrecDoc.setFrecuencia(frecuencia);
				parFrecDoc.setOffsetDocumento(idDocumento);

				// Leo el termino
				String termino = this.terminosGlobalesManager
						.leerTermino(idTermino);

				//Pido al FTRS los datos del termino. Busco el termino
				RegistroFTRS registro = this.arbolFTRS.buscar(termino);
				
				if (registro != null) {
					// Si tengo lista invertida para el termino entonces la leo
					// y la actualizo, sino la inserto.
					if (registro.getBloqueListaInvertida() != -1) {
						//Tengo lista invertida, ver como agarrar el bloque
						RegistroTerminoDocumentos regTD = this.listasInvertidas
								.leerLista(idTermino, registro.getBloqueListaInvertida());

						// Ahora ingreso el ParFrecuenciaDocumento al Registro.
						Collection<ParFrecuenciaDocumento> listaDatosDocumentos = regTD
								.getDatosDocumentos();
						listaDatosDocumentos.add(parFrecDoc);
						Collections
								.sort(
										(List<ParFrecuenciaDocumento>) listaDatosDocumentos,
										(new comparadorFrecuencias()));

						// Ya esta ordenada la lista por frecuencias descendiente
						this.listasInvertidas.modificarLista(
								registro.getBloqueListaInvertida().intValue(), 
								idTermino,
								listaDatosDocumentos);
						ArrayList<TerminoBloque> registrosMovidos = this.listasInvertidas
								.getRegistrosMovidos();

						if (!registrosMovidos.isEmpty()) {
							// Si no esta vacio entonces tengo que actualizar el
							// FTRS
							Iterator<TerminoBloque> itr = registrosMovidos.iterator();
							TerminoBloque tb;
							while (itr.hasNext()) {
								tb = itr.next();
								// Map[idtermino,bloquenuevo]
								termino = this.terminosGlobalesManager
										.leerTermino((Long) tb.getIdTermino());
								// Actualizame el Termino "termino" con el
								// bloque "e.getValue()"
								this.arbolFTRS.modificar(termino, ((Long) tb.getBloque()).intValue());
							}
							// Borrar Map para no repetir
							registrosMovidos.clear();
						}
					} else {
						// No tengo lista invertida
						ArrayList<ParFrecuenciaDocumento> listaTemp = new ArrayList<ParFrecuenciaDocumento>();
						listaTemp.add(parFrecDoc);

						this.listasInvertidas.agregar(idTermino, listaTemp);
						Long bloqueLista = this.listasInvertidas
								.getBloqueInsertado();

						// Actualizame el Termino "termino" con el bloque
						// "bloqueLista"
						this.arbolFTRS.modificar(termino, bloqueLista.intValue());

					}

				}
				//TODO: que pasa si no viene un registro, entonces el termino no existe
				// no deberia pasar nunca eso.
				
				//FIXME: De donde salio ESTO.
				RegistroTerminoDocumentos regTerminoDocumentos = new RegistroTerminoDocumentos();

				// regTerminoDocumentos.

				idTermino = nodo.getIdTermino();
				idDocumento = nodo.getIdDocumento();
				frecuencia = 0L;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			File file = new File(Constantes.ARCHIVO_TRABAJO);
			file.delete();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			File file = new File(Constantes.ARCHIVO_TRABAJO);
			file.delete();
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			File file = new File(Constantes.ARCHIVO_TRABAJO);
			file.delete();
			return;
		}
		
		
	}

	/**
	 * Se encarga de agregar el termino que no existe en el archivo de terminos
	 * globales y en el FTRS.
	 * 
	 * @param textoEscritoExt
	 *            Termino a agregar.
	 */
	private Long agregaTermino(final String termino) {
		// TODO Auto-generated method stub
		// Pasar el parser
		Long idTermino = this.terminosGlobalesManager.agregar(termino);
		/*
		 * agregar al FTRS el termino con idTermino y con el offset a la lista
		 * en null.
		 */
		try {
			this.arbolFTRS.insertar(termino, idTermino);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return idTermino;
	}

	/**
	 * 
	 * @param termino
	 * @param offsetDoc
	 * @throws IOException
	 */
	public boolean validarTermino(final String termino, final Long offsetDoc)
			throws IOException {
		NodoRS idTerminoIdDocumento;
		Long idTermino = 0L;
		RegistroFTRS registroFtrs = this.arbolFTRS.buscar(termino);

		/*
		 * Si es nulo entonces no existe el termino en el ftrs por lo tanto
		 * tampoco existe en el archivo de termino globales.
		 */
		if (registroFtrs == null) {
			// Se encarga de agregar el termino que no existe.
			idTermino = this.agregaTermino(termino);
			if (idTermino == null) {
				return false;
			}
		} else {
			idTermino = registroFtrs.getIdTermino();
		}

		// Escribo el idTermino junto con el offsetDoc
		idTerminoIdDocumento = new NodoRS(idTermino, offsetDoc);
		try {
			archivoTrabajo.write(idTerminoIdDocumento.getBytes(), 0,
					idTerminoIdDocumento.getTamanio());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public final boolean abrirArchivos() {

		try {
			this.terminosGlobalesManager.abrir(
					Constantes.ARCHIVO_TERMINOS_GLOBALES,
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

		// TODO: Pisar el anterior!!
		try {
			archivoTrabajo = new RandomAccessFile(Constantes.ARCHIVO_TRABAJO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA); 
			this.archivoTrabajo.setLength(0L);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
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
			File file = new File(Constantes.ARCHIVO_TRABAJO);
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		this.arbolFTRS.cerrar();
		return true;
	}
	
	public final boolean repitoElemento(final ArrayList<SimilitudDocumento> listResultado,final Long offsetDocumento) {
		
		Iterator<SimilitudDocumento> it;
		it = listResultado.iterator();
		SimilitudDocumento nodo;
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getDocumento().compareTo(offsetDocumento) == 0) {
				return true;
			}
		}
		return false;
	}
	
}
