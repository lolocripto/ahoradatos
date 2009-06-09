package ar.com.datos.grupo5.compresion.lzw;

public class ParLZW {
	private int clave;

	private String valor;

	public ParLZW(int clave, String valor ) {
		this.setClave(clave);
		this.setValor(valor);
	}
	public void setClave(int clave) {
		this.clave = clave;
	}

	public int getClave() {
		return clave;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
	
	public boolean equals(Object o) {
    	System.out.println("clase= "+ o.toString());
    	System.out.println("comparar ("+this.getClave()+","+this.getValor()+")");
    	if (((ParLZW)o).getClave() == this.getClave() ) {
    		return true;
    	}
    	else {
    		return ((ParLZW)o).getValor().equals(this.getValor());
    	}
    		
    }
}
