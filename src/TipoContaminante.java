import java.io.Serializable;

public class TipoContaminante implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String codigo;
	private String nombreContaminante;
	private double valorMinimo;
	private double valorMaximo;
	
	public TipoContaminante(String codigo, String nombreContaminante,
	double valorMinimo, double valorMaximo) {
	this.codigo = codigo;
	this.nombreContaminante = nombreContaminante;
	this.valorMinimo = valorMinimo;
	this.valorMaximo = valorMaximo;
	}
	
	public String getCodigo() {
	return codigo;
	}
	
	public String getNombreContaminante() {
	return nombreContaminante;
	}
	
	public double getValorMinimo() {
	return valorMinimo;
	}
	
	public double getValorMaximo() {
	return valorMaximo;
	}
	
	public void setValorMinimo(double valorMinimo) {
	this.valorMinimo = valorMinimo;
	}
	
	public void setValorMaximo(double valorMaximo) {
	this.valorMaximo = valorMaximo;
	}
	
	@Override
	public String toString() {
	    return "Codigo: " + codigo
	            + "\nNombre: " + nombreContaminante
	            + "\nValor minimo permitido: " + valorMinimo
	            + "\nValor maximo permitido: " + valorMaximo;
	}
}
