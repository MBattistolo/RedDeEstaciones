public class Personal {
 
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellido;
    private char genero;
    private String direccion;
    private String telefono;

//Constructor
public Personal(String tipoDocumento, String numeroDocumento, String nombre, String apellido, char genero, String direccion, String telefono) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.direccion = direccion;
        this.telefono = telefono;
    }

//getters

  public String getTipoDocumento() {
        return tipoDocumento;
    }
 
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public String getApellido() {
        return apellido;
    }
 
    public char getGenero() {
        return genero;
    }
 
    public String getDireccion() {
        return direccion;
    }
 
    public String getTelefono() {
        return telefono;
    }
//setters
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
 
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
