public class EPersonalConMediciones extends Exception{
	
	private static final long serialVersionUID = 1L;
	
public EPersonalConMediciones(String s) {
	super("El personal con el código: " + s + " tiene mediciones asignadas por lo que no puede ser borrado");
	}
}