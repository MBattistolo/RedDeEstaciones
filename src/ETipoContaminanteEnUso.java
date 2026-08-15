public class ETipoContaminanteEnUso extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public ETipoContaminanteEnUso(String s) {
		super("El tipo contaminante con código: " + s + " se encuentra en uso");
	}
}