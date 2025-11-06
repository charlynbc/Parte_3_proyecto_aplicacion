package dataType;

import java.time.LocalDate;


public class DataInscripcion {
	
	//private long id;
	private LocalDate fechaInscripcion;
	private Integer CantidadTuristas;
	private Integer Costo;
	private String Turi;
	   
	
	public DataInscripcion(LocalDate fechaInscripcion, Integer cantidadTuristas, Integer costo, String turista) {
		super();
		//this.id=id;
		this.fechaInscripcion = fechaInscripcion;
		this.CantidadTuristas = cantidadTuristas;
		this.Costo = costo;
		this.Turi = turista;
	}

	public String getTurista() {
		return Turi;
	}

	public void setTurista(String turista) {
		Turi = turista;
	}

	public LocalDate getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(LocalDate fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public Integer getCantidadTuristas() {
		return CantidadTuristas;
	}

	public void setCantidadTuristas(Integer cantidadTuristas) {
		CantidadTuristas = cantidadTuristas;
	}

	public Integer getCosto() {
		return Costo;
	}

	public void setCosto(Integer costo) {
		Costo = costo;
	}
	
	

}
