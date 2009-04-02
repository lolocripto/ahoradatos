package ar.com.datos.grupo5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Esta clase es de ejemplo.
 */
public class main {

	/**
	 * El constructor lo agrego para que checkstyle no me rompa las bolas.
	 */
	public main() {
		super();
	}
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(main.class);

	/**
	 * @param args Los argumentos del programa.
	 */
	public static void main(String[] args) {
		
		
		try {
			
//			Archivo archivo = new Secuencial();
//			
//			archivo.crear("C:\\Users\\PowerData\\Desktop\\test.txt");
//			
//			RegistroDiccionario registro = new RegistroDiccionario();
//			
//			registro.setDato("hola");
//			registro.setOffset(13L);
//			archivo.insertar(registro);
//			registro.setDato("que");
//			registro.setOffset(124L);
//			archivo.insertar(registro);
//			registro.setDato("tal");
//			registro.setOffset(124L);
//			archivo.insertar(registro);
//
//			archivo.cerrar();
//
//			archivo.abrir("C:\\Users\\PowerData\\Desktop\\test.txt",
//					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
//			
//			RegistroDiccionario reg = (RegistroDiccionario) archivo.primero();
//			
//			while (reg != null) {
//				
//				logger.debug("Dato [" + reg.getDato() + "] offset ["
//						+ reg.getOffset() + "]");
//				reg = (RegistroDiccionario) archivo.siguiente();
//			}
			
//			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//			
//			AudioManager manager = new AudioManager();
//			
//			manager.grabar(byteArray);
//			
//			Thread.sleep(6000);
//			
//			manager.terminarGrabacion();
//			
//			//Este reproducir no se puede parar, reproduce todo lo que hay.
//			manager.reproducir();
			
			Archivo archivo = new Directo();
			
			//archivo.crear("/home/cristian/Desktop/audio.data");
			archivo.abrir("/home/cristian/Desktop/audio.data",
					Constantes.ABRIR_PARA_LECTURA);
				
			RegistroAudio registroAudio = new RegistroAudio();
			
//			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			AudioManager manager = new AudioManager();
//			manager.grabar(byteArray);
//			Thread.sleep(6000);
//			manager.terminarGrabacion();
//			
//			registroAudio.setDato(byteArray);
//			
//			archivo.insertar(registroAudio);
			
			Registro recuperado = archivo.leer(archivo.getOffset());
			
			ByteArrayInputStream is = new ByteArrayInputStream(recuperado
					.getBytes());
			manager.reproducir(is);
			
			Thread.sleep(10000);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
