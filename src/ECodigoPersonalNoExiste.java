public class ECodigoPersonalNoExiste extends Exception{
	
	public ECodigoPersonalNoExiste(String s) {
		super("El personal con el código: " + s + " no existe");
	}
}