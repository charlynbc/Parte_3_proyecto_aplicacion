package dataType;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class DataTurista extends DataUsuario{
	
	
	private String nacionalidad;
  
	public DataTurista() {
		super();
		this.setNacionalidad(nacionalidad);
        
    }
	
	public DataTurista(String n, String ap, String ci,String nacionalidad) {
		super(n,ap,ci);
		this.nacionalidad = nacionalidad;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	  /* Sirve para mostrar textualmente la informacion del usuario, por ejemplo en un ComboBox     */
    
	public String toString() {
        return getCedulaIdentidad() + " (" + getNombre() + " " + getApellido() + " "+getNacionalidad()+")";
    }
	
	
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
}
	
	

}
