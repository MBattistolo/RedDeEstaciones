package modelo;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Arrays;

public class Estacion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    private String codigo;
    private String nombre;
    private String comuna;
    private LocalDate fechaInstalacion;
    private Medicion[] mediciones;

    public Estacion(String codigo, String nombre, String comuna, LocalDate fechaInstalacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.comuna = comuna;
        this.fechaInstalacion = fechaInstalacion;
        this.mediciones = new Medicion[0];
    }

    public void addMedicion(Medicion medicion) {
        mediciones = Arrays.copyOf(mediciones, mediciones.length + 1);
        mediciones[mediciones.length - 1] = medicion;
    }

    public Integer buscarMedicion(String codigoMedicion) {
        int i = 0;
        while (i < mediciones.length && !mediciones[i].getCodigo().equals(codigoMedicion)) i++;
        if (i == mediciones.length) return null;

        return i;
    }

    public void eliminarMedicion(String codigoMedicion) {
        Integer i = buscarMedicion(codigoMedicion);

        if (i == null) {
            System.out.println("El codigo buscado no existe");
            return;
        }

        for (int j = i; j < mediciones.length - 1; j++) mediciones[j] = mediciones[j + 1];
        mediciones = Arrays.copyOf(mediciones, mediciones.length - 1);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getComuna() {
        return comuna;
    }

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public Medicion[] getMediciones() {
        return mediciones;
    }
    
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "\nNombre: " + nombre
                + "\nComuna: " + comuna
                + "\nFecha de instalacion: " + fechaInstalacion
                + "\nCantidad de ediciones registradas: " + mediciones.length;
    }
}