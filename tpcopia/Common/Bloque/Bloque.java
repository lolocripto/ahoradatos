package tpdatos.Common.Bloque;

/**
 * Clase ------------------------------------------------------------------------
 * Nombre: Bloque (Implementa manejo y control de bloques en memoria)
 * @author PowerData
 * @version 1.0
 * @created 18-Abr-2009 10:58:48 a.m.
 */
public class Bloque {

	/**
	 * Es una cadena de bytes tal cual se persiste en disco.
	 */
	private char* datos;
	/**
	 * Representa al numero de bloque dentro del archivo.
	 */
	private unsigned int numero;
	/**
	 * Es el offset al primer registro del bloque.
	 */
	private unsigned int offsetADatos;
	/**
	 * Es el offset al proximo registro, utilizado para recuperar todos los registros
	 * de un bloque.
	 */
	private unsigned int offsetToProxReg;
	/**
	 * Es el tamaño del bloque en bytes.
	 */
	private unsigned int tamanio;
	private int tipoOrganizacion;



	public void finalize() throws Throwable {

	}

	/**
	 * Crea un Bloque vacio. Sirve para levantar bloques de disco sin pisar su
	 * información.
	 */
	public Bloque(){

	}

	/**
	 * Este constructor recibe el número de bloque dentro del archivo, y el tamaño
	 * del bloque medido en bytes.
	 * 
	 * @param num
	 * @param tam
	 */
	public Bloque(unsigned int num, unsigned int tam){

	}

	/**
	 * Crea un bloque vacio. Levanta bloques de disco y setea elt tipo de organizacion
	 * del mismo, se utiliza para listar todos los registros del bloque
	 * 
	 * @param numBloque
	 * @param tamanioBloque
	 * @param tipoOrga
	 */
	public Bloque(unsigned int numBloque, unsigned int tamanioBloque, int tipoOrga){

	}

	/**
	 * Inserta un nuevo registro dentro del bloque. Retorna true si la inserción fue
	 * exitosa, o false en caso contrario No comtempla el caso de claves repetidas.
	 * 
	 * @param listaInfoReg
	 * @param registro
	 */
	public int altaRegistro(final ListaInfoRegistro* listaInfoReg, char* registro){
		return 0;
	}

	/**
	 * Da de baja dentro del bloque al registro cuya clave es clavePrimaria.
	 * 
	 * @param listaInfoReg
	 * @param clavePrimaria
	 */
	public int bajaRegistro(final ListaInfoRegistro* listaInfoReg, Clave& clavePrimaria){
		return 0;
	}

	public abstract ~Bloque();

	/**
	 * Este metodo busca un registro en el bloque segun su clavePrimaria. Si lo
	 * encuentra devuelve true y su offset dentro del bloque en "offsetReg"; y si no
	 * lo encuentra devuelve false.
	 * 
	 * @param listaInfoReg
	 * @param clavePrimaria
	 * @param offsetReg
	 */
	public bool buscarRegistro(final ListaInfoRegistro* listaInfoReg, Clave& clavePrimaria, unsigned short* offsetReg){
		return null;
	}

	/**
	 * Este método limpia el bloque poniendo la cantidad de registros en cero y
	 * actualizando el offset al espacio libre.
	 */
	public void clear(){

	}

	public unsigned short getCantidadRegistros(){
		return null;
	}

	/**
	 * Este método recibe un registro, y retorna su clave primaria.
	 * 
	 * @param listaInfoReg
	 * @param registro
	 */
	public Clave* getClavePrimaria(final ListaInfoRegistro* listaInfoReg, char* registro){
		return null;
	}

	public char* getDatos(){
		return null;
	}

	/**
	 * Retorna un registro segun la el offset al mismo y prepara este para que apunte
	 * al siguiente registro
	 */
	public char* getNextRegister(){
		return null;
	}

	public unsigned int getNroBloque(){
		return null;
	}

	public unsigned short getOffsetADatos(){
		return null;
	}

	public unsigned int getOffsetToReg(){
		return null;
	}

	/**
	 * 
	 * @param registro
	 * @param offsetCampo
	 * @param longCampo
	 */
	private char* getRegisterAtribute(string registro, int offsetCampo, int longCampo){
		return null;
	}

	/**
	 * Este método devuelve el registro que empieza en datos[offsetToRegs] y tiene
	 * longitud longReg.
	 * 
	 * @param longReg
	 * @param offsetToReg
	 */
	public char* getRegistro(int longReg, int offsetToReg){
		return null;
	}

	/**
	 * Retorna el registro que se encuentra en la posición 'numReg', comenzando desde
	 * 0. En 'tamReg' devuelve el tamaño del mismo.
	 * 
	 * @param numReg
	 * @param tamReg
	 */
	public char* getRegistroPorNro(unsigned short numReg, unsigned short& tamReg){
		return null;
	}

	public unsigned int getTamanioBloque(){
		return null;
	}

	public unsigned short getTamanioEspacioLibre(){
		return null;
	}

	/**
	 * 
	 * @param listaInfoReg
	 * @param registro
	 */
	public unsigned short getTamanioRegistroConPrefijo(final ListaInfoRegistro* listaInfoReg, char* registro){
		return null;
	}

	/**
	 * Devuelve la longitud del registro, sin incluir los bytes de longitud.
	 * 
	 * @param listaInfoReg
	 * @param registro
	 */
	public unsigned short getTamanioRegistros(final ListaInfoRegistro* listaInfoReg, char* registro){
		return null;
	}

	/**
	 * Si el tipo de organizacion es de registros de longitud fija calcula el tamanio
	 * de los mismos. Si es de longitud variable, obtiene la longitud del registro
	 * corriente
	 */
	public unsigned short getTamanioRegistros(){
		return null;
	}

	public int getTipoOrganizacion(){
		return 0;
	}

	/**
	 * 
	 * @param registro
	 * @param nuevoOffsetEspLibre
	 * @param longitudRegistro
	 */
	private void insertarRegistro(char* registro, unsigned short nuevoOffsetEspLibre, unsigned short longitudRegistro){

	}

	/**
	 * 
	 * @param listaInfoReg
	 * @param longReg
	 * @param clavePrimaria
	 * @param registro
	 */
	public int modificarRegistro(final ListaInfoRegistro* listaInfoReg, unsigned short longReg, Clave& clavePrimaria, char* registro){
		return 0;
	}

	/**
	 * Este metodo reorganiza el bloque luego de una baja. longReg incluye los bytes
	 * utilizados para la longitud del registro en caso de que el mismo sea variable.
	 * 
	 * @param offsetToReg
	 * @param longReg
	 */
	private void organizarBloque(int offsetToReg, int longReg){

	}

	public void resetOffsetToReg(){

	}

	/**
	 * 
	 * @param clave
	 * @param listaTipos
	 */
	public static char* serializarClave(Clave* clave, final ListaTipos* listaTipos){
		return null;
	}

	/**
	 * 
	 * @param data
	 */
	public void setDatos(char* data){

	}

	/**
	 * 
	 * @param num
	 */
	public void setNroBloque(unsigned int num){

	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffsetADatos(unsigned short offset){

	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffsetToReg(unsigned int offset){

	}

	/**
	 * 
	 * @param tam
	 */
	public void setTamanioBloque(unsigned int tam){

	}

	/**
	 * 
	 * @param tipo
	 */
	public void setTipoOrganizacion(int tipo){

	}

	/**
	 * Devuelve true si se puede insertar un registro de longitud "longReg". De lo
	 * contrario devuelve false.
	 * 
	 * @param longReg
	 * @param offsetEspLibre
	 */
	public bool verificarEspacioDisponible(unsigned short longReg, unsigned short offsetEspLibre){
		return null;
	}

}