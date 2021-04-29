package edu.uclm.esi.carreful.model;

import edu.uclm.esi.carreful.model.interfaces.GastosDeEnvio;

public class Recogida extends TipoPedido {

	public Recogida(Pedido pedido) {
		super(pedido);
	}

	public double getGastosDeEnvio() {
		return GastosDeEnvio.getGastosrecogida();
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
