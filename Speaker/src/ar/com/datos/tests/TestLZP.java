package ar.com.datos.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.btree.Clave;
import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.compresion.lzp.Lzp;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.grupo5.registros.RegistroNodo;

public class TestLZP {

	private static Logger LOG = Logger.getLogger(TestBinario.class);
	
	public static void main(String[] args) throws IOException, SessionException {
		
//		Compresor comp = new Lzp();
//		//String cadena = "ABCCBABC,CBCCBCCABA";
//		String cadena = "AAAAAA,AAAAAAB";
//		String[] cadena2 = cadena.split(",");
//		String result = "";
//		
//		comp.iniciarSesion();
//		((Lzp)comp).simular = true;
//		for (int i = 0; i < cadena2.length; i++) {
//			result += comp.comprimir(cadena2[i]);
//			comp.imprimirHashMap();
//		}
//		LOG.info("Cadena sin comprimir: " + cadena.replace(",", ""));
//		result += comp.finalizarSession();
//		LOG.info("Cadena comprimida: "+ result);
//		LOG.info("Aca comienza la Descompresion");
//		LOG.info("Cadena -> " + result);
//		comp.iniciarSesion();
//		StringBuffer cadenaBuffer = new StringBuffer(result);
//		cadenaBuffer.append('\uFFFF');
//		result = comp.descomprimir(cadenaBuffer);
//		LOG.info(result + comp.finalizarSession());
		
		//TEST CON ARCHIVOS
		Compresor comp = new Lzp();
		comp.iniciarSesion();
		((Lzp)comp).simular = false;
		StringBuffer resultado = new StringBuffer();

		FileInputStream fis = new FileInputStream("testLzp.txt");
		InputStreamReader isr = new InputStreamReader(fis, Constantes.CHARSET_UTF16);
		BufferedReader buffer = new BufferedReader(isr);
		
		String datos = buffer.readLine();
		int i = 0;
		while (datos != null)  {
			resultado.append(comp.comprimir(datos + "\n"));
			datos = buffer.readLine();
			LOG.info("Lei linea: " + i++);
		}
		resultado.append(comp.finalizarSession());
		//resultado.append('\uFFFF');
		LOG.info("Cadena comprimida: " + resultado);
		
		comp.iniciarSesion();
		// Descomprimir en bucle
		int start = 0;
		int largo = 40;
		//int largo = resultado.length();
		String res = resultado.substring(start, start + largo);
		String result = "";
		//result = comp.descomprimir(resultado);
		//res = null;
		StringBuffer Sbuffer = new StringBuffer();
		while (res != null) {
			Sbuffer.append(res);
			result += comp.descomprimir(Sbuffer);
			start += largo;
			if (start + largo < resultado.length()) {
				res = resultado.substring(start, start + largo);
			}
			else {
				res = resultado.substring(start);
			}
		}
		LOG.info(result + comp.finalizarSession());
		buffer.close();
		isr.close();
		fis.close();
		
					
		//TEST ARITMETICO
		
//		CompresorAritmetico compresor = new  CompresorAritmetico(0, true);
//		compresor.iniciarSesion();
//		
//		String comprimido = compresor.comprimir("ABC");
//		comprimido += compresor.comprimir("B");
//		comprimido += compresor.comprimir("C");
//		
//		comprimido += compresor.finalizarSession();
//
//
//		compresor.iniciarSesion();
//		
//		StringBuffer aux = new StringBuffer(comprimido);
//		String descomp = "";
//		while (aux.length() > 0) {
//			descomp += compresor.descomprimir(aux);
//		}
//		descomp += compresor.finalizarSession();
//		LOG.info(descomp);
	}
}
