package edu.uclm.esi.carreful.model;

import edu.uclm.esi.carreful.model.interfaces.GASTOS_DE_ENVIO;

public class Domicilio extends TipoPedido implements GASTOS_DE_ENVIO{

	public Domicilio(Pedido pedido) {
		super(pedido);
	}

	public double getGastosDeEnvio() {
		return GASTOS_DOMICILIO;
	}

	@Override
	public Estado updateEstado() {
		// TODO Auto-generated method stub
		return null;
	}
}
