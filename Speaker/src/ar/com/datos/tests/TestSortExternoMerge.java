/**
 * 
 */
package ar.com.datos.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.sortExterno.Merge;
import ar.com.datos.sortExterno.NodoRS;
import ar.com.datos.sortExterno.ReplacementSelection;

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
			
			nodo.setIdTermino(1L);
			nodo.setIdDocumento(1L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(2L);
			nodo.setIdDocumento(1L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(3L);
			nodo.setIdDocumento(2L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(1L);
			nodo.setIdDocumento(2L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(4L);
			nodo.setIdDocumento(2L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(5L);
			nodo.setIdDocumento(2L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(4L);
			nodo.setIdDocumento(3L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(5L);
			nodo.setIdDocumento(3L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(1L);
			nodo.setIdDocumento(3L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(3L);
			nodo.setIdDocumento(4L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(1L);
			nodo.setIdDocumento(4L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(4L);
			nodo.setIdDocumento(4L);
			f.write(nodo.getBytes());
			
			nodo.setIdTermino(5L);
			nodo.setIdDocumento(4L);
			f.write(nodo.getBytes());
			
			f.close();
			
			ReplacementSelection RP = new ReplacementSelection(arch);
			RP.ordenar();
			//RP.listar();
	
			ArrayList<String> LP = (ArrayList<String>) RP.getListaParticiones();
			Merge M = new Merge(LP,RP.getArch()); 
			M.ejecutarMerge();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
