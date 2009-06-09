package ar.com.datos.grupo5.compresion.lzw;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ar.com.datos.grupo5.compresion.conversionBitToByte;
import ar.com.datos.grupo5.excepciones.SessionException;
import ar.com.datos.grupo5.interfaces.Compresor;
import ar.com.datos.grupo5.utils.Conversiones;

public class CompresorLZWBis implements Compresor{

	private HashMap<Object, ParLZW> tablaLZW;
	private final int capacidadTabla = 65535;
	private int ultimoCodigo;
	private final short codigosReservados = 255; //para los ASCII
	private final String caracterEOF = "^EOF";
	private int estadoInicio=0;
	/**
	 * Constructor
	 *
	 */
	public CompresorLZWBis() {
		tablaLZW = new HashMap<Object,ParLZW>();
		ultimoCodigo = 0;
		estadoInicio=0;
		//limpiaTabla();
	}
	
	/**
	 * Inicializa la tabla con los primeros 255 caracteres unicode
	 *
	 */
	private void limpiaTabla(){
		int i = 0;
		for(i = 0; i<codigosReservados; i++) {
			    String valor = new Character(Character.toChars(i)[0]).toString();
	            tablaLZW.put(valor,new ParLZW(i,valor));
		}
		i++;
        tablaLZW.put(this.caracterEOF,new ParLZW(i,this.caracterEOF));
		ultimoCodigo = i ;
		
	}
	/**
	 * Devuelve el codigo asociado
	 * @param valor asociado al codigo buscado
	 * @return int -1 si n(ParLZW)o).getValor() o se encuentra el codigo
	 */
	private int buscaCodigo(String valor) {
		ParLZW par = tablaLZW.get(valor);
		if ( par == null ) {
			return -1;
		}
		return par.getClave();
	}
	
	public byte[] comprimeDatos(String cadena) throws IOException {
		limpiaTabla();
	    boolean comprimido = false;
	    String anterior = new String();
	    String textoComprimido =  new String();
	    int posicion = 0;
	    int ultimoMatcheo = 0;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		String textoComprimidoEnBytes = new String();
		while (!comprimido) {
			Character caracter = null;
			if (!(posicion == cadena.length())) 
			  caracter = new Character(cadena.charAt(posicion));
			String actual = null;
			if (anterior.isEmpty()) {
				actual = caracter.toString();
			}
			else {
				if (caracter !=null)  
				  actual = anterior+ caracter.toString();
				else
			      actual = anterior + this.caracterEOF;
			}
				
			int codigo = buscaCodigo(actual);
			if ( codigo >=0 ) {
			    //Se encontró el codigo y no es la 1ra entrada
				ultimoMatcheo = codigo;
				anterior=actual;
				}
			else {
			    	 //me quedo con el ultimo char de los anteriores
				     anterior= new Character(actual.charAt(actual.length()-1)).toString();
				    	 this.ultimoCodigo++;
				    	 this.tablaLZW.put(actual,new ParLZW(this.ultimoCodigo,actual));
				    	 //System.out.println(ultimoCodigo+"|"+actual);
						 if ( ultimoMatcheo >this.codigosReservados ) {
				    	      //Se emite el codigo en vez del char
				    		 textoComprimido+= Integer.toString(ultimoMatcheo);
				    		 textoComprimidoEnBytes += Conversiones.shortToArrayByte(ultimoMatcheo);
				    		 int segundoByte = (0x0000FF & (ultimoMatcheo));  
				    		 int primerByte = ((ultimoMatcheo) & 0x00FF00 ) >> 8;  
				    		 byte[] codigoBytes= {(byte)primerByte,(byte)segundoByte};
				    		 dos.write(codigoBytes,0,codigoBytes.length);
				    		 ultimoMatcheo = 0;
				    	 }
				    	 else {
				    		 //emite el ultimo char de la cadena
				    		 if (caracter != null ) {
				    			 textoComprimido +=actual.charAt(actual.length()-2);
				    			 textoComprimidoEnBytes +=Conversiones.charToBinaryString(actual.charAt(actual.length()-2));
				    			 byte[] codigoBytes = Character.toString(
								actual.charAt(actual.length() - 2)).getBytes(); 
				    			 dos.write(codigoBytes,0,codigoBytes.length);
				    		 }
				    		 else {
				    		     textoComprimido +=actual.charAt(0);
				    		     textoComprimidoEnBytes +=Conversiones.charToBinaryString(actual.charAt(0));
				    			 byte[] codigoBytes = Character.toString(
											actual.charAt(0)).getBytes(); 
							    			 dos.write(codigoBytes,0,codigoBytes.length);

				    		 }
					    	 ultimoMatcheo = 0;
				    	 }
			    	 
			 		
		    }
			posicion++;
			comprimido = (posicion == cadena.length()+1);
		}
		
		System.out.println(textoComprimido);
		return bos.toByteArray();
		
		}

	public String comprimir(String cadena) throws SessionException {
		if (estadoInicio == 0) 
			throw new SessionException();
		boolean comprimido = false;
	    String anterior = new String();
	    String textoComprimido =  new String();
	    int posicion = 0;
	    int ultimoMatcheo = 0;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(bos);
		String textoComprimidoEnBytes = new String();
		while (!comprimido) {
			Character caracter = null;
			if (!(posicion == cadena.length())) 
			  caracter = new Character(cadena.charAt(posicion));
			String actual = null;
			if (anterior.isEmpty()) {
				actual = caracter.toString();
			}
			else {
				if (caracter !=null)  
				  actual = anterior+ caracter.toString();
				else
			      actual = anterior + this.caracterEOF;
			}
				
			int codigo = buscaCodigo(actual);
			if ( codigo >=0 ) {
			    //Se encontró el codigo y no es la 1ra entrada
				ultimoMatcheo = codigo;
				anterior=actual;
				}
			else {
			    	 //me quedo con el ultimo char de los anteriores
				     anterior= new Character(actual.charAt(actual.length()-1)).toString();
				    	 this.ultimoCodigo++;
				    	 this.tablaLZW.put(actual,new ParLZW(this.ultimoCodigo,actual));
				    	 //System.out.println(ultimoCodigo+"|"+actual);
						 if ( ultimoMatcheo >this.codigosReservados ) {
				    	      //Se emite el codigo en vez del char
				    		 textoComprimido+= Integer.toString(ultimoMatcheo);
				    		 textoComprimidoEnBytes += Conversiones.shortToArrayByte(ultimoMatcheo);
				    		 ultimoMatcheo = 0;
				    	 }
				    	 else {
				    		 //emite el ultimo char de la cadena
				    		 if (caracter != null ) {
				    			 textoComprimido +=actual.charAt(actual.length()-2);
				    			 textoComprimidoEnBytes +=Conversiones.charToBinaryString(actual.charAt(actual.length()-2));
				    		 }
				    		 else {
				    		     textoComprimido +=actual.charAt(0);
				    		     textoComprimidoEnBytes +=Conversiones.charToBinaryString(actual.charAt(0));

				    		 }
					    	 ultimoMatcheo = 0;
				    	 }
			    	 
			 		
		    }
			posicion++;
			comprimido = (posicion == cadena.length()+1);
		}
		
		System.out.println(textoComprimido);
		return textoComprimidoEnBytes;
		
		}

	public String descomprimir(String datos) {
		// TODO Auto-generated method stub
		conversionBitToByte conversor = new conversionBitToByte();
		int posicion = 0;
		while (posicion < datos.length()) {
		  conversor.setBits(datos.substring(posicion,posicion+16));
		  byte[] shortie = conversor.getBytes();
		  System.out.println("|"+shortie[0]+"|"+shortie[1]);
		  posicion+=16;
		}
		return null;
	}
	public void finalizarSession() {
		limpiaTabla();
	    estadoInicio = 0;
		
	}

	public void iniciarSesion() {
      limpiaTabla();
      estadoInicio = 1;
		
	}

	public void imprimirHashMap() {
		// TODO Auto-generated method stub
		
	}

	public String descomprimirDatos(byte[] buffer) {
		// TODO Auto-generated method stub
		String textoDescomprimido = new String();
		if (estadoInicio == 0)
			return null;
		int posicion = 0;
		String anterior = new String();
		String codigoValor = new String();
		while (posicion < buffer.length) {
            
            String actual = null;
            ParLZW NodoActual = null;
            
			byte firstByte = (byte) (0x000000FF & (buffer[posicion]));
			if ((int) firstByte < 16) {
				//es numero
				byte secondByte = (byte) (0x000000FF & (buffer[posicion + 1]));
				posicion++;
				int codigo = (firstByte << 8 | secondByte);
				System.out.println("c>>" + codigo);
				ParLZW aux = new ParLZW(codigo,new String());
				System.out.println("crea aux");
				NodoActual= tablaLZW.get(aux);
				codigoValor = NodoActual.getValor();
				actual = codigoValor;

			} else {
				//era una letra
				char codigo = (char) buffer[posicion];
				System.out.println("c>>" + codigo);
				actual = Character.toString(codigo);
				System.out.println("actual es "+(int)codigo);
			    if (!anterior.isEmpty()) {
					    actual = anterior + actual;
					    NodoActual= (ParLZW)tablaLZW.get(new ParLZW(0,actual));
					    if (NodoActual != null ) {
					    	//Estaba en tabla
					    	
					    	
					    }
					    else {
					    	//nuevo ingreso
					    	ultimoCodigo++;
					    	tablaLZW.put(new ParLZW(ultimoCodigo,actual),new ParLZW(ultimoCodigo,actual) );
					    	if  (!codigoValor.isEmpty() ) {
					    		//era un numero el anterior.
					    		textoDescomprimido += codigoValor;
					    		anterior = codigoValor;
					    		codigoValor = new String();
					    		
					    	}
					    	else { // no era un numero el anterior.
					    		anterior= new Character(actual.charAt(actual.length()-1)).toString();
					    		textoDescomprimido+=anterior;
					    		codigoValor = new String();
					    	}
					    }
					
			    }
			    else {
			    	//era caracter inicial
			    	textoDescomprimido += actual;
			    	anterior = actual;
					codigoValor = new String();
			    }
			        	
			
			}
				
  		posicion++;

		}
		System.out.println("quedp? "+textoDescomprimido);
		return textoDescomprimido;
	}
}
