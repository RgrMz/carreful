package edu.uclm.esi.carreful.model;

import edu.uclm.esi.carreful.model.interfaces.GASTOS_DE_ENVIO;

public class DomExpress extends TipoPedido implements GASTOS_DE_ENVIO {

	public DomExpress(Pedido pedido) {
		super(pedido);
	}

	@Override
	public double getGastosDeEnvio() {
		// TODO Auto-generated method stub
		return GASTOS_EXPRESS;
	}

	@Override
	public Estado updateEstado() {
		return null;
	}

}
