package edu.uclm.esi.carreful.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "Normal")
public class PedidoNormal extends Pedido{

	@Override
	public String generarFactura(Carrito carrito) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizarEstado() {
		// TODO Auto-generated method stub
		
	}

}
