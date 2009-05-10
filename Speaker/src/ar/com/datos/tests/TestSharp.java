package ar.com.datos.tests;

import java.io.IOException;

import ar.com.datos.grupo5.btree.BSharp;

public class TestSharp {

	
	
	public static void main (String[] args){
		
		
		
		BSharp bonsai = new BSharp();
		
		try {
			
			bonsai.insertar("hola", new Long(34));
			bonsai.insertar("caca", new Long(34));
			bonsai.insertar("prueba", new Long(34));
			bonsai.insertar("coma", new Long(34));
			bonsai.insertar("culo", new Long(34));
			bonsai.insertar("tomate", new Long(34));
			bonsai.insertar("arbol", new Long(34));
			bonsai.insertar("jamon", new Long(34));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			bonsai.modificar("culo", 69);
			
			
			System.out.println(bonsai.buscar("culo").getBloqueListaInvertida());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		bonsai.cerrar();
		
		
		
	}
	
	
	
	
	
}
