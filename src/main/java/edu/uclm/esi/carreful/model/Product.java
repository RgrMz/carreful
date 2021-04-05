package edu.uclm.esi.carreful.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Product {
	@Id
	private String nombre;
	private String precio;
	@GeneratedValue
	private Long codigo;
	@Column(columnDefinition = "integer default 0")
	private int stock;
	@Lob
	private String picture;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPrecio() {
		return precio;
	}
	
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
