package ar.com.datos.grupo5.compresion.aritmetico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import sun.text.normalizer.UnicodeSet;

public class LogicaAritmetica {

	
//	private ArrayList<ParCharProb> listaCaracterProb;
	private int bitsUnderflow;
	
	
	
	private ParCharProb segmentar (ArrayList<ParCharProb> contexto, char caracterAcodificar, 
			int longitudInterv, int piso){
		
		//ordeno el contexto por orden alfabetico
		Collections.sort(contexto);
		
		Iterator<ParCharProb> it = contexto.iterator();
		
		boolean encontrado = false;
		ParCharProb elemAnterior =null;
		ParCharProb elemActual =null;
		
		//inicializo el primer elemento
		if (it.hasNext()){
			elemAnterior = it.next();
			elemAnterior.setTecho(piso, longitudInterv);
			if (elemAnterior.getSimboloUnicode() == caracterAcodificar){
				return elemAnterior;
			}
		}
		
		
		while (it.hasNext()){
			//tomo el objeto actual
			elemActual = it.next();
			
			//el piso del elemento actual, sera el techo + 1 del elemento anterior
			elemActual.setTecho(elemAnterior.getTecho()+1, longitudInterv);
			
			//me fijo si es el que debo codificar para no seguir segmentando sin sentido
			if (elemActual.getSimboloUnicode() == caracterAcodificar){
				return elemActual;
			}
			
			elemAnterior = elemActual;
		}
		
		return null;
	}
	
	
}
