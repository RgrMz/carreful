package edu.uclm.esi.carreful.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderedProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	@OneToOne
	private Product product;
	@Column
	private double amount;
	@Column
	private double priceXAmount;
	
	@ManyToOne
	private Pedido pedido;
	
	public OrderedProduct() {
		
	}
	
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public OrderedProduct(Product product, double amount) {
		this.product = product;
		this.amount = amount;
		this.setPriceXAmount();
	}

	public void addAmount(double amount) {
		if(amount>0)
			this.amount+=amount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getName() {
		return this.product.getNombre();
	}
	
	public double getPrice() {
		return this.product.getPrecio();
	}

	public void decreaseAmount(int amount) {
		if(amount>0)
			this.amount-=amount;
	}

	public double getPriceXAmount() {
		return priceXAmount;
	}

	public void setPriceXAmount() {
		this.priceXAmount = this.getPrice();
	}
	
	public boolean isCongelado() {
		return this.product.isCongelado();
	}
}
