package ar.com.datos.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.datos.grupo5.CFFTRS;
import ar.com.datos.grupo5.archivos.ArchivoSecuencialSet;
import ar.com.datos.grupo5.archivos.BloqueFTRS;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.btree.Nodo;
import ar.com.datos.grupo5.registros.RegistroFTRS;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestSecuencialSet2 {

	
	
	public static void main (String[] args){
	
	ArchivoSecuencialSet miArchivo = new ArchivoSecuencialSet();
	
	
	
	
	Nodo Nodo1 = new Nodo();
	Nodo Nodo2 = new Nodo();
	Nodo Nodo3 = new Nodo();
	Nodo Nodo4 = new Nodo();
	
	
	Nodo1.setPunteroBloque(1);
	Nodo2.setPunteroBloque(2);
	Nodo3.setPunteroBloque(3);
	Nodo4.setPunteroBloque(4);
	
	
	//---------------------
	
	RegistroNodo reg11 = new RegistroNodo();
	RegistroNodo reg12 = new RegistroNodo();
	RegistroNodo reg13 = new RegistroNodo();
	RegistroNodo reg14 = new RegistroNodo();
	RegistroNodo reg15 = new RegistroNodo();
	
	
	RegistroNodo reg21 = new RegistroNodo();
	RegistroNodo reg22 = new RegistroNodo();
	RegistroNodo reg23 = new RegistroNodo();
	RegistroNodo reg24 = new RegistroNodo();
	RegistroNodo reg25 = new RegistroNodo();
	
	
	RegistroNodo reg31 = new RegistroNodo();
	RegistroNodo reg32 = new RegistroNodo();
	RegistroNodo reg33 = new RegistroNodo();
	RegistroNodo reg34 = new RegistroNodo();
	RegistroNodo reg35 = new RegistroNodo();
	
	
	RegistroNodo reg41 = new RegistroNodo();
	RegistroNodo reg42 = new RegistroNodo();
	RegistroNodo reg43 = new RegistroNodo();
	RegistroNodo reg44 = new RegistroNodo();
	RegistroNodo reg45 = new RegistroNodo();
	
	RegistroNodo regNuevo = new RegistroNodo();
	
//----------------------	
	
	Clave clave1 = new Clave();
	clave1.setClave("mandarina");
	
	Clave clave2 = new Clave();
	clave2.setClave("manzana");
	
	Clave clave3 = new Clave();
	clave3.setClave("mamadera");
	
	Clave clave4 = new Clave();
	clave4.setClave("mamacora");
	
	Clave clave5 = new Clave();
	clave5.setClave("corbata");
	
	Clave clave6 = new Clave();
	clave6.setClave("corbina");
	
	Clave clave7 = new Clave();
	clave7.setClave("colorada");
	
	Clave clave8 = new Clave();
	clave8.setClave("camaron");
	
	Clave clave9 = new Clave();
	clave9.setClave("lazarillo");
	
	Clave clave10 = new Clave();
	clave10.setClave("lamer");
	
	Clave clave11 = new Clave();
	clave11.setClave("lamela");
	
	Clave clave12 = new Clave();
	clave12.setClave("lampara");
	
	Clave clave13 = new Clave();
	clave13.setClave("formalidad");
	
	Clave clave14 = new Clave();
	clave14.setClave("fortachon");
	
	Clave clave15 = new Clave();
	clave15.setClave("foz");
	
	Clave clave16 = new Clave();
	clave16.setClave("fulbo");
	
	
	
	Clave claveNueva = new Clave();
	claveNueva.setClave("soylanueva");
	
//----------------------	
	
	reg11.setClave(clave1);
	reg12.setClave(clave4);
	reg13.setClave(clave8);
	reg14.setClave(clave12);
	
	reg21.setClave(clave2);
	reg22.setClave(clave5);
	reg23.setClave(clave9);
	reg24.setClave(clave13);
	
	
	reg31.setClave(clave3);
	reg32.setClave(clave6);
	reg33.setClave(clave10);
	reg34.setClave(clave14);
	
	reg41.setClave(clave7);
	reg42.setClave(clave11);
	reg43.setClave(clave15);
	reg44.setClave(clave16);
	
	regNuevo.setClave(claveNueva);
	
//-----------------
	
		Nodo1.getRegistros().add(reg11);
		Nodo1.getRegistros().add(reg12);
		Nodo1.getRegistros().add(reg13);
		Nodo1.getRegistros().add(reg14);
		
		Nodo2.getRegistros().add(reg21);
		Nodo2.getRegistros().add(reg22);
		Nodo2.getRegistros().add(reg23);
		Nodo2.getRegistros().add(reg24);
		
		Nodo3.getRegistros().add(reg31);
		Nodo3.getRegistros().add(reg32);
		Nodo3.getRegistros().add(reg33);
		Nodo3.getRegistros().add(reg34);
		
		Nodo4.getRegistros().add(reg41);
		Nodo4.getRegistros().add(reg42);
		Nodo4.getRegistros().add(reg43);
		Nodo4.getRegistros().add(reg44);
		
		Nodo3.getRegistros().add(regNuevo);
		
	
	
//-------------	
	
	ArrayList<Nodo> lista = new ArrayList<Nodo>();
	
	lista.add(Nodo1);
	lista.add(Nodo2);
	lista.add(Nodo3);
	lista.add(Nodo4);
	
	
	
//----------------
	
	
	
	for (int i = 1; i < 5; i++) {
		
		BloqueFTRS bloque = miArchivo.leerBloque(i);
		
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
	
	
	
	
	System.out.println(" hago la modificacion de bloques");
	
	
	
	miArchivo.bloquesActualizados(lista, "soylanueva", new Long(1), new Long(1));
	
	
	
	System.out.println(" termine la modificacion");
	
	
	
	
	
for (int i = 1; i < 5; i++) {
		
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
