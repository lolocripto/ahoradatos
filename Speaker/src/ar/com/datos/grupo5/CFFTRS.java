package ar.com.datos.grupo5;

import ar.com.datos.grupo5.registros.RegistroFTRS;

public class CFFTRS {

	
	private RegistroFTRS registroAsociado;
	
	private String palabraDecodificada;
	
	
	public CFFTRS() {
		
	}
	
	
	
	public String getPalabraDecodificada() {
		return palabraDecodificada;
	}



	public void setPalabraDecodificada(String palabraDecodificada) {
		this.palabraDecodificada = palabraDecodificada;
	}


	public RegistroFTRS getRegistroAsociado() {
		return registroAsociado;
	}
	
	public void setRegistroAsociado(RegistroFTRS registroAsociado) {
		this.registroAsociado = registroAsociado;
	}
	
	public String getPalabraCodificada(){
		
		return ((ClaveFrontCoding)this.registroAsociado.getClave()).getTermino();
	}
	
	
}
