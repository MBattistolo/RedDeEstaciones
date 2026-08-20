package modelo;
import java.io.Serializable;

public class Personal implements Serializable{
 
	private static final long serialVersionUID = 1L;
	
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellido;
    private char genero;
    //char y no enum porque el genero solo se almacena, ninguna regla del sistema depende de el, en el caso de Jornada si es enum porque critica() 
    //lo evalua
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
    
    @Override
    public String toString() {
        return "Tipo de documento: " + tipoDocumento
                + "\nNumero de documento: " + numeroDocumento
                + "\nNombre: " + nombre + " " + apellido
                + "\nGenero: " + genero
                + "\nDireccion: " + direccion
                + "\nTelefono: " + telefono;
    }
}
