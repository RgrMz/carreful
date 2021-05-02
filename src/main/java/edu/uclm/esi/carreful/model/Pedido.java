package edu.uclm.esi.carreful.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Pedido {
	@Id @Column(length = 36)
	private String idPedido;

	@NotNull @Column(length = 70)
	private String nombre;
	@NotNull @Column(length = 70)
	private String apellidos;
	@NotNull @Column(length = 70)
	private String email;
	@NotNull @Column(length = 70)
	private String telefonoMovil;
	@NotNull @Column(length = 70)
	private String direccion;
	@NotNull @Column(length = 70)
	private String ciudad;
	@NotNull @Column(length = 70)
	private String provincia;
	@NotNull @Column(length = 70)
	private String pais;
	@NotNull
	private int codigoPostal;
	@NotNull
	private double precioPedido;
	@NotNull
	private Estado estado;
	
	@Transient
	private TipoPedido tipo;
	
	@SuppressWarnings("unused")
	private String tipoPedido;
	
	public Pedido(){
		this.idPedido = UUID.randomUUID().toString();
	}

	public String getIdPedido() { 
		return idPedido;
	}
	public void setIdPedido(String idPedido) {
		this.idPedido = idPedido;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefonoMovil() {
		return telefonoMovil;
	}
	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public int getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public TipoPedido getTipo() {
		return tipo;
	}
	public void setTipo(TipoPedido tipo) {
		this.tipo = tipo;
	}
	
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
		try {
			// Hay que coger la full path: edu.uclm.esi...
			@SuppressWarnings("unchecked")
			Class<TipoPedido> clazz = (Class<TipoPedido>) Class.forName("edu.uclm.esi.carreful.model."+tipoPedido);
			this.tipo = clazz.getDeclaredConstructor(Pedido.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getTipoPedido() {
		return this.tipo.getClass().getName();
	}

	public double getPrecioPedido() {
		return precioPedido;
	}

	public void setPrecioPedido(double precioPedido) {
		this.precioPedido = precioPedido;
	}
	
}
