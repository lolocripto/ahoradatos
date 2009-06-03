package ar.com.datos.grupo5.compresion.aritmetico;

public class ParCharProb implements Comparable<ParCharProb>{

	
	private char simboloUnicode;
	private float probabilidad;
	private int techo;
	private int piso;
	

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


	public int getTecho() {
		return techo;
	}


	public void setTecho(int longTotalIntervalo) {
		
       double sky = Math.floor(piso + longTotalIntervalo*this.probabilidad);
	   this.techo = (int)sky;
	}


	public void setTecho(int piso, int longTotalIntervalo) {
		this.piso = piso;
		this.setTecho(longTotalIntervalo);
		}
	
	public int getPiso() {
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
}
