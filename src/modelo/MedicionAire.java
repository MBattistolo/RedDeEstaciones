package modelo;
import java.time.LocalDate;

public class MedicionAire extends Medicion {
	
	private static final long serialVersionUID = 1L;
	
    private TipoContaminante tipoContaminante;

    public MedicionAire(String codigo, Personal tecnicoRegistro, LocalDate fecha, double valorMedido, TipoContaminante tipoContaminante) {
        super(codigo, tecnicoRegistro, fecha, valorMedido);
        this.tipoContaminante = tipoContaminante;
    }

    @Override
    public boolean critica() {
        return valorMedido < tipoContaminante.getValorMinimo() || valorMedido > tipoContaminante.getValorMaximo();
    }

    public TipoContaminante getTipoContaminante() {
        return tipoContaminante;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "\nTecnico que registro: " + tecnicoRegistro.getNombre() + " " + tecnicoRegistro.getApellido()
                + "\nFecha: " + fecha
                + "\nValor Medido: " + valorMedido
                + "\nTipo Contaminante: " + tipoContaminante.getNombreContaminante();
    }
}