package ar.com.datos.capturaaudio.exception;

public class SimpleAudioRecorderException extends Exception{

	public SimpleAudioRecorderException(String msg){
		super(msg);
	}
	
	public SimpleAudioRecorderException(){
		super("SimpleAudioRecorder Exception");
	}
	
	public SimpleAudioRecorderException(Exception e){
		super(e);
	}
}
