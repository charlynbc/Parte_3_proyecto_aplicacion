package dataType;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataListaSalidas2 {
	
	private String nombre;
	private List<DataSalida> salidas=new ArrayList<DataSalida>();
	
	public DataListaSalidas2() {
		
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<DataSalida> getSalidas() {
		return salidas;
	}

	public void setActividades(List<DataSalida> list) {
		this.salidas = (ArrayList<DataSalida>) list;
	}
	 public void addsalidas(DataSalida act ) {
	        this.salidas.add(act);
	    }
}

