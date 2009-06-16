package ar.com.datos.grupo5.compresion;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import java.io.InputStreamReader;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.compresion.aritmetico.CompresorAritmetico;
import ar.com.datos.grupo5.excepciones.SessionException;

import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;

import ar.com.datos.grupo5.utils.Conversiones;
import ar.com.datos.grupo5.utils.MetodoCompresion;

public class CompresorConsola {

	public static void comprimir(MetodoCompresion met, String rutaOrigen,String rutaDestino) {

		try {

			// instancio el conversor a bytes
			conversionBitToByte conversor = new conversionBitToByte();

			// estructuras para leer archivo origen plano
			File origen = new File(rutaOrigen);
			FileInputStream fis = new FileInputStream(origen);
			InputStreamReader isr = new InputStreamReader(fis,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			BufferedReader bufferLectura = new BufferedReader(isr);

			// estructuras para escribir archivo comprimido
			File destino = new File(rutaDestino);
			FileOutputStream fos = new FileOutputStream(destino);

			// genero compresor
			Compresor comp = CompresorFactory.getCompresor(met);
			
			comp.iniciarSesion();

			// instancio todos las estructuras
			StringBuffer sb;
			byte[] datos;
			String binario;

			//debo manejar una lectura anticipada del archivo para poder agregar los saltos de linea
			//donde correspondan y no hacerlo en la ultima linea
			String lectura = bufferLectura.readLine();
			String lecturaAnticipada = bufferLectura.readLine();

			while (lectura != null) {
				
				System.out.print("#");
				
				//si la siguiente lectura es != null, osea que hay otra linea mas, agrego el salto
				if (lecturaAnticipada !=null){
				lectura += "\n"; //agrego el retorno que se come el readline
				}
				
				binario = comp.comprimir(lectura);

				conversor.setBits(binario); // seteo esos bits en el conversor
				fos.write(conversor.getBytes()); // escribo los bytes que pueda
				// al disco

				//leo otra linea anticipada y asigno la lectura anticipada a la actual
				lectura = lecturaAnticipada;
				lecturaAnticipada = bufferLectura.readLine();
			}
			
			System.out.println("#");

			// tratamiento final de la compresion

			// obtengo el string binario del compresor final
			String stringComprimido = comp.finalizarSession();

			if (stringComprimido != null) {
				// seteo los bits que obtengo
				conversor.setBits(stringComprimido);

				// guardo los ultimos bits obtenidos
				fos.write(conversor.getBytes());

			}

			// obtengo el padding
			byte[] padding = conversor.finalizarConversion();

			if (padding != null) {
				// pongo el padding final
				fos.write(padding);

			}

			isr.close();
			fis.close();

			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void descomprimir(MetodoCompresion met, String rutaOrigen, String rutaDestino) {

		try {

			// estructuras para leer archivo origen comprimido
			File origen = new File(rutaOrigen);
			RandomAccessFile raf = new RandomAccessFile(origen,
					Constantes.ABRIR_PARA_LECTURA);

			// estructuras para escribir archivo plano
			File destino = new File(rutaDestino);
			FileOutputStream fos = new FileOutputStream(destino);
			OutputStreamWriter osw = new OutputStreamWriter(fos,
					Constantes.DEFAULT_TEXT_INPUT_CHARSET);
			BufferedWriter bw = new BufferedWriter(osw);

			// genero compresor

			Compresor comp = CompresorFactory.getCompresor(met);
			comp.iniciarSesion();

			// instancio todos las estructuras
			StringBuffer sb = new StringBuffer();
			byte[] datos;
			String binario;

			while (raf.getFilePointer() < raf.length()) {
				
				System.out.print("#");

				// me fijo si puedo leer 32 bytes, para que array tenga ese
				// tamaño
				// en caso contrario tendrá solo la cantidad restante
				long restantes = (raf.length() - raf.getFilePointer());
				if (restantes > 100) {
					datos = new byte[100];
				} else {
					datos = new byte[(int) restantes];
				}
				
				raf.read(datos);

				binario = Conversiones.arrayByteToBinaryString(datos);
				sb.append(binario);
				String lineaDescom = null;

				if (met == MetodoCompresion.ARIT || met == MetodoCompresion.ARIT1) {

					lineaDescom = ((CompresorAritmetico) comp)
							.StringCompleto(sb); // descomprimo

				} else {

					lineaDescom = comp.descomprimir(sb);
				
				}
				
				bw.write(lineaDescom);
			//	sb.delete(0, sb.length());
			}
			
			System.out.println("#");
			
			raf.close();
			bw.flush();
			osw.flush();
			osw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
