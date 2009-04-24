package ar.com.datos.grupo5;

public class ParFrecuenciaDocumento {

	private Long frecuencia;
	private Long offsetDocumento;
	
	public Long getFrecuencia(){
		return this.frecuencia;
	}
	
	public Long getOffsetDocumento(){
		return this.offsetDocumento;
	}
	
	public void setFrecuencia(Long Frecuencia){
		this.frecuencia = Frecuencia;
	}

	public void setOffsetDocumento(Long OffsetDocumento){
		this.offsetDocumento = OffsetDocumento;
	}
	
	public ParFrecuenciaDocumento(){
		this.frecuencia = 0L;
		this.offsetDocumento = 0L;
	}
}
