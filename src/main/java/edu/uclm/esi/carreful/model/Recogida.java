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
	public Estado updateEstado() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public void updateEstado() {
		switch() {
			case recibido:
				ser preparado
			case preparado:
				excepcion
			case default:
				set recibido
		}
	}*/
}
