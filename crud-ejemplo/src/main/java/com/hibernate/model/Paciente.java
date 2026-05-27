package com.hibernate.model;

import java.sql.Blob;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@Entity
@Table (name="paciente")
@Data public class Paciente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idPaciente")
	private int idPaciente;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="glucosa")
	private int glucosa;
	
	@Column(name="hierro")
	private int hierro;

	public Paciente(String nombre, int glucosa, int hierro) {
		super();
		this.nombre = nombre;
		this.glucosa = glucosa;
		this.hierro = hierro;
	}
	
}
