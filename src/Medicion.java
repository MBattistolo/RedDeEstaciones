import java.time.LocalDate;

public abstract class Medicion {
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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getValorMedido() {
        return valorMedido;
    }

    public void setValorMedido(double valorMedido) {
        this.valorMedido = valorMedido;
    }
}