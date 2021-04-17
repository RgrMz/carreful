package edu.uclm.esi.carreful.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance
public abstract class Pedido {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idPedido;

	@NotNull
	private String nombre, apellidos, email, telefonoMovil, direccion, ciudad, provincia, pais;
	@NotNull
	private int codigoPostal;
	@NotNull
	private Carrito carrito;
}
