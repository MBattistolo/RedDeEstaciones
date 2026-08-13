public class ECodigoMedicionNoExiste extends Exception{
	
	public ECodigoMedicionNoExiste(String s) {
		super("La medición con el código: " + s + " no existe");
	}
}