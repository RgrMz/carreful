package edu.uclm.esi.carreful.model;

import edu.uclm.esi.carreful.model.interfaces.GASTOS_DE_ENVIO;

public class Recogida extends TipoPedido implements GASTOS_DE_ENVIO {

	public Recogida(Pedido pedido) {
		super(pedido);
	}

	public double getGastosDeEnvio() {
		return GASTOS_RECOGIDA;
	}

	@Override
	public void updateEstado() {
		switch(this.pedido.getEstado()) {
		case RECIBIDO:
			this.pedido.setEstado(Estado.PREPARADO);
			break;
		case PREPARADO:
			this.pedido.setEstado(Estado.PREPARADO);
			break;
		default :
			this.pedido.setEstado(Estado.RECIBIDO);
			break;
		}
	}
}
