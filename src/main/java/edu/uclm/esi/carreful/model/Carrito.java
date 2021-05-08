package edu.uclm.esi.carreful.model;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Carrito implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, OrderedProduct> products;
	
	public Carrito() {
		this.products = new HashMap<>();
	}

	public void add(Product product, double amount) {
		OrderedProduct orderedProduct = this.products.get(product.getNombre());
		if (orderedProduct==null) {
			orderedProduct = new OrderedProduct(product, amount);
			this.products.put(product.getNombre(), orderedProduct);
		} else {
			orderedProduct.addAmount(amount);
		}
	}

	public Collection<OrderedProduct> getProducts() {
		return products.values();
	}

	public double getImporte() {
		double importe = 0;
		for(Map.Entry<String, OrderedProduct> entry : this.products.entrySet()) {
			importe += entry.getValue().getAmount() * entry.getValue().getPrice();
		}
		return importe;
	}

	public void eliminarUnidad(Product product, int amount) {
		OrderedProduct orderedProduct = this.products.get(product.getNombre());
		if(orderedProduct.getAmount() >= amount) {
			orderedProduct.decreaseAmount(amount);
			if(orderedProduct.getAmount() == 0)
				this.products.remove(product.getNombre());
		}
	}
	
	public void eliminarProducto(Product product) {
		this.products.remove(product.getNombre());
	}
	
	public void vaciarCarrito() {
		this.products.clear();
	}

}
