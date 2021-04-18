package edu.uclm.esi.carreful.model;

public class OrderedProduct {
	private Product product;
	private double amount;
	private double priceXAmount;
	
	public OrderedProduct(Product product, double amount) {
		this.product = product;
		this.amount = amount;
		this.setPriceXAmount(this.getPrice());
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

	public void setPriceXAmount(double priceXAmount) {
		this.priceXAmount = this.getPrice();
	}
}
