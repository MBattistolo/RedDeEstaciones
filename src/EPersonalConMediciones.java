public class EPersonalConMediciones extends Exception{
	
public EPersonalConMediciones(String s) {
	super("El personal con el código: " + s + " tiene mediciones asignadas por lo que no puede ser borrado");
}
}
