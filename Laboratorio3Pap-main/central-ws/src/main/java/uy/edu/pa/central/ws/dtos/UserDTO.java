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
    private String email;
    private String tipoUsuario; // turista | proveedor

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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}
