package edu.uclm.esi.carreful.model;

import javax.validation.constraints.NotNull;

public abstract class TipoPedido {
	
	private Pedido pedido;
	public TipoPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public abstract double getGastosDeEnvio();
	public abstract Estado updateEstado();
	//public abstract Estado getEstado();

}
