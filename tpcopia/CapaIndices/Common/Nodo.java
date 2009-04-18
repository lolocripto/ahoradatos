package tpdatos.CapaIndices.Common;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: Nodo (Abstracta)
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:47:01 a.m.
 */
public class Nodo extends BloqueIndice {

	private bool bstar;
	private SetClaves* claves;
	/**
	 * unsigned int refNodo  Arbol B+ En nodos hoja, refNodo indica offset al hermano
	 * derecho; en nodos internos, indica offset al hijo izquierdo.  Arbol B* En nodos
	 * hoja, refNodo es 0; en nodos internos indica offset a hijo izquierdo.
	 */
	private unsigned short espacioLibre;
	private unsigned char nivel;
	private unsigned int posicionEnArchivo;
	private unsigned int refNodo;
	private unsigned short tamanio;

	public Nodo(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Constructores/Destructor
	 * 
	 * @param refNodo
	 * @param nivel
	 * @param clave
	 * @param tamanio
	 * @param bstar
	 */
	public Nodo(unsigned int refNodo, unsigned char nivel, Clave* clave, unsigned short tamanio, bool bstar){

	}

	/**
	 * 
	 * @param refNodo
	 * @param nivel
	 * @param tamanio
	 * @param bstar
	 */
	public Nodo(unsigned int refNodo, unsigned char nivel, unsigned short tamanio, bool bstar){

	}

	/**
	 * 
	 * @param claves
	 * @param insercion
	 */
	public abstract void actualizarEspacioLibre(SetClaves* claves, bool insercion);

	/**
	 * 
	 * @param clave
	 * @param insercion
	 */
	public abstract void actualizarEspacioLibre(Clave* clave, bool insercion);

	public abstract void actualizarEspacioLibre();

	/**
	 * 
	 * @param claveBuscada
	 */
	public Clave* buscar(Clave* claveBuscada){
		return null;
	}

	/**
	 * 
	 * @param claveBuscada
	 */
	public Clave* buscarSiguiente(Clave* claveBuscada){
		return null;
	}

	/**
	 * 
	 * @param bytesRequeridos
	 * @param clavesPropuestas
	 * @param izquierda
	 */
	public unsigned short bytesACeder(unsigned short bytesRequeridos, unsigned char& clavesPropuestas, bool izquierda){
		return null;
	}

	/**
	 * 
	 * @param clavesPropuestas
	 * @param izquierda
	 */
	public unsigned short bytesACeder(unsigned char clavesPropuestas, bool izquierda){
		return null;
	}

	/**
	 * Devuelve un conjunto con las claves a ceder
	 * 
	 * @param bytesRequeridos
	 * @param izquierda
	 */
	public SetClaves* cederBytes(unsigned short bytesRequeridos, bool izquierda){
		return null;
	}

	/**
	 * Si puede ceder devuelve un conjunto con las claves a ceder, sino devuelve NULL.
	 * 
	 * @param cantClaves
	 * @param izquierda
	 */
	public SetClaves* cederClaves(unsigned short cantClaves, bool izquierda){
		return null;
	}

	public abstract Nodo* copiar();

	/**
	 * 
	 * @param clave
	 * @param codigo
	 */
	public abstract void eliminarClave(Clave* clave, char* codigo);

	/**
	 * 
	 * @param hijo
	 * @param claveProxNodo
	 * @param claveNodoHijo
	 */
	public bool esPadre(final Nodo* hijo, Clave*& claveProxNodo, Clave* claveNodoHijo){
		return null;
	}

	/**
	 * 
	 * @param clave
	 */
	public void extraerClave(Clave* clave){

	}

	public Clave* extraerPrimeraClave(){
		return null;
	}

	public Clave* extraerUltimaClave(){
		return null;
	}

	public unsigned short getCantidadClaves(){
		return null;
	}

	public SetClaves* getClaves(){
		return null;
	}

	public unsigned getHijoIzq(){
		return null;
	}

	public unsigned getHnoDer(){
		return null;
	}

	public unsigned char getNivel(){
		return null;
	}

	public unsigned int getPosicionEnArchivo(){
		return null;
	}

	public unsigned int getRefNodo(){
		return null;
	}

	public unsigned short getTamanio(){
		return null;
	}

	public unsigned short getTamanioEnDiscoSetClaves(){
		return null;
	}

	public abstract unsigned short getTamanioEspacioClaves();

	public static unsigned char getTamanioHeader(){
		return null;
	}

	public abstract unsigned short getTamanioMinimo();

	/**
	 * 
	 * @param clave
	 * @param codigo
	 */
	public abstract void insertarClave(Clave*& clave, char* codigo);

	protected bool isBstar(){
		return null;
	}

	/**
	 * 
	 * @param nodoHno
	 * @param clavePadre
	 */
	public void merge(Nodo* nodoHno, Clave* clavePadre){

	}

	/**
	 * 
	 * @param nodoHno1
	 * @param nodoHno2
	 * @param clavePadre1
	 * @param clavePadre2
	 */
	public void merge(Nodo* nodoHno1, Nodo* nodoHno2, Clave* clavePadre1, Clave* clavePadre2){

	}

	public abstract ~Nodo();

	public unsigned short obtenerBytesRequeridos(){
		return null;
	}

	/**
	 * 
	 * @param cantClaves
	 * @param izquierda
	 */
	public unsigned short obtenerBytesSobrantes(unsigned short& cantClaves, bool izquierda){
		return null;
	}

	/**
	 * 
	 * @param izquierda
	 */
	public unsigned short obtenerBytesSobreMinimo(bool izquierda){
		return null;
	}

	public Clave* obtenerPrimeraClave(){
		return null;
	}

	public Clave* obtenerUltimaClave(){
		return null;
	}

	/**
	 * Método que carga en este nodo todos los mismos valores que posee el nodo
	 * pasado por parámetro
	 * 
	 * @param nodo
	 */
	public abstract Nodo& operator =(final Nodo& nodo);

	/**
	 * 
	 * @param nodo
	 */
	public bool operator ==(final Nodo& nodo){
		return null;
	}

	/**
	 * 
	 * @param nodoHnoDer
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public abstract bool puedePasarClaveHaciaDer(Nodo* nodoHnoDer, Nodo* nodoPadre, Clave* clavePadre);

	/**
	 * 
	 * @param nodoHnoIzq
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public abstract bool puedePasarClaveHaciaIzq(Nodo* nodoHnoIzq, Nodo* nodoPadre, Clave* clavePadre);

	/**
	 * 
	 * @param bytesEntrantes
	 * @param bytesSalientes
	 */
	public bool puedeRecibir(unsigned short bytesEntrantes, unsigned short bytesSalientes){
		return null;
	}

	/**
	 * 
	 * @param nodoHnoDer
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public bool puedeRecibirClaveDesdeDer(Nodo* nodoHnoDer, Nodo* nodoPadre, Clave* clavePadre){
		return null;
	}

	/**
	 * 
	 * @param nodoHnoIzq
	 * @param nodoPadre
	 * @param clavePadre
	 */
	public bool puedeRecibirClaveDesdeIzq(Nodo* nodoHnoIzq, Nodo* nodoPadre, Clave* clavePadre){
		return null;
	}

	/**
	 * 
	 * @param set
	 */
	public void recibir(SetClaves* set){

	}

	/**
	 * 
	 * @param claveVieja
	 * @param claveNueva
	 * @param codigo
	 */
	public bool reemplazarClave(Clave* claveVieja, Clave* claveNueva, char* codigo){
		return null;
	}

	/**
	 * 
	 * @param bstar
	 */
	protected void setBstar(bool bstar){

	}

	/**
	 * 
	 * @param set
	 */
	public void setClaves(SetClaves* set){

	}

	/**
	 * 
	 * @param hijoIzq
	 */
	public void setHijoIzq(unsigned hijoIzq){

	}

	/**
	 * 
	 * @param hnoDer
	 */
	public void setHnoDer(unsigned hnoDer){

	}

	/**
	 * 
	 * @param nivel
	 */
	public void setNivel(unsigned char nivel){

	}

	/**
	 * 
	 * @param posicion
	 */
	public void setPosicionEnArchivo(unsigned int posicion){

	}

	/**
	 * 
	 * @param refNodo
	 */
	public void setRefNodo(unsigned int refNodo){

	}

	/**
	 * 
	 * @param tamanio
	 */
	public void setTamanio(unsigned short tamanio){

	}

	/**
	 * 
	 * @param minClaves
	 */
	public SetClaves* splitB(unsigned short minClaves){
		return null;
	}

	public bool tieneOverflow(){
		return null;
	}

	public bool tieneUnderflow(){
		return null;
	}

	public bool vacio(){
		return null;
	}

}