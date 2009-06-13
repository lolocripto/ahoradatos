package ar.com.datos.tests;

import java.io.FileNotFoundException;

import ar.com.datos.grupo5.compresion.lzp.ListaContextos;

public class TestPaginacionLZP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ListaContextos contextos = null;
		
		try {
			contextos = new ListaContextos();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		contextos.setPosicion("AB", 1);
		contextos.setPosicion("CD", 1);
		contextos.setPosicion("SD", 1);
		contextos.setPosicion("VF", 1);
		contextos.setPosicion("SW", 1);
		
		contextos.setPosicion("QW", 1);
		contextos.setPosicion("WQ", 1);
		contextos.setPosicion("RT", 1);
		contextos.setPosicion("YU", 1);
		contextos.setPosicion("RE", 1);
		
		contextos.setPosicion("UI", 1);
		contextos.setPosicion("MT", 1);
		contextos.setPosicion("BG", 1);
		
		contextos.setPosicion("QW", 2);
		contextos.setPosicion("WQ", 3);
		contextos.setPosicion("RT", 4);
		
		contextos.setPosicion("VF", 6);
		contextos.setPosicion("SW", 5);
		
		contextos.listar();

	}

}
