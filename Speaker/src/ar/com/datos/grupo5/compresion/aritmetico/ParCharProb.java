package ar.com.datos.grupo5.compresion.aritmetico;

public class ParCharProb implements Comparable<ParCharProb>{

	
	private char simboloUnicode;
	private double probabilidad;
	private long techo;
	private long piso;
	private long frecuencia;
	

	public ParCharProb(char simboloUnicode, double probabilidad) {
		super();
		this.simboloUnicode = simboloUnicode;
		this.probabilidad = probabilidad;
	}
	
	public char getSimboloUnicode() {
		return simboloUnicode;
	}


	public void setSimboloUnicode(char simboloUnicode) {
		this.simboloUnicode = simboloUnicode;
	}


	public double getProbabilidad() {
		return probabilidad;
	}


	public void setProbabilidad(double probabilidad) {
		this.probabilidad = probabilidad;
	}


	public long getTecho() {
		return techo;
	}


	public void setTecho(long longTotalIntervalo) {
		//FIXME:Creo que no es sumarle el intervalo al piso directamente porque le estoy sumando uno a toda la suma total
		//Supongo que despues de sacar el techo se aumenta en uno al piso para no agregar unidades de más. Será así?
       double sky = piso + Math.ceil(longTotalIntervalo*this.probabilidad) - 1;
       //double sky = Math.floor(piso + longTotalIntervalo*this.probabilidad);
	   this.techo = new Double(sky).longValue(); 
		   
	}


	public void setTecho(long piso, long longitudInterv) {
		this.piso = piso;
		this.setTecho(longitudInterv);
		}
	
	public void setTecho(UnsignedInt piso, UnsignedInt longitudInterv) {
		this.piso = piso.getLongAsociado();
		
		//Seteo el techo
		double valorRedondeadoArriba = longitudInterv.porFloat(this.probabilidad).getLongAsociado();
		this.techo = piso.mas(new UnsignedInt(new Double(valorRedondeadoArriba).longValue())).getLongAsociado();
		//this.techo = (piso.mas(longitudInterv)).porFloat(this.probabilidad).getLongAsociado();
		}
	
	public long getPiso() {
		return piso;
	}


	public void setPiso(int piso) {
		this.piso = piso;
	}
	
	public int compareTo(ParCharProb obj) {
		
		if (this.simboloUnicode > obj.simboloUnicode)return 1;
		else{
			if (this.simboloUnicode < obj.simboloUnicode)return -1;
			else return 0;
		}
	}

	
	public boolean equals(ParCharProb obj) {
		
		if (this.simboloUnicode == obj.simboloUnicode) return true;
		return false;
	}

	@Override
	public boolean equals(Object o) {
		ParCharProb obj = (ParCharProb) o;
		if (this.simboloUnicode == obj.simboloUnicode) return true;
		return false;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(long frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the frecuencia
	 */
	public long getFrecuencia() {
		return frecuencia;
	}
}
