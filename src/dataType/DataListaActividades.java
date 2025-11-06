package dataType;

import java.util.ArrayList;
import java.util.List;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataListaActividades {
	private String nombre;
	private List<String> actividades= new ArrayList<String>();
	
	public DataListaActividades() {
		
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<String> getActividades() {
		return actividades;
	}

	public void setActividades(List<String> list) {
		this.actividades = (ArrayList<String>) list;
	}
	 public void addactividad(String act ) {
	        this.actividades.add(act);
	    }
}

