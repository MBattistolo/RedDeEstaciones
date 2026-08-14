public class ECodigoNoExiste extends Exception{
	public ECodigoNoExiste(String s1, String s2) {
		super("No existe " + s1 + " con codigo " + s2);
	}
}