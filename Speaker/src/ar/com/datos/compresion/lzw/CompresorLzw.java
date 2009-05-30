package ar.com.datos.compresion.lzw;

import java.io.*;

public class CompresorLzw {
	final int BUFFERSIZE = 32;

	final int CHARSIZE = 8;
	
	boolean estadoInicio = false;

	short[] prefijo;

	short[] character; // caracter agregado

	short codigosUsados;

	short codigosReservados; // Codigos reservados, 0-255 para caracteres
								// ASCII

	short capacidadLzw; // Tells us how many LZW's were allocated

	long buffer;

	int bufferBits; // Cuantos bits usa el buffer

	int bitsxCodigo;

	short bufferSize; // Used for gif, and other possible lzw implentations

	short toNBuffer; // Used for gif, how much before new buffer must be
						// allocated

	short[] stringBuffer;

	/**
	 * Compresor por defecto se utiliza 16 bits
	 * 
	 */
	public CompresorLzw() {
		//Cantidad maxima de bits -1
		inicializa(15);
	}

	/**
	 * Constructor
	 * 
	 * @param bits,
	 *            cantidad de bits de la entrada
	 */
	public CompresorLzw(int bits) {
		inicializa(bits);
	}

	/**
	 * Inicializa la tabla
	 * 
	 * @param bits
	 *            cantidad de bits de la entrada
	 */
	private void inicializa(int bits) {
		prefijo = new short[1 << bits];
		character = new short[1 << bits];
		stringBuffer = new short[1 << bits];
		//uno menos debido al 0
		capacidadLzw = (short) (1 << (bits) - 1);
		codigosUsados = 0;
		codigosReservados = 255;
		buffer = 0;
		bufferBits = 0;
		this.bitsxCodigo = bits;
		bufferSize = 0; 
		toNBuffer = 0;
		estadoInicio = true;
		clearTable();
	}

	/**
	 * Vacia la tabla, utilizado para el Clearing
	 * 
	 */
	public void clearTable() {
		if (estadoInicio) {
			for (int i = 0; i < capacidadLzw + 1; i++) {
				prefijo[i] = 0;
				character[i] = -1; 
				stringBuffer[i] = 0;
			}
		}
	}

	/**
	 * Vaciado completo de la tabla
	 * 
	 * @param bits,
	 *            seteo de bits para la tabla
	 */
	public void clearTableFull(int bits) {
		if (estadoInicio) {
			clearTable();
			codigosUsados = 0;
			codigosReservados = 255;
			buffer = 0;
			bufferBits = 0;
			this.bitsxCodigo = bits;
			bufferSize = 0;
			toNBuffer = 0;
		}
	}

	/**
	 * Escribe un archivo, el codigo
	 * Soporta hasta 6 bits
	 * @param OutputStream fp, int code
	 * @return int 
	 **/ 
	private int escribeCodigo(OutputStream fp, int code) {

		if (!estadoInicio)
			return -1;
		buffer |= code << (bufferBits);
		bufferBits += bitsxCodigo;
		while (bufferBits >= CHARSIZE)// Me muevo de a 8 bits
		{
			try {
				fp.write((byte) buffer); // Escribe los primeros 8 bits
			} catch (IOException e) {
 				System.out.println("Error: " + e.getMessage());
			}
			 // Remueve los bits del buffer
			buffer >>>= CHARSIZE;
			bufferBits -= CHARSIZE;
		}
		return 0;
	}

	/**
	 *  Limpia por si quedo algo
	 */
	private void limpiaCodigo(OutputStream fp) {
		if (estadoInicio)
			if (bufferBits > 0)
				escribeCodigo(fp, 0);
	}

	/** Devuelve el codigo localizado en el archivo
	 * 
	 * @param fp
	 * @return
	 */
	private int getCodigo(InputStream fp) {
		long codigoTemp;
		if (!estadoInicio)
			return 0;
        while (bufferBits <= (BUFFERSIZE - CHARSIZE * 2))
		{ 
			try {
				if (fp.available() <= 0)
					break;
				buffer |= fp.read() << (bufferBits);
				bufferBits += CHARSIZE; 

			}

			catch (IOException e) {
				System.out.println("Error en getCodigo: " + e.getMessage());
			}
		}
		//Sacamos los bits de exceso
		codigoTemp = (buffer << (64 - bitsxCodigo)) >>> (64 - bitsxCodigo);
		buffer >>>= bitsxCodigo; // Remueve bits del buffer
		bufferBits -= bitsxCodigo;
		return (int) codigoTemp; 
	}

	/**
	 * Devuelve la string asociada a un codigo
	**/
	public int getString(int numeroCodigo) {
		int i = 0;
		if (!estadoInicio)
			return -1;
		while (numeroCodigo > codigosReservados) {
			stringBuffer[i] = character[numeroCodigo];
			numeroCodigo = prefijo[numeroCodigo];
			i++;
		}
		stringBuffer[i] = (short) numeroCodigo;
		i++;
		stringBuffer[i] = '\0'; // EOF de string
		return i;
	}

	/**Busqueda del codigo
	 * 
	 * @param stringBuffer
	 * @param caracter
	 * @return int -1 si no se encontro el codigo
	 */
	public int findCode(int stringBuffer, int caracter) {
		if (!estadoInicio)
			return -1;
		//Busco
		for (int i = stringBuffer; i <= codigosUsados
				+ codigosReservados; i++) {
			// Si encuentro el codigo
			if (stringBuffer == prefijo[i]
					&& caracter == this.character[i])
				return i;
		}
		// Si no se encuentra, devolvemos el siguiente codigo
		return codigosUsados + 1 + codigosReservados;
	}

	/**
	 * Comprime un archivo en LZW
	 * @param in, archivo de entrada
	 * @param out, archivo de salida
	 * @return 0 si no hubo error
	 */
	public int comprimirArchivo(String in, String out) {
		BufferedInputStream fileIn;
		BufferedOutputStream fileOut;

		int sigChar = 0;
		int stringBuffer = 0; 
		int  numeroCodigo = 0; 
		boolean seEscribe = false;
		if (!estadoInicio)
			return -1;
		try {
			fileIn = new BufferedInputStream(new FileInputStream(in));
			fileOut = new BufferedOutputStream(new FileOutputStream(out));
			clearTableFull(bitsxCodigo);
			stringBuffer = fileIn.read(); // Toma el primer codigo
		} catch (IOException e) {
			System.out.println("No se puede carga el archivo" + e.getMessage());
			return -1;
		}

		try {
			while ((sigChar = fileIn.read()) != -1) //seria EOF
			{
				 numeroCodigo = findCode(stringBuffer, sigChar);
				 //si esta en la tabla
				if (character[ numeroCodigo] != -1) {

					stringBuffer =  numeroCodigo;
					seEscribe = false;
				} else {
					//Se agrega a la tabla si no esta llena
					if (codigosUsados + codigosReservados < capacidadLzw - 1) {
						prefijo[ numeroCodigo] = (short) stringBuffer;
						character[ numeroCodigo] = (short) sigChar;
						codigosUsados++;
					}
					else {
						System.out.println("condicion era = "+ (codigosUsados + codigosReservados)) ;
						clearTable();
					}
						

					escribeCodigo(fileOut, stringBuffer);
					seEscribe = true;
					stringBuffer = sigChar;
				}
			}
		} catch (IOException e) {
			System.out.println("Error comprimiendo" + e.getMessage());
		}
		if (!seEscribe) {
			escribeCodigo(fileOut, stringBuffer);
		}
		escribeCodigo(fileOut, capacidadLzw); // ENDOFFILE
		limpiaCodigo(fileOut);
		// Cierro archivos
		try {
			fileIn.close();
			fileOut.close();
		} catch (IOException e) {
			System.out.println("Error: no se puedieron cerrar los archivos " + e.getMessage());
		}
		return 0;
	}

	/**
	 * Descomprime un archivo pasado por parametro 
	 * @param in, archivo comprimido en LZW
	 * @param out, archivo de salida descomprimido
	 * @return 0 si no hubo error
	 */
	public int descomprimirArchivo(String in, String out) {
		BufferedInputStream fileIn;
		BufferedOutputStream fileOut;
		int i;
		int primerCodigo = -1; 
		int sigCodigo = 0; 
		int primerChar = 0; 

		if (!estadoInicio)
			return -1;
		try {
			fileIn = new BufferedInputStream(new FileInputStream(in));
			fileOut = new BufferedOutputStream(new FileOutputStream(out));
			clearTableFull(bitsxCodigo);
		} catch (IOException e) {
			System.out.println("No se pudo cambiar el archivo " + e.getMessage());
			return -1;
		}

		clearTableFull(bitsxCodigo);
		primerCodigo = getCodigo(fileIn);
		primerChar = primerCodigo;

		try {
			fileOut.write(primerCodigo);
			while ((sigCodigo = getCodigo(fileIn)) != capacidadLzw) {

				if (sigCodigo == -1) {
					if (bufferBits == 0)
						break;
				}
				// If not in table
				if (sigCodigo > codigosUsados + codigosReservados) {
					codigosUsados++;
					prefijo[sigCodigo] = (short) primerCodigo;
					character[sigCodigo] = (short) primerChar;
					i = getString(sigCodigo);
				}
				// Si el codigo ya esta en la tabla
				// ve si se puede aregar un nuevo codigo en la tabla
				// hay que asegurarse que no se pase del tamaño maximo
				else if (codigosUsados + codigosReservados < capacidadLzw) {
					i = getString(sigCodigo);
					codigosUsados++;
					prefijo[codigosUsados + codigosReservados] = (short) primerCodigo;
					character[codigosUsados + codigosReservados] = stringBuffer[i - 1];
				} else
					i = getString(sigCodigo);

				primerChar = stringBuffer[i - 1];
				primerCodigo = sigCodigo;

				while (i > 0) {

					fileOut.write(stringBuffer[i - 1]);
					fileOut.flush();
					i--;
				}

			}
		} catch (IOException e) {
			System.out.println("Error descomprimiendo" + e.getMessage());
		}
		try {
			fileIn.close();
			fileOut.close();
		} catch (IOException e) {
			System.out.println("No se pudo cerrar los archivos: " + e.getMessage());
		}
		return 0;
	}

}
