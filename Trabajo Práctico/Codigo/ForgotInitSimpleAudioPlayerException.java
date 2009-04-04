package ar.com.datos.reproduccionaudio.exception;

public class ForgotInitSimpleAudioPlayerException extends SimpleAudioPlayerException{

	public ForgotInitSimpleAudioPlayerException(){
		super("ERROR- Forgot to call SimpleAudioPlayer->init()");
	}
}
