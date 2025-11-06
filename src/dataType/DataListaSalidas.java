package dataType;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataListaSalidas {
	
	private String nombre;
	private List<String> salidas=new ArrayList<String>();
	
	public DataListaSalidas() {
		
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<String> getActividades() {
		return salidas;
	}

	public void setActividades(List<String> list) {
		this.salidas = (ArrayList<String>) list;
	}
	 public void addsalidas(String act ) {
	        this.salidas.add(act);
	    }
}

