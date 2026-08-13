public class ECodigoEstacionNoExiste extends Exception{
	
	public ECodigoEstacionNoExiste(String s) {
		super("La estación con el código: " + s + " no existe");
	}
}