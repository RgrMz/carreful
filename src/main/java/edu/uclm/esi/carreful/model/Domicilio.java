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
	public void updateEstado() {
		switch(this.pedido.getEstado()) {
		case RECIBIDO:
			this.pedido.setEstado(Estado.PREPARADO);
			break;
		case PREPARADO:
			this.pedido.setEstado(Estado.EN_CAMINO);
			break;
		case EN_CAMINO:
			this.pedido.setEstado(Estado.ENTREGADO);
			break;
		case ENTREGADO:
			this.pedido.setEstado(Estado.ENTREGADO);
			break;
		default :
			this.pedido.setEstado(Estado.RECIBIDO);
			break;
		}
	}
}
