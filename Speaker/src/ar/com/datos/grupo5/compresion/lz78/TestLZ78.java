package ar.com.datos.grupo5.compresion.lz78;

public class TestLZ78 {
	public static void main( String[] args ) throws ExceptionEncodedListLZ78 {
	  
	try {
		EncoderLZ78 codificador = codificador = new EncoderLZ78("ABRACADABRA");
		 codificador.runCoding();
		String cadena = codificador.getEncodedText().asString();
        String[] cadenaaux = cadena.split(";");		
		 System.out.println("cadena codificada "+cadena);
		 System.out.println("cade " + cadenaaux[0]);
		 System.out.println("index " + cadenaaux[0].charAt(0));
		 System.out.println("char " + cadenaaux[0].charAt(1));
		EncodedElementLZ78 element = new EncodedElementLZ78(0,'a');
		EncodedListLZ78 lista = new EncodedListLZ78();
		lista.addElement(0, "a");
	} catch (ExceptionEncoderLZ78 e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

}
