package edu.uclm.esi.carreful.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.carreful.model.Product;

@Repository
public interface ProductDao extends JpaRepository <Product, String> {

	Optional<Product> findByNombre(String nombre);
	
	@Query(value = "SELECT p.nombre, p.precio, p.stock, p.picture"
			+ " FROM product p INNER JOIN categoria c ON "
			+ " p.id_categoria = c.id WHERE id_categoria = ?1", nativeQuery = true)
	List<Product> productosPorCategoria(int categoria);
}
