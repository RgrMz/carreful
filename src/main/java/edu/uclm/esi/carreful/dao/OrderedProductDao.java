package edu.uclm.esi.carreful.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.carreful.model.OrderedProduct;

@Repository
public interface OrderedProductDao extends JpaRepository<OrderedProduct, String> {
	List<OrderedProduct> findByPedidoIdPedido(String idPedido);
	
	@Query(value = "select *"
			+ "from ordered_product op"
			+ "where pe.id_pedido='4ad05c74-45b8-48d6-b21c-e3fc7164a271';", nativeQuery = true)
	List<OrderedProduct> orderedProductosPorPedido(String idPedido);
}
