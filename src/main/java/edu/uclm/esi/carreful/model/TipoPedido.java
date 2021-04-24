package edu.uclm.esi.carreful.model;

import javax.validation.constraints.NotNull;

public abstract class TipoPedido {
	
	protected Pedido pedido;
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public TipoPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public abstract double getGastosDeEnvio();
	public abstract void updateEstado();
	//public abstract Estado getEstado();

}
