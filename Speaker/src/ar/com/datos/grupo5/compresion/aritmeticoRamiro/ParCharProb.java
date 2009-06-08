package ar.com.datos.grupo5.compresion.aritmeticoRamiro;

public class ParCharProb implements Comparable<ParCharProb>{

	
	private char simboloUnicode;
	private float probabilidad;
	private long techo;
	private long piso;
	private long frecuencia;
	

	public ParCharProb(char simboloUnicode, float probabilidad) {
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


	public float getProbabilidad() {
		return probabilidad;
	}


	public void setProbabilidad(float probabilidad) {
		this.probabilidad = probabilidad;
	}


	public long getTecho() {
		return techo;
	}


	public void setTecho(long longTotalIntervalo) {
		//FIXME:Creo que no es sumarle el intervalo al piso directamente porque le estoy sumando uno a toda la suma total
		//Supongo que despues de sacar el techo se aumenta en uno al piso para no agregar unidades de más. Será así?
       double sky = Math.floor(piso + longTotalIntervalo*this.probabilidad);
	   this.techo = (int)sky;
	}


	public void setTecho(long piso, long longitudInterv) {
		this.piso = piso;
		this.setTecho(longitudInterv);
		}
	
	public void setTecho(UnsignedInt piso, UnsignedInt longitudInterv) {
		this.piso = piso.getLongAsociado();
		
		UnsignedInt nu = piso.mas(longitudInterv);
		
		UnsignedInt nu2 = nu.porFloat(this.probabilidad);
		
		this.techo = nu2.getLongAsociado();
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
