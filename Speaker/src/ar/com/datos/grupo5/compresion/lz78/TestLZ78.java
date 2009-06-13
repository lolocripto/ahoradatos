package ar.com.datos.grupo5.compresion.lz78;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ar.com.datos.grupo5.compresion.ConversorABytes;
import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;

public class TestLZ78 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompresorLZ78 compresorBIS = new CompresorLZ78();
		compresorBIS.iniciarSesion();

		//compresorBIS.comprimeDatos("/wed/we/wee/web/wet");
		try {

			conversionBitToByte conver = new conversionBitToByte();
			compresorBIS.iniciarSesion();
			String paracomp = "lksdalskdalk12 l1k23l1k2 3109310293 09 0 909 0909sd0a9sd09asd0 9as0d9 10923019230912389235098         asdlo ixlkjcl jklkj lkj  j el teto medina se la lastra a 4 manos /11111111111111111111111111111111111111111111\\\\\\\\\111111111111111 ";
			String enbites = compresorBIS.comprimir(paracomp);
			conver.setBits(enbites);
			byte[] cachirla = conver.getBytes();
			String out2 = new String("compresion0001.lzw");
			FileOutputStream fileOut2 = new FileOutputStream(out2);
			fileOut2.write(cachirla);
			fileOut2.close();
			compresorBIS.finalizarSession();
			String y = compresorBIS.descomprimir(enbites);
			System.out.println("comprimio? " + y.equals(paracomp));
			System.out.println("y=" + y + "|");
			System.out.println("x=" + paracomp + "|");
			System.out.println("comprimio? " + y.length() + " " + " "
					+ paracomp.length());
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
