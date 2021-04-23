package edu.uclm.esi.carreful.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Pedido {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idPedido;

	@NotNull @Column(length = 70)
	private String nombre, apellidos, email, telefonoMovil, direccion, ciudad, provincia, pais;
	@NotNull
	private int codigoPostal;
	@NotNull @Column(columnDefinition = "varchar(255) default 'Recibido'")
	private Estado estado;
	
	@Transient
	private TipoPedido tipo;
	
	private String tipoPedido;

	public long getIdPedido() { 
		return idPedido;
	}
	public void setIdPedido(long idPedido) {
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
		this.estado = this.tipo.updateEstado();
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
			Class<TipoPedido> clazz = (Class<TipoPedido>) Class.forName(tipoPedido);
			this.tipo = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getTipoPedido() {
		return this.tipo.getClass().getName();
	}
	
}
