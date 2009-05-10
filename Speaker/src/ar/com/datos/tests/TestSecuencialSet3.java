package ar.com.datos.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestSecuencialSet3 {

	
	public static void main (String[] args){
		
		ArchivoSecuencialSet miArchivo = new ArchivoSecuencialSet();
		
		System.out.println(miArchivo.getCantBloquesUsados());
		
		Nodo Nodo1 = new Nodo();
		Nodo Nodo2 = new Nodo();
		
		
		
		RegistroNodo reg11 = new RegistroNodo();
		RegistroNodo reg12 = new RegistroNodo();
		RegistroNodo reg13 = new RegistroNodo();
		RegistroNodo reg14 = new RegistroNodo();
		RegistroNodo reg15 = new RegistroNodo();
		
		
		Clave clave1 = new Clave();
		clave1.setClave("mandarina");
		
		Clave clave2 = new Clave();
		clave2.setClave("manzana");
		
		Clave clave3 = new Clave();
		clave3.setClave("mamadera");
		
		Clave clave4 = new Clave();
		clave4.setClave("soylanueva");
		
		Clave clave5 = new Clave();
		clave5.setClave("soymasNueva");
		
		
		reg11.setClave(clave1);
		reg12.setClave(clave2);
		reg13.setClave(clave3);
		reg14.setClave(clave4);
		reg15.setClave(clave5);
		

		
	//	Nodo1.getRegistros().add(reg15);
		Nodo1.getRegistros().add(reg11);
		
		
		Nodo2.getRegistros().add(reg13);
		Nodo2.getRegistros().add(reg12);
		Nodo2.getRegistros().add(reg14);
		
		
		Nodo1.setPunteroBloque(1);
		Nodo2.setPunteroBloque(2);
		
		
		ArrayList<Nodo> lista = new ArrayList<Nodo>();
		
		lista.add(Nodo1);
//		lista.add(Nodo2);
	
		
		System.out.println("hago la modificacion de bloques");
		
		
		miArchivo.reservarBloqueLibre();
		
		miArchivo.bloquesActualizados(lista, "mandarina", new Long(1), new Long(1));
		
	//	miArchivo.primeraInsercion( "mamadera", new Long(1), new Long(1));
		
		
		
		System.out.println(" termine la modificacion");
		

		
		
	for (int i = 1; i < 2; i++) {
			
			BloqueFTRS bloque = miArchivo.leerBloque(i);
			
			System.out.println("bloque N:"+bloque.getNumeroBloque()+" puntero al siguiente:"+bloque.getPunteroAlSiguiente());
			
			Collection<CFFTRS> listaPalabras = bloque.getListaTerminosSinFrontCodear();

			Iterator<CFFTRS> it1 = listaPalabras.iterator();
			
			System.out.println(" ");
			System.out.println("Bloque: "+ i);
			System.out.println(" ");
			
			CFFTRS reg2;
			while (it1.hasNext()) {
				 reg2 = it1.next();
				 System.out.println(reg2.getPalabraDecodificada());
			}
			System.out.println(" ");
		}
		
		miArchivo.cerrar();

		}

	}
