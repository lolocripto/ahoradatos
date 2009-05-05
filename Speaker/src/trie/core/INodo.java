package trie.core;


public interface INodo {

	
	public Long getNumeroNodo();
   	
		public String getContenido();
		
		public PunteroSonido getPuntero();
		
		public void setContenido(String nuevoContenido);
		
		public void setPuntero (PunteroSonido nuevoPuntero);
		
		public INodo getHijo(String letraOpalabra);
		
		public INodo getHijo (char letra);
		
		public void setNuevoHijo(INodo nuevoNodo);
		
		public boolean esHoja();

	}

