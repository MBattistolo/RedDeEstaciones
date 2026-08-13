public class ECodigoTipoContaminanteNoExiste extends Exception{
	
	public ECodigoTipoContaminanteNoExiste(String s) {
		super("El tipo de contaminante con código: "+ s + " no existe");
	}
}