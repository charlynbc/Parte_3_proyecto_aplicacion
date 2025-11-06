package dataType;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataProveedor extends DataUsuario{

	
	private String descripcion;

	public DataProveedor() {
		super();
		this.setDescripcion(descripcion);
	}
	public DataProveedor(String n, String ap, String ci,String descripcion) {
		super(n,ap,ci);
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String toString() {
        return getCedulaIdentidad() + " (" + getNombre() + " " + getApellido() + " "+getDescripcion() +")";
    }
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
