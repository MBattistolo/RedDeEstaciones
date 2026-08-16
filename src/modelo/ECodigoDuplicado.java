package modelo;
public class ECodigoDuplicado extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public ECodigoDuplicado(String s1, String s2) {
		super("Ya existe " + s1 + " con el código " + s2);
	}
}