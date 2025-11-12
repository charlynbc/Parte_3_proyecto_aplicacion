package uy.edu.pa.central.ws.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO implements Serializable {
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento;
    private String tipoUsuario; // turista | proveedor
    
    // Campos específicos de Turista
    private String nacionalidad;
    
    // Campos específicos de Proveedor
    private String descripcionProveedor;
    private String linkProveedor;

    // Foto de perfil (Base64 data URI)
    private String imagen;

    public UserDTO() {}

    public UserDTO(String nickname, String nombre, String email, String tipoUsuario) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public String getDescripcionProveedor() { return descripcionProveedor; }
    public void setDescripcionProveedor(String descripcionProveedor) { this.descripcionProveedor = descripcionProveedor; }
    
    public String getLinkProveedor() { return linkProveedor; }
    public void setLinkProveedor(String linkProveedor) { this.linkProveedor = linkProveedor; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
