package edu.uclm.esi.carreful.model;

import java.io.Serializable;

public abstract class TipoPedido implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Pedido pedido;
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	protected TipoPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public abstract double getGastosDeEnvio();
	public abstract void updateEstado();
	//public abstract Estado getEstado();

}
