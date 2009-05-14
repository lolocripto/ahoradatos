package ar.com.datos.tests;

import java.util.ArrayList;

import ar.com.datos.grupo5.sortExterno.Merge;

public class TestMerge {

	public static void main(String[] args) {
		
		
		
	ArrayList<String> lista = new ArrayList<String>();
	
	lista.add("/home/zeke/Escritorio/arcpru/1");
	lista.add("/home/zeke/Escritorio/arcpru/2");
	lista.add("/home/zeke/Escritorio/arcpru/3");
	lista.add("/home/zeke/Escritorio/arcpru/4");
	lista.add("/home/zeke/Escritorio/arcpru/5");
	lista.add("/home/zeke/Escritorio/arcpru/6");
	lista.add("/home/zeke/Escritorio/arcpru/7");
	lista.add("/home/zeke/Escritorio/arcpru/8");
	lista.add("/home/zeke/Escritorio/arcpru/9");
	lista.add("/home/zeke/Escritorio/arcpru/10");
	lista.add("/home/zeke/Escritorio/arcpru/11");
	lista.add("/home/zeke/Escritorio/arcpru/12");
	lista.add("/home/zeke/Escritorio/arcpru/13");
	lista.add("/home/zeke/Escritorio/arcpru/14");
	lista.add("/home/zeke/Escritorio/arcpru/15");
	lista.add("/home/zeke/Escritorio/arcpru/16");
	lista.add("/home/zeke/Escritorio/arcpru/17");
	lista.add("/home/zeke/Escritorio/arcpru/18");
	lista.add("/home/zeke/Escritorio/arcpru/19");
	lista.add("/home/zeke/Escritorio/arcpru/20");
	
	
	Merge mer = new Merge(lista,"/home/zeke/Escritorio/arcpru/final.txt");
	mer.ejecutarMerge();
	
	
	
	
	
	}
}
