package edu.uclm.esi.carreful.model;

public class OrderedProduct {
	private Product product;
	private double amount;
	
	public OrderedProduct(Product product, double amount) {
		this.product = product;
		this.amount = amount;
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
}
