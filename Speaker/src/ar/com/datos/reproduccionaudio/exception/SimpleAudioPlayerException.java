package ar.com.datos.reproduccionaudio.exception;

public class SimpleAudioPlayerException extends Exception{

	public SimpleAudioPlayerException(String msg){
		super(msg);
	}
	
	public SimpleAudioPlayerException(){
		super("ERROR- SimpleAudioPlayerException");
	}
	
	public SimpleAudioPlayerException(Exception e){
		super(e);
	}
}
