package ar.com.datos.capturaaudio.exception;

public class NotEnoughBitsException extends Exception{

	
	public NotEnoughBitsException(String msg){
		super(msg);
	}
	
	public NotEnoughBitsException(){
		super("no habian suficientes bits en el buffer");
	}
	
	public NotEnoughBitsException(Exception e){
		super(e);
	}
	
	
}
