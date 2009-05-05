/**
 * 
 */
package ar.com.datos.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.sortExterno.Merge;
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
		// TODO Auto-generated method stub
		
		String arch = "archivo.txt";

		RandomAccessFile f;
		try {
			f = new RandomAccessFile(arch,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		

		int idT;
		int idD;
		int fdt;

		/* 1Alberto 2Cesar 3Ernesto 4Bartolo 5Demian */
		/* 3 documentos */
		idT = 1; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 1; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 2; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 2; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 1; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 3; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 2; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 1; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 2; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 2; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 4; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 2; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 5; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 2; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 4; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 3; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 5; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 3; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 1; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 3; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 3; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 4; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 1; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 4; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 4; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 4; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 2; 	f.write(Conversiones.intToArrayByte(fdt));

		idT = 5; 	f.write(Conversiones.intToArrayByte(idT)); 
		idD = 4; 	f.write(Conversiones.intToArrayByte(idD));
		fdt = 1; 	f.write(Conversiones.intToArrayByte(fdt));

		f.close();
		
		ReplacementSelection RP = new ReplacementSelection(arch);
		RP.ordenar();
		RP.listar();

		List<String> LP = RP.getListaParticiones();
		Merge M = new Merge(LP,12,RP.getArch()); 
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
