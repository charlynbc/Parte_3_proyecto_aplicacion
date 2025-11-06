package dataType;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({DataTurista.class, DataProveedor.class})
@XmlAccessorType(XmlAccessType.FIELD)
public  class DataUsuario {

	//@XmlElement
    private String nombre;
	//@XmlElement
	private String apellido;
	//@XmlElement
	private String cedulaIdentidad;
    //private byte[] foto;

    public DataUsuario() {
             
    }

    public DataUsuario(String nombre, String apellido, String cedulaIdentidad/*,byte[] foto*/) {
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setCedulaIdentidad(cedulaIdentidad);
      //  this.setFoto(foto);
        
    }

	public String getNombre() {
        return nombre;
    }

	private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    private void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getCedulaIdentidad() {
        return cedulaIdentidad;
    }

    private void setCedulaIdentidad(String cedulaIdentidad) {
        this.cedulaIdentidad = cedulaIdentidad;
    }
    
/*    public byte[] getFoto() {
		return foto;
	}*/
    /* Sirve para mostrar textualmente la informacion del usuario, por ejemplo en un ComboBox
     
    public String toString() {
        return getCedulaIdentidad() + " (" + getNombre() + " " + getApellido() + ")";
    }*/
    
 //   public abstract String toString();
    
 
/*    public void setFoto(byte[] foto) {
		this.foto = foto;
	}*/

}
