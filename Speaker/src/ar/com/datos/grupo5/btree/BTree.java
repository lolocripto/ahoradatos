package ar.com.datos.grupo5.btree;

import ar.com.datos.grupo5.interfaces.Registro;

public interface BTree {

	Registro buscar(final Clave clave);
	
	boolean insertar(final Clave clave, final Registro registro);
	
	boolean modificar(final Clave clave, final Registro registro);
	
}
