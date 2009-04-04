package ar.com.datos.capturaaudio.exception;

public class ForgotInitSimpleAudioRecorderException extends SimpleAudioRecorderException {
	
	public ForgotInitSimpleAudioRecorderException(){
		super("ERROR- Forgot to call SimpleAudioRecorder->init()");
	}
}
