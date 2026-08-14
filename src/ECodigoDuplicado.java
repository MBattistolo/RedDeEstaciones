
public class ECodigoDuplicado extends Exception{
	public ECodigoDuplicado(String s1, String s2) {
		super("Ya existe " + s1 + " con el código " + s2);
	}

}
