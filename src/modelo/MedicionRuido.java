package modelo;
import java.time.LocalDate;

public class MedicionRuido extends Medicion {
	
	private static final long serialVersionUID = 1L;
	
    private Jornada jornada;

    public MedicionRuido(String codigo, Personal tecnicoRegistro, LocalDate fecha, double valorMedido, Jornada jornada) {
        super(codigo, tecnicoRegistro, fecha, valorMedido);
        this.jornada = jornada;
    }

    @Override
    public boolean critica() {
        return valorMedido < 30 || valorMedido > 65 || jornada != Jornada.DIURNA;
    }

    public Jornada getJornada() {
        return jornada;
    }
    
    public void setJornada(Jornada jornada) {
    	this.jornada = jornada;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "\nTecnico que registro: " + tecnicoRegistro.getNombre() + " " + tecnicoRegistro.getApellido()
                + "\nFecha: " + fecha
                + "\nValor Medido: " + valorMedido
                + "\nJornada: " + jornada;
    }
}