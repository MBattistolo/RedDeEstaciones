package modelo;
import java.time.LocalDate;
import java.io.Serializable;

public abstract class Medicion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    protected String codigo;
    protected Personal tecnicoRegistro;
    protected LocalDate fecha;
    protected double valorMedido;

    public Medicion(String codigo, Personal tecnicoRegistro, LocalDate fecha, double valorMedido) {
        this.codigo = codigo;
        this.tecnicoRegistro = tecnicoRegistro;
        this.fecha = fecha;
        this.valorMedido = valorMedido;
    }

    public abstract boolean critica();

    public String getCodigo() {
        return codigo;
    }
    
    public Personal getTecnicoRegistro() {
    	return tecnicoRegistro;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getValorMedido() {
        return valorMedido;
    }

    public void setValorMedido(double valorMedido) {
        this.valorMedido = valorMedido;
    }
}