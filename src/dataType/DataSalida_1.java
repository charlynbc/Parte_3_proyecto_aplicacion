package dataType;


import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataSalida {
	
	private String nombre;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date fecha;
	private Integer hora;
	private Integer turistaMax;
	private String lugar;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date fechaAlta;
	
	public DataSalida() {
		super();
	
	}
	
	public DataSalida(String nombre,Date fecha, Integer hora, Integer turistaMax, String lugar, Date fechaAlta) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.turistaMax = turistaMax;
		this.lugar = lugar;
		this.fechaAlta = fechaAlta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	public Integer getTuristaMax() {
		return turistaMax;
	}

	public void setTuristaMax(Integer turistaMax) {
		this.turistaMax = turistaMax;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	
	
}
