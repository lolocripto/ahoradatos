/**
 * 
 */
package ar.com.datos.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.sortExterno.Merge;
import ar.com.datos.grupo5.sortExterno.NodoRS;
import ar.com.datos.grupo5.sortExterno.ReplacementSelection;
import ar.com.datos.grupo5.utils.Conversiones;

/**
 * @author xxvkue
 *
 */
public class TestSortExternoMerge {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String arch = "./archivoTrabajo.data";

		RandomAccessFile f;
		
		
		try {
		
			//File file = new File(arch);
			//file.delete();
			f = new RandomAccessFile(arch,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		
			NodoRS nodo = new NodoRS();
	
			/* 1Alberto 2Cesar 3Ernesto 4Bartolo 5Demian */
			/* 3 documentos */
			
			
			//Random r = new Random();
			
			//r.nextInt(100);
			
			//creo 1000 nodos con palabras: 100 palabras random diferentes posibles distribuidas uniformemente entre 10 documentos
			for (int i = 0; i < 1000; i++) {
				
				Random r = new Random();
				
				long termino = (long)r.nextInt(100);
				long doc = (long)r.nextInt(10);
				
				System.out.print("generados: " +termino + " " + doc);
				System.out.println(" ");
				
				nodo.setIdTermino(termino);
				nodo.setIdDocumento(doc);
				
				System.out.print("guardados:");
				System.out.print("termino: "+nodo.getIdTermino()+" ");
				System.out.print("documento: "+nodo.getIdDocumento()+ " ");;
				
				System.out.println(" ");
				f.write(nodo.getBytes());
				
			}
			
			f.close();
			
			
			
			ReplacementSelection RP = new ReplacementSelection(arch);
			RP.ordenar();
			//RP.listar();
	
			ArrayList<String> LP = (ArrayList<String>) RP.getListaParticiones();
			Merge M = new Merge(LP,RP.getArch()); 
			M.ejecutarMerge();
			
			
			
			
			
			
			f = new RandomAccessFile(arch,
					Constantes.ABRIR_PARA_LECTURA);
		
			NodoRS nodolectura = new NodoRS();
			
			
			byte[] aux = new byte[16];
			
			for (int i = 0; i < 1000; i++) {
				
				f.read(aux);
				
				nodolectura.setBytes(aux);
				System.out.print("termino: "+nodolectura.getIdTermino()+" ");
				System.out.print("documento: "+nodolectura.getIdDocumento()+ " ");;
				System.out.println(" ");
			}
			
			
			
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
