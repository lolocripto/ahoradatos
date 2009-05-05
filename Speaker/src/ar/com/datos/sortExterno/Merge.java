package ar.com.datos.sortExterno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;

import ar.com.datos.grupo5.Constantes;

/**
 * @author Led Zeppelin
 * @version 1.0
 * @created 04-May-2009 04:15:45 p.m.
 */
public class Merge {

	private String arch;
	private List<NodoParticion> listaNodoParticion;
	private List<String> listaParticiones;
	private int tam;

	public Merge(){
	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param LP
	 * @param tam
	 * @param arch
	 */
	public Merge(final List<String> LP, final int tam, final String archExt){
		this.listaParticiones = LP;
		this.tam = tam;
		int l = archExt.length();
		if (l > 0) {
			this.arch = archExt;
		} else {
			this.arch = null;
		}
	}

	public int ejecutarMerge(){
		generarCantidadesRegistrosPorArchivo();
		int cant = obtenerCantidadParticiones();
//		char* P1 = new char[100];
//		char* P2 = new char[100];
		String P1 = new String();
		String P2 = new String();
		while (cant > 1)
		{
			obtenerDosPaticionesMenores(P1,P2);
			unirDosPaticiones(P1,P2);
			System.out.print("\n");
			listar();
			cant = obtenerCantidadParticiones();
		}
		//renombro archivo original
		if (cant==1)
		{
			File file1 = new File(arch,Constantes.ABRIR_PARA_LECTURA);
			File file = new File(P1,Constantes.ABRIR_PARA_LECTURA);
			file.renameTo(file1);
			file1.delete();
		}
		return 0;
	}

	private int generarCantidadesRegistrosPorArchivo() {
		Iterator<String> it;
		long nRegistros;
		
		RandomAccessFile file;
		NodoParticion NP;
		it = this.listaParticiones.iterator();
		String nodo;
		while (it.hasNext()){
			nodo = it.next();
			//para cada particion calculo la cantidad de registros que tiene
			try {
				file = new RandomAccessFile(nodo,
						Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
				
				nRegistros = file.length() / (3 * Constantes.SIZE_OF_INT);
				file.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
			
			NP = new NodoParticion(nodo, nRegistros);
			this.listaNodoParticion.add(NP);
		}
		return 0;
	}

	public int listar() {
		/*
		list<char*>::iterator it;
		int idt,idd,fdt,i;
		long nRegistros;
		printf("Merge: \n");
		for(it=listaParticiones.begin();it!=listaParticiones.end();it++)
		{
			FILE* p = fopen((char*)(*it),"rb");
			fseek(p,0,SEEK_END);
			nRegistros = ftell(p)/(3*sizeof(int));
			fseek(p,0,SEEK_SET);
			i=0;
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
		}*/
		return 0;
	}

	private int obtenerCantidadParticiones() {
		return this.listaParticiones.size();
	}

	/**
	 * 
	 * @param P1
	 * @param P2
	 */
	private int obtenerDosPaticionesMenores(String P1, String P2) {
		/*
		Iterator<NodoParticion> it;
		int cant,salida;
		for(int i=0;i<2;i++)
		{
			//busco el menor con flag == 0
			it=listaNodoParticion.begin();
			cant=32767;
			while (it!=listaNodoParticion.end())
			{
				if ((((NodoParticion*)(*it))->getFlag()==0) && (((NodoParticion*)(*it))->getNRegistros()<=cant))
					cant=((NodoParticion*)(*it))->getNRegistros();
				it++;
			}
			it=listaNodoParticion.begin();
			salida=0;
			while(it!=listaNodoParticion.end() && salida==0)
			{
				if ((((NodoParticion*)(*it))->getNRegistros() == cant)&&(((NodoParticion*)(*it))->getFlag()==0))
				{
					if(i==0) strcpy(P1,((NodoParticion*)(*it))->getParticion()) ;
						else strcpy(P2,((NodoParticion*)(*it))->getParticion()) ;
					((NodoParticion*)(*it))->setFlag(1);
					salida=1;
				}
				it++;
			}
		}*/
		return 0;
	}

	/**
	 * 
	 * @param P1
	 * @param P2
	 */
	private int unirDosPaticiones(String P1, String P2) {
		/*
		int salida=0,i1=0,i2=0,idT,idD,fdt,idT2,idD2,fdt2,comp;
		long nR1,nR2;
		NodoRS* N1;
		NodoRS* N2;
		FILE* p1 = fopen(P1,"rb");
		FILE* p2 = fopen(P2,"rb");
		
		char* part = (char*)malloc(strlen(P1) + 2);
		strcpy(part,P1);
		int longitud = strlen(part);
		*(part + longitud) = 'P';
		*(part + longitud + 1) = '\0';
		
		FILE* p3 = fopen(part,"w+b");
		
		if(p1!=NULL && p2!=NULL && p3!=NULL)
		{
		fseek(p1,0,SEEK_END);
		nR1 = ftell(p1)/(3*sizeof(int));
		fseek(p1,0,SEEK_SET);
		fseek(p2,0,SEEK_END);
		nR2 = ftell(p2)/(3*sizeof(int));
		fseek(p2,0,SEEK_SET);

		fread(&idT,1,sizeof(int),p1);
		fread(&idD,1,sizeof(int),p1);
		fread(&fdt,1,sizeof(int),p1);
		
		fread(&idT2,1,sizeof(int),p2);
		fread(&idD2,1,sizeof(int),p2);
		fread(&fdt2,1,sizeof(int),p2);
		
		while(salida==0)
		{	
			N1 = new NodoRS(idT,idD,fdt);
			N2 = new NodoRS(idT2,idD2,fdt2);
			comp = ((NodoRS*)N1)->comparar((NodoRS*)N2);
			if(comp==-1) //N1 < N2
			{
				if(i1<nR1)
				{
				fwrite(&idT,1,sizeof(int),p3);
				fwrite(&idD,1,sizeof(int),p3);
				fwrite(&fdt,1,sizeof(int),p3);
				fread(&idT,1,sizeof(int),p1);
				fread(&idD,1,sizeof(int),p1);
				fread(&fdt,1,sizeof(int),p1);
				i1++;
				}
				else idT=32767;
			}
			else
			{
				if(i2<nR2)
				{
				fwrite(&idT2,1,sizeof(int),p3);
				fwrite(&idD2,1,sizeof(int),p3);
				fwrite(&fdt2,1,sizeof(int),p3);
				fread(&idT2,1,sizeof(int),p2);
				fread(&idD2,1,sizeof(int),p2);
				fread(&fdt2,1,sizeof(int),p2);
				i2++;
				}
				else idT2=32767;
			}
			if(feof(p1) && feof(p2)) salida =1;
			delete N1;
			delete N2;
		}
		fclose(p1);
		fclose(p2);
		fclose(p3);
		}

		//actualizo la lista de nodos particion
		FILE* p = fopen(part,"rb");
		fseek(p,0,SEEK_END);
		long nRegistros = ftell(p)/(3*sizeof(int));
		fclose(p);

		list<NodoParticion*>::iterator it;
		it=listaNodoParticion.begin();
		salida=0;
		while(it!=listaNodoParticion.end()&&salida==0)
		{
			if (strcmp(((NodoParticion*)(*it))->getParticion(),P1)==0)
			{
				((NodoParticion*)(*it))->setNRegistros(nRegistros);
				((NodoParticion*)(*it))->setFlag(0);
				salida=1;
			}
			it++;
		}
		it=listaNodoParticion.begin();
		salida=0;
		while(it!=listaNodoParticion.end()&&salida==0)
		{
			if (strcmp(((NodoParticion*)(*it))->getParticion(),P2)==0)
			{
				listaNodoParticion.erase(it);
				salida=1;
			}
			it++;
		}
		
		//actualizo los archivos
		remove(P1);
		remove(P2);
		rename(part,P1);

		//actualizo la lista de particiones
		list<char*>::iterator itt;
		itt=listaParticiones.begin();
		salida=0;
		while(itt!=listaParticiones.end()&&salida==0)
		{
			if (strcmp((char*)(*itt),P2)==0)
			{
				listaParticiones.erase(itt);
				salida=1;
			}
			itt++;
		}	
		*/
		return 0;
	}

}