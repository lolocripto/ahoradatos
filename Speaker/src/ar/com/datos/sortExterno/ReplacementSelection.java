package ar.com.datos.sortExterno;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;


/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:16:43 p.m.
 */
public class ReplacementSelection {

	private String arch;
	private List<NodoRS> listaNodo;
	private List<String> listaParticiones;
	private int memoria;
	private int tam;
	
	public ReplacementSelection(){
	}
	
	public void finalize() throws Throwable {
	}

	/**
	 * Define el nombre del archivo y lee la cantidad de memoria 
	 * en bytes a usar.
	 * @param archExt Nombre del archivo que tiene 
	 * los elementos a ordena.
	 */
	public ReplacementSelection(String archExt){
		this.arch = archExt;
		this.memoria = Constantes.TAMANIO_BUFFER_REPLACEMENT_SELECTION;
	}
	
	private int cuantosDisponibles(){
		/*
		list<NodoRS*>::iterator i;
		int cont=0;
		for(i=listaNodo.begin();i!=listaNodo.end();i++)
		{
			if (((NodoRS*)(*i))->getFlag() == 0) cont+=1;
		}
		return cont;
		*/
		return 0;
	}

	private int hacerDisponible(){
		/*
		list<NodoRS*>::iterator i;
		for(i=listaNodo.begin();i!=listaNodo.end();i++)
		{
			((NodoRS*)(*i))->setFlag(0);
		}
		*/
		return 0;
	}

	public int listar(){
		/*
		list<char*>::iterator it;
		int idt,idd,fdt,i,j=0;
		long nRegistros;
		printf("Replacement selection: \n");
		for(it=listaParticiones.begin();it!=listaParticiones.end();it++)
		{
			FILE* p = fopen((char*)(*it),"rb");
			fseek(p,0,SEEK_END);
			nRegistros = ftell(p)/(3*sizeof(int));
			fseek(p,0,SEEK_SET);
			i=0;
			printf("particion %d : ",j);
			while(i<nRegistros)
			{	
				fread(&idt,1,sizeof(int),p);
			 	fread(&idd,1,sizeof(int),p);
			 	fread(&fdt,1,sizeof(int),p);
				printf("%d ",idt);
				printf("%d ",idd);
				printf("%d | ",fdt);
				i++;
			}
			printf("\n");
			fclose(p);
			j++;
		}
		*/
		return 0;
	}

	/**
	 * 
	 * @return
	 * 		devuelve true si el nodo esta congelado.
	 * False si no lo esta.
	 */
	private boolean nodoRSCongelado() {
		/*
		int f = 0;
		Iterator<NodoRS> it = this.listaNodo.iterator();
		NodoRS nodo;
		while (it.hasNext()) {
			nodo = it.next();
			if (nodo.getFlag() == 10) {
				f++;	
			}
		}
		
		return (f == tam);
		*/
		return true;
	}

	private boolean NodoRSVacio(){
	//	return this.listaNodo.isEmpty();
		return true;
	}

	private NodoRS obtenerMenorNodo(){
		
		int comp;
		Iterator<NodoRS> i,j;
		NodoRS nodoI, nodoJ;
		j= this.listaNodo.iterator();
		i= this.listaNodo.iterator();
		nodoI = i.next();
		nodoJ = j.next();
		while(i.hasNext())
		{
			comp = nodoI.comparar(nodoJ);
			if (comp == -1)
				nodoJ = nodoI;
			nodoI = i.next();
		}
		return nodoJ;
	}

	public int ordenar(){
		int longitud,flag,desc;
		int idT,idD,fdt,idT2,idD2,fdt2,comp;
		long nRegistros,cont=0;
		NodoRS N;
		NodoRS N1;
		NodoRS N2;
		NodoRS menor;
		String part;
		Character chr = '1';
		File P;
		//1-calculo tamaño de lista de objetos a ordenar
		tam = M / ( 3 * Constantes.SIZE_OF_INT ); //divido por el tamaño de un nodo
		//2-abro el archivo
		/*
		File f = fopen(arch.c_str(),"rb");
		if (f != NULL)
		{
			fseek(f,0,SEEK_END);
			nRegistros = ftell(f)/(3*sizeof(int));
			fseek(f,0,SEEK_SET);
			//3-cargo por primera vez el buffer(Lista de NodoRS)
			for(int i=0;i<tam;i++)
			{
				fread(&idT,1,sizeof(int),f);
				fread(&idD,1,sizeof(int),f);
				fread(&fdt,1,sizeof(int),f);
				N = new NodoRS(idT,idD,fdt);
				this->listaNodo.insert(listaNodo.end(),N);
				cont +=1;
			}
			while (!NodoRSCongelado() && !NodoRSVacio())
			{
			//4-arranca el bucle (mientras el buffer no este vacio ni congelado)
			//4.1-creo Pi
			part = (char*)malloc(arch.size() + 7);
		    strcpy(part,arch.c_str());
			strcat(part,"part");
			longitud = strlen(part);
			*(part + longitud) = chr;
			*(part + longitud + 1) = '\0';
			P = fopen(part,"w+b");
			flag=0;
			while (!NodoRSCongelado() && !NodoRSVacio() && flag==0)
			{
			//4.2-tomo el menor del buffer con flag en disponible
			menor = obtenerMenorNodo();
			//4.3-lo grabo en Pi y lo elimino de listaNodo
			idT2 = menor->getIdTermino();
			idD2 = menor->getIdDocumento();
			fdt2 = menor->getFTD();
			fwrite(&idT2,1,sizeof(int),P);
			fwrite(&idD2,1,sizeof(int),P);
			fwrite(&fdt2,1,sizeof(int),P);
			listaNodo.remove(menor);
			//4.4-leo el siguiente del archivo f
			if (cont<nRegistros)
			{
				fread(&idT,1,sizeof(int),f);
				fread(&idD,1,sizeof(int),f);
				fread(&fdt,1,sizeof(int),f);
				cont+=1;
				//4.4.1-si reg(f) < grabado -> congelado
				N1 = new NodoRS(idT,idD,fdt);
				N2 = new NodoRS(idT2,idD2,fdt2);
				comp = ((NodoRS*)N1)->comparar((NodoRS*)N2);
				if ( comp == -1 )  
				{
					N = new NodoRS(idT,idD,fdt);
					N->setFlag(1);
					this->listaNodo.insert(listaNodo.end(),N);
				}
				//4.4.2-si reg(f) > grabado -> disponible
				if ( comp == 1 )  
				{
					N = new NodoRS(idT,idD,fdt);
					N->setFlag(0);
					this->listaNodo.insert(listaNodo.end(),N);
				}
				delete N1;
				delete N2;
			}
			else 
				{
					flag=1;
					desc = cuantosDisponibles();
					//guardo los disponibles que quedaron en la particion 
					for (int j=0;j<desc;j++)
					{
					menor = obtenerMenorNodo();
					idT2 = menor->getIdTermino();
					idD2 = menor->getIdDocumento();
					fdt2 = menor->getFTD();
					fwrite(&idT2,1,sizeof(int),P);
					fwrite(&idD2,1,sizeof(int),P);
					fwrite(&fdt2,1,sizeof(int),P);
					listaNodo.remove(menor);
					}
				}
			}
			//5-cierro Pi y registro que la guarde
			fclose(P);
			listaParticiones.insert(listaParticiones.end(),part);
			//6-pongo los flag en disponible y vuelvo a empezar
			hacerDisponible();
			chr += 1;
			}
			fclose(f);
		}
		if (part != NULL)
		{
			//free((char*)part);
			part = NULL;
		}
		*/
		return 0;
	}

	/**
	 * @return La lista
	 */
	public final List<String> getListaParticiones() {
		return this.listaParticiones;
	}
	
	/**
	 * @return La lista
	 */
	public final String getArch() {
		return this.arch;
	}
}