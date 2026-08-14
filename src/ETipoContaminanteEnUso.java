
public class ETipoContaminanteEnUso extends Exception{
	public ETipoContaminanteEnUso(String s) {
		super("El tipo contaminante con código: " + s + " se encuentra en uso");
	}

}
